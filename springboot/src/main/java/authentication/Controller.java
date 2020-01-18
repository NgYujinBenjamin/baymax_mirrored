package authentication;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.text.SimpleDateFormat;

import connection.*;
import main.java.authentication.Algorithm;
import authentication.*;

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
    
    // to be completed
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

    // to be completed
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
