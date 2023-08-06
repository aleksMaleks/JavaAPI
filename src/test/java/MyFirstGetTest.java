import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class MyFirstGetTest {

    @Test
    public void testRestAssured(){
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");

        Response responseForGet = RestAssured
                .given()
                .body(data)
                .when()
                .get("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseCookie = responseForGet.getCookie("auth_cookie");


        Map<String, String> cookies = new HashMap<>();
        if (responseCookie != null){
            cookies.put("auth_cookie", responseCookie);
        }

        Response responseForCheck = RestAssured
                .given()
                .body(data)
                .cookies(cookies)
                .when()
                .get("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

        responseForCheck.print();

    }

    @Test
    public void getToken(){
        Map<String, Object> body = new HashMap<>();
        body.put("grant_type", "password");
        body.put("client_id", "portal");
        body.put("username", "project_owner_c");
        body.put("password", "QqszEbkTgM");



        Response response = RestAssured
                .given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .contentType("application/x-www-form-urlencoded")
                    .formParam("grant_type", "password")
                    .formParam("username", "project_owner_c")
                    .formParam("password", "QqszEbkTgM")
                    .formParam("client_id", "portal")
//                .body(body)
                .when()
                .post("https://app.gpb.neoflex.cloud/auth/realms/gpb/protocol/openid-connect/token/")
                .andReturn();

//        Response response = RestAssured.given().with().auth().preemptive()
//                .basic("project_owner_c", "QqszEbkTgM")
//                .header("Content-Type", "application/x-www-form-urlencoded")
//                .formParam("grant_type", "password")
//                .formParam("client_id", "portal")
//                .formParam("username", "project_owner_c")
//                .formParam("password", "QqszEbkTgM").when()
//                .post("https://app.gpb.neoflex.cloud/auth/realms/gpb/protocol/openid-connect/token/");

//        System.out.println(response.getBody().asString());
//
//        String responseBody = response.getBody().asString();
//        String token = new org.json.JSONObject(responseBody).getString("access_token");
//        System.out.println(token);


        response.prettyPrint();

    }
}
