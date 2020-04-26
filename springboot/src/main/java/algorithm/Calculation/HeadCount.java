package main.java.algorithm.Calculation;

import main.java.algorithm.Objects.*;

import java.util.*;

import com.google.gson.Gson;

public class HeadCount{
    private HashMap<String, Integer> quarterHC;
    
    /**
     * Calculates the quarterly headcount available to start building a new product.
     * In the current quarter, this is calculated by the number of outstanding jobs in the quarter/ number of weeks left in quarter 
     * In a full quarter, this is calculated by number of jobs in the quarter/ 12, where 12 is the rough estimate of the number of weeks in a quarter
     * @param allProduct Representing all future products to be built in the quarter
     */
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
        
        String currQuarter = getCurrentQtr();
        Set<String> quarters = quarterJobs.keySet();
        for(String q: quarters){
            Integer numQtrJobs = quarterJobs.get(q); 
            Integer numHeadCount = null;
            if (q.equals(currQuarter)) {
            	int currQtrRemWeeks = getCurrentQtrRemWeeks();
            	numHeadCount = (int) Math.ceil((double) numQtrJobs/currQtrRemWeeks);
            } else {
            	numHeadCount = (int) Math.ceil((double) numQtrJobs/12);
            } 
            quarterHC.put(q, numHeadCount);
        }
    }
    
    /**
     * Helper method to identify the current quarter based on the today's date
     * @return Current Quarter in the form of "CY_ _Q _" (e.g CY20Q1)
     */
    private String getCurrentQtr(){
        String quarter = "";
        
    	Date today = new Date();
        
        Integer year = today.getYear() - 100;
    	quarter += "CY" + year.toString();
    	
    	int month = today.getMonth();
    	if (month <= 2) {
    		quarter += "Q1";
    	} else if (month <= 5) {
    		quarter += "Q2";
    	} else if (month <= 8) {
    		quarter += "Q3";
    	} else {
    		quarter += "Q4";
    	}
    	return quarter;
    }
    
    /**
     * Helper method to calculate the number of weeks remaining in the quarter, from today's date
     * @return Number of weeks remaining in the quarter
     */
    private int getCurrentQtrRemWeeks(){
    	Date quarterEnd = null;
        
        Date today = new Date();
        
    	int year = today.getYear();
    	int month = today.getMonth();
    	
    	if (month <= 2) {
    		quarterEnd = new Date(year, 2, 31);
    	} else if (month <= 5) {
    		quarterEnd = new Date(year, 5, 30);
    	} else if (month <= 8) {
    		quarterEnd = new Date(year, 8, 30);
    	} else {
    		quarterEnd = new Date(year, 1, 31);
    	}
    	
    	int remWeeks = (int) ((quarterEnd.getTime() - today.getTime())/ (7* 24 * 60 * 60 * 1000));
    	
    	return remWeeks;
    }
    
    /**
     * Returns Headcount availablility for all build quarters in the masterOps products
     * @return Returns Headcount availability for all build quarters in the masterOps products
     */
    public HashMap<String, Integer> getQuarterHC(){
        return quarterHC;
    }

}