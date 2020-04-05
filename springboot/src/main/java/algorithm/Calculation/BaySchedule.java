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

    private HashMap<Date, Integer> weeklyNewBuild = new HashMap<Date, Integer>();

    /**
     * Constructor for the BaySchedule Class.
     * Assigns base line builds to a bay before allocating future builds to vacant bays.
     * @param baseLineProduct Base line builds that are currently building
     * @param allProduct All future builds that have yet to be allocatd to a bay
     * @param quarterHC Headcount available each quarter. Represents the number of human resources available to start a new build in each week of the quarter.
     * @param maxBays Maximum number of bays that can be utilised in the allocation.
     * @param gapDiff Represents the number of days that the tool start dates can potentially be pulled forward.
     * @throws RuntimeException
     */
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

        for (Product p: allProduct){
            bayAssignment(p, gapDiff);
        }
    }

    /**
     * Returns the recommended production schedule generated
     * @return ArrayList of Bay Objects. Each Bay object contains a list of Products, representing the order of build for that Bay
     */
    public ArrayList<Bay> getSchedule(){
        return schedule;
    }

    
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
	 * Helper method to assign a Product to a particular Bay. 
	 * The Bay that the product is assigned to is the earliest available Bay.
	 * toolStart date of the Product will be updated accordingly: Pulled forward (if possible), or delayed (if there is a lack of available resources)
	 * @param p Product to be assigned to a bay
	 * @param gapDiff Represents the number of days that the tool start dates can potentially be pulled forward
	 */
    private void bayAssignment (Product p, Integer gapDiff){
        Collections.sort(schedule);
        
        Date latestToolStartDate = p.getLatestToolStartDate(); // Represents the latest date at which the tool must start
        Date MRPDate = p.getMRPDate();
        
        Bay b = schedule.get(0); // Earliest bay available
        Date bayAvailDate = b.getAvailableDate(); // Date of when the bay is available
        
        Date earliestToolStartDate;
        if (p.getLockMRPDate() != null && p.getLockMRPDate()){
            // Generate new tool start date based on the specified cycle time
            earliestToolStartDate = DateUtils.addDays(MRPDate, -p.getCycleTimeDays());
        } else {
            // Pull forward the date as early as possible, or if necessary, delay the toolStart (e.g. if latestToolStart is before today, or before bayAvailDate)
            earliestToolStartDate = adjustToolStart(latestToolStartDate, bayAvailDate, gapDiff);
        }

        Boolean scheduled = false;

        while (!scheduled) {
            // Get the week of the new tool start date
            Date weekFriday = getFriday(earliestToolStartDate);

            // Get the number of new builds in that week
            Integer newBuildCount;

            if (weeklyNewBuild.containsKey(weekFriday)){
                newBuildCount = weeklyNewBuild.get(weekFriday);
            } else {
                newBuildCount = 0;
            }
                            
            p.setToolStartDate(earliestToolStartDate); // Set the date will update the buildQtr as well
            String buildQtr = p.getBuildQtr(); // Get the buildQtr
            Integer currQtrHC = Integer.MAX_VALUE; // Initialize to "unlimited" head count
            if (quarterHC.containsKey(buildQtr)){
                currQtrHC = quarterHC.get(buildQtr);
            }

            // Sufficient HC to start new job
            if (newBuildCount < currQtrHC){
                newBuildCount += 1;
                weeklyNewBuild.put(weekFriday, newBuildCount); // Update the weekly new build
                b.addToBaySchedule(p);
                scheduled = true;
            } else {
                // Delay tool start day to the earliest day of the next working week i.e. next friday
                earliestToolStartDate = DateUtils.addDays(weekFriday, 7);
            }
        }
    }

    /**
     * Helper method to obtain the earliest tool start date for the given Product
     * The earliest toolStartDate is the earliest date that is (1) after today's date and (2) after the date where the bay becomes available
     * @param latestToolStartDate Latest date at which the Product needs to begin its build for it to meet MFG Commit Date
     * @param bayAvailDate Earliest date at which the bay becomes available
     * @param gapDiff Represents the number of days that the tool start dates can potentially be pulled forward
     * @return Earliest tool start date for the Product
     */
    private Date adjustToolStart(Date latestToolStartDate, Date bayAvailDate, Integer gapDiff){
        Date earliestToolStartDate;
        Date today = new Date();
        // Date today = new Date(120, 2, 24); // For testing purposes
        if (bayAvailDate.after(today)){
            // If bayAvailableDate is after current date, we can pull forward up to the maxGap or the date when the bay is available
            Long diff = latestToolStartDate.getTime() - bayAvailDate.getTime();
            Integer diffDays = (int) (diff / (24 * 60 * 60 * 1000));
                // diffDays +ve if latestToolStartDate is after bayAvailableDate
                // diffDays -ve if latestToolStartDate is before bayAvailableDate
            
            earliestToolStartDate = DateUtils.addDays(latestToolStartDate, -Math.min(diffDays, gapDiff));
                // if diffDays is -10 (i.e. toolStart is 10 days before bayAvailDate), then -Math.min(diffDays, gapDiff) will give +10
                // earliestToolStartDate will be delayed 10 days
        }
        else {
            // If bayAvailableDate is before current date, we can pull forward up to the maxGap or current date
            Long diff = latestToolStartDate.getTime() - today.getTime();
            Integer diffDays = (int) (diff / (24 * 60 * 60 * 1000));
                // diffDays +ve if latestToolStartDate is after today's date date
                // diffDays -ve if latestToolStartDate is before today's date
            
            earliestToolStartDate = DateUtils.addDays(latestToolStartDate, -Math.min(diffDays, gapDiff));
                // if diffDays is -10 (i.e. toolStart is 10 days before todayDate), then -Math.min(diffDays, gapDiff) will give +10
                // earliestToolStartDate will be delayed 10 days
        }
        return earliestToolStartDate;
    }

    /**
     * Helper method to obtain the date of the earliest preceding Friday
     * For instance, if 7 Jan X0 falls on a Sunday, this method will return 5 Jan X0
     * @param earliestStart
     * @return The date on which the earliest preceding Friday falls
     */
    private Date getFriday (Date earliestToolStartDate){
        Date friday = earliestToolStartDate;
        int day = earliestToolStartDate.getDay();
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