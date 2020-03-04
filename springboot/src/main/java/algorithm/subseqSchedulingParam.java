package main.java.algorithm;
import java.util.*;
import java.text.*;

import main.java.authentication.json.JsonObject;

public class subseqSchedulingParam implements JsonObject{
    public Map<String, Map<String, List<List<Object>>>> preSchedule;
    public Integer numBays;
    public Integer minGap;
    public Integer maxGap;

    public subseqSchedulingParam(Map<String, Map<String, List<List<Object>>>> preSchedule, Integer numBays, Integer minGap, Integer maxGap){
        this.preSchedule= preSchedule;
        this.numBays = numBays;
        this.minGap = minGap;
        this.maxGap = maxGap;
    }
}