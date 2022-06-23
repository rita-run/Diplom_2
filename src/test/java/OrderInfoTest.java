import api.CreateUser;
import api.DeleteUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.CreteUserPojo;
import model.UserLoginPojo;
import model.UserTokenPojo;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class OrderInfoTest extends BaseTest {
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
        //Логиним созданного пользователя и получаем токен
        UserLoginPojo userLoginPojo = new UserLoginPojo(email, password);
        DeleteUser.deleteUser(userLoginPojo);
    }

    @Test
    @DisplayName("Getting the user's orders' data when the user is authorized")
    public void createOrderAuthUser() {
        //Логиним созданного пользователя и получаем токен
        UserLoginPojo userLoginPojo = new UserLoginPojo(email, password);

        UserTokenPojo accessToken = given()
                .header("Content-type", "application/json")
                .body(userLoginPojo)
                .post("/api/auth/login")
                .thenReturn()
                .body()
                .as(UserTokenPojo.class);

        //Запрашиваем заказы и проверям код ответа
        Response response =
                given()
                        .header("Authorization", accessToken.getToken())
                        .header("Content-type", "application/json")
                        .and()
                        .when()
                        .get("/api/orders");
        response.then().statusCode(200);
    }

    @Test
    @DisplayName("Getting the user's orders' data when the user is unauthorized")
    public void createOrderUnAuthUser() {
        //Пробуем запросить заказы и проверям код ответа
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .when()
                        .get("/api/orders");
        response.then().statusCode(401);
    }
}
