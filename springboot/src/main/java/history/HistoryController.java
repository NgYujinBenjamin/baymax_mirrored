package main.java.history;

import authentication.json.BaselineParam;
import main.java.connection.historycon;
import main.java.authentication.json.Baseline;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.sql.SQLException;

import java.util.*;
import java.text.*;
import java.lang.String;

import main.java.algorithm.Objects.*;
import main.java.algorithm.Calculation.*;

@CrossOrigin
@RestController
public class HistoryController {

    public static final historycon conn = new historycon();

    @RequestMapping(path = "/savePreSchedule", method = RequestMethod.POST, consumes = "application/json")
    public Object savePreSchedule(@RequestBody main.java.history.preSchedule param) throws SQLException, Exception {
        Map<String, List<List<Object>>> baseLineOccupancy;
        Map<String, List<List<Object>>> bayOccupancy;
        Integer bay;
        Integer minGap;
        Integer maxGap;
        Integer staffID;


        try {
            baseLineOccupancy = param.baseLineOccupancy;
            bayOccupancy = param.bayOccupancy;
            bay = param.numBays;
            minGap = param.minGap;
            maxGap = param.maxGap;
            staffID = param.staffID;
        } catch (Exception e) {
            throw new Exception("JSON Reading Error");
        }

        ArrayList<main.java.history.MassSlotUploadDetails> MSUList = new ArrayList<>();
        ArrayList<main.java.history.MassSlotUploadDetails> baseLineList = new ArrayList<>();
        
        for(String key : baseLineOccupancy.keySet()){
            List<List<Object>> baseLineOccupancyList = baseLineOccupancy.get(key);
            for (int i = 0; i < baseLineOccupancyList.size(); i++) {
                List dates;
                MassSlotUploadDetails p;
                if (i == 0) {
                    //dates (CY19Q4's [0])
                    // dates = baseLineOccupancyList.get(i);
                    continue;
                } else {
                    //product (CY19Q4's [1,2,3,4,...])
                    p = new MassSlotUploadDetails(baseLineOccupancyList.get(i).get(0));
                    baseLineList.add(p);
                    
                }
            }
        }

        for (String key : bayOccupancy.keySet()) {
            List<List<Object>> bayOccupancyList = bayOccupancy.get(key);
            for (int i = 0; i < bayOccupancyList.size(); i++) {
                List dates;
                MassSlotUploadDetails p;
                if (i == 0) {
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
            int id = conn.getLastHistoryID();
            int next_id = id + 1;
            return_message = conn.addMassSlotUpload(baseLineList,next_id, 1);
            return_message = conn.addMassSlotUpload(MSUList, next_id, 0);

            conn.addHistory(String.valueOf(staffID), String.valueOf(minGap), String.valueOf(maxGap), String.valueOf(bay), modifiedDate);
        } catch (Exception e){
            throw e;
            // throw new Exception("Upload failed");
        }
        return return_message;
    }

    @RequestMapping(path = "/retrievePreSchedule", method = RequestMethod.GET, produces = "application/json")
    public Object retrievePreSchedule() throws SQLException, ClassNotFoundException{
        // return conn.getBaseLineOccupancy("1");
        
        //uncomment all below, null pointer for now???
        ArrayList<Product> allProduct = new ArrayList<Product>();
        ArrayList<Product> baseLineProduct = new ArrayList<Product>();
        

        List<Map<String,Object>> baseLineOccupancy = conn.getBaseLineOccupancy("1");
        List<Map<String,Object>> bayOccupancy = conn.getBayOccupancy("1");
        Integer numBays = conn.getNumBay("1");
        Integer minGap = conn.getMinGap("1");
        Integer maxGap = conn.getMaxGap("1");
        // return numBays;
    
        // return conn.getBaseLineOccupancy("1");
        for (int i = 0; i < baseLineOccupancy.size(); i++){
            Object productRaw = baseLineOccupancy.get(i);
            Product p = new Product(productRaw);
            baseLineProduct.add(p);
        }

        for (int i = 1; i < bayOccupancy.size(); i++){
            Object productRaw = bayOccupancy.get(i);
            Product p = new Product(productRaw);
            allProduct.add(p);                   
        }

        // return baseLineProduct;
        // return allProduct;
 
        
        Collections.sort(allProduct);
        Collections.sort(baseLineProduct);

        BayRequirement bayReq =  new BayRequirement(baseLineProduct, allProduct);
        return BayRequirement.toJSONString(bayReq);

        // BayRequirement bayReq = null;
        // BaySchedule baySchedule = null;

        // Integer gapDiff = maxGap - minGap; // End date alr considers the min gap; Can only pull forward by gapDiff more days
        
        // HashMap<String, Integer> quarterHC = new HeadCount(allProduct).getQuarterHC();
        // Boolean quarterHCChanged = true;
        
       
        // while (quarterHCChanged){
        //     System.out.println(quarterHC);

        //     baySchedule = new BaySchedule(baseLineProduct, allProduct, quarterHC, numBays, gapDiff);

        //     allProduct = baySchedule.getAllProduct();
        //     baseLineProduct = baySchedule.getBaseLineProduct();
        //     bayReq = new BayRequirement(baseLineProduct, allProduct);
            
        //     // Check if quarterHC has changed
        //     HashMap<String, Integer> newQuarterHC = new HeadCount(allProduct).getQuarterHC();
        //     if (quarterHC.equals(newQuarterHC)){
        //         quarterHCChanged = false;
        //     } else {
        //         quarterHC = newQuarterHC;
        //     }
        // }
        // return BayRequirement.toJSONString(bayReq);
    }

    @RequestMapping(path = "/setbaseline", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Map<String, Integer> setbaseline(@RequestBody BaselineParam baselineParam) throws SQLException, ClassNotFoundException, RuntimeException {
        if (conn.baselinePresentForUser(baselineParam.staff_id)) {
            // delete existing rows for given staff_id
            conn.removeBaselineFromUser(baselineParam.staff_id);
        }

        List<Baseline> list = new ArrayList<Baseline>();
        for (Map<String, Object> item : baselineParam.baseline) {
            list.add(new Baseline(item));
        }

        int addedStatus = conn.addNewBaseline(list, baselineParam.staff_id);

        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put("Code", addedStatus);
        return result;
    }

}
