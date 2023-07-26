import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex12 {

    @Test
    public void testRestAssured() {

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        assertEquals(response.getHeader("x-secret-homework-header"), "Some secret value",
                "The header 'x-secret-homework-header' is incorrect");

    }
}
