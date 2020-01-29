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
            return "hello world";   
        } catch(Exception e) {
            return "error";
        }   
    
    }

    @RequestMapping(path = "/algo", method = RequestMethod.GET)
    public String algo(@RequestBody List<Map<String, Object>> data) throws Exception{
        // JsonElement jsonElement = new JsonParser().parse(data);
        // List<Map<String, Object>> allData= new ArrayList<Map<String, Object>>();
        // allData = new Gson().fromJson(data, new TypeToken<List<Map<String, Object>>>() {}.getType());

        
        List<Map<String, Object>> allData = data;
        
        List<Product> allProduct = new ArrayList<>();
        
        for (int i = 0; i < allData.size(); i++){
            Product p = new Product(allData.get(i));
            p.calculateToolStart();
            allProduct.add(p);
        }
        Collections.sort(allProduct);

        BaySchedule baySchedule = new BaySchedule(allProduct);
        baySchedule.generateSchedule(26);
        String jsonToReturn = BaySchedule.toJSONString(baySchedule);
        return jsonToReturn;
    }

}