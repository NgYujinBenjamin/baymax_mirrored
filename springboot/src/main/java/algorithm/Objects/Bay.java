package algorithm.Objects;

import java.util.*;

import algorithm.ExclusionStrategy.Exclude;
import com.google.gson.Gson;
import algorithm.Objects.Product;

public class Bay implements Comparable<Bay>{
    @Exclude
    private Integer bayID;
    
    private ArrayList<Product> baySchedule;
    
    @Exclude
    private Date availableDate;

    public Bay(Integer bayID){
        this.bayID = bayID;
        baySchedule = new ArrayList<Product>();
        availableDate = new Date(0);
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

    public Integer getBayID(){
        return bayID;
    }
}