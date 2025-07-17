package orders;

import com.google.gson.Gson;
import helpers.ApiHelper;
import helpers.Steps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.Courier;
import model.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class AcceptOrderTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper API = new ApiHelper(BASE_URI);
    private static final Steps STEPS = new Steps(API);
    private static final Gson GSON = new Gson();

    @Test
    @Description("Check order acception")
    public void success() {
        Courier courier = new Courier("polter", "vermilion", "polter");
        String jsonCourier = GSON.toJson(courier);
        Order order = new Order("Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                new ArrayList<>(List.of("BLACK"))
                );
        String jsonOrder = GSON.toJson(order);
        Integer courierId = STEPS.createCourier(jsonCourier);
        Integer trackId = STEPS.createOrder(jsonOrder);
        Response orderResponse = API.sendGetOrderByIdRequest(trackId);
        Integer orderId = STEPS.getOrderId(orderResponse);
        Response acceptResponse = API.sendAcceptOrderRequest(orderId, courierId);
        STEPS.compareStatusCode(acceptResponse, 200);
        STEPS.deleteCourier(jsonCourier);
        STEPS.cancelOrder(trackId);
    }

    @Test
    @Description("Check that it's impossible to accept order without providing of courierId")
    public void withoutCourierId() {
        Order order = new Order("Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                new ArrayList<>(List.of("BLACK"))
        );
        String jsonOrder = GSON.toJson(order);
        Integer trackId = STEPS.createOrder(jsonOrder);
        Response orderResponse = API.sendGetOrderByIdRequest(trackId);
        Integer orderId = STEPS.getOrderId(orderResponse);
        Response acceptResponse = API.sendAcceptOrderRequest(orderId, null);
        STEPS.compareStatusCode(acceptResponse, 400);
        STEPS.cancelOrder(trackId);
    }

    @Test
    @Description("Check that it's impossible to accept order with incorrect courierId")
    public void incorrecCouriertId() {
        Order order = new Order("Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                new ArrayList<>(List.of("BLACK"))
        );
        String jsonOrder = GSON.toJson(order);
        Integer trackId = STEPS.createOrder(jsonOrder);
        Response orderResponse = API.sendGetOrderByIdRequest(trackId);
        Integer orderId = STEPS.getOrderId(orderResponse);
        Response acceptResponse = API.sendAcceptOrderRequest(orderId, 99999999);
        STEPS.compareStatusCode(acceptResponse, 404);
        STEPS.cancelOrder(trackId);
    }

    @Test
    @Description("Check that it's impossible to accept order without providing orderId")
    public void emptyOrderNumber() {
        Courier courier = new Courier("polter", "vermilion", "polter");
        String jsonCourier = GSON.toJson(courier);
        Integer courierId = STEPS.createCourier(jsonCourier);
        Response response = API.sendAcceptOrderRequest(null, courierId);
        STEPS.compareStatusCode(response, 500);
        STEPS.deleteCourier(jsonCourier);
    }

    @Test
    @Description("Check that it's impossible to accept order with incorrect orderId")
    public void incorrectOrderNumber() {
        Courier courier = new Courier("polter", "vermilion", "polter");
        String jsonCourier = GSON.toJson(courier);
        Integer courierId = STEPS.createCourier(jsonCourier);
        Response response = API.sendAcceptOrderRequest(99999999, courierId);
        STEPS.compareStatusCode(response, 404);
        STEPS.deleteCourier(jsonCourier);
    }
}
