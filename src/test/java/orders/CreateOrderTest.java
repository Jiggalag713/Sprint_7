package orders;

import helpers.ApiHelper;
import helpers.Steps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper API = new ApiHelper(BASE_URI);
    private static final Steps STEPS = new Steps(API);

    @ParameterizedTest
    @CsvSource({
            "src/test/resources/order/create/blackColor.json, 201",
            "src/test/resources/order/create/bothColors.json, 201",
            "src/test/resources/order/create/greyColor.json, 201",
            "src/test/resources/order/create/noColor.json, 201",
    })
    @Description("Check that it's possible to create order with different value of colors in order")
    public void checkOrderCreation(String path, String statusCode) {
        Response response = API.sendPostOrderRequest(path);
        response.then().assertThat().body("track", not(equalTo("")));
        STEPS.compareStatusCode(response, Integer.parseInt(statusCode));
    }
}
