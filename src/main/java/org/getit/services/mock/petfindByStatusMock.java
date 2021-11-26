package org.getit.services.mock;

import io.restassured.matcher.RestAssuredMatchers.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.Test;

import static io.restassured.RestAssured.get;

public class petfindByStatusMock {

@Test
public void getpetfindByStatus() {
        get("https://petstore.swagger.io/v2/pet/findByStatus").then().statusCode(200).assertThat()
    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("petfindByStatus.json"));
    }
}