package orders;

import helpers.ApiHelper;
import helpers.Steps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;

public class ListOrdersTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper API = new ApiHelper(BASE_URI);
    private static final Steps STEPS = new Steps(API);

    @Test
    @Description("Check that getting of order list returns statusCode 200 and not empty body")
    public void checkOrderList() {
        Response response = API.sendGetOrderListRequest();
        response.then().assertThat().body("orders", not(equalTo("")));
        STEPS.compareStatusCode(response, 200);
    }
}
