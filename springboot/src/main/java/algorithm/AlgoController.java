package main.java.algorithm;

import main.java.algorithm.Calculation.*;
import main.java.algorithm.Objects.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;

// import main.java.algorithm.*;

import java.util.*;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;

// import org.apache.poi.xssf.usermodel.XSSFSheet;  
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.google.gson.*;
import com.google.gson.reflect.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin
@RestController
public class AlgoController {

    @RequestMapping(path = "/firstScheduling", method = RequestMethod.POST, consumes="application/json", produces= "application/json")
    public String testing(@RequestBody firstSchedulingParam param) throws RuntimeException, Exception{
        List<Map<String, Object>> baseLineData;
        List<Map<String, Object>> data;
        Integer numBays;
        Integer minGap;
        Integer maxGap;
        
        try {
            baseLineData = param.baseline;
            data = param.masterOps;
            numBays = param.bay;
            minGap = param.minGap;
            maxGap = param.maxGap;
        } catch(Exception e) {
            return "JSON Reading Error";
        }

        ArrayList<Product> allProduct = new ArrayList<Product>();
        ArrayList<Product> baseLineProduct = new ArrayList<Product>();
        
        for (int i = 0; i < data.size(); i++){
            Product p = new Product(data.get(i));
            allProduct.add(p);
        }

        for (int i = 0; i < baseLineData.size(); i++){
            Product p = new Product(baseLineData.get(i));
            baseLineProduct.add(p);
        }

        Collections.sort(allProduct);
        Collections.sort(baseLineProduct);

        BayRequirement bayReq = null;
        BaySchedule baySchedule = null;

        Integer gapDiff = maxGap - minGap; // End date alr considers the min gap; Can only pull forward by gapDiff more days

        HashMap<String, Integer> quarterHC = new HeadCount(allProduct).getQuarterHC();
        Boolean quarterHCChanged = true;

        while (quarterHCChanged){
            baySchedule = new BaySchedule(baseLineProduct, allProduct, quarterHC, numBays, gapDiff);

            allProduct = baySchedule.getAllProduct();
            baseLineProduct = baySchedule.getBaseLineProduct();
            bayReq = new BayRequirement(baseLineProduct, allProduct);
    
            allProduct = baySchedule.getAllProduct();

            HashMap<String, Integer> newQuarterHC = new HeadCount(allProduct).getQuarterHC();
            if (quarterHC.equals(newQuarterHC)){
                quarterHCChanged = false;
            } else {
                quarterHC = newQuarterHC;
            }
        }
        return BayRequirement.toJSONString(bayReq);
    }

    @RequestMapping(path = "/subseqScheduling", method = RequestMethod.POST, consumes="application/json", produces= "application/json")
    public String testing(@RequestBody subseqSchedulingParam param) throws Exception{
        Map<String, List<List<Object>>> baseLine;
        Map<String, List<List<Object>>> bayOccupancy;
        Integer numBays;
        Integer minGap;
        Integer maxGap;

        try {
            baseLine = param.baseLine;
            bayOccupancy = param.bayOccupancy;
            numBays = param.numBays;
            minGap = param.minGap;
            maxGap = param.maxGap;

        } catch(Exception e) {
            return "JSON Reading Error";
        }

        ArrayList<Product> allProduct = new ArrayList<Product>();
        ArrayList<Product> baseLineProduct = new ArrayList<Product>();
        
        Set<String> baseLineQtrs = baseLine.keySet();
        for (String qtr: baseLineQtrs){
            List<List<Object>> qtrOccupancy = baseLine.get(qtr);
            // Within each quarter
            for (int i = 1; i < qtrOccupancy.size(); i++){
                // Skip index 0 because it is the [weekOf]
                Object productRaw = qtrOccupancy.get(i).get(0);
                Product p = new Product(productRaw);
                baseLineProduct.add(p);                   
            }
        }
        
        Set<String> futureQtrs = bayOccupancy.keySet();
        for (String qtr: futureQtrs){
            List<List<Object>> qtrOccupancy = bayOccupancy.get(qtr);
            // Within each quarter
            for (int i = 1; i < qtrOccupancy.size(); i++){
                // Skip index 0 because it is the [weekOf]
                Object productRaw = qtrOccupancy.get(i).get(0);
                Product p = new Product(productRaw);
                allProduct.add(p);                   
            }
        }
        
        Collections.sort(allProduct);
        Collections.sort(baseLineProduct);

        BayRequirement bayReq = null;
        BaySchedule baySchedule = null;

        Integer gapDiff = maxGap - minGap; // End date alr considers the min gap; Can only pull forward by gapDiff more days
        
        HashMap<String, Integer> quarterHC = new HeadCount(allProduct).getQuarterHC();
        Boolean quarterHCChanged = true;
        
       
        while (quarterHCChanged){
            System.out.println(quarterHC);

            baySchedule = new BaySchedule(baseLineProduct, allProduct, quarterHC, numBays, gapDiff);

            allProduct = baySchedule.getAllProduct();
            baseLineProduct = baySchedule.getBaseLineProduct();
            bayReq = new BayRequirement(baseLineProduct, allProduct);
            
            // Check if quarterHC has changed
            HashMap<String, Integer> newQuarterHC = new HeadCount(allProduct).getQuarterHC();
            if (quarterHC.equals(newQuarterHC)){
                quarterHCChanged = false;
            } else {
                quarterHC = newQuarterHC;
            }
        }
        return BayRequirement.toJSONString(bayReq);
    }

    // @RequestMapping(path = "/algo", method = RequestMethod.POST, consumes="application/json", produces= "application/json")
    // public String algo(@RequestBody List<Map<String, Object>> data) throws Exception{
    //     // JsonElement jsonElement = new JsonParser().parse(data);
    //     // List<Map<String, Object>> allData= new ArrayList<Map<String, Object>>();
    //     // allData = new Gson().fromJson(data, new TypeToken<List<Map<String, Object>>>() {}.getType());
        
    //     List<Map<String, Object>> allData = data;
        
    //     ArrayList<Product> allProduct = new ArrayList<Product>();
        
    //     for (int i = 0; i < allData.size(); i++){
    //         Product p = new Product(allData.get(i));
    //         allProduct.add(p);
    //     }

    //     BayRequirement bayReq = null;

    //     for (int i = 0; i < 10; i++){
    //         HashMap<String, Integer> quarterHC = new HeadCount(allProduct).getQuarterHC();

    //         BaySchedule baySchedule = new BaySchedule(allProduct, quarterHC, 26, 90);

    //         bayReq = new BayRequirement(baySchedule);
    
    //         allProduct = baySchedule.getAllProduct();
    //     }
    //     return BayRequirement.toJSONString(bayReq);
        
    // }

}