import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class NegativeUserCreationTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    @DisplayName("Create a user with a field value missing")
    public void createInvalidUser() {
        String json = "{\"email\": \"harry-potter@yandex.ru\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/auth/register");
        response.then().assertThat()
                .statusCode(403).and().body("message", equalTo("Email, password and name are required fields"));
    }
}
