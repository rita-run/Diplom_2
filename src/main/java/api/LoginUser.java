package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.UserToken;

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

    @Step("Getting a token")
    public static UserToken getToken(Object body) {
        return given()
                .header("Content-type", "application/json")
                .body(body)
                .post("/api/auth/login")
                .thenReturn()
                .body()
                .as(UserToken.class);
    }
}

