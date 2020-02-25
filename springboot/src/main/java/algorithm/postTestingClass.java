package main.java.algorithm;
import java.util.*;
import java.text.*;

import main.java.authentication.json.JsonObject;

public class postTestingClass implements JsonObject{
    public Map<String, Map<String, List<List<Object>>>> preSchedule;
    public Integer numBays;
    public Integer minGap;
    public Integer maxGap;

    public postTestingClass(Map<String, Map<String, List<List<Object>>>> preSchedule, Integer numBays, Integer minGap, Integer maxGap){
        this.preSchedule= preSchedule;
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