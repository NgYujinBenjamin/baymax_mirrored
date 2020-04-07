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
import java.io.*;
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

    @RequestMapping(path = "/firstScheduling", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public String firstScheduling(@RequestBody firstSchedulingParam param) throws RuntimeException, Exception {
        List<Map<String, Object>> baseLineData;
        List<Map<String, Object>> data;
        Integer numBays;
        Integer minGap;
        Integer maxGap;

        try {
            baseLineData = param.baseline;
            data = param.masterOps;
            numBays = param.bay == null ? 16 : param.bay;
            minGap = param.minGap == null ? 0 : param.minGap;
            maxGap = param.maxGap == null ? 90 : param.maxGap;
        } catch (Exception e) {
            return "JSON Reading Error";
        }

        ArrayList<Product> allProduct = new ArrayList<Product>();
        ArrayList<Product> baseLineProduct = new ArrayList<Product>();

        for (int i = 0; i < data.size(); i++) {
            Product p = new Product(data.get(i));
            allProduct.add(p);
        }

        for (int i = 0; i < baseLineData.size(); i++) {
            Product p = new Product(baseLineData.get(i));
            baseLineProduct.add(p);
        }

        Collections.sort(allProduct);
        Collections.sort(baseLineProduct);

        BayRequirement bayReq = null;
        BaySchedule baySchedule = null;

        Integer gapDiff = maxGap - minGap; // End date alr considers the min gap; Can only pull forward by gapDiff more days

        HashSet<HashMap<String, Integer>> historyQuarterHC = new HashSet<HashMap<String, Integer>>();
        HashMap<String, Integer> quarterHC = new HeadCount(allProduct).getQuarterHC();
        historyQuarterHC.add(quarterHC);

        Boolean quarterHCChanged = true;
        Boolean pastQuarterHC = false;

        while (quarterHCChanged && !pastQuarterHC) {
            baySchedule = new BaySchedule(baseLineProduct, allProduct, quarterHC, numBays, gapDiff);
            // System.out.println(quarterHC);

            // For Logging
            ArrayList<Bay> schedule = baySchedule.getSchedule();

            try (PrintStream out = new PrintStream(new FileOutputStream("./firstScheduling_BayDiagnosis_LOG.txt"), true)) {
                out.println("HeadCount Availability:" + quarterHC);
                for (Bay b : schedule) {
                    ArrayList<Product> productList = b.getBaySchedule();
                    out.println("%%%%%%%%%%%%%%%% Bay " + b.getBayID() + " %%%%%%%%%%%%%%%%");
                    for (Product p : productList) {
                        out.print("[ SlotID/UTID:" + p.getSlotID_UTID() + " | ToolStartDate:" + p.getToolStartDate() + " | LeaveBayDate:" + p.getLeaveBayDate() + "] ->");
                        // out.print("[" + p.getArgoID() + " | " + p.getAssignedBayID() + " | "  + p.getGapDays() + "] ->");
                    }
                    out.println();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // End of debugging

            allProduct = baySchedule.getAllProduct();
            baseLineProduct = baySchedule.getBaseLineProduct();
            bayReq = new BayRequirement(baseLineProduct, allProduct);

            HashMap<String, Integer> newQuarterHC = new HeadCount(allProduct).getQuarterHC();
            Integer prevHistoryQuarterHCSize = historyQuarterHC.size();
            historyQuarterHC.add(newQuarterHC);
            Integer currHistoryQuarterHCSize = historyQuarterHC.size();

            if (quarterHC.equals(newQuarterHC)) {
                quarterHCChanged = false;
            } else if (prevHistoryQuarterHCSize == currHistoryQuarterHCSize) {
                pastQuarterHC = true;
            } else {
                quarterHC = newQuarterHC;
            }
        }
        // System.out.println(BayRequirement.toJSONString(bayReq));
        return BayRequirement.toJSONString(bayReq);
    }

    @RequestMapping(path = "/subseqScheduling", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public String subseqScheduling(@RequestBody subseqSchedulingParam param) throws Exception {
        Map<String, List<List<Object>>> baseLineOccupancy;
        Map<String, List<List<Object>>> bayOccupancy;
        Integer numBays;
        Integer minGap;
        Integer maxGap;

        try {
            baseLineOccupancy = param.baseLineOccupancy;
            bayOccupancy = param.bayOccupancy;
            numBays = param.numBays == null ? 16 : param.numBays;
            minGap = param.minGap == null ? 0 : param.minGap;
            maxGap = param.maxGap == null ? 90 : param.maxGap;

        } catch (Exception e) {
            return "JSON Reading Error";
        }

        ArrayList<Product> allProduct = new ArrayList<Product>();
        ArrayList<Product> baseLineProduct = new ArrayList<Product>();

        Set<String> baseLineQtrs = baseLineOccupancy.keySet();
        for (String qtr : baseLineQtrs) {
            List<List<Object>> qtrOccupancy = baseLineOccupancy.get(qtr);
            // Within each quarter
            for (int i = 1; i < qtrOccupancy.size(); i++) {
                // Skip index 0 because it is the [weekOf]
                Object productRaw = qtrOccupancy.get(i).get(0);
                Product p = new Product(productRaw);
                baseLineProduct.add(p);
            }
        }

        Set<String> futureQtrs = bayOccupancy.keySet();
        for (String qtr : futureQtrs) {
            List<List<Object>> qtrOccupancy = bayOccupancy.get(qtr);
            // Within each quarter
            for (int i = 1; i < qtrOccupancy.size(); i++) {
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

        HashSet<HashMap<String, Integer>> historyQuarterHC = new HashSet<HashMap<String, Integer>>();
        HashMap<String, Integer> quarterHC = new HeadCount(allProduct).getQuarterHC();
        historyQuarterHC.add(quarterHC);

        Boolean quarterHCChanged = true;
        Boolean pastQuarterHC = false;

        while (quarterHCChanged && !pastQuarterHC) {
            // System.out.println(quarterHC);

            baySchedule = new BaySchedule(baseLineProduct, allProduct, quarterHC, numBays, gapDiff);

            // For Logging
            ArrayList<Bay> schedule = baySchedule.getSchedule();

            try (PrintStream out = new PrintStream(new FileOutputStream("./subseqScheduling_BayDiagnosis_LOG.txt"), true)) {
                out.println("HeadCount Availability:" + quarterHC);
                for (Bay b : schedule) {
                    ArrayList<Product> productList = b.getBaySchedule();
                    out.println("%%%%%%%%%%%%%%%% Bay " + b.getBayID() + " %%%%%%%%%%%%%%%%");
                    for (Product p : productList) {
                        out.print("[ SlotID/UTID:" + p.getSlotID_UTID() + " | ToolStartDate:" + p.getToolStartDate() + " | LeaveBayDate:" + p.getLeaveBayDate() + "] ->");
                        // out.print("[" + p.getArgoID() + " | " + p.getAssignedBayID() + " | "  + p.getGapDays() + "] ->");
                    }
                    out.println();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // End of debugging


            allProduct = baySchedule.getAllProduct();
            baseLineProduct = baySchedule.getBaseLineProduct();
            bayReq = new BayRequirement(baseLineProduct, allProduct);

            // Check if quarterHC has changed
            HashMap<String, Integer> newQuarterHC = new HeadCount(allProduct).getQuarterHC();
            Integer prevHistoryQuarterHCSize = historyQuarterHC.size();
            historyQuarterHC.add(newQuarterHC);
            Integer currHistoryQuarterHCSize = historyQuarterHC.size();

            if (quarterHC.equals(newQuarterHC)) {
                quarterHCChanged = false;
            } else if (prevHistoryQuarterHCSize == currHistoryQuarterHCSize) {
                pastQuarterHC = true;
            } else {
                quarterHC = newQuarterHC;
            }
        }
        // System.out.println(BayRequirement.toJSONString(bayReq));
        return BayRequirement.toJSONString(bayReq);
    }
}