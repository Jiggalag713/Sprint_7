package couriers;

import com.google.gson.Gson;
import helpers.ApiHelper;
import helpers.Steps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.Courier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CreateCourierTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper API = new ApiHelper(BASE_URI);
    private static final Steps STEPS = new Steps(API);
    private static final Gson GSON = new Gson();

    @Test
    @Description("Check that courier creation returns 201 status code and correct body")
    void createCourierResponse201AndResponseCorrect() {
        Courier courier = new Courier("polter", "vermilion", "polter");
        String json = GSON.toJson(courier);
        try {
            Response response = API.sendCreateCourierRequest(json);
            STEPS.compareStatusCode(response, 201);
            STEPS.checkResponseFieldValue(response, "ok", true);
        } finally {
            STEPS.deleteCourier(json);
        }
    }

    @Test
    @Description("Check that it's impossible to create two identic couriers")
    void createSameCouriersImpossible() {
        Courier courier = new Courier("polter", "vermilion", "polter");
        String json = GSON.toJson(courier);
        try {
            API.sendCreateCourierRequest(json);
            Response failedResponse = API.sendCreateCourierRequest(json);
            STEPS.compareStatusCode(failedResponse, 409);
        } finally {
            STEPS.deleteCourier(json);
        }

    }

    @ParameterizedTest
    @CsvSource({
            "'', '', '', 400",
            "'', vermilion, polter, 400",
            "polter, '', polter, 400",
            "polter, vermilion, '', 201",
    })
    @Description("Check that it's impossible to create courier without any of mandatory field")
    void createCourierWithoutMandatoryFields(String login, String password, String firstName, String statusCode) {
        Courier courier = new Courier(login, password, firstName);
        String json = GSON.toJson(courier);
        Response response = API.sendCreateCourierRequest(json);
        STEPS.compareStatusCode(response, Integer.parseInt(statusCode));
        if (response.statusCode() == 201) {
            STEPS.deleteCourier(json);
        }

    }

    @Test
    @Description("Check that it's impossible to create courier with busy login")
    void createCourierBusyLogin() {
        Courier courier = new Courier("polter", "vermilion", "polter");
        String json = GSON.toJson(courier);
        Courier busyLogin = new Courier("polter", "123", "Another courier");
        String busyJson = GSON.toJson(busyLogin);
        try {
            API.sendCreateCourierRequest(json);
            Response busyResponse = API.sendCreateCourierRequest(busyJson);
            STEPS.compareStatusCode(busyResponse, 409);
        } finally {
            STEPS.deleteCourier(json);
        }
    }
}
