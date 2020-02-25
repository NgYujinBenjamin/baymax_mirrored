package main.java.algorithm;
import java.util.*;
import java.text.*;

import main.java.authentication.json.JsonObject;

public class preTestingClass implements JsonObject{
    public List<Map<String,Object>> baseline;
    public List<Map<String,Object>> data;
    public Integer numBays;
    public Integer minGap;
    public Integer maxGap;

    public preTestingClass(List<Map<String,Object>> baseline, List<Map<String,Object>> data, Integer numBays, Integer minGap, Integer maxGap){
        this.baseline = baseline;
        this.data = data;
        this.numBays = numBays;
        this.minGap = minGap;
        this.maxGap = maxGap;
    }

    // public String getUsername(){
    //     return username;
    // }
    // public String getPassword(){
    //     return password;
    // }
}