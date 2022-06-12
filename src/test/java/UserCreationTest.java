import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import io.qameta.allure.junit4.DisplayName;

public class UserCreationTest {
    String email = "harry-potter@yandex.ru";
    String password = "password";
    String name = "harry";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @After
    public void deleteUser(){
        UserLogin userLogin = new UserLogin(email, password);

        UserTokenPojo accessToken = given()
                .header("Content-type", "application/json")
                .body(userLogin)
                .post("/api/auth/login")
                .thenReturn()
                .body()
                .as(UserTokenPojo.class);

        //Удаляем созданного пользователя
        Response deleteResponse = given()
                .header("Authorization", accessToken.getToken())
                .header("Content-type", "application/json")
                .delete("/api/auth/user");
    }

    @Test
    @DisplayName("A new user creation test")
    public void createNewUser(){
        //Создаем пользователя
        CreteUserPojo userPojo = new CreteUserPojo(email, password, name);
        CreateUser user = new CreateUser();
        Response createUserResponse = user.createUser(userPojo);
        createUserResponse.then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("An already existing user creation test")
    public void createExistentUser(){
        //Создаем пользователя
        CreteUserPojo userPojo = new CreteUserPojo(email, password, name);
        CreateUser user = new CreateUser();
        Response createUserResponse = user.createUser(userPojo);
        createUserResponse.then().assertThat().statusCode(200);

        //Пробуем создать этого же пользователя еще раз, проверяем код ответа и сообщение об ошибке
        Response createExistentUserResponse = user.createUser(userPojo);
        createExistentUserResponse.then().assertThat().statusCode(403).and().body("message", equalTo("User already exists"));
    }
}
