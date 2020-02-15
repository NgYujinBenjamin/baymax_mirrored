package main.java.algorithm;

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

    public void addToBaySchedule (Product p){
        baySchedule.add(p);
        availableDate = p.getEndDate();
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