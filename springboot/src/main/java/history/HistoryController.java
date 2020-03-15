package main.java.history;

import main.java.connection.historycon;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

import java.util.*;
import java.text.*;
import java.lang.String;

@CrossOrigin
@RestController
public class HistoryController {
    
    public static final historycon conn = new historycon();

    @RequestMapping(path = "/savePreSchedule", method = RequestMethod.POST, consumes="application/json")
    public Object savePreSchedule(@RequestBody preSchedule param) throws SQLException, Exception{
        Map<String,List<List<Object>>> baseLineOccupancy; 
        Map<String,List<List<Object>>> bayOccupancy;
        Integer bay;
        Integer minGap;
        Integer maxGap;
        Integer staffID;


        try{
            baseLineOccupancy = param.baseLineOccupancy; 
            bayOccupancy = param.bayOccupancy;
            bay = param.bay;
            minGap = param.minGap;
            maxGap = param.maxGap;
            staffID = param.staffID;
        } catch (Exception e){
            throw new Exception("JSON Reading Error");
        }

        ArrayList<main.java.history.MassSlotUploadDetails> MSUList = new ArrayList<>();

        for(String key : baseLineOccupancy.keySet()){
            List<List<Object>> baseLineOccupancyList = baseLineOccupancy.get(key);
            for (int i = 0; i < baseLineOccupancyList.size(); i++){
                List dates;
                MassSlotUploadDetails p;
                if (i == 0){
                    //dates (CY19Q4's [0])
                    // dates = baseLineOccupancyList.get(i);
                    continue;
                } else {
                    //product (CY19Q4's [1,2,3,4,...])
                    p = new MassSlotUploadDetails(baseLineOccupancyList.get(i).get(0));
                    MSUList.add(p);
                    
                }
            }
        }

        for(String key: bayOccupancy.keySet()){
            List<List<Object>> bayOccupancyList = bayOccupancy.get(key);
            for (int i = 0; i < bayOccupancyList.size(); i++){
                List dates;
                MassSlotUploadDetails p;
                if (i == 0){
                    //dates (CY19Q4's [0])
                    // dates = bayOccupancyList.get(i);
                    continue;
                } else {
                    //product (CY19Q4's [1,2,3,4,...])
                    p = new MassSlotUploadDetails(bayOccupancyList.get(i).get(0));
                    MSUList.add(p);
                }
            }
        }

        String return_message = "Failed";

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String modifiedDate = formatter.format(date);

        try {
            // hardcoded history_id to be 1 should be retrieved to see which is the next history_id to be added
            return_message = conn.addMassSlotUpload(MSUList, 1);
            conn.addHistory(String.valueOf(staffID), String.valueOf(minGap), String.valueOf(maxGap), modifiedDate);
        } catch (Exception e){
            throw new Exception("Upload failed");
        }
        return return_message;
    }

    @RequestMapping(path = "/retrievePreSchedule", method = RequestMethod.GET, produces = "application/json")
    public Object retrievePreSchedule() throws SQLException, ClassNotFoundException{
        return conn.getMassSlotUpload("1");
    }
    
}
