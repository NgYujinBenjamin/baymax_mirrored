package main.java.algorithm.Objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.validator.GenericValidator;

import main.java.algorithm.ExclusionStrategy.Exclude;

public class Product implements Comparable<Product>{
    private Integer argoID;
    private Integer plant;
    private Integer buildComplete;
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
    private Integer committedShip$;
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
    private Integer RMATool;
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
    private String buildQtr;
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
    @Exclude
    private Date latestToolStartDate;
    
    private Date toolStartDate;
    
    private Date endDate;

    @Exclude
    private Date leaveBayDate;
    
    private Integer gapDays;
    
    private Boolean lockMRPDate = null;
    
    private Date sendToStorageDate;
    
    @Exclude
    private Integer assignedBayID;

	/**
	 * Constructor for Product class (used for /firstScheduling). Extracts the relevant attributes from rowData and creates a new Product object with the attributes.
	 * Relevant attributes are defined as the attributes required for output generation. 
	 * Attribute values are initialized to null if it is not found in rowData.
	 * Date values passed in have to be in the format of "dd/MM/yyyy"
	 * @param rowData
	 */
    public Product (Map<String, Object> rowData){
        argoID = (rowData.get("Argo ID") == null) ? null : Integer.parseInt((String) rowData.get("Argo ID"));
        plant = (rowData.get("Plant") == null) ? null : Integer.parseInt((String) rowData.get("Plant"));
        buildComplete = (Integer) rowData.get("Build Complete");
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
        committedShip$ = (Integer) rowData.get("Committed Ship $");
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
        RMATool = (Integer) rowData.get("RMA Tool");
        new_Used = (String) rowData.get("New/Used");
        donorStatus = (String) rowData.get("Donor Status");
        coreUTID = (String) rowData.get("Core UTID");
        coreNotes = (String) rowData.get("Core Notes");
        fabID = (String) rowData.get("Fab ID");
        flex01 = (String) rowData.get("Flex 01");
        flex02 = (String) rowData.get("Flex 02");
        flex03 = (String) rowData.get("Flex 03");
        flex04 = (String) rowData.get("Flex 04");
        buildQtr = (String) rowData.get("Build Qtr");
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
        lockMRPDate = (Boolean) rowData.get("Lock MRP Date");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            MRPDate = GenericValidator.isDate((String)rowData.get("MRP Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("MRP Date")) : null;
            intOpsShipReadinessDate = GenericValidator.isDate((String) rowData.get("Int. Ops Ship Readiness Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Int. Ops Ship Readiness Date")) : null;
            MFGCommitDate = GenericValidator.isDate((String) rowData.get("MFG Commit Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("MFG Commit Date")) : null;
            shipRecogDate = GenericValidator.isDate((String) rowData.get("Ship Recog Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Ship Recog Date")): null;
            handOffDateToDE = GenericValidator.isDate((String) rowData.get("Hand Off Date To DE"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Hand Off Date To DE")): null;
            handOffDateBackToMFG = GenericValidator.isDate((String) rowData.get("Hand Off Date Back To MFG"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Hand Off Date Back To MFG")): null;
            // SAPCustomerReqDate = GenericValidator.isDate((String) rowData.get("SAP Customer Req Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("SAP Customer Req Date")): null;
            // slotRequestDate = GenericValidator.isDate((String) rowData.get("Slot Request Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Slot Request Date")): null;
            // divCommitDate = GenericValidator.isDate((String) rowData.get("Div Commit Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Div Commit Date")): null;
            installStartDate = GenericValidator.isDate((String) rowData.get("Install Start Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String)rowData.get("Install Start Date")): null;
            coreNeedDate = GenericValidator.isDate((String) rowData.get("Core Need Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Core Need Date")): null;
            coreArrivalDate = GenericValidator.isDate((String) rowData.get("Core Arrival Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Core Arrival Date")): null;
            refurbStartDate = GenericValidator.isDate((String) rowData.get("Refurb Start Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Refurb Start Date")): null;
            refurbCompleteDate = GenericValidator.isDate((String) rowData.get("Refurb Complete Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Refurb Complete Date")): null;
            // PGIDate = GenericValidator.isDate((String) rowData.get("PGI Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("PGI Date")): null;
            // createdOn = GenericValidator.isDate((String) rowData.get("Created On"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Created On")): null;
            // changedOn = GenericValidator.isDate((String) rowData.get("Changed On"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Changed On")): null;
            // createdTime = GenericValidator.isDate((String) rowData.get("Created Time"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Created Time")): null;
            // lastChangedTime = GenericValidator.isDate((String) rowData.get("Last Changed Time"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Last Changed Time")): null;
            endDate = GenericValidator.isDate((String) rowData.get("End Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("End Date")): MFGCommitDate;
            sendToStorageDate = GenericValidator.isDate((String) rowData.get("Send To Storage Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Send To Storage Date")): null;
        } catch (ParseException e){
            e.printStackTrace();
        }
        

        latestToolStartDate = DateUtils.addDays(endDate, -cycleTimeDays);
        toolStartDate = DateUtils.addDays(MRPDate, -cycleTimeDays); // Useful for baseline products
        
        if (sendToStorageDate != null){
            leaveBayDate = sendToStorageDate;
        }
        else if (fabName != null && fabName.equals("OPEN")){
            leaveBayDate = intOpsShipReadinessDate;
        } else {
            leaveBayDate = endDate;
        }

        gapDays = (int) ((MFGCommitDate.getTime() - MRPDate.getTime())/ (24 * 60 * 60 * 1000));
    }


    /**
     * Constructor for Product class (used for /subseqScheduling). Extracts the relevant attributes from object passed and creates a new Product object with the attributes.
	 * Relevant attributes are defined as the attributes required for output generation. 
	 * Attribute values are initialized to null if it is not found in rowData.
	 * Date values passed in have to be in the format of "dd/MM/yyyy"
     * @param productRaw Object, that is an instanceOf Map, containing the product details in key-value pairs
     * @throws RuntimeException if there is problem casting the object into type HashMap
     */
    public Product (Object productRaw) throws RuntimeException {
        HashMap<String, Object> productDetails = null;
        
        if (productRaw instanceof Map){
            productDetails = (HashMap<String, Object>) productRaw;
        } else {
        	throw new RuntimeException("The product details passed in are not in the right format. Product details need to be in key-value pairs.");
        }

        argoID = (productDetails.get("argoID") == null) ? null : (Integer) productDetails.get("argoID");
        plant = (productDetails.get("plant") == null) ? null : (Integer) productDetails.get("plant");
        buildComplete = (Integer) productDetails.get("buildComplete");
        slotStatus = (String) productDetails.get("slotStatus");
        planProductType = (String) productDetails.get("planProductType");
        buildCategory = (String) productDetails.get("buildCategory");
        shipRevenueType = (String) productDetails.get("shipRevenueType");
        salesOrder = (productDetails.get("salesOrder") == null) ? null : (Integer) productDetails.get("salesOrder");
        forecastID = (String) productDetails.get("forecastID");
        slotID_UTID = (String) productDetails.get("slotID_UTID");
        // soldToName = (String) productDetails.get("Sold-To Name");
        fabName = (String) productDetails.get("fabName");
        secondaryCustomerName = (String) productDetails.get("secondaryCustomerName");
        // productFamily = (String) productDetails.get("Product Family");
        buildProduct = (String) productDetails.get("buildProduct");
        // forecastProduct = (String) productDetails.get("Forecast Product");
        productPN = (String) productDetails.get("productPN");
        committedShip$ = (Integer) productDetails.get("committedShip$");
        // shipRevenueInt$ = (String) productDetails.get("Ship Revenue (Int $)");
        shipRisk_Upside = (String) productDetails.get("shipRisk_Upside");
        shipRiskReason = (String) productDetails.get("shipRiskReason");
        // deltaDays = (Integer) productDetails.get("Delta Days");
        // region = (String) productDetails.get("Region");
        // SOStatus = (String) productDetails.get("SO Status");
        // caerusPOQtr = (String) productDetails.get("Caerus PO Qtr");
        cycleTimeDays = (Integer) productDetails.get("cycleTimeDays");
        slotPlanNote = (String) productDetails.get("slotPlanNote");
        commentFor$Change = (String) productDetails.get("commentFor$Change");
        configurationNote = (String) productDetails.get("configurationNote");
        dropShip = (String) productDetails.get("dropShip");
        MFGStatus = (String) productDetails.get("MFGStatus");
        quantity = (Integer) productDetails.get("quantity");
        RMATool = (Integer) productDetails.get("RMATool");
        new_Used = (String) productDetails.get("new_Used");
        donorStatus = (String) productDetails.get("donorStatus");
        coreUTID = (String) productDetails.get("coreUTID");
        coreNotes = (String) productDetails.get("coreNotes");
        fabID = (String) productDetails.get("fabID");
        flex01 = (String) productDetails.get("flex01");
        flex02 = (String) productDetails.get("flex02");
        flex03 = (String) productDetails.get("flex03");
        flex04 = (String) productDetails.get("flex04");
        buildQtr = (String) productDetails.get("buildQtr");
        // shipQtr = (String) productDetails.get("Ship Qtr");
        // shipRecogQtr = (String) productDetails.get("Ship Recog Qtr");
        // MFGSite = (String) productDetails.get("MFG Site");
        // division = (String) productDetails.get("Division");
        // holds = (String) productDetails.get("Holds");
        // incoTerms = (String) productDetails.get("IncoTerms");
        // salesSlotIdentifier = (String) productDetails.get("Sales Slot Identifier");
        // MSOwner = (String) productDetails.get("MS Owner");
        // BU = (String) productDetails.get("BU");
        // bookRiskReason = (String) productDetails.get("Book Risk Reason");
        // bookRisk_Upside = (String) productDetails.get("Book Risk/Upside");
        // caerusCommit = (String) productDetails.get("Caerus Commit");
        // caerusDescription = (String) productDetails.get("Caerus Description");
        // caerusProductType = (String) productDetails.get("Caerus Product Type");
        // changeRequestNumber = (productDetails.get("Change Request #") == null) ? null : (Integer) productDetails.get("Change Request #");
        // deliveryNumber = (productDetails.get("Delivery #") == null) ? null : (Integer) productDetails.get("Delivery #"));
        // purchaseOrderNumber = (productDetails.get("Purchase Order #") == null) ? null : (Integer) productDetails.get("Purchase Order #");
        // SBU = (String) productDetails.get("SBU");
        // salesOpsSlotNotes = (String) productDetails.get("Sales Ops Slot Notes");
        // segment = (String) productDetails.get("Segment");
        // slotGap = (String) productDetails.get("Slot Gap");
        // slotRequestQtr = (String) productDetails.get("Slot Request Qtr");
        // soldToID = (String) productDetails.get("Sold-To ID");
        // standardCOGS = (String) productDetails.get("Standard COGS");
        // createdBy = (String) productDetails.get("Created By");
        // changedBy = (String) productDetails.get("Changed By");
        lockMRPDate = (Boolean) productDetails.get("lockMRPDate");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            MRPDate = GenericValidator.isDate((String) productDetails.get("MRPDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("MRPDate")): null;
            intOpsShipReadinessDate = GenericValidator.isDate((String) productDetails.get("intOpsShipReadinessDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("intOpsShipReadinessDate")): null;
            MFGCommitDate = GenericValidator.isDate((String) productDetails.get("MFGCommitDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("MFGCommitDate")): null;
            shipRecogDate = GenericValidator.isDate((String) productDetails.get("shipRecogDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("shipRecogDate")): null;
            handOffDateToDE = GenericValidator.isDate((String) productDetails.get("handOffDateToDE"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("handOffDateToDE")): null;
            handOffDateBackToMFG = GenericValidator.isDate((String) productDetails.get("handOffDateBackToMFG"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("handOffDateBackToMFG")): null;
            // SAPCustomerReqDate = GenericValidator.isDate((String) productDetails.get("SAP Customer Req Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("SAP Customer Req Date")): null;
            // slotRequestDate = GenericValidator.isDate((String) productDetails.get("Slot Request Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("Slot Request Date")): null;
            // divCommitDate = GenericValidator.isDate((String) productDetails.get("Div Commit Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("Div Commit Date")): null;
            installStartDate = GenericValidator.isDate((String) productDetails.get("installStartDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String)productDetails.get("installStartDate")): null;
            coreNeedDate = GenericValidator.isDate((String) productDetails.get("coreNeedDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("coreNeedDate")): null;
            coreArrivalDate = GenericValidator.isDate((String) productDetails.get("coreArrivalDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("coreArrivalDate")): null;
            refurbStartDate = GenericValidator.isDate((String) productDetails.get("refurbStartDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("refurbStartDate")): null;
            refurbCompleteDate = GenericValidator.isDate((String) productDetails.get("refurbCompleteDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("refurbCompleteDate")): null;
            // PGIDate = GenericValidator.isDate((String) productDetails.get("PGI Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("PGI Date")): null;
            // createdOn = GenericValidator.isDate((String) productDetails.get("Created On"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("Created On")): null;
            // changedOn = GenericValidator.isDate((String) productDetails.get("Changed On"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("Changed On")): null;
            // createdTime = GenericValidator.isDate((String) productDetails.get("Created Time"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("Created Time")): null;
            // lastChangedTime = GenericValidator.isDate((String) productDetails.get("Last Changed Time"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("Last Changed Time")): null: null;
            endDate = GenericValidator.isDate((String)productDetails.get("endDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("endDate")): MFGCommitDate;
            sendToStorageDate = GenericValidator.isDate((String) productDetails.get("sendToStorageDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("sendToStorageDate")): null;
            toolStartDate = GenericValidator.isDate((String) productDetails.get("toolStartDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("toolStartDate")): null;
        } catch (ParseException e){
            e.printStackTrace();

        }        

        if (toolStartDate == null){
            toolStartDate = DateUtils.addDays(MRPDate, -cycleTimeDays); // Needed for history, which uses this constructor and does not have toolStartDate value
        }

        if (lockMRPDate != null && lockMRPDate){
            latestToolStartDate = toolStartDate;
        } else {
            latestToolStartDate = DateUtils.addDays(endDate, -cycleTimeDays);
        }
        
        
        if (sendToStorageDate != null){
            leaveBayDate = sendToStorageDate;
        }
        else if (fabName != null && fabName.equals("OPEN")){
            leaveBayDate = intOpsShipReadinessDate;
        } else {
            leaveBayDate = endDate;
        }

        gapDays = (int) ((MFGCommitDate.getTime() - MRPDate.getTime())/ (24 * 60 * 60 * 1000)); 
    }


    /** Compares 2 products based on toolStart and revenue so as to determine scheduling priority 
     * @param anotherProduct Another product that is to be compared with
     * @return Value of 0 if the Products have the same latestToolStart date and committedShip$; A value less than 0 if this Product has a earlier toolStartDate or larger revenue than that of the other Product; A value more than 0 if this Product has a later toolStartDate or smaller revenue than that of the other Product.
     */
    public int compareTo(Product anotherProduct){
        Date thisLatestToolStart = latestToolStartDate;
        Integer thisCommittedShip$ = committedShip$;
        Date otherLatestToolStart = anotherProduct.latestToolStartDate;
        Integer otherCommittedShip$ = anotherProduct.committedShip$;

        if (thisLatestToolStart.compareTo(otherLatestToolStart) == 0){
            return - thisCommittedShip$.compareTo(otherCommittedShip$);
        }
        return thisLatestToolStart.compareTo(otherLatestToolStart);
    }

    /**
     * Returns the ArgoID of the Product
     * @return ArgoID of the Product
     */
    public Integer getArgoID() {
        return argoID;
    }

    /**
     * Returns the Plant of the Product
     * @return Plant of the Product
     */
    public Integer getPlant() {
        return plant;
    }

    /**
     * Returns the Build Complete of the Product
     * @return Build Complete of the Product
     */
    public Integer getBuildComplete() {
        return buildComplete;
    }

    /**
     * Returns the Slot Status of the Product
     * @return Slot Status of the Product
     */
    public String getSlotStatus() {
        return slotStatus;
    }

    /**
     * Returns the Plan Product Type of the Product
     * @return Plan Product Type of the Product
     */
    public String getPlanProductType() {
        return planProductType;
    }

    /**
     * Returns the Build Category of the Product
     * @return Build Category of the Product
     */
    public String getBuildCategory() {
        return buildCategory;
    }
    
    /**
     * Returns the Ship Revenue Type of the Product
     * @return Ship Revenue Type of the Product
     */
    public String getShipRevenueType() {
        return shipRevenueType;
    }

    /**
     * Returns the Sales Order of the Product
     * @return Sales Order of the Product
     */
    public Integer getSalesOrder() {
        return salesOrder;
    }

    /**
     * Returns the Forecast ID of the Product
     * @return Forecast ID of the Product
     */
    public String getForecastID() {
        return forecastID;
    }

    public String getSlotID_UTID() {
        return slotID_UTID;
    }

    public String getFabName() {
        return fabName;
    }

    public String getSecondaryCustomerName() {
        return secondaryCustomerName;
    }

    public String getBuildProduct() {
        return buildProduct;
    }

    public String getProductPN() {
        return productPN;
    }

    public Integer getCommittedShip$() {
        return committedShip$;
    }

    public String getShipRisk_Upside() {
        return shipRisk_Upside;
    }

    public String getShipRiskReason() {
        return shipRiskReason;
    }

    public Date getMRPDate() {
        return MRPDate;
    }

    public Date getIntOpsShipReadinessDate() {
        return intOpsShipReadinessDate;
    }

    public Date getMFGCommitDate() {
        return MFGCommitDate;
    }

    public Date getShipRecogDate() {
        return shipRecogDate;
    }

    public Date getHandOffDateToDE() {
        return handOffDateToDE;
    }

    public Date getHandOffDateBackToMFG() {
        return handOffDateBackToMFG;
    }

    public Date getInstallStartDate() {
        return installStartDate;
    }

    public Integer getCycleTimeDays() {
        return cycleTimeDays;
    }

    public String getSlotPlanNote() {
        return slotPlanNote;
    }

    public String getCommentFor$Change() {
        return commentFor$Change;
    }

    public String getConfigurationNote() {
        return configurationNote;
    }

    public String getDropShip() {
        return dropShip;
    }

    public String getMFGStatus() {
        return MFGStatus;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getRMATool() {
        return RMATool;
    }

    public String getNew_Used() {
        return new_Used;
    }

    public Date getCoreNeedDate() {
        return coreNeedDate;
    }

    public Date getCoreArrivalDate() {
        return coreArrivalDate;
    }

    public Date getRefurbStartDate() {
        return refurbStartDate;
    }

    public Date getRefurbCompleteDate() {
        return refurbCompleteDate;
    }

    public String getDonorStatus() {
        return donorStatus;
    }

    public String getCoreUTID() {
        return coreUTID;
    }

    public String getCoreNotes() {
        return coreNotes;
    }

    public String getFabID() {
        return fabID;
    }

    public String getFlex01() {
        return flex01;
    }

    public String getFlex02() {
        return flex02;
    }

    public String getFlex03() {
        return flex03;
    }

    public String getFlex04() {
        return flex04;
    }

    public String getBuildQtr() {
        return buildQtr;
    }

    public Date getToolStartDate() {
        return toolStartDate;
    }

    public Date getLatestToolStartDate() {
        return latestToolStartDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getLeaveBayDate() {
        return leaveBayDate;
    }

    public Integer getGapDays() {
        return gapDays;
    }
    
    public Integer getAssignedBayID(){
        return assignedBayID;
    }

    public Boolean getLockMRPDate(){
        return lockMRPDate;
    }

    /**
     * Setters
     */
    public void setArgoID(Integer argoID) {
        this.argoID = argoID;
    }

    public void setPlant(Integer plant) {
        this.plant = plant;
    }

    public void setBuildComplete(Integer buildComplete) {
        this.buildComplete = buildComplete;
    }

    public void setSlotStatus(String slotStatus) {
        this.slotStatus = slotStatus;
    }

    public void setPlanProductType(String planProductType) {
        this.planProductType = planProductType;
    }

    public void setBuildCategory(String buildCategory) {
        this.buildCategory = buildCategory;
    }

    public void setShipRevenueType(String shipRevenueType) {
        this.shipRevenueType = shipRevenueType;
    }

    public void setSalesOrder(Integer salesOrder) {
        this.salesOrder = salesOrder;
    }

    public void setForecastID(String forecastID) {
        this.forecastID = forecastID;
    }

    public void setSlotID_UTID(String slotID_UTID) {
        this.slotID_UTID = slotID_UTID;
    }

    public void setFabName(String fabName) {
        this.fabName = fabName;
    }

    public void setSecondaryCustomerName(String secondaryCustomerName) {
        this.secondaryCustomerName = secondaryCustomerName;
    }

    public void setBuildProduct(String buildProduct) {
        this.buildProduct = buildProduct;
    }

    public void setProductPN(String productPN) {
        this.productPN = productPN;
    }

    public void setCommittedShip$(Integer committedShip$) {
        this.committedShip$ = committedShip$;
    }

    public void setShipRisk_Upside(String shipRisk_Upside) {
        this.shipRisk_Upside = shipRisk_Upside;
    }

    public void setShipRiskReason(String shipRiskReason) {
        this.shipRiskReason = shipRiskReason;
    }

    public void setMRPDate(Date MRPDate) {
        this.MRPDate = MRPDate;
    }

    public void setIntOpsShipReadinessDate(Date intOpsShipReadinessDate) {
        this.intOpsShipReadinessDate = intOpsShipReadinessDate;
    }

    public void setMFGCommitDate(Date MFGCommitDate) {
        this.MFGCommitDate = MFGCommitDate;
    }

    public void setShipRecogDate(Date shipRecogDate) {
        this.shipRecogDate = shipRecogDate;
    }

    public void setHandOffDateToDE(Date handOffDateToDE) {
        this.handOffDateToDE = handOffDateToDE;
    }

    public void setHandOffDateBackToMFG(Date handOffDateBackToMFG) {
        this.handOffDateBackToMFG = handOffDateBackToMFG;
    }

    public void setInstallStartDate(Date installStartDate) {
        this.installStartDate = installStartDate;
    }

    public void setCycleTimeDays(Integer cycleTimeDays) {
        this.cycleTimeDays = cycleTimeDays;
    }

    public void setSlotPlanNote(String slotPlanNote) {
        this.slotPlanNote = slotPlanNote;
    }

    public void setCommentFor$Change(String commentFor$Change) {
        this.commentFor$Change = commentFor$Change;
    }

    public void setConfigurationNote(String configurationNote) {
        this.configurationNote = configurationNote;
    }

    public void setDropShip(String dropShip) {
        this.dropShip = dropShip;
    }

    public void setMFGStatus(String MFGStatus) {
        this.MFGStatus = MFGStatus;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setRMATool(Integer RMATool) {
        this.RMATool = RMATool;
    }

    public void setNew_Used(String new_Used) {
        this.new_Used = new_Used;
    }

    public void setCoreNeedDate(Date coreNeedDate) {
        this.coreNeedDate = coreNeedDate;
    }

    public void setCoreArrivalDate(Date coreArrivalDate) {
        this.coreArrivalDate = coreArrivalDate;
    }

    public void setRefurbStartDate(Date refurbStartDate) {
        this.refurbStartDate = refurbStartDate;
    }

    public void setRefurbCompleteDate(Date refurbCompleteDate) {
        this.refurbCompleteDate = refurbCompleteDate;
    }

    public void setDonorStatus(String donorStatus) {
        this.donorStatus = donorStatus;
    }

    public void setCoreUTID(String coreUTID) {
        this.coreUTID = coreUTID;
    }

    public void setCoreNotes(String coreNotes) {
        this.coreNotes = coreNotes;
    }

    public void setFabID(String fabID) {
        this.fabID = fabID;
    }

    public void setFlex01(String flex01) {
        this.flex01 = flex01;
    }

    public void setFlex02(String flex02) {
        this.flex02 = flex02;
    }

    public void setFlex03(String flex03) {
        this.flex03 = flex03;
    }

    public void setFlex04(String flex04) {
        this.flex04 = flex04;
    }

    public void setToolStartDate(Date toolStartDate) {
        this.toolStartDate = toolStartDate;
        MRPDate = DateUtils.addDays(toolStartDate, cycleTimeDays);
        gapDays = (int) ((MFGCommitDate.getTime() - MRPDate.getTime())/ (24 * 60 * 60 * 1000));
        
        Integer MRPYear = MRPDate.getYear() - 100;
        Integer MRPMonth = MRPDate.getMonth();
        String MRPQuarter;
        if (MRPMonth < 3){
            MRPQuarter = "Q1";
        } else if (MRPMonth < 6){
            MRPQuarter = "Q2";
        } else if (MRPMonth < 9){
            MRPQuarter = "Q3";
        } else {
            MRPQuarter = "Q4";
        }

        String buildQtr = "CY" + MRPYear.toString() + MRPQuarter;
        
        if (MRPDate.after(leaveBayDate)){
            leaveBayDate = MRPDate;
        }
    }

    public void setLeaveBayDate(Date leaveBayDate) {
        this.leaveBayDate = leaveBayDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setAssignedBayID(Integer bayID){
        this.assignedBayID = bayID;
    }
}