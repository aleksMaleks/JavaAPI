import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


public class Ex7 {

    public Response response = null;

    @Test
    public void testRestAssured() {
        getResponse("https://playground.learnqa.ru/api/long_redirect");
    }

    public Response getResponse(String url) {
        response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get(url)
                .andReturn();
        System.out.println(response.getStatusCode());
        String responseHeader = response.getHeader("Location");
        if (response.getStatusCode() == 301) {
            System.out.println(responseHeader);
            getResponse(responseHeader);
        }
        return response;
    }
}

