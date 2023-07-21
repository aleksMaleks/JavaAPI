import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;


public class Ex8 {

    @Test
    public void testRestAssured() throws InterruptedException {

        JsonPath response1 = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String token = response1.getString("token");
        int seconds = response1.getInt("seconds");


        JsonPath response2 = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String status1 = response2.getString("status");

        if (status1.equals("Job is NOT ready")) {
            System.out.println("First status is correct!");
        } else {
            System.out.println("First status is incorrect!");
        }


        Thread.sleep(seconds * 1000);
        JsonPath response3 = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String status2 = response3.getString("status");
        if (status2.equals("Job is ready")) {
            System.out.println("Second status is correct!");
        } else {
            System.out.println("Second status is incorrect!");
        }

        String result = response3.get("result");
        if (result != null) {
            System.out.println("Result is correct!");
        } else {
            System.out.println("Result is incorrect!");
        }
    }
}
