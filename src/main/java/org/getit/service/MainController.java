package org.getit.service;

import java.util.Map;
import java.util.Map.Entry;

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

	@GetMapping("/runApiTests")
	public OpenAPI genOpenAPIObj(@RequestBody String swaggerOpenAPIDocUrl) {
		System.out.println(swaggerOpenAPIDocUrl);

//		OpenAPI openAPI = new OpenAPIV3Parser().read(swaggerOpenAPIDocUrl);

//		 SwaggerParseResult result = new OpenAPIParser().readContents(swaggerOpenAPIDocUrl, null, null);
//		 
//		 OpenAPI openAPI = result.getOpenAPI();

		OpenAPI openAPI = new OpenAPIV3Parser().read(swaggerOpenAPIDocUrl);
		
		Map<String, Schema> definitions = openAPI.getComponents().getSchemas();
//		Schema model = openAPI.getPaths().entrySet().iterator().next().getValue().getPost().getRequestBody().getContent().get("application/json").getSchema();
////		Schema model = definitions.get("properties");
//		Example example = ExampleBuilder.fromSchema(model, definitions);
//		SimpleModule simpleModule = new SimpleModule().addSerializer(new JsonNodeExampleSerializer());
//		Json.mapper().registerModule(simpleModule);
//		String jsonExample = Json.pretty(example);
//		System.out.println(jsonExample);

//		System.out.println(openAPI.getPaths().entrySet().iterator().next().getKey().replaceAll("[^0-9a-zA-Z:,]+", ""));
//		System.out.println(openAPI.getPaths().entrySet().iterator().next().getValue());
		
		
		
		//Get task collection
        List<Callable<String>> pathCallableList = new ArrayList<>();

//        JSONObject pathsObject = openAPI.getPaths().entrySet().iterator().next();
        Iterator<Entry<String, PathItem>> keys = openAPI.getPaths().entrySet().iterator();

        while(keys.hasNext()) {
            Entry<String, PathItem> entry = keys.next();
            if (entry != null) {
            	System.out.println(entry.getKey());
            	if (entry.getValue().getPost() != null) {
            		if (entry.getValue().getPost().getRequestBody() != null) {
            			Schema model = entry.getValue().getPost().getRequestBody().getContent().get("application/json").getSchema();
//                		Schema model = definitions.get("properties");
                		Example example = ExampleBuilder.fromSchema(model, definitions);
                		SimpleModule simpleModule = new SimpleModule().addSerializer(new JsonNodeExampleSerializer());
                		Json.mapper().registerModule(simpleModule);
                		String jsonExample = Json.pretty(example);
                		System.out.println(jsonExample);
            		}
            		
            	}
            	
            	
            	
            	
                pathCallableList.add(pathCallable(entry.getKey(), entry.getValue()));
            }
        }

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
