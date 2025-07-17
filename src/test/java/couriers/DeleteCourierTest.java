package couriers;

import helpers.ApiHelper;
import helpers.Steps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class DeleteCourierTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper API = new ApiHelper(BASE_URI);
    private static final Steps STEPS = new Steps(API);

    @Test
    @Description("Check correct removing of courier")
    public void success() {
        Integer courierId = STEPS.createCourier("src/test/resources/courier/create/courier.json");
        Response deleteResponse = API.sendDeleteCourierRequest(courierId);
        STEPS.compareStatusCode(deleteResponse, 200);
        STEPS.checkResponseFieldValue(deleteResponse, "ok", true);
    }

    @Test
    @Description("Check courier removing by incorrect courierId")
    public void failed() {
        Response deleteResponse = API.sendDeleteCourierRequest(999999999);
        STEPS.compareStatusCode(deleteResponse, 404);
    }

    @Test
    @Description("Check removing courier without providing courierId in delete request")
    public void withoutId() {
        Response deleteResponse = API.sendDeleteCourierRequest(null);
        STEPS.compareStatusCode(deleteResponse, 500);
    }
}
