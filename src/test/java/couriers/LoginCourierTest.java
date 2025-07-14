package couriers;

import helpers.ApiHelper;
import helpers.Steps;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class LoginCourierTest {
    private static final String baseURI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper api = new ApiHelper(baseURI);
    private static final Steps steps = new Steps(api);

    @Test
    public void loginCourierTest() {
        Integer expectedId = steps.createCourier("src/test/resources/courier/create/courier.json");
        Response loginResponse = api.sendLoginCourierRequest("src/test/resources/courier/create/courier.json");
        steps.checkResponseFieldValue(loginResponse, "id", expectedId);
        steps.compareStatusCode(loginResponse, 200);
        steps.deleteCourier("src/test/resources/courier/create/courier.json");
    }

    @ParameterizedTest
    @CsvSource({
            "src/test/resources/courier/login/empty.json, 400",
            "src/test/resources/courier/login/withoutLogin.json, 400",
            "src/test/resources/courier/login/withEmptyLogin.json, 400",
            "src/test/resources/courier/login/withEmptyPassword.json, 400",
            "src/test/resources/courier/login/withIncorrectLogin.json, 404",
            "src/test/resources/courier/login/withIncorrectPassword.json, 404"
    })
    public void checkMandatoryFields(String path, String statusCode) {
        api.sendCreateCourierRequest("src/test/resources/courier/create/courier.json");
        Response loginResponse = api.sendLoginCourierRequest(path);
        steps.compareStatusCode(loginResponse, Integer.parseInt(statusCode));
        steps.deleteCourier("src/test/resources/courier/create/courier.json");
    }
}
