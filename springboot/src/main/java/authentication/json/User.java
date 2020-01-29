package main.java.authentication.json;

import java.util.*;
import java.text.*;

public class User implements JsonObject{
    public String username;
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
}