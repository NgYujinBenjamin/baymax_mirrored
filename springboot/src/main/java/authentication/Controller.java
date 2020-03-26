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


@CrossOrigin
@RestController
public class Controller {

    private static final Token TOKEN = new Token();
    private static final userscon userscon = new userscon();
    private static final historycon historyscon = new historycon();
    private static final String ADMINTOKENPREFIX = "adminToken";
    private static final String ADMINTOKEN = "baymaxFTW";

    // done
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

    // original by Ben
    @RequestMapping(path = "/getusers", method = RequestMethod.GET, produces = "application/json")
    public ArrayList<User> getUsers() throws SQLException, ClassNotFoundException {
        return userscon.getAllUsers();
    }

    // done
    @RequestMapping(path = "/changepassword", method = RequestMethod.POST)
    public JsonObject changePass(@RequestBody NewPassword details) throws SQLException, ClassNotFoundException, Exception {
        if (details.getOldPassword().isEmpty() || details.getNewPassword().isEmpty() || details.getUsername().isEmpty() || details.getOldPassword() == null || details.getNewPassword() == null || details.getUsername() == null) {
            throw new NullPointerException("Username or password cannot be empty.");
        }
        userscon.changePassword(details.getUsername(), TOKEN.generateMD5Hash(details.getOldPassword()), TOKEN.generateMD5Hash(details.getNewPassword()));
        return new JsonSuccess("Password has been updated successfully.");
    }

    @RequestMapping(path = "/deleteuser", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public String deleteUser(@RequestBody UserCredentials details, @RequestHeader(ADMINTOKENPREFIX) String token) throws SQLException, ClassNotFoundException, InvalidTokenException {
        if (ADMINTOKEN.equals(token)) {
            String message = userscon.deleteUser(details.staff_id) ? "Success" : "Fail";
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

    // done
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

    // done
    @RequestMapping(path = "/verify", method = RequestMethod.GET)
    public ResponseEntity<JsonObject> verifyToken(@RequestHeader("x-auth-token") String token) throws Exception {
        TOKEN.validateToken(token);
        // No exceptions are needed to be handled.
        // If the token is invalid or expired,
        // the exception would automatically
        // be thrown from Token class
        return new ResponseEntity<JsonObject>(new JsonResponse("SUCCESS", TOKEN.retrieveUserObject(token)), HttpStatus.OK);
    }

    // to be discussed
    @RequestMapping(path = "/history/{staffId}", method = RequestMethod.GET, produces = "application/json")
    public ArrayList<main.java.authentication.json.JsonObject> getHistory(@PathVariable("staffId") String staffId) throws SQLException, ClassNotFoundException {
        return historyscon.getHistory(staffId);
    }

    @RequestMapping(path = "/history/{msuId}", method = RequestMethod.DELETE)
    public String removeHistory(@PathVariable("msuId") String msuId) throws Exception {
        return historyscon.removeHistory(msuId);
    }

    @RequestMapping(path = "/msu/{msuId}", method = RequestMethod.GET, produces = "application/json")
    public ArrayList<JsonObject> getMassSlotUpload(@PathVariable("msuId") String msuId) throws SQLException, ClassNotFoundException {
//        ArrayList<JsonObject> result = historyscon.getMassSlotUpload(msuId);
        return historyscon.getMassSlotUpload(msuId);
    }

    // @RequestMapping(path = "/msu/{staffId}", method = RequestMethod.POST, produces = "application/json")
    // public String addMassSlotUpload(@RequestBody ArrayList<main.java.authentication.json.MassSlotUploadDetails> data,
    //                                 @PathVariable("staffId") String staffId) {
    //     try {
    //         int newHistoryId = historyscon.addHistory(staffId, getCurrentDateTime());
    //         return historyscon.addMassSlotUpload(data, newHistoryId);
    //     } catch (Exception e) {
    //         return "Fail";
    //     }
    // }

    public String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy, HH:mm:ss a");
        Date date = new Date();
        return formatter.format(date);
    }

    // following are api endpoints written for baseline table (previous known as facility_util)
    // have to be redon, can use the above code as reference

//    @RequestMapping(path = "/facility/{staffId}", method = RequestMethod.GET, produces = "application/json")
//    public ResponseEntity getFacilityUtil(@PathVariable("staffId") String staffId) {
//        try {
//            ArrayList<GetFacilityUtilResult> staffUsage = userscon.readFacilityUtil(staffId);
//            ArrayList<JsonObject> result = new ArrayList<JsonObject>();
//            for (JsonObject row : staffUsage) {
//                result.add(row);
//            }
//
//            return new ResponseEntity(new JsonResponses("SUCCESS", result), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity(new JsonError("ERROR", "Backend Issue: Exception occured at getFacilityUtil method in Controller.java. Database usersconection may be lost."), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @RequestMapping(path = "/facility/{staffId}", method = RequestMethod.POST, produces = "application/json")
//    public String updateFacilityUtil(@RequestBody ArrayList<FacilityUtil> data,
//                                     @PathVariable("staffId") String staffId) {
//        int faci_id = 0; // default faci_id
//        try {
//            ArrayList<GetFacilityUtilResult> staffUsage = userscon.readFacilityUtil(staffId);
//
//            if (staffUsage.size() == 0) { // no usage exist for specific user
//                faci_id = userscon.getNextFaciId();
//                String out = userscon.createFacilityUtil(data, staffId, faci_id);
//                return "created facility, next faci_id => " + faci_id;
//            } else if (staffUsage.size() > 0) {
//                faci_id = Integer.parseInt(staffUsage.get(0).getFaci_id());
//                userscon.removeUsage(staffId);
////                String out = userscon.createFacilityUtil(data, staffId, faci_id);
//                return userscon.createFacilityUtil(data, staffId, faci_id);
//            }
//            return "x";
//        } catch (Exception e) {
//            return "y";
//        }
//    }
//
//    @RequestMapping(path = "/facility/{staffId}", method = RequestMethod.DELETE, produces = "application/json")
//    public String removeUsage(@PathVariable("staffId") String staffId) {
//        try {
//            return userscon.removeHistory(staffId);
//        } catch (Exception e) {
//            return "Fail";
//        }
//    }
}
