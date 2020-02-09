package main.java.authentication.json;

import java.util.*;
import java.text.*;

public class GetFacilityUtilResult implements JsonObject {
    private String faci_id;
    private String staff_id;
    private String slot_id_utid;
    private String sales_order;
    private String customer;
    private String configuration;
    private String model;
    private String tool_start;
    private String mfg_commit_ship_date;
    private String bay;
    private String week_of_friday;

    public GetFacilityUtilResult(String faci_id, String staff_id, String slot_id_utid, String sales_order, String customer, String configuration, String model, String tool_start, String mfg_commit_ship_date, String bay, String week_of_friday) {
        this.faci_id = faci_id;
        this.staff_id = staff_id;
        this.slot_id_utid = slot_id_utid;
        this.sales_order = sales_order;
        this.customer = customer;
        this.configuration = configuration;
        this.model = model;
        this.tool_start = tool_start;
        this.mfg_commit_ship_date = mfg_commit_ship_date;
        this.bay = bay;
        this.week_of_friday = week_of_friday;
    }

    public String getFaci_id() {
        return faci_id;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public String getSlot_id_utid() {
        return slot_id_utid;
    }

    public String getSales_order() {
        return sales_order;
    }

    public String getCustomer() {
        return customer;
    }

    public String getConfiguration() {
        return configuration;
    }

    public String getModel() {
        return model;
    }

    public String getTool_start() {
        return tool_start;
    }

    public String getMfg_commit_ship_date() {
        return mfg_commit_ship_date;
    }

    public String getBay() {
        return bay;
    }

    public String getWeek_of_friday() {
        return week_of_friday;
    }
}