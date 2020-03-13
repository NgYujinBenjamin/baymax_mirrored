package main.java.authentication.json;

import java.util.*;
import java.text.*;

public class HistoryDetails implements JsonObject {

    private String histID;
    private String dateGenerated;

    public HistoryDetails(String histID, String dateGenerated) {
        this.histID = histID;
        this.dateGenerated = dateGenerated;
    }

    public String getHistID() {
        return histID;
    }

    public String getDateGenerated() {
        return dateGenerated;
    }
}