package main.java.algorithm;

import java.util.*;
import java.text.*;

import org.apache.commons.lang3.time.DateUtils;

import com.google.gson.Gson;

public class ProductBayReq implements Comparable<Product>{
    private Integer salesOrder;
    private String slotID_UTID;
    private String fabName;
    private String buildProduct;
    private Date MFGCommitDate;
    private Date toolStartDate;

    public Product (Map<String, Object> rowData){
        salesOrder = (rowData.get("Sales Order") == null) ? null : Integer.parseInt((String) rowData.get("Sales Order"));
        slotID_UTID = (String) rowData.get("Slot ID/UTID");
        fabName = (String) rowData.get("Fab Name");
        buildProduct = (String) rowData.get("Build Product");
        MFGCommitDate = (rowData.get("MFG Commit Date") == null) ? null : dateFormat.parse((String) rowData.get("MFG Commit Date"));
        toolStartDate = DateUtils.addDays(MRPDate, -cycleTimeDays);
    }
}