import java.util.*;

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
}