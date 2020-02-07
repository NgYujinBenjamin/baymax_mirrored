package main.java.authentication.json;

import java.util.*;
import java.text.*;

public class JsonResponses implements JsonObject {

    private String type;
    private ArrayList<JsonObject> data;

    public JsonResponses(String type, ArrayList<JsonObject> data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public ArrayList<JsonObject> getData() {
        return data;
    }
}