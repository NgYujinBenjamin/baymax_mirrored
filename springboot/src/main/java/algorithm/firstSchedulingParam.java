package main.java.algorithm;
import java.util.*;
import java.text.*;

import main.java.authentication.json.JsonObject;

public class firstSchedulingParam implements JsonObject{
    public List<Map<String,Object>> baseline;
    public List<Map<String,Object>> masterOps;
    public Integer bay;
    public Integer minGap;
    public Integer maxGap;

    public firstSchedulingParam(List<Map<String,Object>> baseline, List<Map<String,Object>> masterOps, Integer bay, Integer minGap, Integer maxGap){
        this.baseline = baseline;
        this.masterOps = masterOps;
        this.bay = bay;
        this.minGap = minGap;
        this.maxGap = maxGap;
    }
}