import api.CreateUser;
import api.DeleteUser;
import api.LoginUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserUpdateTest extends BaseTest {
    private static final String email = "harry-potter@yandex.ru";
    private static final String newEmail = "ron_weasley@yandex.ru";
    private static final String password = "password";
    private static final String newPassword = "new_password";
    private static final String name = "harry";
    private static final String newName = "new_name";

    @BeforeClass
    public static void cleanseUsers() {
        UserLoginPojo user = new UserLoginPojo(email, password);
        DeleteUser.deleteUser(user);

        user = new UserLoginPojo(newEmail, password);
        DeleteUser.deleteUser(user);

        user = new UserLoginPojo(email, newPassword);
        DeleteUser.deleteUser(user);
    }

    @Before
    public void createNewUser() {
        //Создаем пользователя
        CreteUserPojo userPojo = new CreteUserPojo(email, password, name);
        Response createUserResponse = CreateUser.createUser(userPojo);
        createUserResponse.then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Updating email should return 200")
    public void updateEmail() {
        //Логиним созданного пользователя и получаем токен
        UserLoginPojo userLoginPojo = new UserLoginPojo(email, password);
        UserTokenPojo accessToken = LoginUser.loginAndGetToken(userLoginPojo);

        //Изменяем email и проверяем код ответа
        UserEmailUpdate update = new UserEmailUpdate(newEmail);
        Response response =
                given()
                        .header("Authorization", accessToken.getToken())
                        .header("Content-type", "application/json")
                        .and()
                        .body(update)
                        .when()
                        .patch("/api/auth/user");
        response.then().statusCode(200);

        //Удаляем пользователя
        UserLoginPojo userWithUpdatedEmail = new UserLoginPojo(newEmail, password);
        DeleteUser.deleteUser(userWithUpdatedEmail);
    }

    @Test
    @DisplayName("Updating password should return 200")
    public void updatePassword() {
        //Логиним созданного пользователя и получаем токен
        UserLoginPojo userLoginPojo = new UserLoginPojo(email, password);
        UserTokenPojo accessToken = LoginUser.loginAndGetToken(userLoginPojo);

        //Изменяем password и проверяем код ответа
        UserPasswordUpdate update = new UserPasswordUpdate(newPassword);
        Response response =
                given()
                        .header("Authorization", accessToken.getToken())
                        .header("Content-type", "application/json")
                        .and()
                        .body(update)
                        .when()
                        .patch("/api/auth/user");
        response.then().statusCode(200);

        //Удаляем пользователя
        UserLoginPojo userWithUpdatedPassword = new UserLoginPojo(email, newPassword);
        DeleteUser.deleteUser(userWithUpdatedPassword);
    }

    @Test
    @DisplayName("Updating name should return 200")
    public void updateName() {
        //Логиним созданного пользователя и получаем токен
        UserLoginPojo userLoginPojo = new UserLoginPojo(email, password);
        UserTokenPojo accessToken = LoginUser.loginAndGetToken(userLoginPojo);

        //Изменяем name и проверяем код ответа
        UserNameUpdate update = new UserNameUpdate(newName);
        Response response =
                given()
                        .header("Authorization", accessToken.getToken())
                        .header("Content-type", "application/json")
                        .and()
                        .body(update)
                        .when()
                        .patch("/api/auth/user");
        response.then().statusCode(200);

        //Удаляем пользователя
        UserLoginPojo userWithUpdatedPassword = new UserLoginPojo(email, password);
        DeleteUser.deleteUser(userWithUpdatedPassword);
    }

    @Test
    @DisplayName("Updating name of an unauthorized user should return 401")
    public void updateUnauthorizedName() {
        //Пробуем изменить имя, не авторизуясь, и проверяем код ответа
        UserNameUpdate update = new UserNameUpdate(newName);
        Response response =
                given()
                        //.header("Authorization", accessToken.getToken())
                        .header("Content-type", "application/json")
                        .and()
                        .body(update)
                        .when()
                        .patch("/api/auth/user");
        response.then().statusCode(401).and().body("message", equalTo("You should be authorised"));

        //Удаляем пользователя
        UserLoginPojo userWithUpdatedPassword = new UserLoginPojo(email, password);
        DeleteUser.deleteUser(userWithUpdatedPassword);
    }
}
