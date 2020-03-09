package main.java.history;

import main.java.algorithm.Objects.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.text.*;

@CrossOrigin
@RestController
public class HistoryController {
    @RequestMapping(path = "/savePreSchedule", method = RequestMethod.GET, consumes="application/json")
    public Object savePreSchedule(@RequestBody preSchedule param) throws Exception{
        Map<String,List<List<Object>>> baseLineOccupancy; 
        Map<String,List<List<Object>>> bayOccupancy;
        Integer bay;
        Integer minGap;
        Integer maxGap;

        try{
            baseLineOccupancy = param.baseLineOccupancy; 
            bayOccupancy = param.bayOccupancy;
            bay = param.bay;
            minGap = param.minGap;
            maxGap = param.maxGap;
        } catch (Exception e){
            throw new Exception("JSON Reading Error");
        }

        for(String key : baseLineOccupancy.keySet()){
            List<List<Object>> baseLineOccupancyList = baseLineOccupancy.get(key);
            for (int i = 0; i < baseLineOccupancyList.size(); i++){
                List dates;
                Product p;
                if (i == 0){
                    //dates (CY19Q4's [0])
                    dates = baseLineOccupancyList.get(i);
                } else {
                    //product (CY19Q4's [1,2,3,4,...])
                    p = new Product(baseLineOccupancyList.get(i).get(0));
                }
            }
        }

        for(String key: bayOccupancy.keySet()){
            List<List<Object>> bayOccupancyList = bayOccupancy.get(key);
            for (int i = 0; i < bayOccupancyList.size(); i++){
                List dates;
                Product p;
                if (i == 0){
                    //dates (CY19Q4's [0])
                    dates = bayOccupancyList.get(i);
                } else {
                    //product (CY19Q4's [1,2,3,4,...])
                    p = new Product(bayOccupancyList.get(i).get(0));
                }
            }
        }

        return baseLineOccupancy.get("CY19Q4");

        // return param.bay;
    }

    @RequestMapping(path = "/retrievePreSchedule", method = RequestMethod.GET, produces = "application/json")
    public Object retrievePreSchedule(){
        return "1";
    }
    
}
