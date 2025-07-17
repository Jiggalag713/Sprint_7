package orders;

import com.google.gson.Gson;
import helpers.ApiHelper;
import helpers.Steps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final ApiHelper API = new ApiHelper(BASE_URI);
    private static final Steps STEPS = new Steps(API);
    private static final Gson GSON = new Gson();

    @ParameterizedTest
    @CsvSource({
            "Naruto, Uchiha, 'Konoha, 142 apt.', 4, '+7 800 355 35 35', 5, 2020-06-06, 'Saske, come back to Konoha', BLACK, 201",
            "Naruto, Uchiha, 'Konoha, 142 apt.', 4, '+7 800 355 35 35', 5, 2020-06-06, 'Saske, come back to Konoha', 'BLACK, GREY', 201",
            "Naruto, Uchiha, 'Konoha, 142 apt.', 4, '+7 800 355 35 35', 5, 2020-06-06, 'Saske, come back to Konoha', GREY, 201",
            "Naruto, Uchiha, 'Konoha, 142 apt.', 4, '+7 800 355 35 35', 5, 2020-06-06, 'Saske, come back to Konoha', '', 201",
    })
    @Description("Check that it's possible to create order with different value of colors in order")
    public void checkOrderCreation(String firstName, String lastName,
                                   String address, Integer metroStation,
                                   String phone, Integer rentTime,
                                   String deliveryDate, String comment,
                                   String color, String statusCode) {
        Order order = new Order(firstName,
                lastName,
                address,
                metroStation,
                phone,
                rentTime,
                deliveryDate,
                comment,
                new ArrayList<>(List.of(color))
        );
        String jsonOrder = GSON.toJson(order);
        Response response = API.sendPostOrderRequest(jsonOrder);
        response.then().assertThat().body("track", not(equalTo("")));
        STEPS.compareStatusCode(response, Integer.parseInt(statusCode));
    }
}
