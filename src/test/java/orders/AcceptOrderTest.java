package orders;

import helpers.ApiHelper;
import helpers.Steps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class AcceptOrderTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper API = new ApiHelper(BASE_URI);
    private static final Steps STEPS = new Steps(API);

    @Test
    @Description("Check order acception")
    public void success() {
        Integer courierId = STEPS.createCourier("src/test/resources/courier/create/courier.json");
        Integer trackId = STEPS.createOrder("src/test/resources/order/create/blackColor.json");
        Response orderResponse = API.sendGetOrderByIdRequest(trackId);
        Integer orderId = STEPS.getOrderId(orderResponse);
        Response acceptResponse = API.sendAcceptOrderRequest(orderId, courierId);
        STEPS.compareStatusCode(acceptResponse, 200);
        STEPS.deleteCourier("src/test/resources/courier/create/courier.json");
        STEPS.cancelOrder(trackId);
    }

    @Test
    @Description("Check that it's impossible to accept order without providing of courierId")
    public void withoutCourierId() {
        Integer trackId = STEPS.createOrder("src/test/resources/order/create/blackColor.json");
        Response orderResponse = API.sendGetOrderByIdRequest(trackId);
        Integer orderId = STEPS.getOrderId(orderResponse);
        Response acceptResponse = API.sendAcceptOrderRequest(orderId, null);
        STEPS.compareStatusCode(acceptResponse, 400);
        STEPS.cancelOrder(trackId);
    }

    @Test
    @Description("Check that it's impossible to accept order with incorrect courierId")
    public void incorrecCouriertId() {
        Integer trackId = STEPS.createOrder("src/test/resources/order/create/blackColor.json");
        Response orderResponse = API.sendGetOrderByIdRequest(trackId);
        Integer orderId = STEPS.getOrderId(orderResponse);
        Response acceptResponse = API.sendAcceptOrderRequest(orderId, 99999999);
        STEPS.compareStatusCode(acceptResponse, 404);
        STEPS.cancelOrder(trackId);
    }

    @Test
    @Description("Check that it's impossible to accept order without providing orderId")
    public void emptyOrderNumber() {
        Integer courierId = STEPS.createCourier("src/test/resources/courier/create/courier.json");
        Response response = API.sendAcceptOrderRequest(null, courierId);
        STEPS.compareStatusCode(response, 500);
        STEPS.deleteCourier("src/test/resources/courier/create/courier.json");
    }

    @Test
    @Description("Check that it's impossible to accept order with incorrect orderId")
    public void incorrectOrderNumber() {
        Integer courierId = STEPS.createCourier("src/test/resources/courier/create/courier.json");
        Response response = API.sendAcceptOrderRequest(99999999, courierId);
        STEPS.compareStatusCode(response, 404);
        STEPS.deleteCourier("src/test/resources/courier/create/courier.json");
    }
}
