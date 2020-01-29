package main.java.authentication.json;

import java.util.*;
import java.text.*;

public class JsonResponse implements JsonObject{

    private String type;
    private JsonObject data;
    
    public JsonResponse(String type, JsonObject data){
        this.type = type;
        this.data = data;
    }

    public String getType(){
        return type;
    }

    public JsonObject getData(){
        return data;
    }

}