package main.java.authentication.json.users;

import java.util.*;
import java.text.*;

public class UserCredentials extends User{
    public String staff_id;
    
    public UserCredentials(
        String staff_id,
        String username,
        String firstname,
        String lastname,
        String department,
        String role
        ){
            super(username, firstname, lastname, department, role);
            this.staff_id = staff_id;
    }
}