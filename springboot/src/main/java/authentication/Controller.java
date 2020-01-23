package authentication;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.*;
import java.text.SimpleDateFormat;

import connection.*;
import main.java.authentication.Algorithm;
import main.java.authentication.JsonObject;
import main.java.authentication.LoginDetails;
import main.java.authentication.PseudoToken;
import main.java.authentication.User;
import main.java.authentication.Error;

import authentication.*;

@CrossOrigin
@RestController
public class Controller {

    private static final Token token = new Token();
    private static final Algorithm algorithm = new Algorithm();

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
                conn.addUser(username, password, firstname, lastname, department, role);
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
    
    //to be completed
    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes="application/json", produces= "application/json")
    public JsonObject login(@RequestBody LoginDetails inputDetails) throws Exception{        
        if (inputDetails.getUsername() == null || inputDetails.getPassword() == null){
            return new Error("Username or password cannot be empty");
        }
        mysqlcon conn = new mysqlcon();
        try {
            String username = inputDetails.getUsername();
            String user = conn.getUser(username);
            
            String pass = user.split(" ")[1];
            
            if (inputDetails.getPassword().equals(pass)){
                // Pseudo Token here should be changed into a JWT token instead when details are correct
                PseudoToken rv = new PseudoToken(inputDetails.getUsername() + " " + inputDetails.getPassword());
      
                return rv;
            }
            return new Error("Username or password is incorrect");
            
        } catch(Exception e) {
            Error rv = new Error("Exception occured during login");
            return rv;
        }   
    }
    
    // to be completed
    @RequestMapping(path = "/verify", method = RequestMethod.GET, produces = "application/json")
    public JsonObject verifyToken(@RequestParam(value="token") String user_token) throws Exception{
        //should be using JWT
        String input_name = user_token.split(" ")[0];
        String input_pass = user_token.split(" ")[1];    
        try {    
            return new User(input_name);
        } catch(Exception e) {
            return new Error("Token is invalid");
        }
           
    }

    // to be completed
    // @RequestMapping(path = "/generate", method = RequestMethod.GET)
    // public String generateToken(@RequestParam(value="username") String username) throws Exception{
    //     mysqlcon conn = new mysqlcon();
    //     try {
    //         String user = conn.getUser(username);
    //         String name = user.split(" ")[0];
    //         String pass = user.split(" ")[1];
    //         String return_token = token.createToken(username, pass);
    //         return return_token;   
    //     } catch(Exception e) {
    //         return "500";
    //     }
    // }

    

    // to be ported over, data test
    @RequestMapping(path= "/algorithm", method = RequestMethod.GET)
    public String algorithm(@RequestParam(value="data") Object[][] data) throws Exception{
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
            Date today = new Date();

            int max_bays = 26, num_rows = data.length, num_cols = data[0].length;
            ArrayList<Object[]>[] bay_usage = new ArrayList[max_bays];
            for (int i = 0; i < max_bays; i++) {
                bay_usage[i] = new ArrayList<Object[]>();
            }

            String cols = "";
            for (int i = 0; i < num_cols; i++) {
                if (i < num_cols - 1) {
                    cols += data[0][i] + ", ";
                } else {
                    cols += data[0][i];
                }
            }
            System.out.println(cols);

            int numToolsAllocated = 0;

            for (int i = 0; i < max_bays; i++) {
                Object[] earliestTool = algorithm.getEarliestTool(data);
                if (earliestTool[1] == null) {
                    break;
                }
                algorithm.reset(data, (int) earliestTool[3]);
                numToolsAllocated += 1;
                bay_usage[i].add(earliestTool);
                ArrayList<Object[]> toolsToBeQueuedAfterEarliest = new ArrayList<>();
                algorithm.updateToolsToBeQueuedAfterEarliest(earliestTool, data, toolsToBeQueuedAfterEarliest);
                numToolsAllocated += toolsToBeQueuedAfterEarliest.size();
                for (int j = 0; j < toolsToBeQueuedAfterEarliest.size(); j++) {
                    bay_usage[i].add(toolsToBeQueuedAfterEarliest.get(j));
                }
            }

            System.out.println(numToolsAllocated);
            int bay_num = 0;
            String out = "";
            for (int i = 0; i < max_bays; i++) {
                ArrayList<Object[]> objects = bay_usage[i];
                out += "Bay: "+(bay_num+1)+" ";
                for (int j = 0; j < objects.size(); j++) {
                    out+=objects.get(j)[0]+":::";

                }
                System.out.println(out);
                out = "";
                bay_num+=1;
            }
            return "";
        } catch(Exception e){
            return "Error";
        }
    }

}
