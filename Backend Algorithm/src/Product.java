import java.util.*;
import java.text.*;

import org.apache.commons.lang3.time.DateUtils;

public class Product{
    private Integer argoID;
    private Integer plant;
    private String buildComplete;
    private String slotStatus;
    private String planProductType;
    private String buildCategory;
    private String shipRevenueType;
    private Integer salesOrder;
    private String forecastID;
    private String slotID_UTID;
    // private String soldToName;
    private String fabName;
    private String secondaryCustomerName;
    // private String productFamily;
    private String buildProduct;
    // private String forecastProduct;
    private String productPN;
    private String committedShip$;
    // private String shipRevenueInt$;
    private String shipRisk_Upside;
    private String shipRiskReason;
    private Date MRPDate;
    private Date intOpsShipReadinessDate;
    private Date MFGCommitDate;
    private Date shipRecogDate;
    private Date handOffDateToDE;
    private Date handOffDateBackToMFG;
    // private Date SAPCustomerReqDate;
    // private Date slotRequestDate;
    // private Date divCommitDate;
    // private Integer deltaDays;
    // private String region;
    // private String SOStatus;
    // private String caerusPOQtr;
    private Date installStartDate;
    private Integer cycleTimeDays;
    private String slotPlanNote;
    private String commentFor$Change;
    private String configurationNote;
    private String dropShip;
    private String MFGStatus;
    private Integer quantity;
    private String RMATool;
    private String new_Used;
    private Date coreNeedDate;
    private Date coreArrivalDate;
    private Date refurbStartDate;
    private Date refurbCompleteDate;
    private String donorStatus;
    private String coreUTID;
    private String coreNotes;
    private String fabID;
    // private Date PGIDate;
    private String flex01;
    private String flex02;
    private String flex03;
    private String flex04;
    // private String buildQtr;
    // private String shipQtr;
    // private String shipRecogQtr;
    // private String MFGSite;
    // private String division;
    // private String holds;
    // private String incoTerms;
    // private String salesSlotIdentifier;
    // private String MSOwner;
    // private String BU;
    // private String bookRiskReason;
    // private String bookRisk_Upside;
    // private String caerusCommit;
    // private String caerusDescription;
    // private String caerusProductType;
    // private Integer changeRequestNumber;
    // private Integer deliveryNumber;
    // private Integer purchaseOrderNumber;
    // private String SBU;
    // private String salesOpsSlotNotes;
    // private String segment;
    // private String slotGap;
    // private String slotRequestQtr;
    // private String soldToID;
    // private String standardCOGS;
    // private String createdBy;
    // private Date createdOn;
    // private Date changedOn;
    // private Date createdTime;
    // private String changedBy;
    // private Date lastChangedTime;
    private Date toolStartDate;

