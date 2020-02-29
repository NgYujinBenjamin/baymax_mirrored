package authentication.json;

public class HistoryDetails implements JsonObject {

    private String msuId;
    private String staffId;
    private String dateGenerated;

    public HistoryDetails(String msuId, String staffId, String dateGenerated) {
        this.msuId = msuId;
        this.staffId = staffId;
        this.dateGenerated = dateGenerated;
    }

    public String getMsuId() {
        return msuId;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getDateGenerated() {
        return dateGenerated;
    }
}