package main.java.authentication;

import authentication.Token;
import main.java.connection.historycon;
import main.java.connection.userscon;
import main.java.authentication.json.*;
import main.java.authentication.json.JsonObject;
import main.java.authentication.json.users.*;
import main.java.authentication.json.users.User;
import main.java.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import main.java.exceptions.InvalidTokenException;
import main.java.authentication.json.users.LoginDetails;
import main.java.authentication.json.users.RegistrationDetails;
import main.java.authentication.json.TokenSuccess;
import main.java.authentication.json.JsonObject;
import main.java.authentication.json.JsonResponses;
import main.java.authentication.json.users.NewPassword;

import main.java.history.HistoryController;
import main.java.authentication.json.HistoryDetails;

@CrossOrigin
@RestController
public class Controller {

    private static final Token TOKEN = new Token();
    private static final userscon userscon = new userscon();
    private static final historycon historycon = new historycon();
    private static final String ADMINTOKENPREFIX = "adminToken";
    private static final String ADMINTOKEN = "baymaxFTW";
    private static final HistoryController historyController = new HistoryController();

    /**
     * Registers a new user and automatically signs him in
     * @param userDetails
     * @return a JWT token of successful login
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(path = "/register", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public JsonObject register(@RequestBody RegistrationDetails userDetails) throws SQLException, ClassNotFoundException {
        userscon.addUser(userDetails.getUsername(),
                TOKEN.generateMD5Hash(userDetails.getPassword()),
                userDetails.getFirstname(),
                userDetails.getLastname(),
                userDetails.getDepartment(),
                userDetails.getRole());

        String token = TOKEN.createToken(userDetails.getUsername());
        return new TokenSuccess(token);
    }

    /**
     *
     * @return list of every registered user
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @RequestMapping(path = "/getusers", method = RequestMethod.GET, produces = "application/json")
    public ArrayList<User> getUsers() throws SQLException, ClassNotFoundException {
        return userscon.getAllUsers();
    }

    @RequestMapping(path = "/changepassword", method = RequestMethod.POST)
    public JsonObject changePass(@RequestBody NewPassword details) throws SQLException, ClassNotFoundException, Exception {
        if (details.getOldPassword().isEmpty() || details.getNewPassword().isEmpty() || details.getUsername().isEmpty() || details.getOldPassword() == null || details.getNewPassword() == null || details.getUsername() == null) {
            throw new NullPointerException("Username or password cannot be empty.");
        }
        userscon.changePassword(details.getUsername(), TOKEN.generateMD5Hash(details.getOldPassword()), TOKEN.generateMD5Hash(details.getNewPassword()));
        return new JsonSuccess("Password has been updated successfully.");
    }

    @RequestMapping(path = "/deleteuser", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public String deleteUser(@RequestBody UserCredentials details, @RequestHeader(ADMINTOKENPREFIX) String token) throws SQLException, ClassNotFoundException, InvalidTokenException, Exception {
        if (ADMINTOKEN.equals(token)) {
            ArrayList<HistoryDetails> historyDetails = historyController.getHistory(details.staff_id);
            String message = userscon.deleteUser(details.staff_id, historyDetails) ? "Success" : "Fail";
            return message;
        }
        throw new InvalidTokenException("Invalid Admin Token");
    }

    @RequestMapping(path = "/resetpassword", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public String resetPassword(@RequestBody UserCredentials details, @RequestHeader(ADMINTOKENPREFIX) String token) throws SQLException, ClassNotFoundException, InvalidTokenException {
        if (ADMINTOKEN.equals(token)) {
            String message = userscon.resetPasswordWithStaffid(details.staff_id) ? "Success" : "Fail";
            return message;
        }
        throw new InvalidTokenException("Invalid Admin Token");
    }

    @RequestMapping(path = "/adminconvert", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public JsonObject convertStaffToAdmin(@RequestBody UserCredentials details, @RequestHeader(ADMINTOKENPREFIX) String token) throws SQLException, ClassNotFoundException, InvalidTokenException {
        if (ADMINTOKEN.equals(token)) {
            return userscon.convertStaffToAdmin(details.staff_id);
        }
        throw new InvalidTokenException("Invalid Admin Token");
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public JsonObject login(@RequestBody LoginDetails inputDetails) throws SQLException, ClassNotFoundException, NullPointerException, InvalidTokenException {
        if (inputDetails.getUsername().isEmpty() || inputDetails.getPassword().isEmpty() || inputDetails.getUsername() == null || inputDetails.getPassword() == null) {
            throw new NullPointerException("Username or password cannot be empty.");
        }
        String username = inputDetails.getUsername();
        String passwordHash = TOKEN.generateMD5Hash(inputDetails.getPassword());

        if (userscon.verifyPassword(username, passwordHash)) {
            String token = TOKEN.createToken(inputDetails.getUsername());

            return new TokenSuccess(token);
        }

        throw new NullPointerException("Username or password is invalid");
    }

    @RequestMapping(path = "/verify", method = RequestMethod.GET)
    public ResponseEntity<JsonObject> verifyToken(@RequestHeader("x-auth-token") String token) throws Exception {
        TOKEN.validateToken(token);
        // No exceptions are needed to be handled.
        // If the token is invalid or expired,
        // the exception would automatically
        // be thrown from Token class
        return new ResponseEntity<JsonObject>(new JsonResponse("SUCCESS", TOKEN.retrieveUserObject(token)), HttpStatus.OK);
    }


    public String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy, HH:mm:ss a");
        Date date = new Date();
        return formatter.format(date);
    }

}
