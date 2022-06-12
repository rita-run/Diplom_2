import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;

public class OrderInfoTest {
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
    @DisplayName("Getting the user's orders' data when the user is authorized")
    public void CreateOrderAuthUser(){
        //Логиним созданного пользователя и получаем токен
        UserLogin userLogin = new UserLogin(email, password);

        UserTokenPojo accessToken = given()
                .header("Content-type", "application/json")
                .body(userLogin)
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
    public void CreateOrderUnAuthUser(){
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
