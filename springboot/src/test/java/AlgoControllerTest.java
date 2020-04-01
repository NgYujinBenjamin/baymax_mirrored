import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.algorithm.AlgoController;
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
}
