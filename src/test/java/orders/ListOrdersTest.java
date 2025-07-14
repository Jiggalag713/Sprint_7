package orders;

import helpers.ApiHelper;
import helpers.Steps;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;

public class ListOrdersTest {
    private static final String baseURI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper api = new ApiHelper(baseURI);
    private static final Steps steps = new Steps(api);

    @Test
    public void checkOrderList() {
        Response response = api.sendGetOrderListRequest();
        response.then().assertThat().body("orders", not(equalTo("")));
        steps.compareStatusCode(response, 200);
    }
}
