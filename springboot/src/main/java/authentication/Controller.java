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
import main.java.authentication.json.User;
import main.java.authentication.json.JsonError;
import main.java.authentication.json.JsonResponse;
import main.java.authentication.json.JsonSuccess;
import authentication.Token;

import authentication.*;

@CrossOrigin
@RestController
public class Controller {

    private static final Token TOKEN = new Token();

    // done
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String register(@RequestParam(value="username") String username,
                         @RequestParam(value="password") String password,
                         @RequestParam(value="firstname") String firstname,
                         @RequestParam(value="lastname") String lastname,
                         @RequestParam(value="department") String department,
                         @RequestParam(value="role") String role) throws Exception{
        if (role == null || department == null || lastname == null || firstname == null || password == null || username == null){
            return "400";
        } else {         
            mysqlcon conn = new mysqlcon();
            try {
                conn.addUser(username, TOKEN.generateMD5Hash(password), firstname, lastname, department, role);
                return "200";   
            } catch(Exception e) {
                return "400";
            }   
        }
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
    
    //done
    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes="application/json", produces= "application/json")
    public ResponseEntity<JsonObject> login(@RequestBody LoginDetails inputDetails) throws Exception{        
        if (inputDetails.getUsername() == null || inputDetails.getPassword() == null){
            return new ResponseEntity<JsonObject>(new JsonError("ERROR", "Username or password cannot be empty"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mysqlcon conn = new mysqlcon();
        try {
            String username = inputDetails.getUsername();
            String userObject = conn.getUser(username);
            
            String pass = userObject.split(" ")[1];
            
            if (TOKEN.generateMD5Hash(inputDetails.getPassword()).equals(pass)){
                String token = TOKEN.createToken(inputDetails.getUsername());

                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.set("x-auth-token", token);
                return new ResponseEntity<JsonObject>(new JsonResponse("SUCCESS", new JsonSuccess("200")), responseHeaders, HttpStatus.CREATED);
            }
            return new ResponseEntity<JsonObject>(new JsonError("ERROR", "Username or password is invalid"), HttpStatus.OK);
            
        } catch(Exception e) {
            return new ResponseEntity<JsonObject>(new JsonError("ERROR", "Backend Issue: Exception occured at login method in Controller.java"), HttpStatus.OK);
        }   
    }
    
    // done
    @RequestMapping(path = "/verify", method = RequestMethod.GET) //, method = RequestMethod.GET, produces = "application/json" 
    public ResponseEntity<JsonObject> verifyToken(@RequestHeader("x-auth-token") String token) throws Exception{   
        try {
            if (TOKEN.isTokenValid(token)){

                return new ResponseEntity<JsonObject>(new JsonResponse("SUCCESS", new User(TOKEN.retrieveUsername(token))), HttpStatus.OK);
            }
            return new ResponseEntity<JsonObject>(new JsonError("ERROR", "Token is invalid"), HttpStatus.OK);

        } catch(Exception e) {
            return new ResponseEntity<JsonObject>(new JsonError("ERROR", "Backend Issue: Exception occured at verify method in Controller.java"), HttpStatus.OK);
        }
           
    }

    // to be ported over, data test
    // @RequestMapping(path= "/algorithm", method = RequestMethod.GET)
    // public String algorithm(@RequestParam(value="data") Object[][] data) throws Exception{
    //     try {
    //         SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
    //         Date today = new Date();

    //         int max_bays = 26, num_rows = data.length, num_cols = data[0].length;
    //         ArrayList<Object[]>[] bay_usage = new ArrayList[max_bays];
    //         for (int i = 0; i < max_bays; i++) {
    //             bay_usage[i] = new ArrayList<Object[]>();
    //         }

    //         String cols = "";
    //         for (int i = 0; i < num_cols; i++) {
    //             if (i < num_cols - 1) {
    //                 cols += data[0][i] + ", ";
    //             } else {
    //                 cols += data[0][i];
    //             }
    //         }
    //         System.out.println(cols);

    //         int numToolsAllocated = 0;

    //         for (int i = 0; i < max_bays; i++) {
    //             Object[] earliestTool = algorithm.getEarliestTool(data);
    //             if (earliestTool[1] == null) {
    //                 break;
    //             }
    //             algorithm.reset(data, (int) earliestTool[3]);
    //             numToolsAllocated += 1;
    //             bay_usage[i].add(earliestTool);
    //             ArrayList<Object[]> toolsToBeQueuedAfterEarliest = new ArrayList<>();
    //             algorithm.updateToolsToBeQueuedAfterEarliest(earliestTool, data, toolsToBeQueuedAfterEarliest);
    //             numToolsAllocated += toolsToBeQueuedAfterEarliest.size();
    //             for (int j = 0; j < toolsToBeQueuedAfterEarliest.size(); j++) {
    //                 bay_usage[i].add(toolsToBeQueuedAfterEarliest.get(j));
    //             }
    //         }

    //         System.out.println(numToolsAllocated);
    //         int bay_num = 0;
    //         String out = "";
    //         for (int i = 0; i < max_bays; i++) {
    //             ArrayList<Object[]> objects = bay_usage[i];
    //             out += "Bay: "+(bay_num+1)+" ";
    //             for (int j = 0; j < objects.size(); j++) {
    //                 out+=objects.get(j)[0]+":::";

    //             }
    //             System.out.println(out);
    //             out = "";
    //             bay_num+=1;
    //         }
    //         return "";
    //     } catch(Exception e){
    //         return "Error";
    //     }
    // }

    //testing method
    @RequestMapping(path = "/hello", method = RequestMethod.GET)
    public JsonObject hello() throws Exception{
        
        try {
            return new JsonResponse("SUCCESS", new User("Ben"));   
        } catch(Exception e) {
            return new JsonResponse("ERROR", new User("Ben"));
        }   
    
    }

    // testing method
    // public ResponseEntity<String> handle(String name, String value) {
    //     HttpHeaders responseHeaders = new HttpHeaders();
    //     responseHeaders.set(name, value);
    //     return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED);
    // }

}
