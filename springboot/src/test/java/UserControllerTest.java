import authentication.Token;
import main.java.authentication.Controller;
import main.java.authentication.json.TokenSuccess;
import main.java.authentication.json.users.LoginDetails;
import main.java.authentication.json.users.RegistrationDetails;
import main.java.authentication.json.users.User;
import main.java.authentication.json.users.NewPassword;
import main.java.authentication.json.JsonSuccess;
import main.java.exceptions.InvalidTokenException;
import org.apache.poi.ss.formula.functions.T;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.ResponseEntity;
import main.java.authentication.json.JsonObject;
import main.java.authentication.json.JsonResponse;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
//import main.java.

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    private Controller controller = new Controller();
    private String token = "";
    private Token TOKEN = new Token();

    @Test
    @Order(1)
    public void newUser() throws SQLException, ClassNotFoundException {
        int expectedNum = 1;
        System.out.println("\t ===================== 1 new user test =====================");
        RegistrationDetails registrationDetails = new RegistrationDetails("tukiz", "password", "najulah", "mohamed", "dummy dept", "user");
        controller.register(registrationDetails);

        ArrayList<User> users = controller.getUsers();


        System.out.println("\t Expected 1 User; There are " + users.size() + " users");
        String result = users.size() == expectedNum ? "Success" : "Fail";
        System.out.println("\t " + result + "\n");
        assertEquals(expectedNum, users.size());
    }

    @Test
    @Order(2)
    public void duplicateUser() throws SQLException, ClassNotFoundException {

        System.out.println("\t ===================== 2 duplicate entry test =====================");
        RegistrationDetails registrationDetails = new RegistrationDetails("tukiz", "passwords", "najulahs", "mohameds", "dummy depts", "users");
        Exception exception = assertThrows(
                SQLIntegrityConstraintViolationException.class,
                () -> controller.register(registrationDetails)
        );

        String expectedMsg = "Duplicate entry 'tukiz' for key 'username'";
        String result = exception.getMessage().contains(expectedMsg) ? "Success" : "Fail";
        System.out.println("\t " + result + "\n");

        assertTrue(exception.getMessage().contains(expectedMsg));
    }

    @Test
    @Order(3)
    public void addAnotherUser() throws SQLException, ClassNotFoundException {
        int expectedNum = 2;
        System.out.println("\t ===================== 3 add another user =====================");
        RegistrationDetails registrationDetails = new RegistrationDetails("tukiz1", "password", "najulah", "mohamed", "dummy dept", "user");
        controller.register(registrationDetails);

        ArrayList<User> users = controller.getUsers();

        System.out.println("\t Expected 2 User; There are " + users.size() + " users");
        String result = users.size() == expectedNum ? "Success" : "Fail";
        System.out.println("\t " + result + "\n");
        assertEquals(expectedNum, users.size());
    }

    @Test
    @Order(4)
    public void getUsers() throws SQLException, ClassNotFoundException {
        int expectedNum = 2;
        System.out.println("\t ===================== 4 get all users test =====================");
        ArrayList<User> users = controller.getUsers();

        System.out.println("\t Expected 2 User; There are " + users.size() + " users");
        String result = users.size() == expectedNum ? "Success" : "Fail";
        System.out.println("\t " + result + "\n");
        assertEquals(expectedNum, users.size());
    }

    @Test
    @Order(5)
    public void login() throws SQLException, ClassNotFoundException, NullPointerException, InvalidTokenException, Exception {
        System.out.println("\t ===================== 5 user login successfully test =====================");
        LoginDetails loginDetails = new LoginDetails("tukiz", "password");
        TokenSuccess tokenSuccess = (TokenSuccess) controller.login(loginDetails);
        token = tokenSuccess.getToken();

        System.out.println("\t Expected 1 Token");
        System.out.println("\t " + token + "\n");

        // validate generated token
        TOKEN.validateToken(token);

        // will throw errors if token is not valid, hence can just assert a boolean true
        assertTrue(true);
        System.out.println("\t " + "Success" + "\n");
    }

    @Test
    @Order(6)
    public void wrongpassword() throws SQLException, ClassNotFoundException, NullPointerException, InvalidTokenException {
        System.out.println("\t ===================== 6 wrongpassword test =====================");
        LoginDetails loginDetails = new LoginDetails("tukiz", "passwords");

        Exception exception = assertThrows(
                Exception.class,
                () -> controller.login(loginDetails)
        );

        String expectedMsg = "Username or password is invalid";
        String result = exception.getMessage().contains(expectedMsg) ? "Success" : "Fail";
        System.out.println("\t " + result + "\n");

        assertTrue(exception.getMessage().contains(expectedMsg));
    }

    @Test
    @Order(7)
    public void invaliduser() throws SQLException, ClassNotFoundException, NullPointerException, InvalidTokenException {
        System.out.println("\t ===================== 7 non existent user test =====================");
        LoginDetails loginDetails = new LoginDetails("tuki", "password");

        Exception exception = assertThrows(
                NullPointerException.class,
                () -> controller.login(loginDetails)
        );

        String expectedMsg = "Username or password is invalid";
        String result = exception.getMessage().contains(expectedMsg) ? "Success" : "Fail";
        System.out.println("\t " + result + "\n");
        assertTrue(exception.getMessage().contains(expectedMsg));
    }

    @Test
    @Order(8)
    public void failchangepassNoUname() throws SQLException, ClassNotFoundException, NullPointerException, InvalidTokenException {
        System.out.println("\t ===================== 8 failchangepass no username test =====================");
        NewPassword newPassword = new NewPassword("", "passwor", "newpassword");

        Exception exception = assertThrows(
                NullPointerException.class,
                () -> controller.changePass(newPassword)
        );

        String expectedMsg = "Username or password cannot be empty.";
        String result = exception.getMessage().contains(expectedMsg) ? "Success" : "Fail";
        System.out.println("\t " + result + "\n");
        assertTrue(exception.getMessage().contains(expectedMsg));
    }

    @Test
    @Order(9)
    public void failchangepassNoOldPass() throws SQLException, ClassNotFoundException, NullPointerException, InvalidTokenException {
        System.out.println("\t ===================== 9 failchangepass no old password test =====================");
        NewPassword newPassword = new NewPassword("tukiz", "", "newpassword");

        Exception exception = assertThrows(
                NullPointerException.class,
                () -> controller.changePass(newPassword)
        );

        String expectedMsg = "Username or password cannot be empty.";
        String result = exception.getMessage().contains(expectedMsg) ? "Success" : "Fail";
        System.out.println("\t " + result + "\n");
        assertTrue(exception.getMessage().contains(expectedMsg));
    }

    @Test
    @Order(10)
    public void failchangepassNoNewPass() throws SQLException, ClassNotFoundException, NullPointerException, InvalidTokenException {
        System.out.println("\t ===================== 10 failchangepass no new password test =====================");
        NewPassword newPassword = new NewPassword("tukiz", "passwor", "");

        Exception exception = assertThrows(
                NullPointerException.class,
                () -> controller.changePass(newPassword)
        );

        String expectedMsg = "Username or password cannot be empty.";
        String result = exception.getMessage().contains(expectedMsg) ? "Success" : "Fail";
        System.out.println("\t " + result + "\n");
        assertTrue(exception.getMessage().contains(expectedMsg));
    }

    @Test
    @Order(11)
    public void changepass() throws SQLException, ClassNotFoundException, NullPointerException, InvalidTokenException, Exception {
        System.out.println("\t ===================== 11 correct changepass test =====================");
        NewPassword newPassword = new NewPassword("tukiz", "password", "newpassword");


        JsonSuccess jsonSuccess = (JsonSuccess) controller.changePass(newPassword);
        String expectedMsg = "Password has been updated successfully.";
        String passwordUpdate = jsonSuccess.getMessage().contains(expectedMsg) ? "Success" : "Fail";
        // check whether change pass was successful
        assertTrue(jsonSuccess.getMessage().contains(expectedMsg));

        // from here on try login with new pass and assert if a valid token is generated
        LoginDetails loginDetails = new LoginDetails("tukiz", "newpassword");
        TokenSuccess tokenSuccess = (TokenSuccess) controller.login(loginDetails);
        token = tokenSuccess.getToken();

        System.out.println("\t Expected 1 Token");
        System.out.println("\t " + token + "\n");

        // validate generated token
        TOKEN.validateToken(token);

        // will throw errors if token is not valid, hence can just assert a boolean true
        assertTrue(true);
        System.out.println("\t " + "Success" + "\n");

    }


}
