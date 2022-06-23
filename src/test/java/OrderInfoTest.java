import api.CreateUser;
import api.DeleteUser;
import api.LoginUser;
import api.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.CreteUser;
import model.UserLogin;
import model.UserToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class OrderInfoTest extends BaseTest {
    private static final String email = "harry-potter@yandex.ru";
    private static final String password = "password";
    private static final String name = "harry";

    @Before
    public void createNewUser() {
        //Создаем пользователя
        CreteUser userPojo = new CreteUser(email, password, name);
        Response createUserResponse = CreateUser.createUser(userPojo);
        createUserResponse.then().assertThat().statusCode(200);
    }

    @After
    public void deleteUser() {
        //Логиним созданного пользователя и получаем токен
        UserLogin userLogin = new UserLogin(email, password);
        DeleteUser.deleteUser(userLogin);
    }

    @Test
    @DisplayName("Getting the user's orders' data when the user is authorized")
    public void getOrderInfoAuthUser() {
        //Логиним созданного пользователя и получаем токен
        UserLogin userLogin = new UserLogin(email, password);
        UserToken accessToken = LoginUser.getToken(userLogin);
        Response getOrderResponse = Order.getOrderInfo(accessToken);
        getOrderResponse.then().statusCode(200);
    }

    @Test
    @DisplayName("Getting the user's orders' data when the user is unauthorized")
    public void createOrderUnAuthUser() {
        //Пробуем запросить заказы и проверям код ответа
        Response response = Order.getOrderInfo(null);
        response.then().statusCode(401).and().body("message", equalTo("You should be authorised"));
    }
}
