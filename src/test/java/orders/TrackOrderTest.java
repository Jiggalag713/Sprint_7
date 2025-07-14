package orders;

import helpers.ApiHelper;
import helpers.Steps;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;

public class TrackOrderTest {
    private static final String baseURI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper api = new ApiHelper(baseURI);
    private static final Steps steps = new Steps(api);

    @Test
    public void success() {
        Integer trackId = steps.createOrder("src/test/resources/order/create/blackColor.json");
        Response orderResponse = api.sendGetOrderByIdRequest(trackId);
        orderResponse.then().assertThat().body("order", not(equalTo("")));
        steps.compareStatusCode(orderResponse, 200);
    }

    @Test
    public void emptyNumber() {
        Response orderResponse = api.sendGetOrderByIdRequest(null);
        steps.compareStatusCode(orderResponse, 400);
    }

    @Test void incorrectNumber() {
        Response orderResponse = api.sendGetOrderByIdRequest(99999999);
        steps.compareStatusCode(orderResponse, 404);
    }
}
