import java.util.*;

import com.google.gson.Gson;

public class Bay {
    private ArrayList<Product> schedule;
    private Date availableDate;

    public Bay (){
        schedule = new ArrayList<Product>();
    }

    public void addToSchedule (Product p){
        schedule.add(p);
        availableDate = p.getMRPDate();
    }

    public Date getAvailableDate(){
        return availableDate;
    }

    public ArrayList<Product> getBaySchedule (){
        return schedule;
    }

    public static String toJSONString(Bay b){
        Gson gson = new Gson();
        String json = gson.toJson(b);
        return json;
    }
}