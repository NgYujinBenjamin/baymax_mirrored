package main.java.algorithm.Calculation;

import main.java.algorithm.Objects.*;

import java.util.*;

import com.google.gson.Gson;

public class HeadCount{
    private HashMap<String, Integer> quarterHC;
    
    public HeadCount(HashMap<String, ArrayList<Product>> allProduct){
        
        Set<String> allProductKey = allProduct.keySet();
        quarterHC = new HashMap<String, Integer>();
        
        for (String qtrKey: allProductKey){
            Integer numQtrJobs = allProduct.get(qtrKey).size(); 
            Integer headCount = (int) Math.ceil((double) numQtrJobs/12);
            quarterHC.put(qtrKey, headCount);
        }
    }

    public HashMap<String, Integer> getQuarterHC(){
        return quarterHC;
    }

}