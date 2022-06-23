import api.Order;
import api.CreateUser;
import api.DeleteUser;
import api.LoginUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class OrderCreationTest extends BaseTest {
    private static final String email = "harry-potter@yandex.ru";
    private static final String password = "password";
    private static final String name = "harry";

    @Before
    public void createNewUser() {
        //Создаем пользователя
        CreteUser userPojo = new CreteUser(email, password, name);
        Response createUserResponse = CreateUser.createUser(userPojo);
        createUserResponse.then().assertThat().statusCode(200);
    }

    @After
    public void deleteUser() {
        UserLogin userLogin = new UserLogin(email, password);
        //Удаляем созданного пользователя
        DeleteUser.deleteUser(userLogin);
    }

    @Test
    @DisplayName("Creating an order when user is authorized")
    public void createOrderAuthUser() {
        String[] ingredients = new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"};
        model.Order order = new model.Order(ingredients);

        //Логиним созданного пользователя и получаем токен
        UserLogin userLogin = new UserLogin(email, password);
        UserToken accessToken = LoginUser.getToken(userLogin);

        //Создаем заказ, передаем токен и проверям код ответа
        Response response = Order.createOrder(accessToken.getToken(), order);
        response.then().statusCode(200);
    }

    @Test
    @DisplayName("Creating an order when user is unauthorized")
    public void createOrderUnAuthUser() {
        String[] ingredients = new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"};
        model.Order order = new model.Order(ingredients);

        //Пробуем создать заказ, не передавая токен и проверям код ответа
        Response response = Order.createOrder(null, order);
        response.then().statusCode(200);
    }

    @Test
    @DisplayName("Creating an order with no ingredients")
    public void createOrderWithNoIngredients() {
        String[] ingredients = new String[]{};
        model.Order order = new model.Order(ingredients);

        //Пробуем создать заказ без ингредиентов и проверям код ответа
        Response response = Order.createOrder(null, order);
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Creating an order with invalid hash")
    public void createOrderInvalidHash() {
        String[] ingredients = new String[]{"invalid_hash", "invalid_hash"};
        model.Order order = new model.Order(ingredients);

        //Пробуем создать заказ, передавая невалидные id ингредиентов и проверям код ответа
        Response response = Order.createOrder(null, order);
        response.then().statusCode(400);
    }
}
