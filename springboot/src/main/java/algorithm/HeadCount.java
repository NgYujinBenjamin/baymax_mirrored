package main.java.algorithm;

import java.util.*;

import com.google.gson.Gson;

public class HeadCount{
    private HashMap<String, Integer> quarterHC;
    
    public HeadCount(List<Product> allProduct){
        HashMap<String, Integer> quarterJobs = new HashMap<String, Integer>();
        for (Product p: allProduct){
            String buildQtr = p.getBuildQtr();
            if (!quarterJobs.containsKey(buildQtr)){
                quarterJobs.put(buildQtr, 1);
            } else {
                int newJobCount = quarterJobs.get(buildQtr) + 1;
                quarterJobs.put(buildQtr, newJobCount);
            }
        }
        quarterHC = new HashMap<String, Integer>();
        Set<String> quarters = quarterJobs.keySet();
        for (String q: quarters){
            int headCount = (int) Math.ceil((double)quarterJobs.get(q)/12);
            quarterHC.put(q, headCount);
        }
    }

    public HashMap<String, Integer> getQuarterHC(){
        return quarterHC;
    }

}