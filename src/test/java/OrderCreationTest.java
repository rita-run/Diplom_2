import api.CreateOrder;
import api.CreateUser;
import api.DeleteUser;
import api.LoginUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class OrderCreationTest extends BaseTest {
    private static final String email = "harry-potter@yandex.ru";
    private static final String password = "password";
    private static final String name = "harry";

    @BeforeClass
    public static void cleanseUsers() {
        UserLoginPojo user = new UserLoginPojo(email, password);
        DeleteUser.deleteUser(user);
    }

    @Before
    public void createNewUser() {
        //Создаем пользователя
        CreteUserPojo userPojo = new CreteUserPojo(email, password, name);
        Response createUserResponse = CreateUser.createUser(userPojo);
        createUserResponse.then().assertThat().statusCode(200);
    }

    @After
    public void deleteUser() {
        UserLoginPojo userLoginPojo = new UserLoginPojo(email, password);
        //Удаляем созданного пользователя
        DeleteUser.deleteUser(userLoginPojo);
    }

    @Test
    @DisplayName("Creating an order when user is authorized")
    public void createOrderAuthUser() {
        String[] ingredients = new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"};
        OrderPojo order = new OrderPojo(ingredients);

        //Логиним созданного пользователя и получаем токен
        UserLoginPojo userLoginPojo = new UserLoginPojo(email, password);
        UserTokenPojo accessToken = LoginUser.loginAndGetToken(userLoginPojo);

        //Создаем заказ, передаем токен и проверям код ответа
        Response response = CreateOrder.createOrder(accessToken.getToken(), order);
        response.then().statusCode(200);
    }

    @Test
    @DisplayName("Creating an order when user is unauthorized")
    public void createOrderUnAuthUser() {
        String[] ingredients = new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"};
        OrderPojo order = new OrderPojo(ingredients);

        //Пробуем создать заказ, не передавая токен и проверям код ответа
        Response response = CreateOrder.createOrder(null, order);
        response.then().statusCode(200);
    }

    @Test
    @DisplayName("Creating an order with no ingredients")
    public void createOrderWithNoIngredients() {
        String[] ingredients = new String[]{};
        OrderPojo order = new OrderPojo(ingredients);

        //Пробуем создать заказ без ингредиентов и проверям код ответа
        Response response = CreateOrder.createOrder(null, order);
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Creating an order with invalid hash")
    public void createOrderInvalidHash() {
        String[] ingredients = new String[]{"invalid_hash", "invalid_hash"};
        OrderPojo order = new OrderPojo(ingredients);

        //Пробуем создать заказ, передавая невалидные id ингредиентов и проверям код ответа
        Response response = CreateOrder.createOrder(null, order);
        response.then().statusCode(400);
    }
}
