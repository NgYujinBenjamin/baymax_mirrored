package main.java.authentication.json.users;
import main.java.authentication.json.JsonObject;

import java.util.*;
import java.text.*;

public class RegistrationDetails extends User{
    public String password;
    
    public RegistrationDetails(
        String username,
        String password,
        String firstname,
        String lastname,
        String department,
        String role
        ){
            super(username, firstname, lastname, department, role);
            this.password = password;
            
    }

    public String getPassword(){
        return password;
    }
}