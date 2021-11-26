package hackathon;

import static io.restassured.RestAssured.get;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;
import io.restassured.module.jsv.JsonSchemaValidator;

import org.junit.Test;

public class pet {

@Test
public void postpet() {
        RestAssured
        .given()
            .baseUri("https://petstore.swagger.io/v2/pet")
            .contentType(ContentType.JSON)
            .body("{
  "id" : 10,
  "name" : "doggie",
  "category" : {
    "id" : 1,
    "name" : "Dogs"
  },
  "photoUrls" : [ "string" ],
  "tags" : [ {
    "id" : 0,
    "name" : "string"
  } ],
  "status" : "available"
}")
        .when()
            .post()
        .then()
            .assertThat()
            .statusCode(200)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("pet.json"));
    }
}