import java.util.*;

import com.google.gson.Gson;

public class BaySchedule{
    private List<Product> allProduct;
    private List<Bay> schedule;
    private List<Product> unfulfilled;

    public BaySchedule(List<Product> allProduct){
        this.allProduct = allProduct;
    }
    
    /**
     * Assume 100% fulfillment of manufacturing backlog, while using the least number of Bays
     */
    public void generateSchedule(){
        // Every time we re-generate a schedule, we disregard prior calculation data
        schedule = new ArrayList<Bay>();
        unfulfilled = new ArrayList<Product>();
        
        for (Product p : allProduct){
            Date toolStartDate = p.getToolStartDate();
            // Find out which bay to assign the Product to
            Integer bayAssigned = bayAssignment(toolStartDate);
            // If null is returned, that means there is no suitable bay. For the product to be fulfilled, need to "create"/ utilize a new Bay.
            if (bayAssigned == null){
                Bay b = new Bay();
                b.addToSchedule(p);
                schedule.add(b);
            }
            // Suitable bay is found. We add the product to the existing Bay's schedule. 
            else {
                Bay b = schedule.get(bayAssigned);
                b.addToSchedule(p);
            }
        }
    }

    /**
     * Attempt to fulfill as much of the manufacturing backlog as possible, without exceeding the maximum number of Bays available
     */
    public void generateSchedule(Integer maxBays){
        schedule = new ArrayList<Bay>();
        unfulfilled = new ArrayList<Product>();

        for (Product p : allProduct){
            Date toolStartDate = p.getToolStartDate();
            // Find out which bay to assign the Product to
            Integer bayAssigned = bayAssignment(toolStartDate);
            // If null is returned, that means there is no suitable bay. For the product to be fulfilled, need to "create"/ utilize a new Bay.
            if (bayAssigned == null && schedule.size() < maxBays){
                Bay b = new Bay();
                b.addToSchedule(p);
                schedule.add(b);
            }
            // If null is returned and we cannot "create"/ utilize a new Bay, we mark the Product as unfulfilled
            else if (bayAssigned == null){
                unfulfilled.add(p);
            }
            // Suitable bay is found. We add the product to the existing Bay's schedule. 
            else {
                Bay b = schedule.get(bayAssigned);
                b.addToSchedule(p);
            }
        }
    }

    /**
     * Get the production schedule after generating running generateSchedule()
     * @return
     */
    public List<Bay> getSchedule(){
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
     * Get the earliest tool start date for the set of products
     * @return Earliest Tool Start Date
     */
    public Date getEarliestToolStart(){
        return allProduct.get(0).getToolStartDate();
    }

    /**
     * Get the latest MRP completion date for the set of products
     * @return Latest MRP Date
     */
    public Date getLatestMRP(){
        int lastPosition = allProduct.size() - 1;
        return allProduct.get(lastPosition).getMRPDate();
    }

    public List<Product> getAllProduct(){
        return allProduct;
    }

    /**
     * Helper method to determine which Bay object to assign the product to, based on the Product's Tool Start Date. Tool Start Date is compared against Bay Available Date (Bay Attribute) to determine if bay is suitable to take on the new product.
     * @param toolStartDate Tool Start Date of the product to be assigned to the Bay.
     * @return Integer representing the index position of the Bay to be Assigned in Production Schedule (SchedulerUtils attribute).
     */
    private  Integer bayAssignment (Date toolStartDate){
        for (int i = 0; i < schedule.size(); i++){
            Bay b = schedule.get(i);
            // Check if the Bay available date is earlier than the tool start date. If true, it is a suitable bay and can return the index immediately.
            if (b.getAvailableDate().before(toolStartDate)){
                return i;
            }
        }
        return null;
    }

    public static String toJSONString(BaySchedule bs){
        Gson gson = new Gson();
        String json = gson.toJson(bs);
        return json;
    }
}