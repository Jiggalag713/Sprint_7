package helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.io.File;

import static io.restassured.RestAssured.given;

public class ApiHelper {
    String baseURI;

    public ApiHelper(String baseURI) {
        this.baseURI = baseURI;
    }

    @Step("Send POST to /api/v1/courier")
    public Response sendCreateCourierRequest(String json) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(baseURI + "/api/v1/courier");
    }

    @Step("Send POST to /api/v1/courier/login")
    public Response sendLoginCourierRequest(String json) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(baseURI + "/api/v1/courier/login");
    }

    @Step("Send DELETE to /api/v1/courier/:id")
    public Response sendDeleteCourierRequest(Integer id) {
        return given()
                .header("Content-type", "application/json")
                .body("{\"id\": " + id + " }")
                .when()
                .delete(baseURI + "/api/v1/courier/" + id);
    }

    @Step("Send POST to /api/v1/orders")
    public Response sendPostOrderRequest(String json) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(baseURI + "/api/v1/orders");
    }

    @Step("Send GET to /api/v1/orders")
    public Response sendGetOrderListRequest() {
        return given()
                .get(baseURI + "/api/v1/orders");
    }

    @Step("Send PUT to /api/v1/orders/accept/:id")
    public Response sendAcceptOrderRequest(Integer orderId, Integer courierId) {
        return given()
                .queryParam("courierId", courierId)
                .put(baseURI + "/api/v1/orders/accept/" + orderId);
    }

    @Step("Send PUT to /api/v1/orders/cancel")
    public void sendCancelOrderRequest(Integer trackId) {
        given()
                .queryParam("track", trackId)
                .put(baseURI + "/api/v1/orders/cancel");
    }

    @Step("Send GET to /api/v1/orders/track")
    public Response sendGetOrderByIdRequest(Integer orderId) {
        return given()
                .queryParam("t", orderId)
                .get(baseURI + "/api/v1/orders/track");
    }
}
