package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.UserTokenPojo;

import static io.restassured.RestAssured.given;

public class LoginUser {
    @Step("Logging in a new user")
    public static Response loginUser(Object body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post("/api/auth/login");
    }

    @Step("Logging in a user and getting a token")
    public static UserTokenPojo loginAndGetToken(Object body) {
        return given()
                .header("Content-type", "application/json")
                .body(body)
                .post("/api/auth/login")
                .thenReturn()
                .body()
                .as(UserTokenPojo.class);
    }
}

