package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserAuthTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;

    @BeforeEach
    public void loginUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

//        responseGetAuth.prettyPrint();

        cookie = getCookie(responseGetAuth,"auth_sid");
        header = getHeader(responseGetAuth,"x-csrf-token");
        userIdOnAuth = getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    public void testAuthUser() {

        Response responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn();

        Assertions.assertJsonByName(responseCheckAuth, "user_id", userIdOnAuth);
    }

    @ParameterizedTest
    @ValueSource(strings =  {"cookie", "headers"})
    public void testNegativeAuthUser(String conditin) {
        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if (conditin.equals("cookie")) {
            spec.cookies("auth_sid", cookie);
        } else if (conditin.equals("headers")) {
            spec.header("x-csrf-token", header);
        } else {
            throw  new IllegalArgumentException("Condition value is known: " + conditin);
        }

        Response reponseForCheck = spec.get().andReturn();
        Assertions.assertJsonByName(reponseForCheck, "user_id", 0);

    }
}
