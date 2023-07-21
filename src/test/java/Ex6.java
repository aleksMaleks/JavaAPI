import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


public class Ex6 {

    @Test
    public void testRestAssured() {

        Response response = RestAssured
                .given()
                .redirects()
                .follow(true)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();


        String responseHeader = response.getHeader("X-Host");

        System.out.println(responseHeader);
    }
}
