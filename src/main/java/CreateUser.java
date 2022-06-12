import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import io.qameta.allure.Step;
public class CreateUser {
    @Step("Creating a new user")
    public Response createUser(Object body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post("/api/auth/register");
    }
}
