package main.java.algorithm.Calculation;

import main.java.algorithm.ExclusionStrategy.*;
import main.java.algorithm.Objects.*;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.time.DateUtils;


public class BaySchedule{
    private ArrayList<Product> allProduct;

    private ArrayList<Product> baseLineProduct;

    private HashMap<String, Integer> quarterHC;

    private ArrayList<Bay> schedule;

    private  HashMap<Date, Integer> weeklyNewBuild;

    public BaySchedule(ArrayList<Product> baseLineProduct, ArrayList<Product> allProduct, HashMap<String, Integer> quarterHC, Integer maxBays, Integer gapDiff) throws RuntimeException{
        this.allProduct = allProduct;
        this.baseLineProduct = baseLineProduct;
        this.quarterHC = quarterHC;
        schedule = new ArrayList<Bay>();

        // Create new bays, up to the maximum provided
        for (int i = 0; i < maxBays; i++){
            schedule.add(new Bay(i));
        }

        // Add baseline product to the bays
        if (baseLineProduct.size() <= maxBays){
            for (int j=0; j < baseLineProduct.size(); j++){
                Bay b = schedule.get(j);
                
                Product p = baseLineProduct.get(j);

                b.addToBaySchedule(p);
            }
        } else {
            throw new RuntimeException("Number of Baseline Products exceed Number of Bays allocated");
        }

        generateSchedule(gapDiff);
    }

    /**
     * Attempt to fulfill as much of the manufacturing backlog as possible, without exceeding the maximum number of Bays available
     */
    private void generateSchedule(Integer gapDiff){
        weeklyNewBuild = new HashMap<Date,Integer>();
        
        for (Product p: allProduct){
            bayAssignment(p, gapDiff);
        }
    }

    /**
     * Get the production schedule after generating running generateSchedule()
     * @return
     */
    public ArrayList<Bay> getSchedule(){
        return schedule;
    }

    /**
     * Get the latest MRP completion date for the set of products
     * @return Latest MRP Date
     */
    public ArrayList<Product> getAllProduct(){
        return allProduct;
    }

    public ArrayList<Product> getBaseLineProduct(){
        return baseLineProduct;
    }

    // public HashMap<String, HashMap<String, Date>> getEarliestStartLatestEnd(){
    //     return earliestStartLatestEnd;
    // }


    public static String toJSONString(BaySchedule bs){
        Gson gson = new GsonBuilder().setExclusionStrategies(new JSONExclusionStrategy()).create();
        String json = gson.toJson(bs);
        return json;
    }

    /**
     * Helper method to determine which Bay object to assign the product to, based on the Product's Tool Start Date. Tool Start Date is compared against Bay Available Date (Bay Attribute) to determine if bay is suitable to take on the new product.
     * @param toolStartDate Tool Start Date of the product to be assigned to the Bay.
     * @return Integer representing the index position of the Bay to be Assigned in Production Schedule (SchedulerUtils attribute).
     */
    private void bayAssignment (Product p, Integer gapDiff){
        Collections.sort(schedule);
        
        Date toolStartDate = p.getLatestToolStartDate(); // Represents the latest date at which the tool must start
        
        Bay b = schedule.get(0); // Earliest bay available
        
        // Pull forward the date as early as possible
        
        Date newToolStartDate;
        Date today = new Date();
        // Date today = new Date(119, 10, 1); // For testing purposes
        if (b.getAvailableDate().after(today)){
            // If bayAvailableDate is after current date, we can pull forward up to the maxGap or the date when the bay is available
            Long diff = toolStartDate.getTime() - b.getAvailableDate().getTime();
            Integer diffDays = (int) (diff / (24 * 60 * 60 * 1000));
                // diffDays +ve if toolStartDate is after bayAvailableDate
                // diffDays -ve if toolStartDate is before bayAvailableDate
            
            newToolStartDate = DateUtils.addDays(toolStartDate, -Math.min(diffDays, gapDiff));
        }
        else {
            // If bayAvailableDate is before current date, we can pull forward up to the maxGap or current date
            Long diff = toolStartDate.getTime() - today.getTime();
            Integer diffDays = (int) (diff / (24 * 60 * 60 * 1000));
                // diffDays +ve if toolStartDate is after today's date date
                // diffDays -ve if toolStartDate is before today's date
            
            newToolStartDate = DateUtils.addDays(toolStartDate, -Math.min(diffDays, gapDiff));
        }
        

        Boolean scheduled = false;

        while (!scheduled) {
            // Get the week of the new tool start date
            Date weekFriday = getFriday(newToolStartDate);

            // Get the number of new builds in that week
            Integer newBuildCount;
            if (weeklyNewBuild.containsKey(weekFriday)){
                newBuildCount = weeklyNewBuild.get(weekFriday);
            } else {
                newBuildCount = 0;
            }
                            
            p.setToolStartDate(newToolStartDate); // Set the date will update the buildQtr as well
            String buildQtr = p.getBuildQtr(); // Get the buildQtr
            Integer currQtrHC = Integer.MAX_VALUE; // Initialize to "unlimited" head count
            if (quarterHC.containsKey(buildQtr)){
                currQtrHC = quarterHC.get(buildQtr);
            }

            // Sufficient HC to start new job
            if (newBuildCount < currQtrHC){
                weeklyNewBuild.put(weekFriday, newBuildCount++); // Update the weekly new build
                b.addToBaySchedule(p);
                scheduled = true;
            } else {
                // Delay tool start day to the earliest day of the next working week i.e. next friday
                newToolStartDate = DateUtils.addDays(weekFriday, 7);
            }
        }
    }


    private Date getFriday (Date earliestStart){
        Date friday = earliestStart;
        int day = earliestStart.getDay();
        switch(day){
            case 0: // Sunday
                friday = DateUtils.addDays(friday, -2);
                break;
            case 1: // Monday
                friday = DateUtils.addDays(friday, -3);
                break;
            case 2: // Tuesday
                friday = DateUtils.addDays(friday, -4);
                break;
            case 3: // Wednesday
                friday = DateUtils.addDays(friday, -5);
                break;
            case 4: // Thursday
                friday = DateUtils.addDays(friday, -6);
                break;
            case 6: // Saturday
                friday = DateUtils.addDays(friday, -1);
                break;
        }
        return friday;
    }
}