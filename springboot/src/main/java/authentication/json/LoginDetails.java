package main.java.authentication.json;

import java.util.*;
import java.text.*;

public class LoginDetails implements JsonObject{
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