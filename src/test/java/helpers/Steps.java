package helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;

public class Steps {
    private final ApiHelper api;
    public Steps(ApiHelper api) {
        this.api = api;
    }
    @Step("Compare actual status code with expected")
    public void compareStatusCode(Response response, Integer statusCode) {
        response.then().assertThat().statusCode(statusCode);
    }

    @Step("Check if actual field value equals to expected value")
    public void checkResponseFieldValue(Response response, String key, Object value) {
        response.then().assertThat().body(key, equalTo(value));
    }

    @Step("Get courier id from response body")
    public Integer getCourierId(Response response) {
        return response.path("id");
    }

    @Step("Get order id from response body")
    public Integer getOrderId(Response response) {
        return response.path("order.id");
    }

    @Step("Get order id from response body")
    public Integer getOrderTrackId(Response response) {
        return response.path("track");
    }

    @Step("Remove created courier")
    public void deleteCourier(String path) {
        Response loginResponse = api.sendLoginCourierRequest(path);
        Integer id = getCourierId(loginResponse);
        Response deleteResponse = api.sendDeleteCourierRequest(id);
        compareStatusCode(deleteResponse, 200);
    }

    @Step("Create courier and return courier id")
    public Integer createCourier(String path) {
        api.sendCreateCourierRequest(path);
        Response loginResponse = api.sendLoginCourierRequest(path);
        return getCourierId(loginResponse);
    }

    @Step("Create order and return order id")
    public Integer createOrder(String path) {
        Response response = api.sendPostOrderRequest(path);
        return getOrderTrackId(response);
    }

    @Step("Cancel order")
    public void cancelOrder(Integer orderId) {
        api.sendCancelOrderRequest(orderId);
    }
}
