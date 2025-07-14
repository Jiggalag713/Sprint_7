package orders;

import helpers.ApiHelper;
import helpers.Steps;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
    private static final String baseURI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper api = new ApiHelper(baseURI);
    private static final Steps steps = new Steps(api);

    @ParameterizedTest
    @CsvSource({
            "src/test/resources/order/create/blackColor.json, 201",
            "src/test/resources/order/create/bothColors.json, 201",
            "src/test/resources/order/create/greyColor.json, 201",
            "src/test/resources/order/create/noColor.json, 201",
    })
    public void checkOrderCreation(String path, String statusCode) {
        Response response = api.sendPostOrderRequest(path);
        response.then().assertThat().body("track", not(equalTo("")));
        steps.compareStatusCode(response, Integer.parseInt(statusCode));
    }
}
