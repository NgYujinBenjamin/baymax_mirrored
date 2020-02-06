package main.java.authentication.json;

import java.util.*;
import java.text.*;

public class RegistrationDetails implements JsonObject{
    public String username;
    public String firstname;
    public String lastname;
    public String department;
    public String role;

    public RegistrationDetails(
        String username,
        String firstname,
        String lastname,
        String department,
        String role
        ){
            this.username = username;
            this.firstname = firstname;
            this.lastname = lastname;
            this.department = department;
            this.role = role;
    }

    public String getUsername(){
        return username;
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