import io.restassured.RestAssured;
import org.junit.BeforeClass;

public class BaseTest {
    @BeforeClass
    public static void setUpBaseUri() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
}
