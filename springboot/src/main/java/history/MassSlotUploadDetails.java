package main.java.history;

import main.java.algorithm.ExclusionStrategy.*;

import java.util.*;
import java.text.*;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.validator.GenericValidator;

import com.google.gson.Gson;

public class MassSlotUploadDetails implements Comparable<MassSlotUploadDetails>{
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
    private Integer CommittedShip;
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

    public MassSlotUploadDetails (Object productRaw){
        HashMap<String, Object> productDetails = null;
        
        if (productRaw instanceof Map){
            productDetails = (HashMap<String, Object>) productRaw;
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
        CommittedShip = (Integer) productDetails.get("committedShip$");
        shipRisk_Upside = (String) productDetails.get("shipRisk_Upside");
        shipRiskReason = (String) productDetails.get("shipRiskReason");
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
        lockMRPDate = (Boolean) productDetails.get("lockMRPDate");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            MRPDate = GenericValidator.isDate((String) productDetails.get("MRPDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("MRPDate")): null;
            intOpsShipReadinessDate = GenericValidator.isDate((String) productDetails.get("intOpsShipReadinessDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("intOpsShipReadinessDate")): null;
            MFGCommitDate = GenericValidator.isDate((String) productDetails.get("MFGCommitDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("MFGCommitDate")): null;
            shipRecogDate = GenericValidator.isDate((String) productDetails.get("shipRecogDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("shipRecogDate")): null;
            handOffDateToDE = GenericValidator.isDate((String) productDetails.get("handOffDateToDE"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("handOffDateToDE")): null;
            handOffDateBackToMFG = GenericValidator.isDate((String) productDetails.get("handOffDateBackToMFG"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("handOffDateBackToMFG")): null;
            installStartDate = GenericValidator.isDate((String) productDetails.get("installStartDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String)productDetails.get("installStartDate")): null;
            coreNeedDate = GenericValidator.isDate((String) productDetails.get("coreNeedDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("coreNeedDate")): null;
            coreArrivalDate = GenericValidator.isDate((String) productDetails.get("coreArrivalDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("coreArrivalDate")): null;
            refurbStartDate = GenericValidator.isDate((String) productDetails.get("refurbStartDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("refurbStartDate")): null;
            refurbCompleteDate = GenericValidator.isDate((String) productDetails.get("refurbCompleteDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("refurbCompleteDate")): null;
            endDate = GenericValidator.isDate((String)productDetails.get("endDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("endDate")): MFGCommitDate;
            sendToStorageDate = GenericValidator.isDate((String) productDetails.get("sendToStorageDate"), "dd/MM/yyyy", true) ? dateFormat.parse((String) productDetails.get("sendToStorageDate")): null;
        } catch (ParseException e){
            e.printStackTrace();

        }        

        // latestToolStartDate = DateUtils.addDays(endDate == null? "" : endDate, -cycleTimeDays);
        // toolStartDate = latestToolStartDate;

        if (sendToStorageDate != null){
            leaveBayDate = sendToStorageDate;
        }
        else if (fabName != null && fabName.equals("OPEN")){
            leaveBayDate = intOpsShipReadinessDate;
        } else {
            leaveBayDate = endDate;
        }

        //gapDays = (int) ((MFGCommitDate.getTime() - MRPDate.getTime())/ (24 * 60 * 60 * 1000)); 
    }

    public int compareTo(MassSlotUploadDetails other){
        Date thisToolStart = toolStartDate;
        Integer thisCommittedShip = CommittedShip;
        Date otherToolStart = other.toolStartDate;
        Integer otherCommittedShip = other.CommittedShip;

        if (thisToolStart.compareTo(otherToolStart) == 0){
            return - thisCommittedShip.compareTo(otherCommittedShip);
        }
        return thisToolStart.compareTo(otherToolStart);
    }

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public Integer getArgo_id() {
        return argoID;
    }

    public Integer getPlant() {
        return plant;
    }

    public Integer getBuild_complete() {
        return buildComplete;
    }

    public String getSlot_status() {
        return slotStatus;
    }

    public String getPlan_product_type() {
        return planProductType;
    }

    public String getBuild_category() {
        return buildCategory;
    }

    public String getShip_rev_type() {
        return shipRevenueType;
    }

    public Integer getSales_order() {
        return salesOrder;
    }

    public String getForecast_id() {
        return forecastID;
    }

    public String getSlot_id() {
        return slotID_UTID;
    }

    // missing in db table
    public String getFabName() {
        return fabName;
    }

    public String getSecondary_customer_name() {
        return secondaryCustomerName;
    }

    public String getBuild_product() {
        return buildProduct;
    }

    public String getProduct_pn() {
        return productPN;
    }

    public Integer getCommited_ship() {
        return CommittedShip;
    }

    public String getShip_risk() {
        return shipRisk_Upside;
    }

    public String getShip_risk_reason() {
        return shipRiskReason;
    }

    public String getMrp_date() {
        return MRPDate != null ? formatter.format(MRPDate) : null;
        // return MRPDate;
    }

    public String getInt_ops_ship_ready_date() {
        return intOpsShipReadinessDate != null ? formatter.format(intOpsShipReadinessDate) : null;
        // return intOpsShipReadinessDate;
    }

    public String getMfg_commit_date() {
        return MFGCommitDate != null ? formatter.format(MFGCommitDate) : null;
        // return MFGCommitDate;
    }

    public String getShip_recognition_date() {
        return shipRecogDate != null ? formatter.format(shipRecogDate) : null;
        // return shipRecogDate;
    }

    public String getOff_date_to_de() {
        return handOffDateToDE != null ? formatter.format(handOffDateToDE) : null;
        // return handOffDateToDE;
    }

    public String getOff_date_to_mfg() {
        return handOffDateBackToMFG != null ? formatter.format(handOffDateBackToMFG) : null;
        // return handOffDateBackToMFG;
    }

    public String getInstall_start_date() {
        return installStartDate != null ? formatter.format(installStartDate) : null;
        // return installStartDate;
    }

    public Integer getCycle_time_days() {
        return cycleTimeDays;
    }

    public String getSlot_plan_notes() {
        return slotPlanNote;
    }

    public String getComment_for_change() {
        return commentFor$Change;
    }

    public String getConfig_note() {
        return configurationNote;
    }

    public String getDrop_ship() {
        return dropShip;
    }

    public String getMfg_status() {
        return MFGStatus;
    }

    public Integer getQty() {
        return quantity;
    }

    public Integer getRma() {
        return RMATool;
    }

    public String getCategory() {
        return new_Used;
    }

    public String getCore_need_date() {
        return coreNeedDate != null ? formatter.format(coreNeedDate) : null;
        // return coreNeedDate;
    }

    public String getCore_arrival_date() {
        return coreArrivalDate != null ? formatter.format(coreArrivalDate) : null;
        // return coreArrivalDate;
    }

    public String getRefurb_start_date() {
        return refurbStartDate != null ? formatter.format(refurbStartDate) : null;
        // return refurbStartDate;
    }

    public String getRefurb_complete_date() {
        return refurbCompleteDate != null ? formatter.format(refurbCompleteDate) : null;
        // return refurbCompleteDate;
    }

    public String getSendToStorage() {
        return sendToStorageDate != null ? formatter.format(sendToStorageDate) : null;
        // return refurbCompleteDate;
    }

    public String getDonor_status() {
        return donorStatus;
    }

    public String getCore_utid() {
        return coreUTID;
    }

    public String getCore_notes() {
        return coreNotes;
    }

    public String getFab_id() {
        return fabID;
    }

    //flex 1-4 missing in db table
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

    public String getToolStartDate() {
        return toolStartDate != null ? formatter.format(toolStartDate) : null;
        // return toolStartDate;
    }

    public String getLatestToolStartDate() {
        return latestToolStartDate != null ? formatter.format(latestToolStartDate) : null;
        // return latestToolStartDate;
    }

    public String getEndDate() {
        return endDate != null ? formatter.format(endDate) : null;
        // return endDate;
    }

    public String getLeaveBayDate() {
        return leaveBayDate != null ? formatter.format(leaveBayDate) : null;
        // return leaveBayDate;
    }

    public Integer getGapDays() {
        return gapDays;
    }
    
    public Integer getAssignedBayID(){
        return assignedBayID;
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

    public void setCommittedShip(Integer CommittedShip) {
        this.CommittedShip = CommittedShip;
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