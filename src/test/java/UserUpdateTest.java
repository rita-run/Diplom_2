import api.CreateUser;
import api.DeleteUser;
import api.LoginUser;
import api.UpdateUserInfo;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserUpdateTest extends BaseTest {
    private static final String email = "harry-potter@yandex.ru";
    private static final String newEmail = "ron_weasley@yandex.ru";
    private static final String password = "password";
    private static final String newPassword = "new_password";
    private static final String name = "harry";
    private static final String newName = "new_name";

    @Before
    public void createNewUser() {
        //Создаем пользователя
        CreteUser userPojo = new CreteUser(email, password, name);
        Response createUserResponse = CreateUser.createUser(userPojo);
        createUserResponse.then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Updating email should return 200")
    public void updateEmail() {
        //Логиним созданного пользователя и получаем токен
        UserLogin userLogin = new UserLogin(email, password);
        UserToken accessToken = LoginUser.getToken(userLogin);

        //Изменяем email и проверяем код ответа
        UserEmailUpdate update = new UserEmailUpdate(newEmail);
        Response response = UpdateUserInfo.updateUserInfo(accessToken, update);
        response.then().statusCode(200);

        //Удаляем пользователя
        UserLogin userWithUpdatedEmail = new UserLogin(newEmail, password);
        DeleteUser.deleteUser(userWithUpdatedEmail);
    }

    @Test
    @DisplayName("Updating password should return 200")
    public void updatePassword() {
        //Логиним созданного пользователя и получаем токен
        UserLogin userLogin = new UserLogin(email, password);
        UserToken accessToken = LoginUser.getToken(userLogin);

        //Изменяем password и проверяем код ответа
        UserPasswordUpdate update = new UserPasswordUpdate(newPassword);
        Response response = UpdateUserInfo.updateUserInfo(accessToken, update);
        response.then().statusCode(200);

        //Удаляем пользователя
        UserLogin userWithUpdatedPassword = new UserLogin(email, newPassword);
        DeleteUser.deleteUser(userWithUpdatedPassword);
    }

    @Test
    @DisplayName("Updating name should return 200")
    public void updateName() {
        //Логиним созданного пользователя и получаем токен
        UserLogin userLogin = new UserLogin(email, password);
        UserToken accessToken = LoginUser.getToken(userLogin);

        //Изменяем name и проверяем код ответа
        UserNameUpdate update = new UserNameUpdate(newName);
        Response response = UpdateUserInfo.updateUserInfo(accessToken, update);
        response.then().statusCode(200);

        //Удаляем пользователя
        UserLogin userWithUpdatedPassword = new UserLogin(email, password);
        DeleteUser.deleteUser(userWithUpdatedPassword);
    }

    @Test
    @DisplayName("Updating name of an unauthorized user should return 401")
    public void updateUnauthorizedName() {
        //Пробуем изменить имя, не авторизуясь, и проверяем код ответа
        UserNameUpdate update = new UserNameUpdate(newName);
        Response response = UpdateUserInfo.updateUserInfo(null, update);
        response.then().statusCode(401).and().body("message", equalTo("You should be authorised"));

        //Удаляем пользователя
        UserLogin userWithUpdatedPassword = new UserLogin(email, password);
        DeleteUser.deleteUser(userWithUpdatedPassword);
    }
}
