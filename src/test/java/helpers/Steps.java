package helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;

public class Steps {
    private final ApiHelper API;
    public Steps(ApiHelper api) {
        this.API = api;
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
    public void deleteCourier(String json) {
        Response loginResponse = API.sendLoginCourierRequest(json);
        Integer id = getCourierId(loginResponse);
        Response deleteResponse = API.sendDeleteCourierRequest(id);
        compareStatusCode(deleteResponse, 200);
    }

    @Step("Create courier and return courier id")
    public Integer createCourier(String path) {
        API.sendCreateCourierRequest(path);
        Response loginResponse = API.sendLoginCourierRequest(path);
        return getCourierId(loginResponse);
    }

    @Step("Create order and return order id")
    public Integer createOrder(String json) {
        Response response = API.sendPostOrderRequest(json);
        return getOrderTrackId(response);
    }

    @Step("Cancel order")
    public void cancelOrder(Integer trackId) {
        API.sendCancelOrderRequest(trackId);
    }
}
