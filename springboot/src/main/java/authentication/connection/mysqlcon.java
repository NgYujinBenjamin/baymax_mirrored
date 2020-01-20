package connection;

import java.sql.*;

public class mysqlcon{
    
    public String getUser(String username){
        try{
            Class.forName("com.mysql.jdbc.Driver");

            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/baymaxdb?useSSL=false","root","");
            Statement stmt=con.createStatement();
            String my_string = "select * from users WHERE username = '" + username + "'";
            ResultSet rs=stmt.executeQuery(my_string);

            String rv = "";
            while(rs.next())
            rv += (rs.getString(2)+" "+rs.getString(3));

            con.close();
            return rv;
        } catch(Exception e){ 
            System.out.println(e);
            return "Error";
        }
    }
    
    public String addUser(String username, String password, String firstname, String lastname, String department, String role){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/baymaxdb?useSSL=false","root","");
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
            Class.forName("com.mysql.jdbc.Driver");
            
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/baymaxdb?useSSL=false","root","");
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
            Class.forName("com.mysql.jdbc.Driver");
            
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/baymaxdb?useSSL=false","root","");
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