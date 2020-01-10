package authentication;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;


import connection.*;
import authentication.*;

@RestController
public class Controller {

    private static final Token token = new Token();
    
    @RequestMapping(path = "/signup", method = RequestMethod.GET)
    public String signup(@RequestParam(value="username") String username,
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
    
    @RequestMapping(path = "/generate", method = RequestMethod.GET)
    public String generateToken(@RequestParam(value="username") String username) throws Exception{
        mysqlcon conn = new mysqlcon();
        try {
            String user = conn.getUser(username);
            String name = user.split(" ")[0];
            String pass = user.split(" ")[1];
            String return_token = token.createToken(username, pass);
            return return_token;   
        } catch(Exception e) {
            return "500";
        }
    }

    @RequestMapping(path = "/authenticate", method = RequestMethod.GET)
    public Boolean authenticateToken(@RequestParam(value="username") String username,
                                     @RequestParam(value="token") String user_token) throws Exception{
         mysqlcon conn = new mysqlcon();
        try {
            String user = conn.getUser(username);
            String name = user.split(" ")[0];
            String pass = user.split(" ")[1];        
            Boolean is_valid = token.authenticateToken(user_token, name, pass);
            return is_valid;
        } catch(Exception e) {
            return false;
        }
        
        
    }

    @RequestMapping(path= "/hello", method = RequestMethod.GET)
    public String helloworld() throws Exception{
        mysqlcon conn = new mysqlcon();
        try {
            return "Hello world!";
        } catch(Exception e){
            return "Error";
        }
    }

}
