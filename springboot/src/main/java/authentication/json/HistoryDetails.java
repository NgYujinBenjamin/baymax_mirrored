package main.java.authentication.json;

import java.util.*;
import java.text.*;

public class HistoryDetails implements JsonObject {

    private String msuId;
    private String date_generated;

    public HistoryDetails(String msuId, String date_generated) {
        this.msuId = msuId;
        this.date_generated = date_generated;
    }

    public String getMsuId() {
        return msuId;
    }

    public String getDate_generated() {
        return date_generated;
    }
}