    public Product (Map<String, Object> rowData){
        argoID = (rowData.get("Argo ID") == null) ? null : Integer.parseInt((String) rowData.get("Argo ID"));
        plant = (rowData.get("Plant") == null) ? null : Integer.parseInt((String) rowData.get("Plant"));
        buildComplete = (String) rowData.get("Build Complete");
        slotStatus = (String) rowData.get("Slot Status");
        planProductType = (String) rowData.get("Plan Product Type");
        buildCategory = (String) rowData.get("Build Category");
        shipRevenueType = (String) rowData.get("Ship Revenue Type");
        salesOrder = (rowData.get("Sales Order") == null) ? null : Integer.parseInt((String) rowData.get("Sales Order"));
        forecastID = (String) rowData.get("Forecast ID");
        slotID_UTID = (String) rowData.get("Slot ID/UTID");
        // soldToName = (String) rowData.get("Sold-To Name");
        fabName = (String) rowData.get("Fab Name");
        secondaryCustomerName = (String) rowData.get("Secondary Customer Name");
        // productFamily = (String) rowData.get("Product Family");
        buildProduct = (String) rowData.get("Build Product");
        // forecastProduct = (String) rowData.get("Forecast Product");
        productPN = (String) rowData.get("Product PN");
        committedShip$ = (String) rowData.get("Committed Ship $");
        // shipRevenueInt$ = (String) rowData.get("Ship Revenue (Int $)");
        shipRisk_Upside = (String) rowData.get("Ship Risk/Upside");
        shipRiskReason = (String) rowData.get("Ship Risk Reason");
        // deltaDays = (Integer) rowData.get("Delta Days");
        // region = (String) rowData.get("Region");
        // SOStatus = (String) rowData.get("SO Status");
        // caerusPOQtr = (String) rowData.get("Caerus PO Qtr");
        cycleTimeDays = (Integer) rowData.get("Cycle Time Days");
        slotPlanNote = (String) rowData.get("Slot Plan Note");
        commentFor$Change = (String) rowData.get("Comment For $ Change");
        configurationNote = (String) rowData.get("Configuration Note");
        dropShip = (String) rowData.get("Drop Ship");
        MFGStatus = (String) rowData.get("MFG Status");
        quantity = (Integer) rowData.get("Quantity");
        RMATool = (String) rowData.get("RMA Tool");
        new_Used = (String) rowData.get("New/Used");
        donorStatus = (String) rowData.get("Donor Status");
        coreUTID = (String) rowData.get("Core UTID");
        coreNotes = (String) rowData.get("Core Notes");
        fabID = (String) rowData.get("Fab ID");
        flex01 = (String) rowData.get("Flex 01");
        flex02 = (String) rowData.get("Flex 02");
        flex03 = (String) rowData.get("Flex 03");
        flex04 = (String) rowData.get("Flex 04");
        // buildQtr = (String) rowData.get("Build Qtr");
        // shipQtr = (String) rowData.get("Ship Qtr");
        // shipRecogQtr = (String) rowData.get("Ship Recog Qtr");
        // MFGSite = (String) rowData.get("MFG Site");
        // division = (String) rowData.get("Division");
        // holds = (String) rowData.get("Holds");
        // incoTerms = (String) rowData.get("IncoTerms");
        // salesSlotIdentifier = (String) rowData.get("Sales Slot Identifier");
        // MSOwner = (String) rowData.get("MS Owner");
        // BU = (String) rowData.get("BU");
        // bookRiskReason = (String) rowData.get("Book Risk Reason");
        // bookRisk_Upside = (String) rowData.get("Book Risk/Upside");
        // caerusCommit = (String) rowData.get("Caerus Commit");
        // caerusDescription = (String) rowData.get("Caerus Description");
        // caerusProductType = (String) rowData.get("Caerus Product Type");
        // changeRequestNumber = (rowData.get("Change Request #") == null) ? null : Integer.parseInt((String) rowData.get("Change Request #"));
        // deliveryNumber = (rowData.get("Delivery #") == null) ? null : Integer.parseInt((String) rowData.get("Delivery #"));
        // purchaseOrderNumber = (rowData.get("Purchase Order #") == null) ? null : Integer.parseInt((String) rowData.get("Purchase Order #"));
        // SBU = (String) rowData.get("SBU");
        // salesOpsSlotNotes = (String) rowData.get("Sales Ops Slot Notes");
        // segment = (String) rowData.get("Segment");
        // slotGap = (String) rowData.get("Slot Gap");
        // slotRequestQtr = (String) rowData.get("Slot Request Qtr");
        // soldToID = (String) rowData.get("Sold-To ID");
        // standardCOGS = (String) rowData.get("Standard COGS");
        // createdBy = (String) rowData.get("Created By");
        // changedBy = (String) rowData.get("Changed By");
        
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            MRPDate = (rowData.get("MRP Date") == null) ? null : dateFormat.parse((String) rowData.get("MRP Date"));
            intOpsShipReadinessDate = (rowData.get("Int. Ops Ship Readiness Date") == null) ? null : dateFormat.parse((String) rowData.get("Int. Ops Ship Readiness Date"));
            MFGCommitDate = (rowData.get("MFG Commit Date") == null) ? null : dateFormat.parse((String) rowData.get("MFG Commit Date"));
            shipRecogDate = (rowData.get("Ship Recog Date") == null) ? null : dateFormat.parse((String) rowData.get("Ship Recog Date"));
            handOffDateToDE = (rowData.get("Hand Off Date To DE") == null) ? null : dateFormat.parse((String) rowData.get("Hand Off Date To DE"));
            handOffDateBackToMFG = (rowData.get("Hand Off Date Back To MFG") == null) ? null : dateFormat.parse((String) rowData.get("Hand Off Date Back To MFG"));
            // SAPCustomerReqDate = (rowData.get("SAP Customer Req Date") == null) ? null : dateFormat.parse((String) rowData.get("SAP Customer Req Date"));
            // slotRequestDate = (rowData.get("Slot Request Date") == null) ? null : dateFormat.parse((String) rowData.get("Slot Request Date"));
            // divCommitDate = (rowData.get("Div Commit Date") == null) ? null : dateFormat.parse((String) rowData.get("Div Commit Date"));
            installStartDate = (rowData.get("Install Start Date") == null) ? null : dateFormat.parse((String)rowData.get("Install Start Date"));
            coreNeedDate = (rowData.get("Core Need Date") == null) ? null : dateFormat.parse((String) rowData.get("Core Need Date"));
            coreArrivalDate = (rowData.get("Core Arrival Date") == null) ? null : dateFormat.parse((String) rowData.get("Core Arrival Date"));
            refurbStartDate = (rowData.get("Refurb Start Date") == null) ? null : dateFormat.parse((String) rowData.get("Refurb Start Date"));
            refurbCompleteDate = (rowData.get("Refurb Complete Date") == null) ? null : dateFormat.parse((String) rowData.get("Refurb Complete Date"));
            // PGIDate = (rowData.get("PGI Date") == null) ? null : dateFormat.parse((String) rowData.get("PGI Date"));
            // createdOn = (rowData.get("Created On") == null) ? null : dateFormat.parse((String) rowData.get("Created On"));
            // changedOn = (rowData.get("Changed On") == null) ? null : dateFormat.parse((String) rowData.get("Changed On"));
            // createdTime = (rowData.get("Created Time") == null) ? null : dateFormat.parse((String) rowData.get("Created Time"));
            // lastChangedTime = (rowData.get("Last Changed Time") == null) ? null : dateFormat.parse((String) rowData.get("Last Changed Time"));
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    /**
     * To calculate the ToolStartDate for a product , based on 1 pair of mrpDate and cycleTimeDays
     * 
     */
    public void calculateToolStart(){
        toolStartDate = DateUtils.addDays(MRPDate, -cycleTimeDays);
    }

    public Date getToolStart(){
        return toolStartDate;
    }

}