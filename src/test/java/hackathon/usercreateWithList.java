package hackathon;

import static io.restassured.RestAssured.get;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;
import io.restassured.module.jsv.JsonSchemaValidator;

import org.junit.Test;

public class usercreateWithList {

@Test
public void postusercreateWithList() {
        RestAssured
        .given()
            .baseUri("https://petstore.swagger.io/v2/user/createWithList")
            .contentType(ContentType.JSON)
            .body("[ {
  "id" : 10,
  "username" : "theUser",
  "firstName" : "John",
  "lastName" : "James",
  "email" : "john@email.com",
  "password" : "12345",
  "phone" : "12345",
  "userStatus" : 1
} ]")
        .when()
            .post()
        .then()
            .assertThat()
            .statusCode(200)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("usercreateWithList.json"));
    }
}