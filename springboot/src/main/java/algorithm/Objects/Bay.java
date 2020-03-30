package main.java.algorithm.Objects;

import main.java.algorithm.ExclusionStrategy.*;

import java.util.*;

import com.google.gson.Gson;

public class Bay implements Comparable<Bay>{
    @Exclude
    private Integer bayID;
    
    private ArrayList<Product> baySchedule;
    
    @Exclude
    private Date availableDate;
    
	/**
	 * Constructor for Bay class. Creates a new Bay object with the assigned bay ID, bay availability set to January 1, 1970 and intializes an empty bay schedule.    
	 * @param bayID Bay ID assigned to the object
	 */
    public Bay(Integer bayID){
        this.bayID = bayID;
        baySchedule = new ArrayList<Product>();
        availableDate = new Date(0);
    }

    /**
     * To assign products to this bay and add to the bay schedule
     * @param p Product that is to be added to the bay
     */
    public void addToBaySchedule (Product p){
        p.setAssignedBayID(bayID);
        
        baySchedule.add(p);
        availableDate = p.getLeaveBayDate();
    }

    /**
     * Obtain the earliest available date that the bay is vacant. This is based on the date at which the last product leaves the bay.
     * @return Date from which bay is vacant
     */
    public Date getAvailableDate(){
        return availableDate;
    }

    /**
     * Obtain the sequence of products to be built in this bay
     * @return Arraylist of Products representing the bay schedule 
     */
    public ArrayList<Product> getBaySchedule (){
        return baySchedule;
    }

    /**
     * Compares 2 bays based on availability date. Earlier bay 
     * @param other Other bay to be compared with
     * @return 0 if this Bay has the same availability date as the other Bay; Less than 0 if this Bay has an earlier availability Date than the other Bay; More than 0 if this Bay has a later availability Date than the other Bay
     */
    public int compareTo(Bay other){
        Date thisAvailDate = availableDate;
        Date otherAvailDate = other.availableDate;
        
        return thisAvailDate.compareTo(otherAvailDate);
    }

    /**
     * Obtain the ID assigned to the Bay
     * @return ID assigned to the Bay
     */
    public Integer getBayID(){
        return bayID;
    }
}