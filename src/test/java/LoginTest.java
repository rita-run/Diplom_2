import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LoginTest {
    String email = "harry-potter@yandex.ru";
    String password = "password";
    String name = "harry";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @Before
    public void createNewUser() {
        //Создаем пользователя
        CreteUserPojo userPojo = new CreteUserPojo(email, password, name);
        CreateUser user = new CreateUser();
        Response createUserResponse = user.createUser(userPojo);
        createUserResponse.then().assertThat().statusCode(200);
    }

    @After
    public void deleteUser(){
        //Логиним созданного пользователя и получаем токен
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
    @DisplayName("Login with an existent user")
    public void loginUser(){
        UserLogin userLogin = new UserLogin(email, password);

        Response response =
                given()
                        //.header("Authorization", accessToken.getToken())
                        .header("Content-type", "application/json")
                        .and()
                        .body(userLogin)
                        .when()
                        .post("/api/auth/login");
        response.then().statusCode(200);
    }

    @Test
    @DisplayName("Login with an invalid password")
    public void loginInvalidPassword(){
        UserLogin userLogin = new UserLogin(email, "invalid_password");

        Response response =
                given()
                        //.header("Authorization", accessToken.getToken())
                        .header("Content-type", "application/json")
                        .and()
                        .body(userLogin)
                        .when()
                        .post("/api/auth/login");
        response.then().statusCode(401).and().body("message", equalTo("email or password are incorrect"));
    }
}
