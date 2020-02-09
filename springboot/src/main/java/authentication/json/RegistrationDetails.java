package main.java.authentication.json;

import java.util.*;
import java.text.*;

//modify code - inform Ben
//@MODIFY - password, getPassword method
public class RegistrationDetails implements JsonObject{
    public String username;
    public String password;
    public String firstname;
    public String lastname;
    public String department;
    public String role;

    public RegistrationDetails(
        String username,
        String password,
        String firstname,
        String lastname,
        String department,
        String role
        ){
            this.username = username;
            this.password = password;
            this.firstname = firstname;
            this.lastname = lastname;
            this.department = department;
            this.role = role;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getFirstname(){
        return firstname;
    }
    public String getLastname(){
        return lastname;
    }
    public String getDepartment(){
        return department;
    }
    public String getRole(){
        return role;
    }
}