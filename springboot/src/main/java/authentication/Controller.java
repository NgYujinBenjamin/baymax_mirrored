package main.java.authentication;

import com.auth0.jwt.JWT;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import org.springframework.http.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;

import connection.*;

import main.java.authentication.json.users.*;
import main.java.authentication.json.*;
import authentication.Token;

import authentication.*;

import main.java.exceptions.*;


@CrossOrigin
@RestController
public class Controller {

    private static final Token TOKEN = new Token();

    // done
    @RequestMapping(path = "/register", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public JsonObject register(@RequestBody RegistrationDetails userDetails) throws SQLException, ClassNotFoundException{
        mysqlcon conn = new mysqlcon();
        try {
            conn.addUser(userDetails.getUsername(),
                    TOKEN.generateMD5Hash(userDetails.getPassword()),
                    userDetails.getFirstname(),
                    userDetails.getLastname(),
                    userDetails.getDepartment(),
                    userDetails.getRole());

            String token = TOKEN.createToken(userDetails.getUsername());
            return new TokenSuccess(token);
        } catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e){
            throw new ClassNotFoundException("Backend Issue: ClassNotFound Exception at register method in Controller.java");
        }
    }

    // original by Ben
    @RequestMapping(path = "/getusers", method = RequestMethod.GET, produces = "application/json")
    public ArrayList<User> getUsers() throws SQLException, ClassNotFoundException{
        mysqlcon conn = new mysqlcon();
        try {
            return conn.getAllUsers();

        } catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Backend Issue: ClassNotFound Exception at getUsers method in Controller.java");
        }
    }

    // done
    @RequestMapping(path = "/changepassword", method = RequestMethod.POST)
    public JsonObject changePass(@RequestBody NewPassword details) throws SQLException, ClassNotFoundException {
        if (details.getOldPassword() == null || details.getNewPassword() == null || details.getUsername() == null) {
            throw new NullPointerException("Username or password cannot be empty.");
        }
        mysqlcon conn = new mysqlcon();
        try {
            conn.changePassword(details.getUsername(), TOKEN.generateMD5Hash(details.getOldPassword()), TOKEN.generateMD5Hash(details.getNewPassword()));
            return new JsonSuccess("Password has been updated successfully.");
        } catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e){
            throw new ClassNotFoundException("Backend Issue: ClassNotFound Exception at changePassword method in Controller.java");
        }
    }

    // done
    @RequestMapping(path = "/resetpassword", method = RequestMethod.GET)
    public JsonObject resetPass(@RequestParam(value = "username") String username) throws SQLException, ClassNotFoundException {
        if (username == null) {
            throw new NullPointerException("Username cannot be empty.");
        } 
        mysqlcon conn = new mysqlcon();
        try {
            conn.resetPassword(username);
            return new JsonSuccess("Password has been reset successfully");
        } catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e){
            throw new ClassNotFoundException("Backend Issue: ClassNotFound Exception at changePassword method in Controller.java");
        }
    }


    // done    
    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public JsonObject login(@RequestBody LoginDetails inputDetails) throws SQLException, ClassNotFoundException, NullPointerException, InvalidTokenException{
        if (inputDetails.getUsername() == null || inputDetails.getPassword() == null) {
            throw new NullPointerException("Username or password cannot be empty.");
        }
        mysqlcon conn = new mysqlcon();
        try {
            String username = inputDetails.getUsername();
            RegistrationDetails userObject = conn.getUser(username);

            String pass = userObject.getPassword();

            if (TOKEN.generateMD5Hash(inputDetails.getPassword()).equals(pass)) {
                String token = TOKEN.createToken(inputDetails.getUsername());

                return new TokenSuccess(token);
            } else {
                throw new InvalidTokenException("Username or password is invalid");
            }
        } catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e){
            throw new ClassNotFoundException("Backend Issue: ClassNotFound Exception at login method in Controller.java");
        } catch (NullPointerException e){
            throw new NullPointerException("Username or password is invalid.");
        }
    }

    // done
    @RequestMapping(path = "/verify", method = RequestMethod.GET)
    public ResponseEntity<JsonObject> verifyToken(@RequestHeader("x-auth-token") String token)  throws Exception {
        try {

            if (TOKEN.isTokenValid(token)) {

                return new ResponseEntity<JsonObject>(new JsonResponse("SUCCESS", TOKEN.retrieveUserObject(token)), HttpStatus.OK);
            }
            throw new InvalidTokenException("Token is invalid");

        }  catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e){
            throw new ClassNotFoundException("Backend Issue: ClassNotFound Exception at verify method in Controller.java");
        } catch (NullPointerException e){
            throw new NullPointerException("Token is invalid");
        }

    }

    // to be discussed
    @RequestMapping(path = "/history/{staffId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getHistory(@PathVariable("staffId") String staffId) {
        mysqlcon conn = new mysqlcon();
        try {
            ArrayList<JsonObject> result = conn.getHistory(staffId);
            return new ResponseEntity(new JsonResponses("SUCCESS", result), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new JsonError("ERROR", "Backend Issue: Exception occured at getHistory method in Controller.java. Database connection may be lost."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/history/{msuId}", method = RequestMethod.DELETE)
    public String removeHistory(@PathVariable("msuId") String msuId) {
        mysqlcon conn = new mysqlcon();
        try {
            return conn.removeHistory(msuId);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @RequestMapping(path = "/msu/{msuId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getMassSlotUpload(@PathVariable("msuId") String msuId) {
        mysqlcon conn = new mysqlcon();
        try {
            ArrayList<JsonObject> result = conn.getMassSlotUpload(msuId);
            return new ResponseEntity(new JsonResponses("SUCCESS", result), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new JsonError("ERROR", "Backend Issue: Exception occured at getMassSlotUpload method in Controller.java. Database connection may be lost."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/msu/{staffId}", method = RequestMethod.POST, produces = "application/json")
    public String addMassSlotUpload(@RequestBody ArrayList<MassSlotUploadDetails> data,
                                    @PathVariable("staffId") String staffId) {
        mysqlcon conn = new mysqlcon();
        try {
            int newHistoryId = conn.addHistory(staffId);
            return conn.addMassSlotUpload(data, newHistoryId);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @RequestMapping(path = "/facility/{staffId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getFacilityUtil(@PathVariable("staffId") String staffId) {
        mysqlcon conn = new mysqlcon();
        try {
            ArrayList<GetFacilityUtilResult> staffUsage = conn.readFacilityUtil(staffId);
            ArrayList<JsonObject> result = new ArrayList<JsonObject>();
            for (JsonObject row : staffUsage) {
                result.add(row);
            }

            return new ResponseEntity(new JsonResponses("SUCCESS", result), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new JsonError("ERROR", "Backend Issue: Exception occured at getFacilityUtil method in Controller.java. Database connection may be lost."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/facility/{staffId}", method = RequestMethod.POST, produces = "application/json")
    public String updateFacilityUtil(@RequestBody ArrayList<FacilityUtil> data,
                                     @PathVariable("staffId") String staffId) {
        mysqlcon conn = new mysqlcon();
        int faci_id = 0; // default faci_id
        try {
            ArrayList<GetFacilityUtilResult> staffUsage = conn.readFacilityUtil(staffId);

            if (staffUsage.size() == 0) { // no usage exist for specific user
                faci_id = conn.getNextFaciId();
                String out = conn.createFacilityUtil(data, staffId, faci_id);
                return "created facility, next faci_id => " + faci_id;
            } else if (staffUsage.size() > 0) {
                faci_id = Integer.parseInt(staffUsage.get(0).getFaci_id());
                conn.removeUsage(staffId);
//                String out = conn.createFacilityUtil(data, staffId, faci_id);
                return conn.createFacilityUtil(data, staffId, faci_id);
            }
            return "x";
        } catch (Exception e) {
            return "y";
        }
    }

    @RequestMapping(path = "/facility/{staffId}", method = RequestMethod.DELETE, produces = "application/json")
    public String removeUsage(@PathVariable("staffId") String staffId) {
        mysqlcon conn = new mysqlcon();
        try {
            return conn.removeHistory(staffId);
        } catch (Exception e) {
            return "Fail";
        }
    }


}
