package main.java.authentication.json.users;

import java.util.*;
import java.text.*;

import main.java.authentication.json.JsonObject;

public class ResetPassword implements JsonObject{
    private String username;
    // private String password;

    public ResetPassword(String username){
        this.username = username;
        // this.password = password;
    }

    public String getUsername(){
        return username;
    }
    // public String getPassword(){
        // return password;
    // }
}