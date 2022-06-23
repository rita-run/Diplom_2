import api.CreateUser;
import api.DeleteUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.*;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserCreationTest extends BaseTest {
    private static final String email = "harry-potter@yandex.ru";
    private static final String password = "password";
    private static final String name = "harry";

    @BeforeClass
    public static void cleanseUsers() {
        UserLoginPojo user = new UserLoginPojo(email, password);
        DeleteUser.deleteUser(user);
    }

    @After
    public void deleteUser() {
        //Логиним созданного пользователя и получаем токен
        UserLoginPojo userLoginPojo = new UserLoginPojo(email, password);
        DeleteUser.deleteUser(userLoginPojo);
    }

    @Test
    @DisplayName("A new user creation test")
    public void createNewUser() {
        //Создаем пользователя
        CreteUserPojo userPojo = new CreteUserPojo(email, password, name);
        Response createUserResponse = CreateUser.createUser(userPojo);
        createUserResponse.then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("An already existing user creation test")
    public void createExistentUser() {
        //Создаем пользователя
        CreteUserPojo userPojo = new CreteUserPojo(email, password, name);
        Response createUserResponse = CreateUser.createUser(userPojo);
        createUserResponse.then().assertThat().statusCode(200);

        //Пробуем создать этого же пользователя еще раз, проверяем код ответа и сообщение об ошибке
        Response createExistentUserResponse = CreateUser.createUser(userPojo);
        createExistentUserResponse.then().assertThat().statusCode(403).and().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Creating a user with a missing field should return 403")
    public void creatingUserWithMissingFieldShouldReturn403() {
        //Создаем пользователя
        CreteUserPojo userPojo = new CreteUserPojo(email, null, name);
        Response createUserResponse = CreateUser.createUser(userPojo);
        //    Пробуем создать пользователя, не передавая пароля, проверяем код ответа и сообщение об ошибке
        createUserResponse.then().assertThat().statusCode(403).and().body("message", equalTo("Email, password and name are required fields"));
    }
}
