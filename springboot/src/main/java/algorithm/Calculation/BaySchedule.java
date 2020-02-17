package main.java.algorithm.Calculation;

import main.java.algorithm.ExclusionStrategy.*;
import main.java.algorithm.Objects.*;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.time.DateUtils;


public class BaySchedule{
    @Exclude
    private HashMap<String, ArrayList<Product>> allProduct;

    @Exclude
    private HashMap<String, Integer> quarterHC;

    private HashMap<String, ArrayList<Bay>> schedule;
    
    @Exclude
    private HashMap<String, HashMap<String, Date>> earliestStartLatestEnd = new HashMap<String, HashMap<String, Date>>();

    
    public BaySchedule(HashMap<String, ArrayList<Product>> allProduct, HashMap<String, Integer> quarterHC, Integer maxBays, Integer maxPreBuild){
        this.allProduct = allProduct;
        this.quarterHC = quarterHC;
        generateSchedule(maxBays, maxPreBuild);
    }
    
    /**
     * Assume 100% fulfillment of manufacturing backlog, while using the least number of Bays
     */
    // public void generateSchedule(){
    //     // Every time we re-generate a schedule, we disregard prior calculation data
    //     schedule = new ArrayList<Bay>();
    //     unfulfilled = new ArrayList<Product>();
        
    //     for (Product p : allProduct){
    //         Date toolStartDate = p.getToolStartDate();
    //         // Find out which bay to assign the Product to
    //         Integer bayAssigned = bayAssignment(toolStartDate);
    //         // If null is returned, that means there is no suitable bay. For the product to be fulfilled, need to "create"/ utilize a new Bay.
    //         if (bayAssigned == null){
    //             Bay b = new Bay();
    //             b.addToBaySchedule(p);
    //             schedule.add(b);
    //         }
    //         // Suitable bay is found. We add the product to the existing Bay's schedule. 
    //         else {
    //             Bay b = schedule.get(bayAssigned);
    //             b.addToBaySchedule(p);
    //         }
    //     }
    // }

    /**
     * Attempt to fulfill as much of the manufacturing backlog as possible, without exceeding the maximum number of Bays available
     */
    private void generateSchedule(Integer maxBays, Integer maxPreBuild){
        schedule = new HashMap<String, ArrayList<Bay>>();

        Set<String> allProductionQtr = allProduct.keySet();

        for (String qtr: allProductionQtr){
            ArrayList<Product> qtrProducts = allProduct.get(qtr);

            // 1a. Find the earliest start date and latest end date of the qtr
            Date earliestStart = DateUtils.addDays(qtrProducts.get(0).getToolStartDate(), -maxPreBuild);
            Date latestEnd = earliestStart;       
            
            for (Product p : qtrProducts){
                Date endDate = p.getEndDate();

                if (endDate.after(latestEnd)){
                    latestEnd = endDate;
                }
            }

            // 1b. Store the earliest start date and latest end date of the quarter        
            HashMap<String, Date> qtrEarliestStartLatestEnd = new HashMap<String, Date>();
            qtrEarliestStartLatestEnd.put("Earliest Start", earliestStart);
            qtrEarliestStartLatestEnd.put("Latest End", latestEnd);
            
            earliestStartLatestEnd.put(qtr, qtrEarliestStartLatestEnd);
            
            // 2. Generate the weeks in the quarter
            ArrayList<Date> qtrWeekOf = generateWeekOf(earliestStart, latestEnd);

            // 3. For each week, we use weeklyNewBuild have to keep track of the new build
            HashMap<Date, Integer> weeklyNewBuild = new HashMap<Date,Integer>();

            for (Date d: qtrWeekOf){
                weeklyNewBuild.put(d, 0);
            }

            // 4. Use tempUnfulfilled to store products that cannot be fitted properly. We will just add it in later 
            ArrayList<Product> tempUnfulfilled = new ArrayList<Product>();


            ArrayList<Bay> qtrSchedule = new ArrayList<Bay>();
            qtrSchedule.add(new Bay());
            Integer currQtrHC = quarterHC.get(qtr);

            // 5. For each product, we assign it to a bay (if available) and track the new builds in a week.
            for (Product p : qtrProducts){
                // Find if Product can be assigned to a bay
                Boolean bayAssigned = bayAssignment(p, maxPreBuild, qtrSchedule, weeklyNewBuild, currQtrHC);
                
                // If product is unassigned, that means there is no suitable bay. For the product to be fulfilled, need to "create"/ utilize a new Bay.
                if (!bayAssigned && qtrSchedule.size() < maxBays){
                    Bay b = new Bay();
                    b.addToBaySchedule(p);
                    qtrSchedule.add(b);
                }
                // If product is unassigned and we cannot "create"/ utilize a new Bay, we mark the Product as unfulfilled
                else if (!bayAssigned){
                    tempUnfulfilled.add(p);
                }
            }
            
            // 6. For the unfulfilled product, we just put it at the end of each earliest available Bay
            for (Product p: tempUnfulfilled){
                unfulfilledBayAssignment (p, qtrSchedule, weeklyNewBuild, currQtrHC);
            }

            // Store the schedule for the quarter
            schedule.put(qtr, qtrSchedule);
        }
    }

