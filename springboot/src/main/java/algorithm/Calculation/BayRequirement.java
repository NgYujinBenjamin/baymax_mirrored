package main.java.algorithm.Calculation;

import main.java.algorithm.ExclusionStrategy.*;
import main.java.algorithm.Objects.*;

import java.util.*;

import com.google.gson.*;

import org.apache.commons.lang3.time.DateUtils;

public class BayRequirement{
    private TreeMap<String, ArrayList<ArrayList<Object>>> bayOccupancy;

    public BayRequirement(BaySchedule BS){
      
        ArrayList<Product> allProduct = BS.getAllProduct();
        HashMap<String, HashMap<String, Date>> earliestStartLatestEnd = findEarliestStartLatestEnd(allProduct);
        
        bayOccupancy = new TreeMap<String, ArrayList<ArrayList<Object>>>();
        // {Q1: [ (weekOf)[Date1, Date2...], (Product1)[Product1, E, E, E, O...], (Product2)[Product2, E, O, O, E...], ...], Q2:...}
        
        Set<String> allProductionQtr = earliestStartLatestEnd.keySet();

        for (String qtr: allProductionQtr){
            
            ArrayList<ArrayList<Object>> qtrBayOccupancy = new ArrayList<ArrayList<Object>>();

            Date earliestStart = earliestStartLatestEnd.get(qtr).get("Earliest Start");
            Date latestEnd = earliestStartLatestEnd.get(qtr).get("Latest End");

            ArrayList<Object> qtrWeekOf = generateWeekOf(earliestStart, latestEnd);

            qtrBayOccupancy.add(qtrWeekOf);

            bayOccupancy.put(qtr, qtrBayOccupancy);
        }

        for (Product p: allProduct){
            String buildQtr = p.getBuildQtr();
            ArrayList<ArrayList<Object>> qtrBayOccupancy = bayOccupancy.get(buildQtr);
            ArrayList<Object> qtrWeekOf = qtrBayOccupancy.get(0);
            
            ArrayList<Object> productRequirement = generateRequirement(p, qtrWeekOf);
            qtrBayOccupancy.add(productRequirement);

            bayOccupancy.put(buildQtr, qtrBayOccupancy);
        }
    }

    private HashMap<String, HashMap<String, Date>> findEarliestStartLatestEnd(ArrayList<Product> allProduct){
        Collections.sort(allProduct);
        HashMap<String, HashMap<String, Date>> earliestStartLatestEnd = new HashMap<String, HashMap<String, Date>>();

        for (Product p : allProduct){
            String buildQtr = p.getBuildQtr();
            Date toolStartDate = p.getToolStartDate();
            Date endDate = p.getEndDate();

            if (earliestStartLatestEnd.containsKey(buildQtr)){
                HashMap<String, Date> qtrEarliestStartLatestEnd = earliestStartLatestEnd.get(buildQtr);
                Date qtrEarliestStart = qtrEarliestStartLatestEnd.get("Earliest Start");
                Date qtrLatestEnd = qtrEarliestStartLatestEnd.get("Latest End");
                if (toolStartDate.before(qtrEarliestStart)){
                    qtrEarliestStart = toolStartDate;
                }
                if (endDate.after(qtrLatestEnd)){
                    qtrLatestEnd = endDate;
                }
                qtrEarliestStartLatestEnd.put("Earliest Start", qtrEarliestStart);
                qtrEarliestStartLatestEnd.put("Latest End", qtrLatestEnd);
                earliestStartLatestEnd.put(buildQtr, qtrEarliestStartLatestEnd);
            } 
            else {
                HashMap<String, Date> qtrEarliestStartLatestEnd = new HashMap<String, Date>();
                qtrEarliestStartLatestEnd.put("Earliest Start", toolStartDate);
                qtrEarliestStartLatestEnd.put("Latest End", endDate);
                earliestStartLatestEnd.put(buildQtr, qtrEarliestStartLatestEnd);
            }
        }
        return earliestStartLatestEnd;
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

    public TreeMap<String, ArrayList<ArrayList<Object>>> getBayOccupancy(){
        return bayOccupancy;
    }

    public static String toJSONString(BayRequirement br){
        Gson gson = new GsonBuilder().setExclusionStrategies(new JSONExclusionStrategy()).create();
        String json = gson.toJson(br);
        return json;
    }
}
