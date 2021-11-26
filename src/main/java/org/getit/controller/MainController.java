package org.getit.controller;

import io.swagger.models.Response;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.getit.model.TestCase;
import org.getit.model.TestSuite;
import org.getit.util.TestGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.module.SimpleModule;

import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.oas.inflector.examples.models.Example;
import io.swagger.oas.inflector.examples.ExampleBuilder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.oas.inflector.processors.JsonNodeExampleSerializer;
import io.swagger.util.Json;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;


@RestController
@RequestMapping("/")
public class MainController {

	static String baseUrl = "https://petstore.swagger.io/v2";

	@GetMapping("/runApiTests")
	public OpenAPI genOpenAPIObj(@RequestBody String swaggerOpenAPIDocUrl) throws IOException {

		String[] pathArray = new String[]{"/pet/{petId}/uploadImage", "/pet/{petId}", "/pet/findByTags", "/store/order", "/store/order/{orderId}", "/user/{username}", "/user/login"};
		List<String> pathsList = new ArrayList<>(Arrays.asList(pathArray));
		ArrayList<String> testFiles = new ArrayList<String>();

    TestGenerator testGenerator = new TestGenerator();

//		OpenAPI openAPI = new OpenAPIV3Parser().read(swaggerOpenAPIDocUrl);

//		 SwaggerParseResult result = new OpenAPIParser().readContents(swaggerOpenAPIDocUrl, null, null);
//		 
//		 OpenAPI openAPI = result.getOpenAPI();

		OpenAPI openAPI = new OpenAPIV3Parser().read(swaggerOpenAPIDocUrl);
		
		Map<String, Schema> definitions = openAPI.getComponents().getSchemas();
		
		
		
		//Get task collection
        List<Callable<String>> pathCallableList = new ArrayList<>();

//        JSONObject pathsObject = openAPI.getPaths().entrySet().iterator().next();
        Iterator<Entry<String, PathItem>> keys = openAPI.getPaths().entrySet().iterator();

        while (keys.hasNext()) {
            TestSuite testSuite = new TestSuite();
            ArrayList testCases = new ArrayList();
            Entry<String, PathItem> entry = keys.next();
            if (entry != null && !pathsList.contains(entry.getKey())) {
							String path = entry.getKey();
            	if (entry.getValue().getPost() != null) {
            		if (entry.getValue().getPost().getRequestBody() != null && entry.getValue().getPost().getResponses().get("200") != null) {
            			Schema requestModel = entry.getValue().getPost().getRequestBody().getContent().get("application/json").getSchema();
									Schema responseModel = entry.getValue().getPost().getResponses().get("200").getContent().get("application/json").getSchema();


									Example requestExample = ExampleBuilder.fromSchema(requestModel, definitions);
									Example responseExample = ExampleBuilder.fromSchema(responseModel, definitions);

									SimpleModule simpleModule = new SimpleModule().addSerializer(new JsonNodeExampleSerializer());
									Json.mapper().registerModule(simpleModule);

									String requestJsonExample = Json.pretty(requestExample);
//									System.out.println("request body: " + requestJsonExample);

									String responseJsonExample = Json.pretty(responseExample);
//									System.out.println("response body: " + responseJsonExample);

									String responseJsonFileName = entry.getKey().replaceAll("[^a-zA-Z0-9]", "") + ".json";
									String responseJsonFilePath = "target/classes/" + responseJsonFileName;

									try (FileWriter file = new FileWriter(responseJsonFilePath)) {
										//We can write any JSONArray or JSONObject instance to the file
										file.write(responseJsonExample);
										file.flush();

//										JSONParser parser = new JSONParser();
//										JSONObject json = (JSONObject) parser.parse(requestJsonExample);

										TestCase testCase = new TestCase();
										testCase.setMethod("post");
										testCase.setUrl(baseUrl + path);
										testCase.setRequest_body(requestJsonExample);
										testCase.setResponse_path(responseJsonFileName);

										testCases.add(testCase);

									} catch (IOException e) {
										e.printStackTrace();
									}


            		}
            	} else if (entry.getValue().getGet() != null) {
								if (entry.getValue().getGet().getRequestBody() == null && entry.getValue().getGet().getResponses().get("200") != null) {
									Schema responseModel = entry.getValue().getGet().getResponses().get("200").getContent().get("application/json").getSchema();

									Example responseExample = ExampleBuilder.fromSchema(responseModel, definitions);

									SimpleModule simpleModule = new SimpleModule().addSerializer(new JsonNodeExampleSerializer());
									Json.mapper().registerModule(simpleModule);

									String responseJsonExample = Json.pretty(responseExample);
									System.out.println("response body: " + responseJsonExample);

									String responseJsonFileName = entry.getKey().replaceAll("[^a-zA-Z0-9]", "") + ".json";
									String responseJsonFilePath = "target/classes/" + responseJsonFileName;

									try (FileWriter file = new FileWriter(responseJsonFilePath)) {
										//We can write any JSONArray or JSONObject instance to the file
										file.write(responseJsonExample);
										file.flush();

										TestCase testCase = new TestCase();
										testCase.setMethod("get");
										testCase.setUrl(baseUrl + path);
										testCase.setResponse_path(responseJsonFileName);
										testCase.setRequest_body("");

										testCases.add(testCase);

									} catch (IOException e) {
										e.printStackTrace();
									}

								}
							}

       				if (testCases.size() > 0) {
					testSuite.setPath(path);
					testSuite.setTestCases(testCases);
					String tests = testGenerator.generateTests(testSuite);
					testFiles.add(tests);
				}
            	
                pathCallableList.add(pathCallable(entry.getKey(), entry.getValue()));
            }
        }
		System.out.println(testFiles.toArray().toString());

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        try {
            List<Future<String>> resultFutures = executorService.invokeAll(pathCallableList);

            for (Future<String> future : resultFutures) {
//                System.out.println(future.get());
            }
        } catch (InterruptedException e ) {
            e.printStackTrace();
        }

        //shutting down the executor service
        executorService.shutdown();


		
		return openAPI;

	}
	
	public Callable pathCallable(String path, PathItem pathObject) {
	    return () -> "Create template with the path : " + path + " in thread - " + Thread.currentThread().getName() + " | from object " + pathObject;
	}


}
