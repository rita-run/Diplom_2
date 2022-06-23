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

public class LoginTest extends BaseTest {
    private static final String email = "harry-potter@yandex.ru";
    private static final String password = "password";
    private static final String name = "harry";

    @Before
    public void createNewUser() {
        //Создаем пользователя
        CreteUser user = new CreteUser(email, password, name);
        Response createUserResponse = CreateUser.createUser(user);
        createUserResponse.then().assertThat().statusCode(200);
    }

    @After
    public void deleteUser() {
        UserLogin userLogin = new UserLogin(email, password);
        //Удаляем созданного пользователя
        DeleteUser.deleteUser(userLogin);
    }

    @Test
    @DisplayName("Login with an existent user")
    public void loginUser() {
        UserLogin userLogin = new UserLogin(email, password);
        Response response = LoginUser.loginUser(userLogin);
        response.then().statusCode(200);
    }

    @Test
    @DisplayName("Login with an invalid password")
    public void loginInvalidPassword() {
        UserLogin userLogin = new UserLogin(email, "invalid_password");
        Response response = LoginUser.loginUser(userLogin);
        response.then().statusCode(401).and().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Login with an invalid email should return 401")
    public void loginInvalidEmail() {
        UserLogin userLogin = new UserLogin("invalid_email", password);
        Response response = LoginUser.loginUser(userLogin);
        response.then().statusCode(401).and().body("message", equalTo("email or password are incorrect"));
    }
}
