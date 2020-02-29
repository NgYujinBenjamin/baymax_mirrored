package algorithm;

import algorithm.Calculation.BayRequirement;
import algorithm.Calculation.BaySchedule;
import algorithm.Calculation.HeadCount;
import algorithm.Objects.Product;
import org.springframework.web.bind.annotation.*;

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
    public String testing(@RequestBody firstSchedulingParam param) throws Exception{
        List<Map<String, Object>> baseLineData;
        List<Map<String, Object>> data;
        Integer numBays;
        Integer minGap;
        Integer maxGap;
        
        try {
            baseLineData = param.baseLineData;
            data = param.data;
            numBays = param.numBays;
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
        Collections. sort(baseLineProduct);

        BayRequirement bayReq = null;
        BaySchedule baySchedule = null;

        Integer gapDiff = maxGap - minGap; // End date alr considers the min gap; Can only pull forward by gapDiff more days

        HashMap<String, Integer> quarterHC = new HeadCount(allProduct).getQuarterHC();
        Boolean quarterHCChanged = true;

        while (quarterHCChanged){
            baySchedule = new BaySchedule(baseLineProduct, allProduct, quarterHC, numBays, gapDiff);

            bayReq = new BayRequirement(baySchedule);
    
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
        
        try {
            Map<String, Map<String, List<List<Object>>>> preSchedule = param.preSchedule;
            Integer numBays = param.numBays;
            Integer minGap = param.minGap;
            Integer maxGap = param.maxGap;

            Object to_print = preSchedule.get("baseline").get("CY10Q1").get(1).get(0);
            
            Map <String,String> map = new ObjectMapper().convertValue(to_print, Map.class);

            return map + " " + param.numBays + " " + param.minGap + " " + param.maxGap;
        } catch(Exception e) {
            return "error";
        }   
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