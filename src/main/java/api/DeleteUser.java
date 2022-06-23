package api;

import io.qameta.allure.Step;
import model.UserTokenPojo;

import static io.restassured.RestAssured.given;

public class DeleteUser {
    @Step("Deleting a user")
    public static void deleteUser(Object userLogin) {
        //Логиним созданного пользователя и получаем токен
        UserTokenPojo accessToken = LoginUser.loginAndGetToken(userLogin);
        if (accessToken.getToken() != null) {
            //Удаляем созданного пользователя
            given()
                    .header("Authorization", accessToken.getToken())
                    .header("Content-type", "application/json")
                    .delete("/api/auth/user");
        }
    }
}