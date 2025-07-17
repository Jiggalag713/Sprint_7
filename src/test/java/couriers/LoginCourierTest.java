package couriers;

import helpers.ApiHelper;
import helpers.Steps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class LoginCourierTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper API = new ApiHelper(BASE_URI);
    private static final Steps STEPS = new Steps(API);

    @Test
    @Description("Check correct login by existed courier")
    public void loginCourierTest() {
        Integer expectedId = STEPS.createCourier("src/test/resources/courier/create/courier.json");
        Response loginResponse = API.sendLoginCourierRequest("src/test/resources/courier/create/courier.json");
        STEPS.checkResponseFieldValue(loginResponse, "id", expectedId);
        STEPS.compareStatusCode(loginResponse, 200);
        STEPS.deleteCourier("src/test/resources/courier/create/courier.json");
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
    @Description("Check courier login without some mandatory fields")
    public void checkMandatoryFields(String path, String statusCode) {
        API.sendCreateCourierRequest("src/test/resources/courier/create/courier.json");
        Response loginResponse = API.sendLoginCourierRequest(path);
        STEPS.compareStatusCode(loginResponse, Integer.parseInt(statusCode));
        STEPS.deleteCourier("src/test/resources/courier/create/courier.json");
    }
}
