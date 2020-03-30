package main.java.history;

import java.util.*;
import java.text.*;

import main.java.authentication.json.JsonObject;

public class preSchedule implements JsonObject{
    public Map<String,List<List<Object>>> baseLineOccupancy;
    public Map<String,List<List<Object>>> bayOccupancy;
    public Integer minGap;
    public Integer maxGap;
    public Integer numBays;
    public Integer staffID;

    public preSchedule(
        Map<String,List<List<Object>>> baseLineOccupancy, Map<String,List<List<Object>>> bayOccupancy, Integer numBays, Integer minGap, Integer maxGap, Integer staffID){
        this.baseLineOccupancy = baseLineOccupancy;
        this.bayOccupancy = bayOccupancy;
        this.numBays = numBays;
        this.minGap = minGap;
        this.maxGap = maxGap;
        this.staffID = staffID;
    }
}