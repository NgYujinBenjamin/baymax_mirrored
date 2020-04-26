package main.java.authentication.json.users;

import java.util.*;
import java.text.*;

import main.java.authentication.json.JsonObject;

public class ResetPassword implements JsonObject{
    private String username;

    public ResetPassword(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }
}