package main.java.algorithm;

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
    private List<Product> unfulfilled;
    
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
        unfulfilled = new ArrayList<Product>();

        Set<String> allProductionQtr = allProduct.keySet();

        for (String qtr: allProductionQtr){
            ArrayList<Product> qtrProducts = allProduct.get(qtr);

            Date earliestStart = qtrProducts.get(0).getToolStartDate();
            Date latestEnd = earliestStart;
            
            ArrayList<Bay> qtrSchedule = new ArrayList<Bay>();
            qtrSchedule.add(new Bay());
            
            for (Product p : qtrProducts){
                Date endDate = p.getEndDate();

                if (endDate.compareTo(latestEnd) > 0){
                    latestEnd = endDate;
                }

                // Find if Product can be assigned to a bay
                Boolean bayAssigned = bayAssignment(p, maxPreBuild, qtrSchedule);
                
                // If product is unassigned, that means there is no suitable bay. For the product to be fulfilled, need to "create"/ utilize a new Bay.
                if (!bayAssigned && qtrSchedule.size() < maxBays){
                    Bay b = new Bay();
                    b.addToBaySchedule(p);
                    qtrSchedule.add(b);
                }
                // If product is unassigned we cannot "create"/ utilize a new Bay, we mark the Product as unfulfilled
                else if (!bayAssigned){
                    unfulfilled.add(p);
                }
            }    
            
            // Store the schedule for the quarter
            schedule.put(qtr, qtrSchedule);

            // Store the earliest start date and latest end date of the quarter        
            HashMap<String, Date> qtrEarliestStartLatestEnd = new HashMap<String, Date>();
            qtrEarliestStartLatestEnd.put("Earliest Start", earliestStart);
            qtrEarliestStartLatestEnd.put("Latest End", latestEnd);
            
            earliestStartLatestEnd.put(qtr, qtrEarliestStartLatestEnd);

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
     * Get the list of Products that cannot be fulfilled due to the constraints set (e.g. Maximum number of Bays available), after running generateSchedule()
     * @return List of Products that cannot be fulfilled
     */
    public List<Product> getUnfulfilledList(){
        return unfulfilled;
    }

    /**
     * Get the latest MRP completion date for the set of products
     * @return Latest MRP Date
     */
    public HashMap<String, ArrayList<Product>> getAllProduct(){
        return allProduct;
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
    private  boolean bayAssignment (Product p, Integer maxPreBuild, ArrayList<Bay> qtrSchedule){
        Collections.sort(qtrSchedule);     // Sort the bays in order of earliest available date first

        Date toolStartDate = p.getToolStartDate();
        
        Bay b = qtrSchedule.get(0);
        // Check if the Bay available date is earlier than the tool start date. If true, it is a suitable bay and can return the index immediately.
        if (b.getAvailableDate().before(toolStartDate)){
            // Pull forward the date
            Long diff = toolStartDate.getTime() - b.getAvailableDate().getTime();
            Integer diffDays = (int) (diff / (24 * 60 * 60 * 1000));
            Date newToolStartDate = DateUtils.addDays(toolStartDate, -Math.min(diffDays, maxPreBuild));
            p.setToolStartDate(newToolStartDate);
            b.addToBaySchedule(p);

            return true;
        }
        return false;
    }

}