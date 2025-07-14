package couriers;

import helpers.ApiHelper;
import helpers.Steps;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CreateCourierTest {
    private static final String baseURI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper api = new ApiHelper(baseURI);
    private static final Steps steps = new Steps(api);

    @Test
    void createCourierResponse201AndResponseCorrect() {
        try {
            Response response = api.sendCreateCourierRequest("src/test/resources/courier/create/courier.json");
            steps.compareStatusCode(response, 201);
            steps.checkResponseFieldValue(response, "ok", true);
        } finally {
            steps.deleteCourier("src/test/resources/courier/create/courier.json");
        }
    }

    @Test
    void createSameCouriersImpossible() {
        try {
            api.sendCreateCourierRequest("src/test/resources/courier/create/courier.json");
            Response failedResponse = api.sendCreateCourierRequest("src/test/resources/courier/create/courier.json");
            steps.compareStatusCode(failedResponse, 409);
        } finally {
            steps.deleteCourier("src/test/resources/courier/create/courier.json");
        }

    }

    @ParameterizedTest
    @CsvSource({
            "src/test/resources/courier/create/empty.json, 400",
            "src/test/resources/courier/create/withoutLogin.json, 400",
            "src/test/resources/courier/create/withoutPassword.json, 400",
            "src/test/resources/courier/create/withoutFirstName.json, 201",
    })
    void createCourierWithoutMandatoryFields(String path, String statusCode) {
        Response response = api.sendCreateCourierRequest(path);
        steps.compareStatusCode(response, Integer.parseInt(statusCode));
        if (response.statusCode() == 201) {
            steps.deleteCourier("src/test/resources/courier/create/withoutFirstName.json");
        }

    }

    @Test
    void createCourierBusyLogin() {
        try {
            api.sendCreateCourierRequest("src/test/resources/courier/create/courier.json");
            Response busyResponse = api.sendCreateCourierRequest("src/test/resources/courier/create/busyLogin.json");
            steps.compareStatusCode(busyResponse, 409);
        } finally {
            steps.deleteCourier("src/test/resources/courier/create/courier.json");
        }
    }
}