    /**
     * Get the production schedule after generating running generateSchedule()
     * @return
     */
    public HashMap<String, ArrayList<Bay>> getSchedule(){
        return schedule;
    }

    /**
     * Get the latest MRP completion date for the set of products
     * @return Latest MRP Date
     */
    public HashMap<String, ArrayList<Product>> getAllProduct(){
        return allProduct;
    }

    public HashMap<String, HashMap<String, Date>> getEarliestStartLatestEnd(){
        return earliestStartLatestEnd;
    }


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
    private boolean bayAssignment (Product p, Integer maxPreBuild, ArrayList<Bay> qtrSchedule,  HashMap<Date, Integer> weeklyNewBuild, Integer currQtrHC){
        Collections.sort(qtrSchedule);     // Sort the bays in order of earliest available date first

        Date toolStartDate = p.getToolStartDate(); // Represents the latest date at which the tool must start
        
        Bay b = qtrSchedule.get(0);
        
        // Check if the Bay available date is earlier than the tool start date. If true, it is a suitable bay and can return the index immediately.
        if (b.getAvailableDate().before(toolStartDate)){
            // Pull forward the date as early as possible
            Long diff = toolStartDate.getTime() - b.getAvailableDate().getTime();
            Integer diffDays = (int) (diff / (24 * 60 * 60 * 1000));
            Date newToolStartDate = DateUtils.addDays(toolStartDate, -Math.min(diffDays, maxPreBuild));
            
            while (newToolStartDate.before(toolStartDate)){
                // Get the week of the new tool start date
                Date weekFriday = getFriday(newToolStartDate);

                // Get the number of new builds in that week
                Integer newBuildCount = weeklyNewBuild.get(weekFriday);

                // Sufficient HC to start new job
                if (newBuildCount < currQtrHC){
                    weeklyNewBuild.put(weekFriday, newBuildCount++);
                    p.setToolStartDate(newToolStartDate);
                    b.addToBaySchedule(p);
                    
                    return true;
                } else {
                    // Delay tool start day to the earliest day of the next working week i.e. next friday
                    newToolStartDate = DateUtils.addDays(weekFriday, 7);
                }
            }         
        }
        return false;
    }

    private void unfulfilledBayAssignment (Product p, ArrayList<Bay> qtrSchedule,  HashMap<Date, Integer> weeklyNewBuild, Integer currQtrHC){
        Collections.sort(qtrSchedule);     // Sort the bays in order of earliest available date first

        Date toolStartDate = p.getToolStartDate();
        
        Bay b = qtrSchedule.get(0);
        
        // Bay available date is the new tool start date
        Date newToolStartDate = b.getAvailableDate();

        // Get the week of the new tool start date
        Date weekFriday = getFriday(newToolStartDate);
            
        // Get the number of new builds in that week
        Integer newBuildCount = weeklyNewBuild.get(weekFriday);

        while (newBuildCount >= currQtrHC){
            // Delay tool start day to the earliest day of the next working week i.e. next friday
            newToolStartDate = DateUtils.addDays(weekFriday, 7);
            weekFriday = getFriday(newToolStartDate);
            newBuildCount = weeklyNewBuild.get(weekFriday);

        }
        weeklyNewBuild.put(weekFriday, newBuildCount++);
        p.setToolStartDate(newToolStartDate);
        b.addToBaySchedule(p);
    }

    private ArrayList<Date> generateWeekOf (Date earliestStart, Date latestEnd){
        ArrayList<Date> weekOf = new ArrayList<>();
        Date friday = getFriday(earliestStart);      

        while (friday.before(latestEnd)){
            weekOf.add(friday);
            friday = DateUtils.addDays(friday, 7);
        }
        return weekOf;
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