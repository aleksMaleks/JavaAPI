import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex13 {


    private static Stream<Arguments> valuesAndExpectedResultForAdd() {
        return Stream.of(Arguments.of("Mobile", "No", "Android", "Mozilla/5.0 (Linux; U; " +
                        "Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) " +
                        "Version/4.0 Mobile Safari/534.30"),
                Arguments.of("Mobile", "Chrome", "iOS", "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) " +
                        "AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1"),
                Arguments.of("Googlebot", "Unknown", "Unknown", "Mozilla/5.0 (compatible; Googlebot/2.1; " +
                        "+http://www.google.com/bot.html)"),
                Arguments.of("Web", "Chrome", "No", "Mozilla/5.0 (compatible; Googlebot/2.1; " +
                        "+http://www.google.com/bot.html)"),
                Arguments.of("Mobile", "No", "iPhone", "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 " +
                        "like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 " +
                        "Safari/604.1")
        );
    }


    @ParameterizedTest
    @MethodSource({"valuesAndExpectedResultForAdd"})
    public void testRestAssured(String platform, String browser, String device, String userAgent) {


        Response response = RestAssured
                .given()
                .header("User-Agent", userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .andReturn();

//        response.prettyPrint();

        String resPlatform = response.jsonPath().getString("platform");
        String resBrowser = response.jsonPath().getString("browser");
        String resDevice = response.jsonPath().getString("device");

        System.out.println(userAgent);

        assertEquals(platform, resPlatform, "Platform should be '" + platform + "'");
        assertEquals(browser, resBrowser, "Browser should be '" + browser + "'");
        assertEquals(device, resDevice, "Device should be '" + device + "'");

    }
}
