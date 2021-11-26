package hackathon;

import static io.restassured.RestAssured.get;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;
import io.restassured.module.jsv.JsonSchemaValidator;

import org.junit.Test;

public class petfindByStatus {

@Test
public void getpetfindByStatus() {
        get("https://petstore.swagger.io/v2/pet/findByStatus").then().statusCode(200).assertThat()
    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("petfindByStatus.json"));
    }
}