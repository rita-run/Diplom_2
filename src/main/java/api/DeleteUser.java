package api;

import io.qameta.allure.Step;
import model.UserToken;

import static io.restassured.RestAssured.given;

public class DeleteUser {
    @Step("Deleting a user")
    public static void deleteUser(Object userLogin) {
        //Логиним созданного пользователя и получаем токен
        UserToken accessToken = LoginUser.getToken(userLogin);
        if (accessToken.getToken() != null) {
            //Удаляем созданного пользователя
            given()
                    .header("Authorization", accessToken.getToken())
                    .header("Content-type", "application/json")
                    .delete("/api/auth/user");
        }
    }
}