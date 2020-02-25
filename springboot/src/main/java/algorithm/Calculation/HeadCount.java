package main.java.algorithm.Calculation;

import main.java.algorithm.Objects.*;

import java.util.*;

import com.google.gson.Gson;

public class HeadCount{
    private HashMap<String, Integer> quarterHC;
    
    public HeadCount(ArrayList<Product> allProduct){
        HashMap<String, Integer> quarterJobs = new HashMap<String, Integer>();
        quarterHC = new HashMap<String, Integer>();
        
        Integer newBuildCount;
        for (Product p: allProduct){
            String buildQtr = p.getBuildQtr();
            if (quarterJobs.containsKey(buildQtr)){
                newBuildCount = quarterJobs.get(buildQtr) + 1;
            } else {
                newBuildCount = 1;
            }
            quarterJobs.put(buildQtr, newBuildCount);
        }
        
        Set<String> quarters = quarterJobs.keySet();
        for(String q: quarters){
            Integer numQtrJobs = quarterJobs.get(q); 
            Integer numHeadCount = (int) Math.ceil((double) numQtrJobs/12);
            quarterHC.put(q, numHeadCount);
        }
    }

    public HashMap<String, Integer> getQuarterHC(){
        return quarterHC;
    }

}