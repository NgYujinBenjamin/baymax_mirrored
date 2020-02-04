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

import java.util.*;
import java.text.SimpleDateFormat;

import connection.*;

import main.java.authentication.json.JsonObject;
import main.java.authentication.json.LoginDetails;
import main.java.authentication.json.TokenSuccess;
import main.java.authentication.json.User;
import main.java.authentication.json.JsonError;
import main.java.authentication.json.JsonResponse;
import main.java.authentication.json.JsonSuccess;
import main.java.authentication.json.TokenSuccess;
import authentication.Token;

import authentication.*;

@CrossOrigin
@RestController
public class Controller {

    private static final Token TOKEN = new Token();

    // done
    // @RequestParam(value="username") String username,
    //                      @RequestParam(value="password") String password,
    //                      @RequestParam(value="firstname") String firstname,
    //                      @RequestParam(value="lastname") String lastname,
    //                      @RequestParam(value="department") String department,
    //                      @RequestParam(value="role") String role
    @RequestMapping(path = "/register", method = RequestMethod.POST, consumes="application/json", produces= "application/json")
    public JsonObject register(@RequestBody User userDetails) throws Exception{
        // if (role == null || department == null || lastname == null || firstname == null || password == null || username == null){
        //     return "400";
        // } else {         
        mysqlcon conn = new mysqlcon();
        try {
            conn.addUser(userDetails.getUsername(), TOKEN.generateMD5Hash("password"), userDetails.getFirstName(), userDetails.getLastName(), userDetails.getDepartment(), userDetails.getRole());
            return new JsonSuccess("200");   
        } catch(Exception e) {
            return new JsonError("ERROR", "Backend Issue: Exception occured at register method in Controller.java");
        }   
        // }
    }
    
    // done
    @RequestMapping(path = "/changepassword", method = RequestMethod.GET)
    public String changePass(@RequestParam(value="username") String username,
                         @RequestParam(value="oldpassword") String oldpassword,
                         @RequestParam(value="newpassword") String newpassword) throws Exception{
        if (oldpassword == null || newpassword == null || username == null){
            return "400";
        } else {         
            mysqlcon conn = new mysqlcon();
            try {
                conn.changePassword(username, oldpassword, newpassword);
                return "200";   
            } catch(Exception e) {
                return "400";
            }   
        }
    }

    // done
    @RequestMapping(path = "/resetpassword", method = RequestMethod.GET)
    public String resetPass(@RequestParam(value="username") String username) throws Exception{
        if (username == null){
            return "400";
        } else {         
            mysqlcon conn = new mysqlcon();
            try {
                conn.resetPassword(username);
                return "200";   
            } catch(Exception e) {
                return "400";
            }   
        }
    }
    

    // done    
    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes="application/json", produces= "application/json")
    public JsonObject login(@RequestBody LoginDetails inputDetails) throws Exception{        
        if (inputDetails.getUsername() == null || inputDetails.getPassword() == null){
            return new JsonError("ERROR", "Username or password cannot be empty");
        }
        mysqlcon conn = new mysqlcon();
        try {
            String username = inputDetails.getUsername();
            String userObject = conn.getUser(username);
            
            String pass = userObject.split(" ")[1];
            
            if (TOKEN.generateMD5Hash(inputDetails.getPassword()).equals(pass)){
                String token = TOKEN.createToken(inputDetails.getUsername());

                return new TokenSuccess(token);
            } else {
                return new JsonError("ERROR", "Username or password is invalid");
            }
        } catch(Exception e) {
            return new JsonError("ERROR", "Backend Issue: Exception occured at login method in Controller.java");
        }   
    }

    // done
    @RequestMapping(path = "/verify", method = RequestMethod.GET) //, method = RequestMethod.GET, produces = "application/json" 
    public ResponseEntity<JsonObject> verifyToken(@RequestHeader("x-auth-token") String token) throws Exception{   
        try {
            
            if (TOKEN.isTokenValid(token)){

                return new ResponseEntity<JsonObject>(new JsonResponse("SUCCESS", TOKEN.retrieveUserObject(token)), HttpStatus.OK);
            }
            return new ResponseEntity<JsonObject>(new JsonError("ERROR", "Token is invalid"), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch(Exception e) {
            return new ResponseEntity<JsonObject>(new JsonError("ERROR", "Backend Issue: Exception occured at verify method in Controller.java, token might be missing"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
           
    }


    //testing method
    // @RequestMapping(path = "/hello", method = RequestMethod.GET)
    // public JsonObject hello() throws Exception{
        
    //     try {
    //         return new JsonResponse("SUCCESS", new User("Ben"));   
    //     } catch(Exception e) {
    //         return new JsonResponse("ERROR", new User("Ben"));
    //     }   
    
    // }

    // testing method
    // public ResponseEntity<String> handle(String name, String value) {
    //     HttpHeaders responseHeaders = new HttpHeaders();
    //     responseHeaders.set(name, value);
    //     return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED);
    // }

}
