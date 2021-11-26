package org.getit.services.mock;

import io.restassured.matcher.RestAssuredMatchers.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class petfindByStatusMock {

@Test
public void getpetfindByStatus() {
    String endpoint="https://reqres.in/api/users?page=1";
    var response=given().when().get(endpoint).then();
    response.log().body();
    }
}