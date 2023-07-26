import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex11 {

    @Test
    public void testRestAssured() {

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        assertEquals(response.getCookie("HomeWork"), "hw_value",
                "The cookie 'hw_value' is incorrect");

    }
}
