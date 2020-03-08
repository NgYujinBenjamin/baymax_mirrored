package main.java.algorithm;
import java.util.*;
import java.text.*;

import main.java.authentication.json.JsonObject;

public class subseqSchedulingParam implements JsonObject{
    public Map<String, List<List<Object>>> baseLineOccupancy;
    public Map<String, List<List<Object>>> bayOccupancy;
    public Integer numBays;
    public Integer minGap;
    public Integer maxGap;

    public subseqSchedulingParam(Map<String, List<List<Object>>> baseLineOccupancy, Map<String, List<List<Object>>> bayOccupancy, Integer numBays, Integer minGap, Integer maxGap){
        this.baseLineOccupancy = baseLineOccupancy;
        this.bayOccupancy = bayOccupancy;
        this.numBays = numBays;
        this.minGap = minGap;
        this.maxGap = maxGap;
    }
}