package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.UserToken;

import static io.restassured.RestAssured.given;

public class UpdateUserInfo {
    @Step("Deleting a user")
    public static Response updateUserInfo(UserToken accessToken, Object update) {
        RequestSpecification request = given()
                .header("Content-type", "application/json");
        if (accessToken != null) {
            request = request.header("Authorization", accessToken.getToken());
        }
        return request.and()
                .body(update)
                .when()
                .patch("/api/auth/user");
    }
}
