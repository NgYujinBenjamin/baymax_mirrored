package main.java.algorithm;

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

@CrossOrigin
@RestController
public class AlgoController {
    


    @RequestMapping(path = "/testing", method = RequestMethod.GET)
    public String testing() throws Exception{
        
        try {
            Integer hello = 123;
            return hello.toString();   
        } catch(Exception e) {
            return "error";
        }   
    
    }

    @RequestMapping(path = "/algo", method = RequestMethod.POST, consumes="application/json", produces= "application/json")
    public String algo(@RequestBody List<Map<String, Object>> data) throws Exception{
        // JsonElement jsonElement = new JsonParser().parse(data);
        // List<Map<String, Object>> allData= new ArrayList<Map<String, Object>>();
        // allData = new Gson().fromJson(data, new TypeToken<List<Map<String, Object>>>() {}.getType());

        
        List<Map<String, Object>> allData = data;
        
        HashMap<String, ArrayList<Product>> allProduct = new  HashMap<String, ArrayList<Product>>();
        
        for (int i = 0; i < allData.size(); i++){
            Product p = new Product(allData.get(i));
            String buildQtr = p.getBuildQtr();

            if (allProduct.containsKey(buildQtr)){
                ArrayList<Product> qtrProducts = allProduct.get(buildQtr);
                qtrProducts.add(p);
            } else {
                ArrayList<Product> qtrProducts = new ArrayList<Product>();
                qtrProducts.add(p);
                allProduct.put(buildQtr, qtrProducts);
            }
        }

        Set<String> allProductionQtr = allProduct.keySet();
        for (String qtrKey: allProductionQtr){
            ArrayList<Product> qtrProducts = allProduct.get(qtrKey);
            Collections.sort(qtrProducts);
        }

        HashMap<String, Integer> quarterHC = new HeadCount(allProduct).getQuarterHC();

        BaySchedule baySchedule = new BaySchedule(allProduct, quarterHC, 26, 90);

        return BaySchedule.toJSONString(baySchedule);
    }

}