package main.java.algorithm;

import java.util.*;

import com.google.gson.*;

import org.apache.commons.lang3.time.DateUtils;

public class BayRequirement{
    private HashMap<String, ArrayList<ArrayList<Object>>> bayOccupancy;

    public BayRequirement(BaySchedule bS){
      
        HashMap<String, ArrayList<Product>> allProduct = bS.getAllProduct();
        HashMap<String, HashMap<String, Date>> earliestStartLatestEnd = bS.getEarliestStartLatestEnd();
        Set<String> allProductionQtr = allProduct.keySet();

        bayOccupancy = new HashMap<String, ArrayList<ArrayList<Object>>>();

        for (String qtr: allProductionQtr){
            
            ArrayList<ArrayList<Object>> qtrBayOccupancy = new ArrayList<ArrayList<Object>>();

            Date earliestStart = earliestStartLatestEnd.get(qtr).get("Earliest Start");
            Date latestEnd = earliestStartLatestEnd.get(qtr).get("Latest End");

            ArrayList<Object> qtrWeekOf = generateWeekOf(earliestStart, latestEnd);

            qtrBayOccupancy.add(qtrWeekOf);

            ArrayList<Product> qtrProducts = allProduct.get(qtr);
            
            
            for (Product p: qtrProducts){
                ArrayList<Object> requirement = generateRequirement(p, qtrWeekOf);
                qtrBayOccupancy.add(requirement);
            }

            bayOccupancy.put(qtr, qtrBayOccupancy);
        }
        
    }

    private ArrayList<Object> generateRequirement(Product p, ArrayList<Object> qtrWeekOf){
        ArrayList<Object> requirement = new ArrayList<Object>();
        requirement.add(p);
        Date toolStartDate = p.getToolStartDate();
        Date leaveBayDate = p.getLeaveBayDate();
        Date oneWeekBefToolStartDate = DateUtils.addDays(toolStartDate, -7);
        
        for (Object week: qtrWeekOf){
            Date d = (Date) week;
            if (d.before(oneWeekBefToolStartDate) || d.after(leaveBayDate)){
                requirement.add("E");
            }
            else{
                requirement.add("O");
            }
        }
        return requirement;
    }  

    private ArrayList<Object> generateWeekOf (Date earliestStart, Date latestEnd){
        
        ArrayList<Object> weekOf = new ArrayList<>();
        Date friday = earliestStart;

        int day = friday.getDay();
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
        weekOf.add(friday);

        while (friday.before(latestEnd)){
            friday = DateUtils.addDays(friday, 7);
            weekOf.add(friday);
        }
        return weekOf;
    }

    public static String toJSONString(BayRequirement br){
        Gson gson = new GsonBuilder().setExclusionStrategies(new JSONExclusionStrategy()).create();
        String json = gson.toJson(br);
        return json;
    }
}
