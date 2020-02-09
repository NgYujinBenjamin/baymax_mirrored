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

import java.util.*;
import java.text.SimpleDateFormat;

import connection.*;

import main.java.authentication.json.JsonObject;
import main.java.authentication.json.LoginDetails;
import main.java.authentication.json.RegistrationDetails;
import main.java.authentication.json.TokenSuccess;
import main.java.authentication.json.User;
import main.java.authentication.json.JsonError;
import main.java.authentication.json.JsonResponse;
import main.java.authentication.json.JsonSuccess;
import main.java.authentication.json.TokenSuccess;
import main.java.authentication.json.HistoryDetails;
import main.java.authentication.json.JsonResponses;
import main.java.authentication.json.MassSlotUploadDetails;
import main.java.authentication.json.FacilityUtil;
import main.java.authentication.json.GetFacilityUtilResult;
import authentication.Token;

import authentication.*;

@CrossOrigin
@RestController
public class Controller {

    private static final Token TOKEN = new Token();

    // done
    // modify code - inform Ben
    // @MODIFY - return token, hashed password
    @RequestMapping(path = "/register", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public JsonObject register(@RequestBody RegistrationDetails userDetails) throws Exception {
        mysqlcon conn = new mysqlcon();
        try {
            conn.addUser(userDetails.getUsername(),
                    TOKEN.generateMD5Hash(userDetails.getPassword()),
                    userDetails.getFirstname(),
                    userDetails.getLastname(),
                    userDetails.getDepartment(),
                    userDetails.getRole());

            User userObject = conn.getUser(userDetails.getUsername());

            String token = TOKEN.createToken(userObject.getUsername());
            return new TokenSuccess(token);
            // return userObject;
        } catch (Exception e) {
            return new JsonError("ERROR", "Backend Issue: Exception occured at register method in Controller.java");
        }
    }

    // original by Ben
    @RequestMapping(path = "/getusers", method = RequestMethod.GET, produces = "application/json")
    public ArrayList<User> getUsers() throws Exception {
        mysqlcon conn = new mysqlcon();
        try {
            return conn.getAllUsers();
        } catch (Exception e) {
            return new ArrayList<User>();
        }
    }

    // alternative by naj to return list of users with the success message
//    @RequestMapping(path = "/getusers", method = RequestMethod.GET, produces = "application/json")
//    public ResponseEntity getUsers() throws Exception {
//        mysqlcon conn = new mysqlcon();
//        try {
//            ArrayList<JsonObject> result = conn.getAllUsers();
//            return new ResponseEntity(new JsonResponses("SUCCESS", result), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity(new JsonError("ERROR", "Backend Issue: Exception occured at getusers method in Controller.java. Database connection may be lost."), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    // done
    @RequestMapping(path = "/changepassword", method = RequestMethod.GET)
    public String changePass(@RequestParam(value = "username") String username,
                             @RequestParam(value = "oldpassword") String oldpassword,
                             @RequestParam(value = "newpassword") String newpassword) throws Exception {
        if (oldpassword == null || newpassword == null || username == null) {
            return "400";
        } else {
            mysqlcon conn = new mysqlcon();
            try {
                conn.changePassword(username, oldpassword, newpassword);
                return "200";
            } catch (Exception e) {
                return "400";
            }
        }
    }

    // done
    @RequestMapping(path = "/resetpassword", method = RequestMethod.GET)
    public String resetPass(@RequestParam(value = "username") String username) throws Exception {
        if (username == null) {
            return "400";
        } else {
            mysqlcon conn = new mysqlcon();
            try {
                conn.resetPassword(username);
                return "200";
            } catch (Exception e) {
                return "400";
            }
        }
    }


    // done    
    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public JsonObject login(@RequestBody LoginDetails inputDetails) throws Exception {
        if (inputDetails.getUsername() == null || inputDetails.getPassword() == null) {
            return new JsonError("ERROR", "Username or password cannot be empty");
        }
        mysqlcon conn = new mysqlcon();
        try {
            String username = inputDetails.getUsername();
            User userObject = conn.getUser(username);

            String pass = userObject.getPassword();

            if (TOKEN.generateMD5Hash(inputDetails.getPassword()).equals(pass)) {
                String token = TOKEN.createToken(inputDetails.getUsername());

                return new TokenSuccess(token);
            } else {
                return new JsonError("ERROR", "Username or password is invalid");
            }
        } catch (Exception e) {
            return new JsonError("ERROR", "Backend Issue: Exception occured at login method in Controller.java");
        }
    }

    // done
    @RequestMapping(path = "/verify", method = RequestMethod.GET)
    //, method = RequestMethod.GET, produces = "application/json"
    public ResponseEntity<JsonObject> verifyToken(@RequestHeader("x-auth-token") String token) throws Exception {
        try {

            if (TOKEN.isTokenValid(token)) {

                return new ResponseEntity<JsonObject>(new JsonResponse("SUCCESS", TOKEN.retrieveUserObject(token)), HttpStatus.OK);
            }
            return new ResponseEntity<JsonObject>(new JsonError("ERROR", "Token is invalid"), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            return new ResponseEntity<JsonObject>(new JsonError("ERROR", "Backend Issue: Exception occured at verify method in Controller.java, token might be missing"), HttpStatus.INTERNAL_SERVER_ERROR);
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
