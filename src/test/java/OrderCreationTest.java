import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.qameta.allure.junit4.DisplayName;

public class OrderCreationTest {
    String email = "harry-potter@yandex.ru";
    String password = "password";
    String name = "harry";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @Before
    public void createNewUser() {
        //Создаем пользователя
        CreteUserPojo userPojo = new CreteUserPojo(email, password, name);
        CreateUser user = new CreateUser();
        Response createUserResponse = user.createUser(userPojo);
        createUserResponse.then().assertThat().statusCode(200);
    }

    @After
    public void deleteUser(){
        //Логиним созданного пользователя и получаем токен
        UserLogin userLogin = new UserLogin(email, password);

        UserTokenPojo accessToken = given()
                .header("Content-type", "application/json")
                .body(userLogin)
                .post("/api/auth/login")
                .thenReturn()
                .body()
                .as(UserTokenPojo.class);

        //Удаляем созданного пользователя
        Response deleteResponse = given()
                .header("Authorization", accessToken.getToken())
                .header("Content-type", "application/json")
                .delete("/api/auth/user");
    }

    @Test
    @DisplayName("Creating an order when user is authorized")
    public void CreateOrderAuthUser(){
        String[] ingredients = new String[] {"61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f"};
        OrderPojo order = new OrderPojo(ingredients);

        //Логиним созданного пользователя и получаем токен
        UserLogin userLogin = new UserLogin(email, password);

        UserTokenPojo accessToken = given()
                .header("Content-type", "application/json")
                .body(userLogin)
                .post("/api/auth/login")
                .thenReturn()
                .body()
                .as(UserTokenPojo.class);

        //Создаем заказ, передаем токен и проверям код ответа
        Response response =
                given()
                        .header("Authorization", accessToken.getToken())
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/orders");
        response.then().statusCode(200);
    }

    @Test
    @DisplayName("Creating an order when user is unauthorized")
    public void CreateOrderUnAuthUser(){
        String[] ingredients = new String[] {"61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f"};
        OrderPojo order = new OrderPojo(ingredients);

        //Пробуем создать заказ, не передавая токен и проверям код ответа
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/orders");
        response.then().statusCode(200);
    }

    @Test
    @DisplayName("Creating an order with no ingredients")
    public void CreateOrderWithNoIngredients(){
        String[] ingredients = new String[] {};
        OrderPojo order = new OrderPojo(ingredients);

        //Пробуем создать заказ без ингредиентов и проверям код ответа
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/orders");
                response.then().assertThat().statusCode(400).and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Creating an order with invalid hash")
    public void CreateOrderInvalidHash(){
        String[] ingredients = new String[] {"invalid_hash","invalid_hash"};
        OrderPojo order = new OrderPojo(ingredients);

        //Пробуем создать заказ, передавая невалидные id ингредиентов и проверям код ответа
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/orders");
        response.then().statusCode(400);
    }
}
