package main.java.authentication.json;

import java.util.*;
import java.text.*;

public class JsonSuccess implements JsonObject{

    private String message;
    
    public JsonSuccess(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

}