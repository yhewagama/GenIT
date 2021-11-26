package org.getit.services.mock;

import io.restassured.matcher.RestAssuredMatchers.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.Test;

import static io.restassured.RestAssured.get;

public class storeinventoryMock {

@Test
public void getstoreinventory() {
        get("https://petstore.swagger.io/v2/store/inventory").then().statusCode(200).assertThat()
    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("storeinventory.json"));
    }
}