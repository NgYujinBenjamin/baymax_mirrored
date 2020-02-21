package main.java.algorithm.Objects;

import main.java.algorithm.ExclusionStrategy.*;

import java.util.*;

import com.google.gson.Gson;

public class Bay implements Comparable<Bay>{
    private ArrayList<Product> baySchedule;
    
    @Exclude
    private Date availableDate;

    public Bay (){
        baySchedule = new ArrayList<Product>();
        availableDate = new Date(0);
    }

    public Bay (Date availableDate){
        baySchedule = new ArrayList<Product>();
        this.availableDate = availableDate;
    }

    public void addToBaySchedule (Product p){
        baySchedule.add(p);
        availableDate = p.getLeaveBayDate();
    }

    public Date getAvailableDate(){
        return availableDate;
    }

    public ArrayList<Product> getBaySchedule (){
        return baySchedule;
    }

    public int compareTo(Bay other){
        Date thisAvailDate = availableDate;
        Date otherAvailDate = other.availableDate;
        
        return thisAvailDate.compareTo(otherAvailDate);
    }

    public static String toJSONString(Bay b){
        Gson gson = new Gson();
        String json = gson.toJson(b);
        return json;
    }
}