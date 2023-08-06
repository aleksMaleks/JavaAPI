import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ex9 {

    @Test
    public void testRestAssured() {
        List<String> list = Arrays.asList("password", "1234", "12345", "111111", "121212", "123123", "123456",
                "555555", "654321", "666666", "696969", "888888", "1234567", "7777777", "12345678", "123456789",
                "1234567890", "!@#$%^&*", "000000", "123qwe", "1q2w3e4r", "1qaz2wsx", "aa123456", "abc123",
                "access", "admin", "adobe123", "ashley", "azerty", "bailey", "baseball", "batman", "charlie",
                "donald", "dragon", "flower", "Football", "football", "freedom", "hello", "hottie", "iloveyou",
                "jesus", "letmein", "login", "lovely", "loveme", "master", "michael", "monkey", "mustang", "ninja",
                "passw0rd", "password", "password1", "photoshop", "princess", "qazwsx", "qwerty", "qwerty123",
                "qwertyuiop", "shadow", "solo", "starwars", "sunshine", "superman", "trustno1", "welcome",
                "whatever", "zaq1zaq1");

        for (String pas : list) {
            Map<String, Object> body = new HashMap<>();
            body.put("login", "super_admin");
            body.put("password", pas);


            Response response1 = RestAssured
                    .given()
                    .body(body)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String cookie = response1.getCookie("auth_cookie");

            Map<String, String> data = new HashMap<>();
            data.put("auth_cookie", cookie);

            Response response2 = RestAssured
                    .given()
                    .cookies(data)
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            String password = response2.asString();
            if (password.equals("You are authorized")) {
                System.out.println("Correct password is '" + pas + "'.");
            }
        }
    }
}
