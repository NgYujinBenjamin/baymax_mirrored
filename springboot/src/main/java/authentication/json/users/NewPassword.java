package main.java.authentication.json.users;

import java.util.*;
import java.text.*;

import main.java.authentication.json.JsonObject;

public class NewPassword implements JsonObject{
    private String username;
    private String oldpassword;
    private String newpassword;

    public NewPassword(String username, String oldpassword, String newpassword){
        this.username = username;
        this.oldpassword = oldpassword;
        this.newpassword = newpassword;
    }

    public String getUsername(){
        return username;
    }
    public String getOldPassword(){
        return oldpassword;
    }
    public String getNewPassword(){
        return newpassword;
    }
}