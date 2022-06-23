package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.OrderPojo;

import static io.restassured.RestAssured.given;

public class CreateOrder {
    @Step("Creating a new order")
    public static Response createOrder(String accessToken, OrderPojo body) {
        //Логиним созданного пользователя и получаем токен
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
}
