package main.java.authentication;

import java.util.*;
import java.text.*;

public class Error implements JsonObject{
    private String message;

    public Error(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}