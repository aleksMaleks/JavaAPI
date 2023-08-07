package tests;

import io.qameta.allure.Description;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UserDeleteTest extends BaseTestCase {

    private int userId = 0;
    private String header = null;
    private String cookie = null;
    private Map<String, String> userData = new HashMap<>();

    public void generateUser() {
        userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestWithJsonPath("https://playground.learnqa.ru/api/user/", userData);

        userId = responseCreateAuth.getInt("id");
    }

    public void loginUser() {
        Response responseGetAuth = apiCoreRequests
                .makePostRequestWithResponse("https://playground.learnqa.ru/api/user/login", userData);
        header = responseGetAuth.getHeader("x-csrf-token");
        cookie = responseGetAuth.getCookie("auth_sid");
    }

    @Description("This test delete user by id 2")
    @DisplayName("Test negative delete user")
    @Test
    public void testGetUserDataNotAuth() {
        userData = new HashMap<>();
        userData.put("email", "vinkotov@example.com");
        userData.put("password", "1234");

        loginUser();

        Response responseUserData = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/2", header, cookie);

        Document document = Jsoup.parse(responseUserData.asString());
        String paragraphElement = document.body().text();

        Assertions.assertResponseCodeEquals(responseUserData, 400);
        assertEquals(paragraphElement, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Description("This test delete just created user")
    @DisplayName("Test positive delete user")
    @Test
    public void testDeleteJustCreatedUser() {
        userData = new HashMap<>();
        userData.put("email", "vinkotov@example.com");
        userData.put("password", "1234");

        generateUser();
        loginUser();

        String url = "https://playground.learnqa.ru/api/user/" + userId;

        apiCoreRequests.makeGetRequest(url, header, cookie);

        Response responseDeletedUserData = apiCoreRequests.makeDeleteRequest(url, header, cookie);


        Response responseUserData = apiCoreRequests.makeGetRequest(url, header, cookie);
        Document document = Jsoup.parse(responseUserData.asString());
        String paragraphElement = document.body().text();
//        System.out.println(paragraphElement);

        Assertions.assertResponseCodeEquals(responseDeletedUserData, 200);
        assertEquals(paragraphElement, "User not found");
    }

    @Description("This test delete user with auth another user")
    @DisplayName("Test negative delete user")
    @Test
    void testEditWithAnotherUser() {
        generateUser();
        loginUser();
        int userId1 = userId;

        String header1 = header;
        String cookie1 = cookie;
        Response responseUserData1 = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId1, header1, cookie1);
        responseUserData1.prettyPrint();

        generateUser();
        loginUser();
        int userId2 = userId;
        String header2 = header;
        String cookie2 = cookie;
        Response responseUserData2 = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId2, header2, cookie2);
        responseUserData2.prettyPrint();

        Response responseDeletedUserData = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId1, header2, cookie2);
//        responseDeletedUserData.prettyPrint();

        Response responseUserData3 = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId1, header1, cookie1);
        responseUserData3.prettyPrint();

        Response responseUserData4 = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId2, header2, cookie2);
        responseUserData4.prettyPrint();

        Document document = Jsoup.parse(responseUserData4.asString());
        String paragraphElement = document.body().text();

        assertEquals(paragraphElement, "User not found");
        Assertions.assertResponseCodeEquals(responseDeletedUserData, 200);
    }
}
