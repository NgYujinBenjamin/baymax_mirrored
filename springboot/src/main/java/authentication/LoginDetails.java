package main.java.authentication;

import java.util.*;
import java.text.*;

public class LoginDetails{
    private String username;
    private String password;

    public LoginDetails(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
}