package main.java.authentication.json;

import java.util.*;
import java.text.*;

public class JsonError implements JsonObject{

    private String type;
    private String msg;
    
    public JsonError(String type, String msg){
        this.type = type;
        this.msg = msg;
    }

    public String getType(){
        return type;
    }

    public String getMessage(){
        return msg;
    }

}