package main.java.authentication.json;

import java.util.*;
import java.text.*;

public class GetMassSlotUploadResult implements JsonObject{
    private String msu_id;
    private String argo_id;
    private String slot_id;
    private String slot_status;
    private String ship_rev_type;
    private String build_category;
    private String build_product;
    private String slot_plan_notes;
    private String plan_product_type;
    private String ship_risk;
    private String ship_risk_reason;
    private String comment_for_change;
    private String commited_ship;
    private String secondary_customer_name;
    private String fab_id;
    private String sales_order;
    private String forecast_id;
    private String mfg_commit_date;
    private String ship_recognition_date;
    private String mrp_date;
    private String build_complete;
    private String int_ops_ship_ready_date;
    private String plant;
    private String category;
    private String core_need_date;
    private String core_arrival_date;
    private String refurb_start_date;
    private String refurb_complete_date;
    private String donor_status;
    private String core_utid;
    private String core_notes;
    private String mfg_status;
    private String qty;
    private String config_note;
    private String drop_ship;
    private String rma;
    private String product_pn;
    private String off_date_to_de;
    private String off_date_to_mfg;
    private String install_start_date;
    private String cycle_time_days;
    private String flex;
    private String fulfilled;

    public GetMassSlotUploadResult(String msu_id, String argo_id, String slot_id, String slot_status, String ship_rev_type, String build_category, String build_product, String slot_plan_notes, String plan_product_type, String ship_risk, String ship_risk_reason, String comment_for_change, String commited_ship, String secondary_customer_name, String fab_id, String sales_order, String forecast_id, String mfg_commit_date, String ship_recognition_date, String mrp_date, String build_complete, String int_ops_ship_ready_date, String plant, String category, String core_need_date, String core_arrival_date, String refurb_start_date, String refurb_complete_date, String donor_status, String core_utid, String core_notes, String mfg_status, String qty, String config_note, String drop_ship, String rma, String product_pn, String off_date_to_de, String off_date_to_mfg, String install_start_date, String cycle_time_days, String flex, String fulfilled) {
        this.msu_id = msu_id;
        this.argo_id = argo_id;
        this.slot_id = slot_id;
        this.slot_status = slot_status;
        this.ship_rev_type = ship_rev_type;
        this.build_category = build_category;
        this.build_product = build_product;
        this.slot_plan_notes = slot_plan_notes;
        this.plan_product_type = plan_product_type;
        this.ship_risk = ship_risk;
        this.ship_risk_reason = ship_risk_reason;
        this.comment_for_change = comment_for_change;
        this.commited_ship = commited_ship;
        this.secondary_customer_name = secondary_customer_name;
        this.fab_id = fab_id;
        this.sales_order = sales_order;
        this.forecast_id = forecast_id;
        this.mfg_commit_date = mfg_commit_date;
        this.ship_recognition_date = ship_recognition_date;
        this.mrp_date = mrp_date;
        this.build_complete = build_complete;
        this.int_ops_ship_ready_date = int_ops_ship_ready_date;
        this.plant = plant;
        this.category = category;
        this.core_need_date = core_need_date;
        this.core_arrival_date = core_arrival_date;
        this.refurb_start_date = refurb_start_date;
        this.refurb_complete_date = refurb_complete_date;
        this.donor_status = donor_status;
        this.core_utid = core_utid;
        this.core_notes = core_notes;
        this.mfg_status = mfg_status;
        this.qty = qty;
        this.config_note = config_note;
        this.drop_ship = drop_ship;
        this.rma = rma;
        this.product_pn = product_pn;
        this.off_date_to_de = off_date_to_de;
        this.off_date_to_mfg = off_date_to_mfg;
        this.install_start_date = install_start_date;
        this.cycle_time_days = cycle_time_days;
        this.flex = flex;
        this.fulfilled = fulfilled;
    }

    public String getMsu_id() {
        return msu_id;
    }

    public String getArgo_id() {
        return argo_id;
    }

    public String getSlot_id() {
        return slot_id;
    }

    public String getSlot_status() {
        return slot_status;
    }

    public String getShip_rev_type() {
        return ship_rev_type;
    }

    public String getBuild_category() {
        return build_category;
    }

    public String getBuild_product() {
        return build_product;
    }

    public String getSlot_plan_notes() {
        return slot_plan_notes;
    }

    public String getPlan_product_type() {
        return plan_product_type;
    }

    public String getShip_risk() {
        return ship_risk;
    }

    public String getShip_risk_reason() {
        return ship_risk_reason;
    }

    public String getComment_for_change() {
        return comment_for_change;
    }

    public String getCommited_ship() {
        return commited_ship;
    }

    public String getSecondary_customer_name() {
        return secondary_customer_name;
    }

    public String getFab_id() {
        return fab_id;
    }

    public String getSales_order() {
        return sales_order;
    }

    public String getForecast_id() {
        return forecast_id;
    }

    public String getMfg_commit_date() {
        return mfg_commit_date;
    }

    public String getShip_recognition_date() {
        return ship_recognition_date;
    }

    public String getMrp_date() {
        return mrp_date;
    }

    public String getBuild_complete() {
        return build_complete;
    }

    public String getInt_ops_ship_ready_date() {
        return int_ops_ship_ready_date;
    }

    public String getPlant() {
        return plant;
    }

    public String getCategory() {
        return category;
    }

    public String getCore_need_date() {
        return core_need_date;
    }

    public String getCore_arrival_date() {
        return core_arrival_date;
    }

    public String getRefurb_start_date() {
        return refurb_start_date;
    }

    public String getRefurb_complete_date() {
        return refurb_complete_date;
    }

    public String getDonor_status() {
        return donor_status;
    }

    public String getCore_utid() {
        return core_utid;
    }

    public String getCore_notes() {
        return core_notes;
    }

    public String getMfg_status() {
        return mfg_status;
    }

    public String getQty() {
        return qty;
    }

    public String getConfig_note() {
        return config_note;
    }

    public String getDrop_ship() {
        return drop_ship;
    }

    public String getRma() {
        return rma;
    }

    public String getProduct_pn() {
        return product_pn;
    }

    public String getOff_date_to_de() {
        return off_date_to_de;
    }

    public String getOff_date_to_mfg() {
        return off_date_to_mfg;
    }

    public String getInstall_start_date() {
        return install_start_date;
    }

    public String getCycle_time_days() {
        return cycle_time_days;
    }

    public String getFlex() {
        return flex;
    }

    public String getFulfilled() {
        return fulfilled;
    }
}