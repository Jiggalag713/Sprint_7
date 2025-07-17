package orders;

import com.google.gson.Gson;
import helpers.ApiHelper;
import helpers.Steps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;

public class TrackOrderTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper API = new ApiHelper(BASE_URI);
    private static final Steps STEPS = new Steps(API);
    private static final Gson GSON = new Gson();

    @Test
    @Description("Check that getting of existing order by id returns status code 200")
    public void success() {
        model.Order order = new Order("Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                new ArrayList<>(List.of("BLACK"))
        );
        String jsonOrder = GSON.toJson(order);
        Integer trackId = STEPS.createOrder(jsonOrder);
        Response orderResponse = API.sendGetOrderByIdRequest(trackId);
        orderResponse.then().assertThat().body("order", not(equalTo("")));
        STEPS.compareStatusCode(orderResponse, 200);
    }

    @Test
    @Description("Check that getting order by empty id returns statusCode 400")
    public void emptyNumber() {
        Response orderResponse = API.sendGetOrderByIdRequest(null);
        STEPS.compareStatusCode(orderResponse, 400);
    }

    @Test
    @Description("Check that getting order by incorrect id returns statusCode 404")
    void incorrectNumber() {
        Response orderResponse = API.sendGetOrderByIdRequest(99999999);
        STEPS.compareStatusCode(orderResponse, 404);
    }
}
