package main.java.history;

import authentication.json.BaselineParam;
import main.java.connection.historycon;
import main.java.authentication.json.Baseline;

import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

import java.util.*;
import java.text.*;
import java.lang.String;

import com.google.gson.*;

import main.java.algorithm.Objects.*;
import main.java.algorithm.Calculation.*;
import main.java.authentication.json.JsonObject;

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
        Integer histID;


        try {
            baseLineOccupancy = param.baseLineOccupancy;
            bayOccupancy = param.bayOccupancy;
            bay = param.numBays == null ? 16 : param.numBays;
            minGap = param.minGap == null ? 0 : param.minGap;
            maxGap = param.maxGap == null ? 90 : param.maxGap;
            staffID = param.staffID;
            histID = param.histID;
        } catch (Exception e) {
            throw new Exception("JSON Reading Error");
        }

        ArrayList<main.java.history.MassSlotUploadDetails> MSUList = new ArrayList<>();
        ArrayList<main.java.history.MassSlotUploadDetails> baseLineList = new ArrayList<>();

        for (String key : baseLineOccupancy.keySet()) {
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


        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String modifiedDate = formatter.format(date);


        // hardcoded historyID to be 1 should be retrieved to see which is the next historyID to be added
        int id = conn.getLastHistoryID();
        int next_id = id + 1;
        if (histID != null) {
            conn.removeSpecificMSU(histID);

            conn.updateMSU(baseLineList, histID, 1);
            conn.updateMSU(MSUList, histID, 0);

            return conn.updateHistory(String.valueOf(histID), String.valueOf(staffID), String.valueOf(minGap), String.valueOf(maxGap), String.valueOf(bay), modifiedDate);
            // return histID;
        }
        conn.addMassSlotUpload(baseLineList, next_id, 1);
        conn.addMassSlotUpload(MSUList, next_id, 0);

        return conn.addHistory(String.valueOf(staffID), String.valueOf(minGap), String.valueOf(maxGap), String.valueOf(bay), modifiedDate);
    }

    @RequestMapping(path = "/gethistory/{staffID}/{historyID}", method = RequestMethod.GET, produces = "application/json")
    public Object retrievePreSchedule(@PathVariable("staffID") String staffID, @PathVariable("historyID") String historyID) throws SQLException, ClassNotFoundException, ParseException {
        ArrayList<Product> allProduct = new ArrayList<Product>();
        ArrayList<Product> baseLineProduct = new ArrayList<Product>();


        List<Map<String, Object>> baseLineOccupancy = conn.getBaseLineOccupancy(historyID);
        List<Map<String, Object>> bayOccupancy = conn.getBayOccupancy(historyID);

        Integer numBays = conn.getNumBay(historyID, staffID);
        Integer minGap = conn.getMinGap(historyID, staffID);
        Integer maxGap = conn.getMaxGap(historyID, staffID);

        for (Object productRaw : baseLineOccupancy) {
//            System.out.println(productRaw);
            Product p = new Product(productRaw);
            p.updateStringFields();
            // System.out.println(p.getMRPDate());
            baseLineProduct.add(p);
        }

        for (int i = 1; i < bayOccupancy.size(); i++) {
            Object productRaw = bayOccupancy.get(i);
            Product p = new Product(productRaw);
            p.updateStringFields();
            allProduct.add(p);
        }


        Collections.sort(allProduct);
        Collections.sort(baseLineProduct);

        BayRequirement bayReq = new BayRequirement(baseLineProduct, allProduct);
        getAHistory rv = new getAHistory(bayReq, numBays, minGap, maxGap);
        // System.out.println(bayReq.getBayOccupancy().get("CY20Q1").get(0));
        // System.out.println(rv.baseLineOccupancy.get("CY20Q1").get(0));
        // return rv;

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(rv);
        return json;

        // return BayRequirement.toJSONString(bayReq);
    }

    @RequestMapping(path = "/history/{staffId}", method = RequestMethod.GET, produces = "application/json")
    public ArrayList<JsonObject> getHistory(@PathVariable("staffId") String staffId) throws SQLException, ClassNotFoundException, ParseException {
        return conn.getHistory(staffId);
    }

    @RequestMapping(path = "/history/{historyID}", method = RequestMethod.DELETE)
    public String removeHistory(@PathVariable("historyID") String historyID) throws Exception {
        return conn.removeHistory(historyID);
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

    @RequestMapping(path = "/getbaseline", method = RequestMethod.GET, produces = "application/json")
//    public Map<String, Integer> getbaseline(@RequestParam String staff_id) throws SQLException, ClassNotFoundException, RuntimeException {
    public List<Map<String, Object>> getbaseline(@RequestParam String staff_id) throws SQLException, ClassNotFoundException, RuntimeException {
        if (conn.baselinePresentForUser(staff_id)) {
            return conn.getBaselineForUser(staff_id);
        }
        return new ArrayList<>();
    }

}
