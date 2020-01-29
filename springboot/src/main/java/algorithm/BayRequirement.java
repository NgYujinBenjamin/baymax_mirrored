package main.java.algorithm;

import java.security.spec.ECPublicKeySpec;
import java.util.*;

import com.google.gson.Gson;

import org.apache.commons.lang3.time.DateUtils;

public class BayRequirement{
    private List<Date> weekOf;
    private ArrayList<ArrayList<Object>> bayUsage;

    public BayRequirement(BaySchedule bS){
        Date friday = bS.getEarliestToolStart();
        Date lastDay = bS.getLatestMRP();

        weekOf = new ArrayList<Date>();
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

        while (friday.before(lastDay)){
            friday = DateUtils.addDays(friday, 7);
            weekOf.add(friday);
        }
        
        bayUsage = new ArrayList<ArrayList<Object>>();

        for (Product p: bS.getAllProduct()){
            ArrayList<Object> requirement = generateRequirement(p);
            bayUsage.add(requirement);
        }
    }

    private ArrayList<Object> generateRequirement(Product p){
        ArrayList<Object> requirement = new ArrayList<Object>();
        requirement.add(p);
        Date toolStartDate = p.getToolStartDate();
        Date mrpDate = p.getMRPDate();
        Date oneWeekBefToolStartDate = DateUtils.addDays(toolStartDate, -7);
        
        for (Date d: weekOf){
            if (d.before(oneWeekBefToolStartDate) || d.after(mrpDate)){
                requirement.add("E");
            }
            else{
                requirement.add("O");
            }
        }
        return requirement;
    }

    public static String toJSONString(BayRequirement bR){
        Gson gson = new Gson();
        String json = gson.toJson(bR);
        return json;
    }
}
