import main.java.authentication.Controller;
import main.java.authentication.json.users.RegistrationDetails;
import main.java.authentication.json.users.User;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    private Controller controller = new Controller();

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
}
