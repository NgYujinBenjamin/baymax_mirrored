import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import main.java.algorithm.AlgoController;
import main.java.algorithm.Calculation.*;
import main.java.algorithm.Objects.*;

import main.java.algorithm.firstSchedulingParam;
import main.java.algorithm.subseqSchedulingParam;
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

    @Test
    @Order(13)
    public void testAlgo13() throws Exception {
        System.out.println("===================== 13. Test Product Exception: Missing Build Quarter =====================");

        ObjectMapper objectMapper = new ObjectMapper();

        String inputPath = "./src/test/AlgoControllerTestFiles/input/13_productException_input.txt";
        
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
        assertEquals("Please ensure that there are no empty Build Qtrs", exception.getMessage());
        System.out.println(true);
    }

    @Test
    @Order(14)
    public void testAlgo14() throws Exception {
        System.out.println("===================== 14. Test Product Exception: Missing Cycle Time =====================");

        ObjectMapper objectMapper = new ObjectMapper();

        String inputPath = "./src/test/AlgoControllerTestFiles/input/14_productException_input.txt";
        
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
        assertEquals("Please ensure that all Cycle Time is filled in", exception.getMessage());
        System.out.println(true);
    }

    @Test
    @Order(15)
    public void testAlgo15() throws Exception {
        System.out.println("===================== 15. Test Product Exception: Non-numerical Cycle Time =====================");

        ObjectMapper objectMapper = new ObjectMapper();

        String inputPath = "./src/test/AlgoControllerTestFiles/input/15_productException_input.txt";
        
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
        assertEquals("Please ensure that Cycle Time is in numerical format, and is a whole number", exception.getMessage());
        System.out.println(true);
    }


    @Test
    @Order(16)
    public void testAlgo16() throws Exception {
        System.out.println("===================== 16. Test Product Exception: Non-numerical Revenue (Committed Ship $) =====================");

        ObjectMapper objectMapper = new ObjectMapper();

        String inputPath = "./src/test/AlgoControllerTestFiles/input/16_productException_input.txt";
        
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
        assertEquals("Please ensure that Committed Ship $ is in numerical format, and is a whole number", exception.getMessage());
        System.out.println(true);
    }

    @Test
    @Order(17)
    public void testAlgo17() throws Exception {
        System.out.println("===================== 17. Test if same results are returned if no edits (Usual scenario) =====================");
        Gson gson = new Gson();
        
        // To find out where to store the input & output files
        // File directory = new File("./");
        // System.out.println(directory.getAbsolutePath());

        String inputPath = "./src/test/AlgoControllerTestFiles/input/17_subseqSchedule_input.txt";
        String jsonInput = null;
        try {
            jsonInput = Files.readString(Paths.get(inputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Input File");
        }
        
        String outputPath = "./src/test/AlgoControllerTestFiles/output/17_subseqSchedule_output.txt";
        String jsonExpectedOutput = null;
        try {
            jsonExpectedOutput = Files.readString(Paths.get(outputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Output File");
        }

        subseqSchedulingParam subseqSchedulingParam = gson.fromJson(jsonInput, new TypeToken<subseqSchedulingParam>() {}.getType());
        Map<String, List<List<Object>>> newBaseLine = new HashMap<String, List<List<Object>>>();

        // For double values in masterOps & baseLine, we need to change it back to Integer
        // Limitation of GSON
        Set<String> baseLineQtrs = subseqSchedulingParam.baseLineOccupancy.keySet();

        for (String qtr : baseLineQtrs) {
            List<List<Object>> oldBaseLineData = subseqSchedulingParam.baseLineOccupancy.get(qtr);
            List<List<Object>> newBaseLineData = new ArrayList<List<Object>>();

            newBaseLineData.add(oldBaseLineData.get(0));

            for (int i = 1; i < oldBaseLineData.size(); i++){
                List<Object> baseLineProductDetails = oldBaseLineData.get(i);

                Map<String, Object> oldBaseLineProduct = (Map<String, Object>) baseLineProductDetails.get(0);
                Map<String, Object> newBaseLineProduct = new HashMap<>();

                Set<String> productKeys = oldBaseLineProduct.keySet();

                for (String key : productKeys) {
                    Object value = oldBaseLineProduct.get(key);
                    if (value != null && value.getClass().getSimpleName().equals("Double")){
                        Double valueDouble = (Double) value;
                        newBaseLineProduct.put(key, valueDouble.intValue());
                    } else {
                        newBaseLineProduct.put(key, value);
                    }
                }
                baseLineProductDetails.set(0, newBaseLineProduct);
                
                newBaseLineData.add(baseLineProductDetails);
            }
            newBaseLine.put(qtr, newBaseLineData);
        }

        Map<String, List<List<Object>>> newBayOccupancy = new HashMap<String, List<List<Object>>>();

        Set<String> bayOccupancyQtrs = subseqSchedulingParam.bayOccupancy.keySet();

        for (String qtr : bayOccupancyQtrs) {
            List<List<Object>> oldBayOccupancyData = subseqSchedulingParam.bayOccupancy.get(qtr);
            List<List<Object>> newBayOccupancyData = new ArrayList<List<Object>>();

            newBayOccupancyData.add(oldBayOccupancyData.get(0));

            for (int i = 1; i < oldBayOccupancyData.size(); i++){
                List<Object> bayOccupancyProductDetails = oldBayOccupancyData.get(i);

                Map<String, Object> oldBayOccupancyProduct = (Map<String, Object>) bayOccupancyProductDetails.get(0);
                Map<String, Object> newBayOccupancyProduct = new HashMap<>();

                Set<String> productKeys = oldBayOccupancyProduct.keySet();

                for (String key : productKeys) {
                    Object value = oldBayOccupancyProduct.get(key);
                    if (value != null && value.getClass().getSimpleName().equals("Double")){
                        Double valueDouble = (Double) value;
                        newBayOccupancyProduct.put(key, valueDouble.intValue());
                    } else {
                        newBayOccupancyProduct.put(key, value);
                    }
                }
                bayOccupancyProductDetails.set(0, newBayOccupancyProduct);

                newBayOccupancyData.add(bayOccupancyProductDetails);
            }
            newBayOccupancy.put(qtr, newBayOccupancyData);
        }

        subseqSchedulingParam updatedSchedulingParam = new subseqSchedulingParam(newBaseLine, newBayOccupancy, subseqSchedulingParam.numBays,subseqSchedulingParam.minGap,subseqSchedulingParam.maxGap);

        String result = controller.subseqScheduling(updatedSchedulingParam);
        System.out.println(result.equals(jsonExpectedOutput));
        assertEquals(result, jsonExpectedOutput);
    }

    @Test
    @Order(18)
    public void testAlgo18() throws Exception {
        System.out.println("===================== 18. Ensure that bay is made available for next product from specified 'Send to Storage' date =====================");
        Gson gson = new Gson();
        
        // To find out where to store the input & output files
        // File directory = new File("./");
        // System.out.println(directory.getAbsolutePath());

        String inputPath = "./src/test/AlgoControllerTestFiles/input/18_subseqSchedule_input.txt";
        String jsonInput = null;
        try {
            jsonInput = Files.readString(Paths.get(inputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Input File");
        }
        
        String outputPath = "./src/test/AlgoControllerTestFiles/output/18_subseqSchedule_output.txt";
        String jsonExpectedOutput = null;
        try {
            jsonExpectedOutput = Files.readString(Paths.get(outputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Output File");
        }

        subseqSchedulingParam subseqSchedulingParam = gson.fromJson(jsonInput, new TypeToken<subseqSchedulingParam>() {}.getType());
        Map<String, List<List<Object>>> newBaseLine = new HashMap<String, List<List<Object>>>();

        // For double values in masterOps & baseLine, we need to change it back to Integer
        // Limitation of GSON
        Set<String> baseLineQtrs = subseqSchedulingParam.baseLineOccupancy.keySet();

        for (String qtr : baseLineQtrs) {
            List<List<Object>> oldBaseLineData = subseqSchedulingParam.baseLineOccupancy.get(qtr);
            List<List<Object>> newBaseLineData = new ArrayList<List<Object>>();

            newBaseLineData.add(oldBaseLineData.get(0));

            for (int i = 1; i < oldBaseLineData.size(); i++){
                List<Object> baseLineProductDetails = oldBaseLineData.get(i);

                Map<String, Object> oldBaseLineProduct = (Map<String, Object>) baseLineProductDetails.get(0);
                Map<String, Object> newBaseLineProduct = new HashMap<>();

                Set<String> productKeys = oldBaseLineProduct.keySet();

                for (String key : productKeys) {
                    Object value = oldBaseLineProduct.get(key);
                    if (value != null && value.getClass().getSimpleName().equals("Double")){
                        Double valueDouble = (Double) value;
                        newBaseLineProduct.put(key, valueDouble.intValue());
                    } else {
                        newBaseLineProduct.put(key, value);
                    }
                }
                baseLineProductDetails.set(0, newBaseLineProduct);
                
                newBaseLineData.add(baseLineProductDetails);
            }
            newBaseLine.put(qtr, newBaseLineData);
        }

        Map<String, List<List<Object>>> newBayOccupancy = new HashMap<String, List<List<Object>>>();

        Set<String> bayOccupancyQtrs = subseqSchedulingParam.bayOccupancy.keySet();

        for (String qtr : bayOccupancyQtrs) {
            List<List<Object>> oldBayOccupancyData = subseqSchedulingParam.bayOccupancy.get(qtr);
            List<List<Object>> newBayOccupancyData = new ArrayList<List<Object>>();

            newBayOccupancyData.add(oldBayOccupancyData.get(0));

            for (int i = 1; i < oldBayOccupancyData.size(); i++){
                List<Object> bayOccupancyProductDetails = oldBayOccupancyData.get(i);

                Map<String, Object> oldBayOccupancyProduct = (Map<String, Object>) bayOccupancyProductDetails.get(0);
                Map<String, Object> newBayOccupancyProduct = new HashMap<>();

                Set<String> productKeys = oldBayOccupancyProduct.keySet();

                for (String key : productKeys) {
                    Object value = oldBayOccupancyProduct.get(key);
                    if (value != null && value.getClass().getSimpleName().equals("Double")){
                        Double valueDouble = (Double) value;
                        newBayOccupancyProduct.put(key, valueDouble.intValue());
                    } else {
                        newBayOccupancyProduct.put(key, value);
                    }
                }
                bayOccupancyProductDetails.set(0, newBayOccupancyProduct);

                newBayOccupancyData.add(bayOccupancyProductDetails);
            }
            newBayOccupancy.put(qtr, newBayOccupancyData);
        }

        subseqSchedulingParam updatedSchedulingParam = new subseqSchedulingParam(newBaseLine, newBayOccupancy, subseqSchedulingParam.numBays,subseqSchedulingParam.minGap,subseqSchedulingParam.maxGap);

        String result = controller.subseqScheduling(updatedSchedulingParam);
        System.out.println(result.equals(jsonExpectedOutput));
        assertEquals(result, jsonExpectedOutput);
    }

    @Test
    @Order(19)
    public void testAlgo19() throws Exception {
        System.out.println("===================== 19. Ensure that bay respect the lockMRP date (after original MRP Date) =====================");
        Gson gson = new Gson();
        
        // To find out where to store the input & output files
        // File directory = new File("./");
        // System.out.println(directory.getAbsolutePath());

        String inputPath = "./src/test/AlgoControllerTestFiles/input/19_subseqSchedule_input.txt";
        String jsonInput = null;
        try {
            jsonInput = Files.readString(Paths.get(inputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Input File");
        }
        
        String outputPath = "./src/test/AlgoControllerTestFiles/output/19_subseqSchedule_output.txt";
        String jsonExpectedOutput = null;
        try {
            jsonExpectedOutput = Files.readString(Paths.get(outputPath));
        } catch (IOException e){
            System.out.println("Problem Reading Output File");
        }

        subseqSchedulingParam subseqSchedulingParam = gson.fromJson(jsonInput, new TypeToken<subseqSchedulingParam>() {}.getType());
        Map<String, List<List<Object>>> newBaseLine = new HashMap<String, List<List<Object>>>();

        // For double values in masterOps & baseLine, we need to change it back to Integer
        // Limitation of GSON
        Set<String> baseLineQtrs = subseqSchedulingParam.baseLineOccupancy.keySet();

        for (String qtr : baseLineQtrs) {
            List<List<Object>> oldBaseLineData = subseqSchedulingParam.baseLineOccupancy.get(qtr);
            List<List<Object>> newBaseLineData = new ArrayList<List<Object>>();

            newBaseLineData.add(oldBaseLineData.get(0));

            for (int i = 1; i < oldBaseLineData.size(); i++){
                List<Object> baseLineProductDetails = oldBaseLineData.get(i);

                Map<String, Object> oldBaseLineProduct = (Map<String, Object>) baseLineProductDetails.get(0);
                Map<String, Object> newBaseLineProduct = new HashMap<>();

                Set<String> productKeys = oldBaseLineProduct.keySet();

                for (String key : productKeys) {
                    Object value = oldBaseLineProduct.get(key);
                    if (value != null && value.getClass().getSimpleName().equals("Double")){
                        Double valueDouble = (Double) value;
                        newBaseLineProduct.put(key, valueDouble.intValue());
                    } else {
                        newBaseLineProduct.put(key, value);
                    }
                }
                baseLineProductDetails.set(0, newBaseLineProduct);
                
                newBaseLineData.add(baseLineProductDetails);
            }
            newBaseLine.put(qtr, newBaseLineData);
        }

        Map<String, List<List<Object>>> newBayOccupancy = new HashMap<String, List<List<Object>>>();

        Set<String> bayOccupancyQtrs = subseqSchedulingParam.bayOccupancy.keySet();

        for (String qtr : bayOccupancyQtrs) {
            List<List<Object>> oldBayOccupancyData = subseqSchedulingParam.bayOccupancy.get(qtr);
            List<List<Object>> newBayOccupancyData = new ArrayList<List<Object>>();

            newBayOccupancyData.add(oldBayOccupancyData.get(0));

            for (int i = 1; i < oldBayOccupancyData.size(); i++){
                List<Object> bayOccupancyProductDetails = oldBayOccupancyData.get(i);

                Map<String, Object> oldBayOccupancyProduct = (Map<String, Object>) bayOccupancyProductDetails.get(0);
                Map<String, Object> newBayOccupancyProduct = new HashMap<>();

                Set<String> productKeys = oldBayOccupancyProduct.keySet();

                for (String key : productKeys) {
                    Object value = oldBayOccupancyProduct.get(key);
                    if (value != null && value.getClass().getSimpleName().equals("Double")){
                        Double valueDouble = (Double) value;
                        newBayOccupancyProduct.put(key, valueDouble.intValue());
                    } else {
                        newBayOccupancyProduct.put(key, value);
                    }
                }
                bayOccupancyProductDetails.set(0, newBayOccupancyProduct);

                newBayOccupancyData.add(bayOccupancyProductDetails);
            }
            newBayOccupancy.put(qtr, newBayOccupancyData);
        }

        subseqSchedulingParam updatedSchedulingParam = new subseqSchedulingParam(newBaseLine, newBayOccupancy, subseqSchedulingParam.numBays,subseqSchedulingParam.minGap,subseqSchedulingParam.maxGap);

        String result = controller.subseqScheduling(updatedSchedulingParam);
        System.out.println(result.equals(jsonExpectedOutput));
        assertEquals(result, jsonExpectedOutput);
    }

    @Test
    @Order(20)
    public void testAlgo20() throws Exception {
        System.out.println("===================== 20. Test Product Exception: Missing Cycle Time =====================");

        ObjectMapper objectMapper = new ObjectMapper();

        String inputPath = "./src/test/AlgoControllerTestFiles/input/20_productException2_input.txt";
        
        File input = new File(inputPath);

        Object productRaw = null;
        
        try {
            productRaw = objectMapper.readValue(input, Object.class);
        } catch (IOException e){
            e.printStackTrace();
        }

        final Object product = productRaw;

        Throwable exception = assertThrows(RuntimeException.class, () -> {
            Product p = new Product(product);
        });

        assertEquals("Please ensure that Cycle Time is not empty and in a numerical format", exception.getMessage());
        System.out.println(true);
    }

    @Test
    @Order(21)
    public void testAlgo21() throws Exception {
        System.out.println("===================== 21. Test Product Exception: Non-numerical Cycle Time =====================");

        ObjectMapper objectMapper = new ObjectMapper();

        String inputPath = "./src/test/AlgoControllerTestFiles/input/21_productException2_input.txt";
        
        File input = new File(inputPath);

        Object productRaw = null;
        
        try {
            productRaw = objectMapper.readValue(input, Object.class);
        } catch (IOException e){
            e.printStackTrace();
        }

        final Object product = productRaw;

        Throwable exception = assertThrows(RuntimeException.class, () -> {
            Product p = new Product(product);
        });

        assertEquals("Please ensure that Cycle Time is not empty and in a numerical format", exception.getMessage());
        System.out.println(true);
    }

    @Test
    @Order(22)
    public void testAlgo22() throws Exception {
        System.out.println("===================== 22. Test Product Exception: Missing MRP Date  =====================");

        ObjectMapper objectMapper = new ObjectMapper();

        String inputPath = "./src/test/AlgoControllerTestFiles/input/22_productException2_input.txt";
        
        File input = new File(inputPath);

        Object productRaw = null;
        
        try {
            productRaw = objectMapper.readValue(input, Object.class);
        } catch (IOException e){
            e.printStackTrace();
        }

        final Object product = productRaw;

        Throwable exception = assertThrows(RuntimeException.class, () -> {
            Product p = new Product(product);
        });

        assertEquals("Please ensure that MRP Date is not empty, and in dd/mm/yyyy format", exception.getMessage());
        System.out.println(true);
    }

    @Test
    @Order(23)
    public void testAlgo23() throws Exception {
        System.out.println("===================== 23. Test Product Exception: Wrong Format MRP Date  =====================");

        ObjectMapper objectMapper = new ObjectMapper();

        String inputPath = "./src/test/AlgoControllerTestFiles/input/23_productException2_input.txt";
        
        File input = new File(inputPath);

        Object productRaw = null;
        
        try {
            productRaw = objectMapper.readValue(input, Object.class);
        } catch (IOException e){
            e.printStackTrace();
        }

        final Object product = productRaw;

        Throwable exception = assertThrows(RuntimeException.class, () -> {
            Product p = new Product(product);
        });

        assertEquals("Please ensure that MRP Date is not empty, and in dd/mm/yyyy format", exception.getMessage());
        System.out.println(true);
    }

    @Test
    @Order(24)
    public void testAlgo24() throws Exception {
        System.out.println("===================== 24. Test Product Exception: Locked MRP Date is earlier than suggested  =====================");

        ObjectMapper objectMapper = new ObjectMapper();

        String inputPath = "./src/test/AlgoControllerTestFiles/input/24_productException2_input.txt";
        
        File input = new File(inputPath);

        Object productRaw = null;
        
        try {
            productRaw = objectMapper.readValue(input, Object.class);
        } catch (IOException e){
            e.printStackTrace();
        }

        final Object product = productRaw;

        Throwable exception = assertThrows(RuntimeException.class, () -> {
            Product p = new Product(product);
        });

        assertEquals("The locked MRP date of a product (Slot ID: 888888) cannot be brought forward earlier than the recommended MRP date of Thu May 28 00:00:00 SGT 2020", exception.getMessage());
        System.out.println(true);
    }

    @Test
    @Order(25)
    public void testAlgo25() throws Exception {
        System.out.println("===================== 25. Test Product Exception: Send To Storage Date is earlier than MRP Date  =====================");

        ObjectMapper objectMapper = new ObjectMapper();

        String inputPath = "./src/test/AlgoControllerTestFiles/input/25_productException2_input.txt";
        
        File input = new File(inputPath);

        Object productRaw = null;
        
        try {
            productRaw = objectMapper.readValue(input, Object.class);
        } catch (IOException e){
            e.printStackTrace();
        }

        final Object product = productRaw;

        Throwable exception = assertThrows(RuntimeException.class, () -> {
            Product p = new Product(product);
        });

        assertEquals("The product with Slot ID: 888888 cannot be sent to storage before the earliest MRP date of Thu May 28 00:00:00 SGT 2020", exception.getMessage());
        System.out.println(true);
    }
}
