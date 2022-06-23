package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.UserToken;

import static io.restassured.RestAssured.given;

public class Order {
    @Step("Creating a new order")
    public static Response createOrder(String accessToken, model.Order body) {
        RequestSpecification request = given()
                .header("Content-type", "application/json");
        if (accessToken != null) {
            request = request.header("Authorization", accessToken);
        }
        return request.and()
                .body(body)
                .when()
                .post("/api/orders");
    }

    @Step("Getting an order info")
    public static Response getOrderInfo(UserToken accessToken) {
        RequestSpecification request = given()
                .header("Content-type", "application/json");
        if (accessToken != null) {
            request = request.header("Authorization", accessToken.getToken());
        }
        return request.and()
                .when()
                .get("/api/orders");
    }
}
