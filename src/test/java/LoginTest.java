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

public class LoginTest extends BaseTest {
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
    @DisplayName("Login with an existent user")
    public void loginUser() {
        UserLoginPojo userLoginPojo = new UserLoginPojo(email, password);
        Response response = LoginUser.loginUser(userLoginPojo);
        response.then().statusCode(200);
    }

    @Test
    @DisplayName("Login with an invalid password")
    public void loginInvalidPassword() {
        UserLoginPojo userLoginPojo = new UserLoginPojo(email, "invalid_password");
        Response response = LoginUser.loginUser(userLoginPojo);
        response.then().statusCode(401).and().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Login with an invalid email should return 401")
    public void loginInvalidEmail() {
        UserLoginPojo userLoginPojo = new UserLoginPojo("invalid_email", password);
        Response response = LoginUser.loginUser(userLoginPojo);
        response.then().statusCode(401).and().body("message", equalTo("email or password are incorrect"));
    }
}
