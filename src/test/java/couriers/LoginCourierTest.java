package couriers;

import com.google.gson.Gson;
import helpers.ApiHelper;
import helpers.Steps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.Courier;
import model.Login;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class LoginCourierTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper API = new ApiHelper(BASE_URI);
    private static final Steps STEPS = new Steps(API);
    private static final Gson GSON = new Gson();

    @Test
    @Description("Check correct login by existed courier")
    public void loginCourierTest() {
        Courier courier = new Courier("polter", "vermilion", "polter");
        String json = GSON.toJson(courier);
        Integer expectedId = STEPS.createCourier(json);
        Response loginResponse = API.sendLoginCourierRequest(json);
        STEPS.checkResponseFieldValue(loginResponse, "id", expectedId);
        STEPS.compareStatusCode(loginResponse, 200);
        STEPS.deleteCourier(json);
    }

    @ParameterizedTest
    @CsvSource({
            "'', '', 400",
            "'', password, 400",
            "polter, '', 400",
            "polter+1, password, 404",
            "polter, password!, 404"
    })
    @Description("Check courier login without some mandatory fields")
    public void checkMandatoryFields(String login, String password, String statusCode) {
        Courier courier = new Courier("polter", "vermilion", "polter");
        String json = GSON.toJson(courier);
        Login loginObj = new Login(login, password);
        String loginJson = GSON.toJson(loginObj);
        API.sendCreateCourierRequest(json);
        Response loginResponse = API.sendLoginCourierRequest(loginJson);
        STEPS.compareStatusCode(loginResponse, Integer.parseInt(statusCode));
        STEPS.deleteCourier(json);
    }
}
