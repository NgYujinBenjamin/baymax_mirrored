package connection;

import java.sql.*;
import java.util.*;

import main.java.authentication.json.User;

public class mysqlcon{
    
    private final String connectionPassword = "";
    private final String port = "3306";
    private final String databaseName = "baymaxdb";
    private final String connection = "jdbc:mysql://localhost:"+port+"/"+databaseName+"?useSSL=false";
    private final String driverName = "com.mysql.jdbc.Driver";

    public User getUser(String username){
        try{
            Class.forName(driverName);

            Connection con=DriverManager.getConnection(connection,"root",connectionPassword);
            Statement stmt=con.createStatement();
            String my_string = "select * from users WHERE username = '" + username + "'";
            ResultSet rs=stmt.executeQuery(my_string);

            User rv = null;

            while(rs.next()){
                rv = new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7));
            }
            con.close();
            return rv;
        } catch(Exception e){ 
            System.out.println(e);
            //to be changed
            return new User("error","error","error","error","error");
        }
    }

    public ArrayList<User> getAllUsers(){
        try{
            Class.forName(driverName);

            Connection con=DriverManager.getConnection(connection,"root",connectionPassword);
            Statement stmt=con.createStatement();
            String my_string = "select * from users";
            ResultSet rs=stmt.executeQuery(my_string);

            ArrayList<User> rv = new ArrayList<User>();
            while(rs.next()){
                rv.add(new User(rs.getString(1),rs.getString(2),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));
            }
            con.close();
            return rv;
        } catch(Exception e){ 
            System.out.println(e);
            return new ArrayList<User>();
        }
    }
    
    public String addUser(String username, String password, String firstname, String lastname, String department, String role){
        try{
            Class.forName(driverName);

            Connection con=DriverManager.getConnection(connection,"root",connectionPassword);
            Statement stmt=con.createStatement();
            String my_string = "insert into users (username, password, firstname, lastname, department, role) values ('" + username + "', '" + password + "', '" + firstname + "', '" + lastname + "', '" + department + "', '" + role + "')";
            stmt.executeUpdate(my_string);
            con.close();
            return "200";
        } catch(Exception e){ 
            System.out.println(e);
            return "400";
        }
    }

    public String changePassword(String username, String oldpassword, String newpassword){
        try{
            Class.forName(driverName);

            Connection con=DriverManager.getConnection(connection,"root",connectionPassword);
            Statement stmt=con.createStatement();
            String my_string = "update users set password = '" + newpassword + "' where username = '" + username + "' and password = '" + oldpassword + "';";
            stmt.executeUpdate(my_string);
            con.close();
            
            return "200";
        } catch(Exception e){
            System.out.println(e);
            return "400";
        }
    }

    public String resetPassword(String username){
        try{
            Class.forName(driverName);

            Connection con=DriverManager.getConnection(connection,"root",connectionPassword);
            Statement stmt=con.createStatement();
            String my_string = "update users set password = 'password' where username = '" + username + "';";
            stmt.executeUpdate(my_string);
            con.close();
            
            return "200";
        } catch(Exception e){
            System.out.println(e);
            return "400";
        }
    }
    
}