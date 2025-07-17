package couriers;

import helpers.ApiHelper;
import helpers.Steps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CreateCourierTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper API = new ApiHelper(BASE_URI);
    private static final Steps STEPS = new Steps(API);

    @Test
    @Description("Check that courier creation returns 201 status code and correct body")
    void createCourierResponse201AndResponseCorrect() {
        try {
            Response response = API.sendCreateCourierRequest("src/test/resources/courier/create/courier.json");
            STEPS.compareStatusCode(response, 201);
            STEPS.checkResponseFieldValue(response, "ok", true);
        } finally {
            STEPS.deleteCourier("src/test/resources/courier/create/courier.json");
        }
    }

    @Test
    @Description("Check that it's impossible to create two identic couriers")
    void createSameCouriersImpossible() {
        try {
            API.sendCreateCourierRequest("src/test/resources/courier/create/courier.json");
            Response failedResponse = API.sendCreateCourierRequest("src/test/resources/courier/create/courier.json");
            STEPS.compareStatusCode(failedResponse, 409);
        } finally {
            STEPS.deleteCourier("src/test/resources/courier/create/courier.json");
        }

    }

    @ParameterizedTest
    @CsvSource({
            "src/test/resources/courier/create/empty.json, 400",
            "src/test/resources/courier/create/withoutLogin.json, 400",
            "src/test/resources/courier/create/withoutPassword.json, 400",
            "src/test/resources/courier/create/withoutFirstName.json, 201",
    })
    @Description("Check that it's impossible to create courier without any of mandatory field")
    void createCourierWithoutMandatoryFields(String path, String statusCode) {
        Response response = API.sendCreateCourierRequest(path);
        STEPS.compareStatusCode(response, Integer.parseInt(statusCode));
        if (response.statusCode() == 201) {
            STEPS.deleteCourier("src/test/resources/courier/create/withoutFirstName.json");
        }

    }

    @Test
    @Description("Check that it's impossible to create courier with busy login")
    void createCourierBusyLogin() {
        try {
            API.sendCreateCourierRequest("src/test/resources/courier/create/courier.json");
            Response busyResponse = API.sendCreateCourierRequest("src/test/resources/courier/create/busyLogin.json");
            STEPS.compareStatusCode(busyResponse, 409);
        } finally {
            STEPS.deleteCourier("src/test/resources/courier/create/courier.json");
        }
    }
}
