package algorithm;
import authentication.json.JsonObject;

import java.util.*;
import java.text.*;


public class firstSchedulingParam implements JsonObject {
    public List<Map<String,Object>> baseLineData;
    public List<Map<String,Object>> data;
    public Integer numBays;
    public Integer minGap;
    public Integer maxGap;

    public firstSchedulingParam(List<Map<String,Object>> baseLineData, List<Map<String,Object>> data, Integer numBays, Integer minGap, Integer maxGap){
        this.baseLineData = baseLineData;
        this.data = data;
        this.numBays = numBays;
        this.minGap = minGap;
        this.maxGap = maxGap;
    }
}