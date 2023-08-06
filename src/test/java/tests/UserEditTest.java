package tests;

import io.qameta.allure.Description;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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
    @DisplayName("Test positive create user")
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

    @Description("This test edits just created user")
    @DisplayName("Test positive create user")
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

    @Description("This test edits just created user")
    @DisplayName("Test positive create user")
    @Test
    void testEditWithAnotherUser() {
        generateUser();
        loginUser();
        int userId1 = userId;

        String header1 = header;
        String cookie1 = cookie;
        Response responseUserData1 = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId1, header1, cookie1);
//        ResponseBody responseBody1 = responseUserData1.getBody();
//        String firstName1 = responseBody1.jsonPath().getString("firstName");
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



}
