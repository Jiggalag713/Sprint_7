package orders;

import helpers.ApiHelper;
import helpers.Steps;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class AcceptOrderTest {
    private static final String baseURI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper api = new ApiHelper(baseURI);
    private static final Steps steps = new Steps(api);

    @Test
    public void success() {
        Integer courierId = steps.createCourier("src/test/resources/courier/create/courier.json");
        Integer trackId = steps.createOrder("src/test/resources/order/create/blackColor.json");
        Response orderResponse = api.sendGetOrderByIdRequest(trackId);
        Integer orderId = steps.getOrderId(orderResponse);
        Response acceptResponse = api.sendAcceptOrderRequest(orderId, courierId);
        steps.compareStatusCode(acceptResponse, 200);
        steps.deleteCourier("src/test/resources/courier/create/courier.json");
        steps.cancelOrder(orderId);
    }

    @Test
    public void withoutCourierId() {
        Integer trackId = steps.createOrder("src/test/resources/order/create/blackColor.json");
        Response orderResponse = api.sendGetOrderByIdRequest(trackId);
        Integer orderId = steps.getOrderId(orderResponse);
        Response acceptResponse = api.sendAcceptOrderRequest(orderId, null);
        steps.compareStatusCode(acceptResponse, 400);
        steps.cancelOrder(orderId);
    }

    @Test
    public void incorrecCouriertId() {
        Integer trackId = steps.createOrder("src/test/resources/order/create/blackColor.json");
        Response orderResponse = api.sendGetOrderByIdRequest(trackId);
        Integer orderId = steps.getOrderId(orderResponse);
        Response acceptResponse = api.sendAcceptOrderRequest(orderId, 99999999);
        steps.compareStatusCode(acceptResponse, 404);
        steps.cancelOrder(orderId);
    }

    @Test
    public void emptyOrderNumber() {
        Integer courierId = steps.createCourier("src/test/resources/courier/create/courier.json");
        Response response = api.sendAcceptOrderRequest(null, courierId);
        steps.compareStatusCode(response, 500);
        steps.deleteCourier("src/test/resources/courier/create/courier.json");
    }

    @Test
    public void incorrectOrderNumber() {
        Integer courierId = steps.createCourier("src/test/resources/courier/create/courier.json");
        Response response = api.sendAcceptOrderRequest(99999999, courierId);
        steps.compareStatusCode(response, 404);
        steps.deleteCourier("src/test/resources/courier/create/courier.json");
    }
}
