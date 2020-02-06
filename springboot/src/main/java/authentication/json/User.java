package main.java.authentication.json;

import java.util.*;
import java.text.*;

public class User implements JsonObject{
    public String staff_id;
    public String username;
    private String password;
    public String firstname;
    public String lastname;
    public String department;
    public String role;

    public User(
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

    public User(
        String staff_id,
        String username,
        String firstname,
        String lastname,
        String department,
        String role
        ){
            this.staff_id = staff_id;
            this.username = username;
            this.firstname = firstname;
            this.lastname = lastname;
            this.department = department;
            this.role = role;
    }
    
    public User(
        String staff_id,
        String username,
        String password,
        String firstname,
        String lastname,
        String department,
        String role
        ){
            this.staff_id = staff_id;
            this.username = username;
            this.password = password;
            this.firstname = firstname;
            this.lastname = lastname;
            this.department = department;
            this.role = role;
    }
    
    public String getStaff_id(){
        return staff_id;
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