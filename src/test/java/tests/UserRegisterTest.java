package tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {

    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithResponse("https://playground.learnqa.ru/api/user/", userData);


        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email
                + "' already exists");
    }

    @Test
    public void testCreateUserSuccessfully() {
//        String email = DataGenerator.getRandomEmail();

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithResponse("https://playground.learnqa.ru/api/user/", userData);


        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");

    }

    @Description("This test check register user with bad email")
    @DisplayName("Test negative register user")
    @Test
    public void testCreateUserWithBadEmail() {
        String email = "vinexample.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithResponse("https://playground.learnqa.ru/api/user/", userData);


        System.out.println(responseCreateAuth.asString());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }

    @Description("This test check register user without any parameter")
    @DisplayName("Test negative register user")
    @ParameterizedTest
    @ValueSource(strings =  {"email", "password", "username", "firstName", "lastName"})
    public void testCreateUserWithoutAnyParameter(String parameter) {
        Map<String, String> userData = new HashMap<>();
        userData.put(parameter, null);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithResponse("https://playground.learnqa.ru/api/user/", userData);


        System.out.println(responseCreateAuth.asString());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth,
                "The following required params are missed: " + parameter);
    }

    @Description("This test check register user with very short username - one character")
    @DisplayName("Test negative register user")
    @Test
    public void testCreateUserWithShortUsername() {
        String username = "v";

        Map<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithResponse("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(
                responseCreateAuth, "The value of 'username' field is too short");
    }

    @Description("This test check register user with very long username - 250 characters")
    @DisplayName("Test positive register user")
    @Test
    public void testCreateUserWithLongUsername() {
        String username = "dsfkdfjhdfkjghdfkdfgjhdsfkdfjhdfkjghdfkdfgjhdsfkdfjhdfkjghdfkdfgjhdsfkdfjhdfkjghdfkdf" +
                "gjhdsfkdfjhdfkjghdfkdfgjhdsfkdfjhdfkjghdfkdfgjhdsfkdfjhdfkjghdfkdfgjhdsfkdfjhdfkjghdfkdfgjhdsfk" +
                "dfjhdfkjghdfkdfgjhdsfkdfjhdfkjghdfkdfgjhdsfkdfjhdfkjghdfkdfgjhdsfkdfjhg";

        Map<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithResponse("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(
                responseCreateAuth, "The value of 'username' field is too long");
    }
}
