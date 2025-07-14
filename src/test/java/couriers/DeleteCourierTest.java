package couriers;

import helpers.ApiHelper;
import helpers.Steps;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class DeleteCourierTest {
    private static final String baseURI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper api = new ApiHelper(baseURI);
    private static final Steps steps = new Steps(api);

    @Test
    public void success() {
        Integer courierId = steps.createCourier("src/test/resources/courier/create/courier.json");
        Response deleteResponse = api.sendDeleteCourierRequest(courierId);
        steps.compareStatusCode(deleteResponse, 200);
        steps.checkResponseFieldValue(deleteResponse, "ok", true);
    }

    @Test
    public void failed() {
        Response deleteResponse = api.sendDeleteCourierRequest(999999999);
        steps.compareStatusCode(deleteResponse, 404);
    }

    @Test
    public void withoutId() {
        Response deleteResponse = api.sendDeleteCourierRequest(null);
        steps.compareStatusCode(deleteResponse, 500);
    }
}
