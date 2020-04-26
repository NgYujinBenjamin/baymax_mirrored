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

public class Product implements Comparable<Product> {
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
    private String fabName;
    private String secondaryCustomerName;
    private String buildProduct;
    private String productPN;
    private Integer committedShip$;
    private String shipRisk_Upside;
    private String shipRiskReason;
    private Date MRPDate;
    private Date intOpsShipReadinessDate;
    private Date MFGCommitDate;
    private Date shipRecogDate;
    private Date handOffDateToDE;
    private Date handOffDateBackToMFG;
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
    private String flex01;
    private String flex02;
    private String flex03;
    private String flex04;
    private String buildQtr;
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
     *
     * @param rowData
     */
    public Product(Map<String, Object> rowData) throws RuntimeException {
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
        fabName = (String) rowData.get("Fab Name");
        secondaryCustomerName = (String) rowData.get("Secondary Customer Name");
        buildProduct = (String) rowData.get("Build Product");
        productPN = (String) rowData.get("Product PN");
        try {
            committedShip$ = (Integer) rowData.get("Committed Ship $");
        } catch (Exception e) {
            throw new RuntimeException("Please ensure that Committed Ship $ is in numerical format, and is a whole number");
        }


        if (committedShip$ == null) {
            throw new RuntimeException("Please fill in all values for the column Committed Ship $");
        }

        shipRisk_Upside = (String) rowData.get("Ship Risk/Upside");
        shipRiskReason = (String) rowData.get("Ship Risk Reason");
        try {
            cycleTimeDays = (Integer) rowData.get("Cycle Time Days");
        } catch (Exception e) {
            throw new RuntimeException("Please ensure that Cycle Time is in numerical format, and is a whole number");
        }

        if (cycleTimeDays == null) {
            throw new RuntimeException("Please ensure that all Cycle Time is filled in");
        }

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

        if (buildQtr == null) {
            throw new RuntimeException("Please ensure that there are no empty Build Qtrs");
        }

        lockMRPDate = (Boolean) rowData.get("Lock MRP Date");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            MRPDate = GenericValidator.isDate((String) rowData.get("MRP Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("MRP Date")) : null;
            intOpsShipReadinessDate = GenericValidator.isDate((String) rowData.get("Int. Ops Ship Readiness Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Int. Ops Ship Readiness Date")) : null;
            MFGCommitDate = GenericValidator.isDate((String) rowData.get("MFG Commit Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("MFG Commit Date")) : null;
            shipRecogDate = GenericValidator.isDate((String) rowData.get("Ship Recog Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Ship Recog Date")) : null;
            handOffDateToDE = GenericValidator.isDate((String) rowData.get("Hand Off Date To DE"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Hand Off Date To DE")) : null;
            handOffDateBackToMFG = GenericValidator.isDate((String) rowData.get("Hand Off Date Back To MFG"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Hand Off Date Back To MFG")) : null;
            installStartDate = GenericValidator.isDate((String) rowData.get("Install Start Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Install Start Date")) : null;
            coreNeedDate = GenericValidator.isDate((String) rowData.get("Core Need Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Core Need Date")) : null;
            coreArrivalDate = GenericValidator.isDate((String) rowData.get("Core Arrival Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Core Arrival Date")) : null;
            refurbStartDate = GenericValidator.isDate((String) rowData.get("Refurb Start Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Refurb Start Date")) : null;
            refurbCompleteDate = GenericValidator.isDate((String) rowData.get("Refurb Complete Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Refurb Complete Date")) : null;
            endDate = GenericValidator.isDate((String) rowData.get("End Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("End Date")) : MFGCommitDate;
            sendToStorageDate = GenericValidator.isDate((String) rowData.get("Send To Storage Date"), "dd/MM/yyyy", true) ? dateFormat.parse((String) rowData.get("Send To Storage Date")) : null;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (MRPDate == null) {
            throw new RuntimeException("Please ensure that MRP Date is not empty, and in dd/mm/yyyy format");
        }

        if (MFGCommitDate == null) {
            throw new RuntimeException("Please ensure that MFG Commit Date is not empty, and in dd/mm/yyyy format");
        }

        latestToolStartDate = DateUtils.addDays(endDate, -cycleTimeDays);
        toolStartDate = DateUtils.addDays(MRPDate, -cycleTimeDays);

        if (sendToStorageDate != null) {
            leaveBayDate = sendToStorageDate;
        } else if (fabName != null && fabName.equals("OPEN")) {
            if (intOpsShipReadinessDate == null) {
                throw new RuntimeException("For OPEN products, please fill in the Internal Ops Ship Readiness Date");
            }
            leaveBayDate = intOpsShipReadinessDate;
        } else {
            leaveBayDate = endDate;
        }

        gapDays = (int) ((MFGCommitDate.getTime() - MRPDate.getTime()) / (24 * 60 * 60 * 1000));
    }


    /**
     * Constructor for Product class (used for /subseqScheduling). Extracts the relevant attributes from object passed and creates a new Product object with the attributes.
     * Relevant attributes are defined as the attributes required for output generation.
     * Attribute values are initialized to null if it is not found in rowData.
     * Date values passed in have to be in the format of "dd/MM/yyyy"
     *
     * @param productRaw Object, that is an instanceOf Map, containing the product details in key-value pairs
     * @throws RuntimeException if there is problem casting the object into type HashMap
     */
    public Product(Object productRaw) throws RuntimeException {
        HashMap<String, Object> productDetails = null;

        if (productRaw instanceof Map) {
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
        fabName = (String) productDetails.get("fabName");
        secondaryCustomerName = (String) productDetails.get("secondaryCustomerName");
        buildProduct = (String) productDetails.get("buildProduct");
        productPN = (String) productDetails.get("productPN");
        committedShip$ = (Integer) productDetails.get("committedShip$");
        shipRisk_Upside = (String) productDetails.get("shipRisk_Upside");
        shipRiskReason = (String) productDetails.get("shipRiskReason");
        try {
            cycleTimeDays = (Integer) productDetails.get("cycleTimeDays");
        } catch (Exception e) {
            throw new RuntimeException("Please ensure that Cycle Time is not empty and in a numerical format");
        }

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
        lockMRPDate = (Boolean) productDetails.get("lockMRPDate");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            MRPDate = GenericValidator.isDate((String) productDetails.get("MRPDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("MRPDate")) : null;
            intOpsShipReadinessDate = GenericValidator.isDate((String) productDetails.get("intOpsShipReadinessDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("intOpsShipReadinessDate")) : null;
            MFGCommitDate = GenericValidator.isDate((String) productDetails.get("MFGCommitDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("MFGCommitDate")) : null;
            shipRecogDate = GenericValidator.isDate((String) productDetails.get("shipRecogDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("shipRecogDate")) : null;
            handOffDateToDE = GenericValidator.isDate((String) productDetails.get("handOffDateToDE"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("handOffDateToDE")) : null;
            handOffDateBackToMFG = GenericValidator.isDate((String) productDetails.get("handOffDateBackToMFG"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("handOffDateBackToMFG")) : null;
            installStartDate = GenericValidator.isDate((String) productDetails.get("installStartDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("installStartDate")) : null;
            coreNeedDate = GenericValidator.isDate((String) productDetails.get("coreNeedDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("coreNeedDate")) : null;
            coreArrivalDate = GenericValidator.isDate((String) productDetails.get("coreArrivalDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("coreArrivalDate")) : null;
            refurbStartDate = GenericValidator.isDate((String) productDetails.get("refurbStartDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("refurbStartDate")) : null;
            refurbCompleteDate = GenericValidator.isDate((String) productDetails.get("refurbCompleteDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("refurbCompleteDate")) : null;
            endDate = GenericValidator.isDate((String) productDetails.get("endDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("endDate")) : MFGCommitDate;
            sendToStorageDate = GenericValidator.isDate((String) productDetails.get("sendToStorageDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("sendToStorageDate")) : null;
            toolStartDate = GenericValidator.isDate((String) productDetails.get("toolStartDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("toolStartDate")) : null;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (toolStartDate == null) {
            toolStartDate = DateUtils.addDays(MRPDate, -cycleTimeDays);
        }

        if (MRPDate == null) {
            throw new RuntimeException("Please ensure that MRP Date is not empty, and in dd/mm/yyyy format");
        }

        if (lockMRPDate != null && lockMRPDate) {
            Date originalMRPDate = DateUtils.addDays(toolStartDate, cycleTimeDays);
            if (MRPDate.before(originalMRPDate)) {
                throw new RuntimeException("The locked MRP date of a product (Slot ID: " + slotID_UTID + ") cannot be brought forward earlier than the recommended MRP date of " + originalMRPDate);
            }
            latestToolStartDate = toolStartDate;
        } else {
            latestToolStartDate = DateUtils.addDays(endDate, -cycleTimeDays);
        }

        if (sendToStorageDate != null) {
            Date originalMRPDate = DateUtils.addDays(toolStartDate, cycleTimeDays);

            if (sendToStorageDate.before(MRPDate)) {
                throw new RuntimeException("The product with Slot ID: " + slotID_UTID + " cannot be sent to storage before the earliest MRP date of " + originalMRPDate);
            }
            leaveBayDate = sendToStorageDate;
        } else if (fabName != null && fabName.equals("OPEN")) {
            leaveBayDate = intOpsShipReadinessDate;
        } else {
            leaveBayDate = endDate;
        }

        gapDays = (int) ((MFGCommitDate.getTime() - MRPDate.getTime()) / (24 * 60 * 60 * 1000));
    }


    /**
     * Compares 2 products based on toolStart and revenue so as to determine scheduling priority
     *
     * @param anotherProduct Another product that is to be compared with
     * @return Value of 0 if the Products have the same latestToolStart date and committedShip$; A value less than 0 if this Product has a earlier toolStartDate or larger revenue than that of the other Product; A value more than 0 if this Product has a later toolStartDate or smaller revenue than that of the other Product.
     */
    public int compareTo(Product anotherProduct) {
        Date thisLatestToolStart = latestToolStartDate;
        Integer thisCommittedShip$ = committedShip$;
        Date otherLatestToolStart = anotherProduct.latestToolStartDate;
        Integer otherCommittedShip$ = anotherProduct.committedShip$;

        if (thisLatestToolStart.compareTo(otherLatestToolStart) == 0) {
            return -thisCommittedShip$.compareTo(otherCommittedShip$);
        }
        return thisLatestToolStart.compareTo(otherLatestToolStart);
    }

    /**
     * Returns the ArgoID of the Product
     *
     * @return ArgoID of the Product
     */
    public Integer getArgoID() {
        return argoID;
    }

    /**
     * Returns the Plant of the Product
     *
     * @return Plant of the Product
     */
    public Integer getPlant() {
        return plant;
    }

    /**
     * Returns the Build Complete of the Product
     *
     * @return Build Complete of the Product
     */
    public Integer getBuildComplete() {
        return buildComplete;
    }

    /**
     * Returns the Slot Status of the Product
     *
     * @return Slot Status of the Product
     */
    public String getSlotStatus() {
        return slotStatus;
    }

    /**
     * Returns the Plan Product Type of the Product
     *
     * @return Plan Product Type of the Product
     */
    public String getPlanProductType() {
        return planProductType;
    }

    /**
     * Returns the Build Category of the Product
     *
     * @return Build Category of the Product
     */
    public String getBuildCategory() {
        return buildCategory;
    }

    /**
     * Returns the Ship Revenue Type of the Product
     *
     * @return Ship Revenue Type of the Product
     */
    public String getShipRevenueType() {
        return shipRevenueType;
    }

    /**
     * Returns the Sales Order of the Product
     *
     * @return Sales Order of the Product
     */
    public Integer getSalesOrder() {
        return salesOrder;
    }

    /**
     * Returns the Forecast ID of the Product
     *
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

    public Integer getAssignedBayID() {
        return assignedBayID;
    }

    public Boolean getLockMRPDate() {
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
        this.MRPDate = DateUtils.addDays(toolStartDate, cycleTimeDays);

        if (this.fabName != null && this.fabName.equals("OPEN")) {
            this.gapDays = (int) ((intOpsShipReadinessDate.getTime() - MRPDate.getTime()) / (24 * 60 * 60 * 1000));
        } else {
            this.gapDays = (int) ((MFGCommitDate.getTime() - MRPDate.getTime()) / (24 * 60 * 60 * 1000));
        }


        Integer MRPYear = MRPDate.getYear() - 100;
        Integer MRPMonth = MRPDate.getMonth();
        String MRPQuarter;

        if (MRPMonth < 3) {
            MRPQuarter = "Q1";
        } else if (MRPMonth < 6) {
            MRPQuarter = "Q2";
        } else if (MRPMonth < 9) {
            MRPQuarter = "Q3";
        } else {
            MRPQuarter = "Q4";
        }

        this.buildQtr = "CY" + MRPYear.toString() + MRPQuarter;

        if (MRPDate.after(leaveBayDate)) {
            leaveBayDate = MRPDate;
        }
    }

    public void setLeaveBayDate(Date leaveBayDate) {
        this.leaveBayDate = leaveBayDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setAssignedBayID(Integer bayID) {
        this.assignedBayID = bayID;
    }

    @Override
    public String toString() {
        return "Product{" +
                "argoID=" + argoID +
                ", plant=" + plant +
                ", buildComplete=" + buildComplete +
                ", slotStatus='" + slotStatus + '\'' +
                ", planProductType='" + planProductType + '\'' +
                ", buildCategory='" + buildCategory + '\'' +
                ", shipRevenueType='" + shipRevenueType + '\'' +
                ", salesOrder=" + salesOrder +
                ", forecastID='" + forecastID + '\'' +
                ", slotID_UTID='" + slotID_UTID + '\'' +
                ", fabName='" + fabName + '\'' +
                ", secondaryCustomerName='" + secondaryCustomerName + '\'' +
                ", buildProduct='" + buildProduct + '\'' +
                ", productPN='" + productPN + '\'' +
                ", committedShip$=" + committedShip$ +
                ", shipRisk_Upside='" + shipRisk_Upside + '\'' +
                ", shipRiskReason='" + shipRiskReason + '\'' +
                ", MRPDate=" + MRPDate +
                ", intOpsShipReadinessDate=" + intOpsShipReadinessDate +
                ", MFGCommitDate=" + MFGCommitDate +
                ", shipRecogDate=" + shipRecogDate +
                ", handOffDateToDE=" + handOffDateToDE +
                ", handOffDateBackToMFG=" + handOffDateBackToMFG +
                ", installStartDate=" + installStartDate +
                ", cycleTimeDays=" + cycleTimeDays +
                ", slotPlanNote='" + slotPlanNote + '\'' +
                ", commentFor$Change='" + commentFor$Change + '\'' +
                ", configurationNote='" + configurationNote + '\'' +
                ", dropShip='" + dropShip + '\'' +
                ", MFGStatus='" + MFGStatus + '\'' +
                ", quantity=" + quantity +
                ", RMATool=" + RMATool +
                ", new_Used='" + new_Used + '\'' +
                ", coreNeedDate=" + coreNeedDate +
                ", coreArrivalDate=" + coreArrivalDate +
                ", refurbStartDate=" + refurbStartDate +
                ", refurbCompleteDate=" + refurbCompleteDate +
                ", donorStatus='" + donorStatus + '\'' +
                ", coreUTID='" + coreUTID + '\'' +
                ", coreNotes='" + coreNotes + '\'' +
                ", fabID='" + fabID + '\'' +
                ", flex01='" + flex01 + '\'' +
                ", flex02='" + flex02 + '\'' +
                ", flex03='" + flex03 + '\'' +
                ", flex04='" + flex04 + '\'' +
                ", buildQtr='" + buildQtr + '\'' +
                ", latestToolStartDate=" + latestToolStartDate +
                ", toolStartDate=" + toolStartDate +
                ", endDate=" + endDate +
                ", leaveBayDate=" + leaveBayDate +
                ", gapDays=" + gapDays +
                ", lockMRPDate=" + lockMRPDate +
                ", sendToStorageDate=" + sendToStorageDate +
                ", assignedBayID=" + assignedBayID +
                '}';
    }

    /**
     * checks whether the str parameter contains the string "null"
     *
     * @param str
     * @return null or the str parameter itself
     */
    public String convertNullStringToNull(String str) {
        if (str == null || str.length() == 0 || str.contains("null")) {
            return null;
        }

        return str;
    }


    /**
     * For every String variable
     * Convert all "null" values into null
     */
    public void updateStringFields() {

        this.slotStatus = convertNullStringToNull(this.slotStatus);
        this.planProductType=convertNullStringToNull(this.planProductType);
        this.buildCategory=convertNullStringToNull(this.buildCategory);
        this.shipRevenueType=convertNullStringToNull(this.shipRevenueType);
        this.forecastID=convertNullStringToNull(this.forecastID);
        this.slotID_UTID=convertNullStringToNull(this.slotID_UTID);
        this.fabName=convertNullStringToNull(this.fabName);
        this.secondaryCustomerName=convertNullStringToNull(this.secondaryCustomerName);
        this.buildProduct=convertNullStringToNull(this.buildProduct);
        this.productPN=convertNullStringToNull(this.productPN);
        this.shipRisk_Upside=convertNullStringToNull(this.shipRisk_Upside);
        this.shipRiskReason=convertNullStringToNull(this.shipRiskReason);
        this.slotPlanNote=convertNullStringToNull(this.slotPlanNote);
        this.commentFor$Change=convertNullStringToNull(this.commentFor$Change);
        this.configurationNote=convertNullStringToNull(this.configurationNote);
        this.dropShip=convertNullStringToNull(this.dropShip);
        this.MFGStatus=convertNullStringToNull(this.MFGStatus);
        this.new_Used=convertNullStringToNull(this.new_Used);
        this.donorStatus=convertNullStringToNull(this.donorStatus);
        this.coreUTID=convertNullStringToNull(this.coreUTID);
        this.coreNotes=convertNullStringToNull(this.coreNotes);
        this.fabID=convertNullStringToNull(this.fabID);
        this.flex01=convertNullStringToNull(this.flex01);
        this.flex02=convertNullStringToNull(this.flex02);
        this.flex03=convertNullStringToNull(this.flex03);
        this.flex04=convertNullStringToNull(this.flex04);
        this.buildQtr=convertNullStringToNull(this.buildQtr);
    }
}