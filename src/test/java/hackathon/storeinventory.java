package hackathon;

import static io.restassured.RestAssured.get;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;
import io.restassured.module.jsv.JsonSchemaValidator;

import org.junit.Test;

public class storeinventory {

@Test
public void getstoreinventory() {
        get("https://petstore.swagger.io/v2/store/inventory").then().statusCode(200).assertThat()
    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("storeinventory.json"));
    }
}