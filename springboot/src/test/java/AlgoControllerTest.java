import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import main.java.algorithm.AlgoController;
import main.java.algorithm.Calculation.*;
import main.java.algorithm.Objects.*;

import main.java.algorithm.firstSchedulingParam;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import java.sql.SQLException;
import java.util.*;
import java.nio.file.*;
import java.io.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlgoControllerTest {
    private AlgoController controller = new AlgoController();
    
    // Window of Testing: Set "today" to any day between 01 April 2020 to 01 May 2020 in baySchedule
    @Test
    @Order(1)
    public void testAlgo1() throws Exception {
        System.out.println("===================== 1. Test No Change in Baseline Information =====================");
        Gson gson = new Gson();
        
        // To find out where to store the input & output files
        // File directory = new File("./");
        // System.out.println(directory.getAbsolutePath());

        String inputPath = "./src/test/AlgoControllerTestFiles/input/01_firstSchedule_input.txt";
        String jsonInput = null;
        try {
            jsonInput = Files.readString(Paths.get(inputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Input File");
        }
        
        String outputPath = "./src/test/AlgoControllerTestFiles/output/01_firstSchedule_output.txt";
        String jsonExpectedOutput = null;
        try {
            jsonExpectedOutput = Files.readString(Paths.get(outputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Output File");
        }

        firstSchedulingParam firstSchedulingParam = gson.fromJson(jsonInput, new TypeToken<firstSchedulingParam>() {}.getType());
        List<Map<String, Object>> newBaseLine = new ArrayList<>();

        // For double values in masterOps & baseLine, we need to change it back to Integer
        // Limitation of GSON
        for (Map<String, Object> baseLine : firstSchedulingParam.baseline) {
            Set<String> baseLineKeys = baseLine.keySet();
            Map<String, Object> baseLineProduct = new HashMap<>();
            for (String key : baseLineKeys) {
                String classType = baseLine.get(key).getClass().getSimpleName();
                if (classType.equals("Double")) {
                    Double value = (Double) baseLine.get(key);
                    baseLineProduct.put(key, value.intValue());
                } else {
                    baseLineProduct.put(key, baseLine.get(key));
                }
            }
            newBaseLine.add(baseLineProduct);
        }

        List<Map<String, Object>> newMasterOps = new ArrayList<>();

        for (Map<String, Object> masterOps : firstSchedulingParam.masterOps) {
            Set<String> masterOpsKeys = masterOps.keySet();
            Map<String, Object> masterOpsProduct = new HashMap<>();
            for (String key : masterOpsKeys) {
                String classType = masterOps.get(key).getClass().getSimpleName();
                if (classType.equals("Double")) {
                    Double value = (Double) masterOps.get(key);
                    masterOpsProduct.put(key, value.intValue());
                } else {
                    masterOpsProduct.put(key, masterOps.get(key));
                }

            }
            newMasterOps.add(masterOpsProduct);
        }

        firstSchedulingParam updatedSchedulingParam = new firstSchedulingParam(newBaseLine, newMasterOps, firstSchedulingParam.bay,firstSchedulingParam.minGap,firstSchedulingParam.maxGap);
        String result = controller.firstScheduling(updatedSchedulingParam);
        
        System.out.println(result.equals(jsonExpectedOutput));
        assertEquals(result, jsonExpectedOutput);
    }

    @Test
    @Order(2)
    public void testAlgo2() throws Exception {
        System.out.println("===================== 2. Test Pull Forward masterOps Product to when Bay is Available =====================");
        Gson gson = new Gson();
        
        // To find out where to store the input & output files
        // File directory = new File("./");
        // System.out.println(directory.getAbsolutePath());

        String inputPath = "./src/test/AlgoControllerTestFiles/input/02_firstSchedule_input.txt";
        String jsonInput = null;
        try {
            jsonInput = Files.readString(Paths.get(inputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Input File");
        }
        
        String outputPath = "./src/test/AlgoControllerTestFiles/output/02_firstSchedule_output.txt";
        String jsonExpectedOutput = null;
        try {
            jsonExpectedOutput = Files.readString(Paths.get(outputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Output File");
        }

        firstSchedulingParam firstSchedulingParam = gson.fromJson(jsonInput, new TypeToken<firstSchedulingParam>() {}.getType());
        List<Map<String, Object>> newBaseLine = new ArrayList<>();

        // For double values in masterOps & baseLine, we need to change it back to Integer
        // Limitation of GSON
        for (Map<String, Object> baseLine : firstSchedulingParam.baseline) {
            Set<String> baseLineKeys = baseLine.keySet();
            Map<String, Object> baseLineProduct = new HashMap<>();
            for (String key : baseLineKeys) {
                String classType = baseLine.get(key).getClass().getSimpleName();
                if (classType.equals("Double")) {
                    Double value = (Double) baseLine.get(key);
                    baseLineProduct.put(key, value.intValue());
                } else {
                    baseLineProduct.put(key, baseLine.get(key));
                }
            }
            newBaseLine.add(baseLineProduct);
        }

        List<Map<String, Object>> newMasterOps = new ArrayList<>();

        for (Map<String, Object> masterOps : firstSchedulingParam.masterOps) {
            Set<String> masterOpsKeys = masterOps.keySet();
            Map<String, Object> masterOpsProduct = new HashMap<>();
            for (String key : masterOpsKeys) {
                String classType = masterOps.get(key).getClass().getSimpleName();
                if (classType.equals("Double")) {
                    Double value = (Double) masterOps.get(key);
                    masterOpsProduct.put(key, value.intValue());
                } else {
                    masterOpsProduct.put(key, masterOps.get(key));
                }

            }
            newMasterOps.add(masterOpsProduct);
        }

        firstSchedulingParam updatedSchedulingParam = new firstSchedulingParam(newBaseLine, newMasterOps, firstSchedulingParam.bay,firstSchedulingParam.minGap,firstSchedulingParam.maxGap);
        String result = controller.firstScheduling(updatedSchedulingParam);
        
        System.out.println(result.equals(jsonExpectedOutput));
        assertEquals(result, jsonExpectedOutput);
    }

    @Test
    @Order(3)
    public void testAlgo3() throws Exception {
        System.out.println("===================== 3. Test Pull Forward masterOps Product and Respect maxGap =====================");
        Gson gson = new Gson();
        
        // To find out where to store the input & output files
        // File directory = new File("./");
        // System.out.println(directory.getAbsolutePath());

        String inputPath = "./src/test/AlgoControllerTestFiles/input/03_firstSchedule_input.txt";
        String jsonInput = null;
        try {
            jsonInput = Files.readString(Paths.get(inputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Input File");
        }
        
        String outputPath = "./src/test/AlgoControllerTestFiles/output/03_firstSchedule_output.txt";
        String jsonExpectedOutput = null;
        try {
            jsonExpectedOutput = Files.readString(Paths.get(outputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Output File");
        }

        firstSchedulingParam firstSchedulingParam = gson.fromJson(jsonInput, new TypeToken<firstSchedulingParam>() {}.getType());
        List<Map<String, Object>> newBaseLine = new ArrayList<>();

        // For double values in masterOps & baseLine, we need to change it back to Integer
        // Limitation of GSON
        for (Map<String, Object> baseLine : firstSchedulingParam.baseline) {
            Set<String> baseLineKeys = baseLine.keySet();
            Map<String, Object> baseLineProduct = new HashMap<>();
            for (String key : baseLineKeys) {
                String classType = baseLine.get(key).getClass().getSimpleName();
                if (classType.equals("Double")) {
                    Double value = (Double) baseLine.get(key);
                    baseLineProduct.put(key, value.intValue());
                } else {
                    baseLineProduct.put(key, baseLine.get(key));
                }
            }
            newBaseLine.add(baseLineProduct);
        }

        List<Map<String, Object>> newMasterOps = new ArrayList<>();

        for (Map<String, Object> masterOps : firstSchedulingParam.masterOps) {
            Set<String> masterOpsKeys = masterOps.keySet();
            Map<String, Object> masterOpsProduct = new HashMap<>();
            for (String key : masterOpsKeys) {
                String classType = masterOps.get(key).getClass().getSimpleName();
                if (classType.equals("Double")) {
                    Double value = (Double) masterOps.get(key);
                    masterOpsProduct.put(key, value.intValue());
                } else {
                    masterOpsProduct.put(key, masterOps.get(key));
                }

            }
            newMasterOps.add(masterOpsProduct);
        }

        firstSchedulingParam updatedSchedulingParam = new firstSchedulingParam(newBaseLine, newMasterOps, firstSchedulingParam.bay,firstSchedulingParam.minGap,firstSchedulingParam.maxGap);
        String result = controller.firstScheduling(updatedSchedulingParam);
        
        System.out.println(result.equals(jsonExpectedOutput));
        assertEquals(result, jsonExpectedOutput);
    }

    @Test
    @Order(4)
    public void testAlgo4() throws Exception {
        System.out.println("===================== 4. Test Priority to masterOps Product with higher revenue =====================");
        Gson gson = new Gson();
        
        // To find out where to store the input & output files
        // File directory = new File("./");
        // System.out.println(directory.getAbsolutePath());

        String inputPath = "./src/test/AlgoControllerTestFiles/input/04_firstSchedule_input.txt";
        String jsonInput = null;
        try {
            jsonInput = Files.readString(Paths.get(inputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Input File");
        }
        
        String outputPath = "./src/test/AlgoControllerTestFiles/output/04_firstSchedule_output.txt";
        String jsonExpectedOutput = null;
        try {
            jsonExpectedOutput = Files.readString(Paths.get(outputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Output File");
        }

        firstSchedulingParam firstSchedulingParam = gson.fromJson(jsonInput, new TypeToken<firstSchedulingParam>() {}.getType());
        List<Map<String, Object>> newBaseLine = new ArrayList<>();

        // For double values in masterOps & baseLine, we need to change it back to Integer
        // Limitation of GSON
        for (Map<String, Object> baseLine : firstSchedulingParam.baseline) {
            Set<String> baseLineKeys = baseLine.keySet();
            Map<String, Object> baseLineProduct = new HashMap<>();
            for (String key : baseLineKeys) {
                String classType = baseLine.get(key).getClass().getSimpleName();
                if (classType.equals("Double")) {
                    Double value = (Double) baseLine.get(key);
                    baseLineProduct.put(key, value.intValue());
                } else {
                    baseLineProduct.put(key, baseLine.get(key));
                }
            }
            newBaseLine.add(baseLineProduct);
        }

        List<Map<String, Object>> newMasterOps = new ArrayList<>();

        for (Map<String, Object> masterOps : firstSchedulingParam.masterOps) {
            Set<String> masterOpsKeys = masterOps.keySet();
            Map<String, Object> masterOpsProduct = new HashMap<>();
            for (String key : masterOpsKeys) {
                String classType = masterOps.get(key).getClass().getSimpleName();
                if (classType.equals("Double")) {
                    Double value = (Double) masterOps.get(key);
                    masterOpsProduct.put(key, value.intValue());
                } else {
                    masterOpsProduct.put(key, masterOps.get(key));
                }

            }
            newMasterOps.add(masterOpsProduct);
        }

        firstSchedulingParam updatedSchedulingParam = new firstSchedulingParam(newBaseLine, newMasterOps, firstSchedulingParam.bay,firstSchedulingParam.minGap,firstSchedulingParam.maxGap);
        String result = controller.firstScheduling(updatedSchedulingParam);
        
        System.out.println(result.equals(jsonExpectedOutput));
        assertEquals(result, jsonExpectedOutput);
    }

    @Test
    @Order(5)
    public void testAlgo5() throws Exception {
        System.out.println("===================== 5. Test masterOps Product would go to earliest available bay =====================");
        Gson gson = new Gson();
        
        // To find out where to store the input & output files
        // File directory = new File("./");
        // System.out.println(directory.getAbsolutePath());

        String inputPath = "./src/test/AlgoControllerTestFiles/input/05_firstSchedule_input.txt";
        String jsonInput = null;
        try {
            jsonInput = Files.readString(Paths.get(inputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Input File");
        }
        
        String outputPath = "./src/test/AlgoControllerTestFiles/output/05_firstSchedule_output.txt";
        String jsonExpectedOutput = null;
        try {
            jsonExpectedOutput = Files.readString(Paths.get(outputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Output File");
        }

        firstSchedulingParam firstSchedulingParam = gson.fromJson(jsonInput, new TypeToken<firstSchedulingParam>() {}.getType());
        List<Map<String, Object>> newBaseLine = new ArrayList<>();

        // For double values in masterOps & baseLine, we need to change it back to Integer
        // Limitation of GSON
        for (Map<String, Object> baseLine : firstSchedulingParam.baseline) {
            Set<String> baseLineKeys = baseLine.keySet();
            Map<String, Object> baseLineProduct = new HashMap<>();
            for (String key : baseLineKeys) {
                String classType = baseLine.get(key).getClass().getSimpleName();
                if (classType.equals("Double")) {
                    Double value = (Double) baseLine.get(key);
                    baseLineProduct.put(key, value.intValue());
                } else {
                    baseLineProduct.put(key, baseLine.get(key));
                }
            }
            newBaseLine.add(baseLineProduct);
        }

        List<Map<String, Object>> newMasterOps = new ArrayList<>();

        for (Map<String, Object> masterOps : firstSchedulingParam.masterOps) {
            Set<String> masterOpsKeys = masterOps.keySet();
            Map<String, Object> masterOpsProduct = new HashMap<>();
            for (String key : masterOpsKeys) {
                String classType = masterOps.get(key).getClass().getSimpleName();
                if (classType.equals("Double")) {
                    Double value = (Double) masterOps.get(key);
                    masterOpsProduct.put(key, value.intValue());
                } else {
                    masterOpsProduct.put(key, masterOps.get(key));
                }

            }
            newMasterOps.add(masterOpsProduct);
        }

        firstSchedulingParam updatedSchedulingParam = new firstSchedulingParam(newBaseLine, newMasterOps, firstSchedulingParam.bay,firstSchedulingParam.minGap,firstSchedulingParam.maxGap);
        String result = controller.firstScheduling(updatedSchedulingParam);
        
        System.out.println(result.equals(jsonExpectedOutput));
        assertEquals(result, jsonExpectedOutput);
    }

    @Test
    @Order(6)
    public void testAlgo6() throws Exception {
        System.out.println("===================== 6. Test 'OPEN' products leave bay on Internal Ops Readiness Date =====================");
        Gson gson = new Gson();
        
        // To find out where to store the input & output files
        // File directory = new File("./");
        // System.out.println(directory.getAbsolutePath());

        String inputPath = "./src/test/AlgoControllerTestFiles/input/06_firstSchedule_input.txt";
        String jsonInput = null;
        try {
            jsonInput = Files.readString(Paths.get(inputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Input File");
        }
        
        String outputPath = "./src/test/AlgoControllerTestFiles/output/06_firstSchedule_output.txt";
        String jsonExpectedOutput = null;
        try {
            jsonExpectedOutput = Files.readString(Paths.get(outputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Output File");
        }

        firstSchedulingParam firstSchedulingParam = gson.fromJson(jsonInput, new TypeToken<firstSchedulingParam>() {}.getType());
        List<Map<String, Object>> newBaseLine = new ArrayList<>();

        // For double values in masterOps & baseLine, we need to change it back to Integer
        // Limitation of GSON
        for (Map<String, Object> baseLine : firstSchedulingParam.baseline) {
            Set<String> baseLineKeys = baseLine.keySet();
            Map<String, Object> baseLineProduct = new HashMap<>();
            for (String key : baseLineKeys) {
                String classType = baseLine.get(key).getClass().getSimpleName();
                if (classType.equals("Double")) {
                    Double value = (Double) baseLine.get(key);
                    baseLineProduct.put(key, value.intValue());
                } else {
                    baseLineProduct.put(key, baseLine.get(key));
                }
            }
            newBaseLine.add(baseLineProduct);
        }

        List<Map<String, Object>> newMasterOps = new ArrayList<>();

        for (Map<String, Object> masterOps : firstSchedulingParam.masterOps) {
            Set<String> masterOpsKeys = masterOps.keySet();
            Map<String, Object> masterOpsProduct = new HashMap<>();
            for (String key : masterOpsKeys) {
                String classType = masterOps.get(key).getClass().getSimpleName();
                if (classType.equals("Double")) {
                    Double value = (Double) masterOps.get(key);
                    masterOpsProduct.put(key, value.intValue());
                } else {
                    masterOpsProduct.put(key, masterOps.get(key));
                }

            }
            newMasterOps.add(masterOpsProduct);
        }

        firstSchedulingParam updatedSchedulingParam = new firstSchedulingParam(newBaseLine, newMasterOps, firstSchedulingParam.bay,firstSchedulingParam.minGap,firstSchedulingParam.maxGap);
        String result = controller.firstScheduling(updatedSchedulingParam);
        
        System.out.println(result.equals(jsonExpectedOutput));
        assertEquals(result, jsonExpectedOutput);
    }
    
    @Test
    @Order(7)
    public void testAlgo7() throws Exception {
        System.out.println("===================== 7. Test Future Quarter HeadCount =====================");
        
        ObjectMapper objectMapper = new ObjectMapper();

        String inputPath = "./src/test/AlgoControllerTestFiles/input/07_headCount_input.txt";
        String outputPath = "./src/test/AlgoControllerTestFiles/output/07_headCount_output.txt";
        
        File input = new File(inputPath);

        Map<String, Object> allData = null;
        
        try {
            allData= objectMapper.readValue(input, new TypeReference<Map<String, Object>>(){});
        } catch (IOException e){
            e.printStackTrace();
        }

        Object masterOps = allData.get("masterOps");
        Object baseLine = allData.get("baseline");

        List<Map<String, Object>> masterOpsData = (List<Map<String, Object>>) masterOps;
        
        ArrayList<Product> allProduct = new ArrayList<Product>();
        
        for (int i = 0; i < masterOpsData.size(); i++){
            Product p = new Product(masterOpsData.get(i));
            allProduct.add(p);
        }

        HashMap<String, Integer> quarterHC = new HeadCount(allProduct).getQuarterHC();

        String expectedOutput = null;
        try {
            expectedOutput = Files.readString(Paths.get(outputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Output File");
        }
        
        System.out.println(expectedOutput.equals(quarterHC.toString()));
        assertEquals(quarterHC.toString(), expectedOutput);
    }

    @Test
    @Order(8)
    public void testAlgo8() throws Exception {
        System.out.println("===================== 8. Test Current Quarter HeadCount =====================");
        
        ObjectMapper objectMapper = new ObjectMapper();

        String inputPath = "./src/test/AlgoControllerTestFiles/input/08_headCount_input.txt";
        
        File input = new File(inputPath);

        Map<String, Object> allData = null;
        
        try {
            allData= objectMapper.readValue(input, new TypeReference<Map<String, Object>>(){});
        } catch (IOException e){
            e.printStackTrace();
        }

        Object masterOps = allData.get("masterOps");

        List<Map<String, Object>> masterOpsData = (List<Map<String, Object>>) masterOps;
        
        ArrayList<Product> allProduct = new ArrayList<Product>();
        
        for (int i = 0; i < masterOpsData.size(); i++){
            Product p = new Product(masterOpsData.get(i));
            allProduct.add(p);
        }

        HashMap<String, Integer> quarterHC = new HeadCount(allProduct).getQuarterHC();

        HashMap<String, Integer> expectedOutput = new HashMap<>();
        Date today = new Date(); // Note: Window of testing: 01 Apr 2020 to 01 May 2020

        // Based on calculations, 01 Apr to 21 Apr 2020, HC = 3; 22 Apr to 01 May, HC = 4        
        if (today.before(new Date(120,3,22))){
            expectedOutput.put("CY20Q2", 3);
        } else{
            expectedOutput.put("CY20Q2", 4);
        }
        
        System.out.println(expectedOutput.equals(quarterHC));
        assertEquals(quarterHC, expectedOutput);
    }

    @Test
    @Order(9)
    public void testAlgo9() throws Exception {
        System.out.println("===================== 9. Test Product Exception: No Revenue (Committed Ship $) =====================");

        ObjectMapper objectMapper = new ObjectMapper();

        String inputPath = "./src/test/AlgoControllerTestFiles/input/09_productException_input.txt";
        
        File input = new File(inputPath);

        Map<String, Object> allData = null;
        
        try {
            allData= objectMapper.readValue(input, new TypeReference<Map<String, Object>>(){});
        } catch (IOException e){
            e.printStackTrace();
        }

        Object masterOps = allData.get("masterOps");

        List<Map<String, Object>> masterOpsData = (List<Map<String, Object>>) masterOps;
        
        ArrayList<Product> allProduct = new ArrayList<Product>();


        Throwable exception = assertThrows(RuntimeException.class, () -> {
            for (int i = 0; i < masterOpsData.size(); i++){
                Product p = new Product(masterOpsData.get(i));
                allProduct.add(p);
            }
        });
        assertEquals("Please fill in all values for the column Committed Ship $", exception.getMessage());
        System.out.println(true);
      }

      @Test
      @Order(10)
      public void testAlgo10() throws Exception {
          System.out.println("===================== 10. Test Product Exception: Missing MRP Date =====================");
  
          ObjectMapper objectMapper = new ObjectMapper();
  
          String inputPath = "./src/test/AlgoControllerTestFiles/input/10_productException_input.txt";
          
          File input = new File(inputPath);
  
          Map<String, Object> allData = null;
          
          try {
              allData= objectMapper.readValue(input, new TypeReference<Map<String, Object>>(){});
          } catch (IOException e){
              e.printStackTrace();
          }
  
          Object masterOps = allData.get("masterOps");
  
          List<Map<String, Object>> masterOpsData = (List<Map<String, Object>>) masterOps;
          
          ArrayList<Product> allProduct = new ArrayList<Product>();
  
  
          Throwable exception = assertThrows(RuntimeException.class, () -> {
              for (int i = 0; i < masterOpsData.size(); i++){
                  Product p = new Product(masterOpsData.get(i));
                  allProduct.add(p);
              }
          });
          assertEquals("Please ensure that MRP Date is not empty, and in dd/mm/yyyy format", exception.getMessage());
          System.out.println(true);
        }

        @Test
        @Order(11)
        public void testAlgo11() throws Exception {
            System.out.println("===================== 11. Test Product Exception: MRP Date Incorrect Format =====================");
    
            ObjectMapper objectMapper = new ObjectMapper();
    
            String inputPath = "./src/test/AlgoControllerTestFiles/input/10_productException_input.txt";
            
            File input = new File(inputPath);
    
            Map<String, Object> allData = null;
            
            try {
                allData= objectMapper.readValue(input, new TypeReference<Map<String, Object>>(){});
            } catch (IOException e){
                e.printStackTrace();
            }
    
            Object masterOps = allData.get("masterOps");
    
            List<Map<String, Object>> masterOpsData = (List<Map<String, Object>>) masterOps;
            
            ArrayList<Product> allProduct = new ArrayList<Product>();
    
    
            Throwable exception = assertThrows(RuntimeException.class, () -> {
                for (int i = 0; i < masterOpsData.size(); i++){
                    Product p = new Product(masterOpsData.get(i));
                    allProduct.add(p);
                }
            });
            assertEquals("Please ensure that MRP Date is not empty, and in dd/mm/yyyy format", exception.getMessage());
            System.out.println(true);
          }

        @Test
        @Order(12)
        public void testAlgo12() throws Exception {
            System.out.println("===================== 12. Test Product Exception: Missing MFG Commit Date =====================");
    
            ObjectMapper objectMapper = new ObjectMapper();
    
            String inputPath = "./src/test/AlgoControllerTestFiles/input/12_productException_input.txt";
            
            File input = new File(inputPath);
    
            Map<String, Object> allData = null;
            
            try {
                allData= objectMapper.readValue(input, new TypeReference<Map<String, Object>>(){});
            } catch (IOException e){
                e.printStackTrace();
            }
    
            Object masterOps = allData.get("masterOps");
    
            List<Map<String, Object>> masterOpsData = (List<Map<String, Object>>) masterOps;
            
            ArrayList<Product> allProduct = new ArrayList<Product>();
    
    
            Throwable exception = assertThrows(RuntimeException.class, () -> {
                for (int i = 0; i < masterOpsData.size(); i++){
                    Product p = new Product(masterOpsData.get(i));
                    allProduct.add(p);
                }
            });
            assertEquals("Please ensure that MFG Commit Date is not empty, and in dd/mm/yyyy format", exception.getMessage());
            System.out.println(true);
          }
          
}
