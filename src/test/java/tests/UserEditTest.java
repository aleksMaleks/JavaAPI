package tests;

import io.qameta.allure.*;
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

class UserEditTest extends BaseTestCase {

    private int userId = 0;
    private String email = null;
    private String password = null;
    private String header = null;
    private String cookie = null;
    private Map<String, String> userData = new HashMap<>();
    private Map<String, String> editData =  new HashMap<>();


    public void generateUser() {
        userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestWithJsonPath("https://playground.learnqa.ru/api/user/", userData);

        userId = responseCreateAuth.getInt("id");
        email = userData.get("email");
        password = userData.get("password");
    }

    public void loginUser() {
        Response responseGetAuth = apiCoreRequests
                .makePostRequestWithResponse("https://playground.learnqa.ru/api/user/login", userData);
        header = responseGetAuth.getHeader("x-csrf-token");
        cookie = responseGetAuth.getCookie("auth_sid");
    }

    public Response editUser(String url, Map<String, String> editData) {
        System.out.println(userId);
        System.out.println(url);
        Response response = apiCoreRequests.makePutRequestWithJsonPath(url, header, cookie, editData);
        return response;
    }

    @Description("This test edits just created user")
    @DisplayName("Test positive edit user")
    @Test
    void testEditJustCreatedUser() {
        generateUser();
        loginUser();

        String newName = "Changed Name";
        String url = "https://playground.learnqa.ru/api/user/" + userId;
        editData.put("firstName", newName);
        editUser(url, editData);

        Response responseUserData = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId, header, cookie);

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Description("This test edits wit not authorized user")
    @DisplayName("Test negative edit user")
    @Test
    void testEditWithNotAuthorizedUser() {
        generateUser();
        loginUser();

        String newName = "Changed Name";

        header = header + 1;
        String url = "https://playground.learnqa.ru/api/user/" + userId;
        editData.put("firstName", newName);
        Response responseEditUserData = editUser(url, editData);

        Assertions.assertResponseTextEquals(responseEditUserData, "Auth token not supplied");
    }

    @Link("https://example.org")
    @TmsLink("test-1")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test edits with another user")
    @DisplayName("Test negative edit user")
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

        String newName = "Changed Name";
        editData.put("firstName", newName);
        String url = "https://playground.learnqa.ru/api/user/" + userId1;
        Response responseEditUserData = editUser(url, editData);

        Response responseUserData3 = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId1, header1, cookie1);
        responseUserData3.prettyPrint();

        Response responseUserData4 = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId2, header2, cookie2);
        responseUserData4.prettyPrint();


        Assertions.assertJsonByName(responseUserData3, "firstName", "learnqa");
        Assertions.assertJsonByName(responseUserData4, "firstName", "Changed Name");
        Assertions.assertResponseCodeNotEquals(responseEditUserData, 200);
    }

    @Description("This test edits with email without @")
    @DisplayName("Test negative edit user")
    @Test
    void testEditWithEmailWithoutAt() {
        generateUser();
        loginUser();

        String newEmail = "vinexample.com";
        String url = "https://playground.learnqa.ru/api/user/" + userId;
        editData.put("email", newEmail);
        Response responseEditUserData = editUser(url, editData);

        Document document = Jsoup.parse(responseEditUserData.asString());
        String paragraphElement = document.body().text();

        System.out.println(responseEditUserData.statusCode());
        Assertions.assertResponseCodeEquals(responseEditUserData, 400);
        assertEquals(paragraphElement, "Invalid email format");
    }

    @Description("This test edits with a very short firstName - one character")
    @DisplayName("Test negative edit user")
    @Test
    void testEditWithFirstName() {
        generateUser();
        loginUser();

        String newFirstName = "v";
        String url = "https://playground.learnqa.ru/api/user/" + userId;
        editData.put("firstName", newFirstName);
        Response responseEditUserData = editUser(url, editData);

        Assertions.assertResponseCodeEquals(responseEditUserData, 400);
        Assertions.assertJsonByName(
                responseEditUserData, "error", "Too short value for field firstName");
    }
}
