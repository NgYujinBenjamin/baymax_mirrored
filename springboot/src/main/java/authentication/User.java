package main.java.authentication;

import java.util.*;
import java.text.*;

public class User{
    private String username;

    public User(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }
}