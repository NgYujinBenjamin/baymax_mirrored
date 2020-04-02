package main.java.connection;

import main.java.authentication.json.HistoryDetails;
import main.java.authentication.json.JsonObject;
import main.java.authentication.json.users.RegistrationDetails;
import main.java.authentication.json.users.User;
import main.java.authentication.json.users.UserCredentials;
import main.java.history.MassSlotUploadDetails;
import main.java.authentication.json.Baseline;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class historycon extends main.java.connection.mysqlcon {

    public ArrayList<JsonObject> getAllHistory() throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String my_string = "select * from history";
        ResultSet rs = stmt.executeQuery(my_string);

        ArrayList<JsonObject> rv = new ArrayList<>();
        while (rs.next()) {
            rv.add(new HistoryDetails(rs.getString(1), rs.getString(5)));
        }
        con.close();
        return rv;
    }

    public ArrayList<JsonObject> getHistory(String staffId) throws SQLException, ClassNotFoundException, ParseException {
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String my_string = "select * from history where staff_id = '" + staffId + "' order by historyID desc";
        ResultSet rs = stmt.executeQuery(my_string);

        ArrayList<JsonObject> rv = new ArrayList<>();
        ArrayList<String> historyId_to_remove = new ArrayList<>();
        int count = 0;

        while (rs.next()) {
            count = count + 1;
            if (count <= 10){
                String currentDate = rs.getString(6);
                SimpleDateFormat formatterForFrontEnd = new SimpleDateFormat("dd MMM yyyy");
                SimpleDateFormat formatterForBackEnd = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = formatterForBackEnd.parse(currentDate);
                rv.add(new HistoryDetails(rs.getString(1), formatterForFrontEnd.format(date)));
            } else {
                historyId_to_remove.add(rs.getString(1));
            }
        }
        con.close();
        if (historyId_to_remove.size() > 0){
            for(String id : historyId_to_remove){
                removeHistory(id);
            }
        }
        return rv;
    }

    public int addHistory(String staffId, String minGap, String maxGap, String numBay,String date) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();

        String query = "insert into history (staff_id, min_gap, max_gap, num_bay ,date_generated)";
        query += " values (?,?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, staffId);
        pstmt.setString(2, minGap);
        pstmt.setString(3, maxGap);
        pstmt.setString(4, numBay);
        pstmt.setString(5, date);
        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(1);
        }

        con.close();
        return id;
    }

    public String removeHistory(String historyId) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String sqlStr = "delete from history where historyID = '" + historyId + "'";
        stmt.executeUpdate(sqlStr);

        sqlStr = "delete from mass_slot_upload where historyId = '" + historyId + "'";
        stmt.executeUpdate(sqlStr);

        con.close();

        return "Success";
    }

    public List<Map<String,Object>> getBaseLineOccupancy(String msuId) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String my_string = "select * from mass_slot_upload where historyID = '" + msuId + "' AND isBaseLine = '1'";
        ResultSet rs = stmt.executeQuery(my_string);
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        
        List<Map<String,Object>> data = new ArrayList<>();
        ArrayList<main.java.history.MassSlotUploadDetails> rv2 = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        while (rs.next()) {
            HashMap row = new HashMap(columns);
            for (int i=3; i<=columns; i++){
                //39: product pn
                //40: send to storage date
                //45-47 : flex01 - flex03
                if (i==45 || i==46 || i==47){
                    continue;
                } else {
                    try {
                        if (rs.getObject(i) instanceof Integer){
                            row.put(md.getColumnName(i), rs.getObject(i));
                        } else {
                            String date = df.format(rs.getObject(i));
                            row.put(md.getColumnName(i), date);
                        }                         
                    } catch (Exception e){
                        row.put(md.getColumnName(i), rs.getObject(i));
                        continue;
                    }
                }
            }
            data.add(row);            
        }
        con.close();
        return data;
    }

    
    public List<Map<String,Object>> getBayOccupancy(String msuId) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String my_string = "select * from mass_slot_upload where historyID = '" + msuId + "' AND isBaseLine = '0'";
        ResultSet rs = stmt.executeQuery(my_string);
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        
        List<Map<String,Object>> data = new ArrayList<>();
        ArrayList<main.java.history.MassSlotUploadDetails> rv2 = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        while (rs.next()) {
            HashMap row = new HashMap(columns);
            for (int i=3; i<=columns; i++){
                //39: product pn
                //40: send to storage date
                //45-47 : flex01 - flex03
                if (i==45 || i==46 || i==47){
                    continue;
                } else {
                    try {
                        if (rs.getObject(i) instanceof Integer){
                            row.put(md.getColumnName(i), rs.getObject(i));
                        } else {
                            String date = df.format(rs.getObject(i));
                            row.put(md.getColumnName(i), date);
                        }                         
                    } catch (Exception e){
                        row.put(md.getColumnName(i), rs.getObject(i));
                        continue;
                    }
                }
            }
            data.add(row);
        }
        con.close();
        return data;
    }

    public int getMinGap(String msuId, String staffID) throws SQLException, ClassNotFoundException{
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String defSql = "select min_gap from history where historyID = '" +msuId+ "' AND staff_id = '" + staffID + "';";
        ResultSet rs = stmt.executeQuery(defSql);
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }

    public int getMaxGap(String msuId, String staffID) throws SQLException, ClassNotFoundException{
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String defSql = "select max_gap from history where historyID = '" +msuId+ "'  AND staff_id = '" + staffID + "';";
        ResultSet rs = stmt.executeQuery(defSql);
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }
    public int getNumBay(String msuId, String staffID) throws SQLException, ClassNotFoundException{
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String defSql = "select num_bay from history where historyID = '" +msuId+ "' AND staff_id = '" + staffID + "';";
        ResultSet rs = stmt.executeQuery(defSql);
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }


    public int getLastHistoryID() throws SQLException, ClassNotFoundException{
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String defSql = "select MAX(historyID) from history;";
        ResultSet rs = stmt.executeQuery(defSql);
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(1);
        }

        return id;
    }

    public int getFirstHistoryID() throws SQLException, ClassNotFoundException{
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String defSql = "select MIN(historyID) from history;";
        ResultSet rs = stmt.executeQuery(defSql);
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(1);
        }

        return id;
    }

    public int getHistoryCount() throws SQLException, ClassNotFoundException{
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String defSql = "select COUNT(historyID) from history;";
        ResultSet rs = stmt.executeQuery(defSql);
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(1);
        }

        return id;
    }

    // public String addMassSlotUpload(ArrayList<main.java.authentication.json.MassSlotUploadDetails> data, int msuId) throws SQLException, ClassNotFoundException {
    public String addMassSlotUpload(ArrayList<main.java.history.MassSlotUploadDetails> data, int msuId, int isBaseLine) throws SQLException, ClassNotFoundException {

        Connection con = super.getConnection();
        // for (main.java.authentication.json.MassSlotUploadDetails row : data) {
        for (main.java.history.MassSlotUploadDetails row : data) {
            Statement stmt = con.createStatement();
            String defSql = "insert into mass_slot_upload ("+
            "historyID, isBaseLine, argoID, slotID_UTID, fabName ,slotStatus, shipRevenueType, buildCategory, "+
            "buildProduct, slotPlanNote, planProductType, shipRisk_Upside, shipRiskReason, "+
            "commentFor$Change, committedShip$, secondaryCustomerName, fabID, salesOrder, "+
            "forecastID, MFGCommitDate, shipRecogDate, MRPDate, buildComplete, "+
            "intOpsShipReadinessDate, plant, new_Used, coreNeedDate, coreArrivalDate, "+
            "refurbStartDate, refurbCompleteDate, donorStatus, coreUTID, coreNotes, "+
            "MFGStatus, quantity, configurationNote, dropShip, RMATool, productPN, sendToStorageDate, "+
            "handOffDateToDE, handOffDateBackToMFG, installStartDate, cycleTimeDays, flex01, "+
            "flex02, flex03, flex04, fulfilled, buildQtr, endDate) ";
            defSql += "values (";
            defSql += "'" + msuId + "',";
            defSql += "'" + isBaseLine + "',";
            defSql += "'" + row.getArgo_id() + "',";
            defSql += "'" + row.getSlot_id() + "',";
            defSql += '"' + row.getFabName() + '"'+",";
            defSql += "'" + row.getSlot_status() + "',";
            defSql += "'" + row.getShip_rev_type() + "',";
            defSql += "'" + row.getBuild_category() + "',";
            defSql += "'" + row.getBuild_product() + "',";
            defSql += "'" + row.getSlot_plan_notes() + "',";
            defSql += "'" + row.getPlan_product_type() + "',";
            defSql += "'" + row.getShip_risk() + "',";
            defSql += "'" + row.getShip_risk_reason() + "',";
            defSql += "'" + row.getComment_for_change() + "',";
            defSql += "'" + row.getCommited_ship() + "',";
            defSql += "'" + row.getSecondary_customer_name() + "',";
            defSql += row.getFab_id() == null ? "null ," : "'" + row.getFab_id() + "',";
            defSql += row.getSales_order() == null ? "null ," : "'" + row.getSales_order() + "',";
            defSql += row.getForecast_id() == null ? "null ," : "'" + row.getForecast_id() + "',";
            defSql += "'" + row.getMfg_commit_date() + "',";
            defSql += "'" + row.getShip_recognition_date() + "',";
            defSql += "'" + row.getMrp_date() + "',";
            defSql += "'" + row.getBuild_complete() + "',";
            defSql += "'" + row.getInt_ops_ship_ready_date() + "',";
            defSql += "'" + row.getPlant() + "',";
            defSql += "'" + row.getCategory() + "',";
            defSql += row.getCore_need_date() == null ? "null ," : "'" + row.getCore_need_date() + "',";
            defSql += row.getCore_arrival_date() == null ? "null ," : "'" + row.getCore_arrival_date() + "',";
            defSql += row.getRefurb_start_date() == null ? "null ," : "'" + row.getRefurb_start_date() + "',";
            defSql += row.getRefurb_complete_date() == null ? "null ," : "'" + row.getRefurb_complete_date() + "',";
            defSql += "'" + row.getDonor_status() + "',";
            defSql += row.getCore_utid() == null ? "null ," : "'" + row.getCore_utid() + "',";
            defSql += "'" + row.getCore_notes() + "',";
            defSql += "'" + row.getMfg_status() + "',";
            defSql += row.getQty() == null ? "null ," : "'" + row.getQty() + "',";
            defSql += "'" + row.getConfig_note() + "',";
            defSql += "'" + row.getDrop_ship() + "',";
            defSql += "'" + row.getRma() + "',";
            defSql += "'" + row.getProduct_pn() + "',";
            defSql += "null ,";
            defSql += row.getOff_date_to_de() == null ? "null ," : "'" + row.getOff_date_to_de() + "',";
            defSql += row.getOff_date_to_mfg() == null ? "null ," : "'" + row.getOff_date_to_mfg() + "',";
            defSql += row.getInstall_start_date() == null ? "null ," : "'" + row.getInstall_start_date() + "',";
            defSql += "'" + row.getCycle_time_days() + "',";
            // defSql += row.getFlex() == null ? "null ," : "'" + row.getFlex() + "',";
            // defSql += "'" + row.getFulfilled() + "'";
            defSql += "null ,";
            defSql += "null ,";
            defSql += "null ,";
            defSql += "null ,";
            defSql += "0,";
            defSql += "'" + row.getBuildQtr() + "',";
            defSql += "'" + row.getEndDate() + "'";

            defSql += ");";
            // return defSql;
            stmt.executeUpdate(defSql);
        }

        con.close();

        return "Success";
    }

    public int addNewBaseline(List<Baseline> baselines, String staff_id) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();

        con.setAutoCommit(false);

        String query = "insert into baseline (`staff_id`,`Argo ID`,`Plant`,`Build Complete`,`Slot Status`,`Plan Product Type`,`Build Category`,`Ship Revenue Type`,`Sales Order`,`Forecast ID`,`Slot ID/UTID`,`Sold-To Name`,`Fab Name`,`Secondary Customer Name`,`Product Family`,`Build Product`,`Forecast Product`,`Product PN`,`Committed Ship $`,`Ship Revenue (Int $)`,`Ship Risk/Upside`,`Ship Risk Reason`,`MRP Date`,`Int. Ops Ship Readiness Date`,`MFG Commit Date`,`Ship Recog Date`,`Hand Off Date To DE`,`Hand Off Date Back To MFG`,`SAP Customer Req Date`,`Slot Request Date`,`Div Commit Date`,`Delta Days`,`Region`,`SO Status`,`Caerus PO Qtr`,`Install Start Date`,`Cycle Time Days`,`Slot Plan Note`,`Comment For $ Change`,`Configuration Note`,`Drop Ship`,`MFG Status`,`Quantity`,`RMA Tool`,`New/Used`,`Core Need Date`,`Core Arrival Date`,`Refurb Start Date`,`Refurb Complete Date`,`Donor Status`,`Core UTID`,`Core Notes`,`Fab ID`,`PGI Date`,`Flex 01`,`Flex 02`,`Flex 03`,`Flex 04`,`Build Qtr`,`Ship Qtr`,`Ship Recog Qtr`,`MFG Site`,`Division`,`Holds`,`IncoTerms`,`Sales Slot Identifier`,`MS Owner`,`BU`,`Book Risk Reason`,`Book Risk/Upside`,`Caerus Commit`,`Caerus Description`,`Caerus Product Type`,`Change Request #`,`Delivery #`,`Purchase Order #`,`SBU`,`Sales Ops Slot Notes`,`Segment`,`Slot Gap`,`Slot Request Qtr`,`Sold-To ID`,`Standard COGS`,`Created By`,`Created On`,`Changed On`,`Created Time`,`Changed By`,`Last Changed Time`) ";
        query += "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(query);

        for (Baseline baseline : baselines) {
            pstmt.setString(1, staff_id);
            pstmt.setString(2, baseline.Argo_ID);
            pstmt.setString(3, baseline.Plant);
            pstmt.setObject(4, baseline.Build_Complete);
            pstmt.setString(5, baseline.Slot_Status);
            pstmt.setString(6, baseline.Plan_Product_Type);
            pstmt.setString(7, baseline.Build_Category);
            pstmt.setString(8, baseline.Ship_Revenue_Type);
            pstmt.setString(9, baseline.Sales_Order);
            pstmt.setString(10, baseline.Forecast_ID);
            pstmt.setString(11, baseline.Slot_ID_UTID);
            pstmt.setString(12, baseline.Sold_To_Name);
            pstmt.setString(13, baseline.Fab_Name);
            pstmt.setString(14, baseline.Secondary_Customer_Name);
            pstmt.setString(15, baseline.Product_Family);
            pstmt.setString(16, baseline.Build_Product);
            pstmt.setString(17, baseline.Forecast_Product);
            pstmt.setString(18, baseline.Product_PN);
            pstmt.setObject(19, baseline.Committed_Ship_$);
            pstmt.setObject(20, baseline.Ship_Revenue_Int_$);
            pstmt.setString(21, baseline.Ship_Risk_Upside);
            pstmt.setString(22, baseline.Ship_Risk_Reason);
            pstmt.setString(23, baseline.MRP_Date);
            pstmt.setString(24, baseline.Int_Ops_Ship_Readiness_Date);
            pstmt.setString(25, baseline.MFG_Commit_Date);
            pstmt.setString(26, baseline.Ship_Recog_Date);
            pstmt.setString(27, baseline.Hand_Off_Date_To_DE);
            pstmt.setString(28, baseline.Hand_Off_Date_Back_To_MFG);
            pstmt.setString(29, baseline.SAP_Customer_Req_Date);
            pstmt.setString(30, baseline.Slot_Request_Date);
            pstmt.setString(31, baseline.Div_Commit_Date);
            pstmt.setObject(32, baseline.Delta_Days);
            pstmt.setString(33, baseline.Region);
            pstmt.setString(34, baseline.SO_Status);
            pstmt.setString(35, baseline.Caerus_PO_Qtr);
            pstmt.setString(36, baseline.Install_Start_Date);
            pstmt.setObject(37, baseline.Cycle_Time_Days);
            pstmt.setString(38, baseline.Slot_Plan_Note);
            pstmt.setString(39, baseline.Comment_For_$_Change);
            pstmt.setString(40, baseline.Configuration_Note);
            pstmt.setString(41, baseline.Drop_Ship);
            pstmt.setString(42, baseline.MFG_Status);
            pstmt.setObject(43, baseline.Quantity);
            pstmt.setObject(44, baseline.RMA_Tool);
            pstmt.setString(45, baseline.New_Used);
            pstmt.setString(46, baseline.Core_Need_Date);
            pstmt.setString(47, baseline.Core_Arrival_Date);
            pstmt.setString(48, baseline.Refurb_Start_Date);
            pstmt.setString(49, baseline.Refurb_Complete_Date);
            pstmt.setString(50, baseline.Donor_Status);
            pstmt.setString(51, baseline.Core_UTID);
            pstmt.setString(52, baseline.Core_Notes);
            pstmt.setString(53, baseline.Fab_ID);
            pstmt.setString(54, baseline.PGI_Date);
            pstmt.setString(55, baseline.Flex_01);
            pstmt.setString(56, baseline.Flex_02);
            pstmt.setString(57, baseline.Flex_03);
            pstmt.setString(58, baseline.Flex_04);
            pstmt.setString(59, baseline.Build_Qtr);
            pstmt.setString(60, baseline.Ship_Qtr);
            pstmt.setString(61, baseline.Ship_Recog_Qtr);
            pstmt.setString(62, baseline.MFG_Site);
            pstmt.setString(63, baseline.Division);
            pstmt.setString(64, baseline.Holds);
            pstmt.setString(65, baseline.IncoTerms);
            pstmt.setString(66, baseline.Sales_Slot_Identifier);
            pstmt.setString(67, baseline.MS_Owner);
            pstmt.setString(68, baseline.BU);
            pstmt.setString(69, baseline.Book_Risk_Reason);
            pstmt.setString(70, baseline.Book_Risk_Upside);
            pstmt.setString(71, baseline.Caerus_Commit);
            pstmt.setString(72, baseline.Caerus_Description);
            pstmt.setString(73, baseline.Caerus_Product_Type);
            pstmt.setString(74, baseline.Change_Request);
            pstmt.setString(75, baseline.Delivery);
            pstmt.setString(76, baseline.Purchase_Order);
            pstmt.setString(77, baseline.SBU);
            pstmt.setString(78, baseline.Sales_Ops_Slot_Notes);
            pstmt.setString(79, baseline.Segment);
            pstmt.setString(80, baseline.Slot_Gap);
            pstmt.setString(81, baseline.Slot_Request_Qtr);
            pstmt.setString(82, baseline.Sold_To_ID);
            pstmt.setString(83, baseline.Standard_COGS);
            pstmt.setString(84, baseline.Created_By);
            pstmt.setString(85, baseline.Created_On);
            pstmt.setString(86, baseline.Changed_On);
            pstmt.setString(87, baseline.Created_Time);
            pstmt.setString(88, baseline.Changed_By);
            pstmt.setString(89, baseline.Last_Changed_Time);
            pstmt.addBatch();
        }

        int[] updateCounts = pstmt.executeBatch();
        int status = super.checkUpdateCounts(updateCounts);

        pstmt.close();
        con.close();

        if (status == 1) {
            return 200;// successfully added certificates
        }
        return 400;
    }

    public List<Map<String, Object>> getBaselineForUser(String staff_id) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();

        String query = "select `Argo ID`,`Plant`,`Build Complete`,`Slot Status`,`Plan Product Type`,`Build Category`,`Ship Revenue Type`,`Sales Order`,`Forecast ID`,`Slot ID/UTID`,`Sold-To Name`,`Fab Name`,`Secondary Customer Name`,`Product Family`,`Build Product`,`Forecast Product`,`Product PN`,`Committed Ship $`,`Ship Revenue (Int $)`,`Ship Risk/Upside`,`Ship Risk Reason`,`MRP Date`,`Int. Ops Ship Readiness Date`,`MFG Commit Date`,`Ship Recog Date`,`Hand Off Date To DE`,`Hand Off Date Back To MFG`,`SAP Customer Req Date`,`Slot Request Date`,`Div Commit Date`,`Delta Days`,`Region`,`SO Status`,`Caerus PO Qtr`,`Install Start Date`,`Cycle Time Days`,`Slot Plan Note`,`Comment For $ Change`,`Configuration Note`,`Drop Ship`,`MFG Status`,`Quantity`,`RMA Tool`,`New/Used`,`Core Need Date`,`Core Arrival Date`,`Refurb Start Date`,`Refurb Complete Date`,`Donor Status`,`Core UTID`,`Core Notes`,`Fab ID`,`PGI Date`,`Flex 01`,`Flex 02`,`Flex 03`,`Flex 04`,`Build Qtr`,`Ship Qtr`,`Ship Recog Qtr`,`MFG Site`,`Division`,`Holds`,`IncoTerms`,`Sales Slot Identifier`,`MS Owner`,`BU`,`Book Risk Reason`,`Book Risk/Upside`,`Caerus Commit`,`Caerus Description`,`Caerus Product Type`,`Change Request #`,`Delivery #`,`Purchase Order #`,`SBU`,`Sales Ops Slot Notes`,`Segment`,`Slot Gap`,`Slot Request Qtr`,`Sold-To ID`,`Standard COGS`,`Created By`,`Created On`,`Changed On`,`Created Time`,`Changed By`,`Last Changed Time` from baseline where staff_id = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, staff_id);

        ResultSet rs = pstmt.executeQuery();

        List<Map<String, Object>> list = new ArrayList<>();

        while (rs.next()) {
            Map<String, Object> map = new HashMap<>();
            map.put("Argo ID",rs.getString(1));
            map.put("Plant",rs.getString(2));
            map.put("Build Complete",rs.getInt(3));
            map.put("Slot Status",rs.getString(4));
            map.put("Plan Product Type",rs.getString(5));
            map.put("Build Category",rs.getString(6));
            map.put("Ship Revenue Type",rs.getString(7));
            map.put("Sales Order",rs.getString(8));
            map.put("Forecast ID",rs.getString(9));
            map.put("Slot ID/UTID",rs.getString(10));
            map.put("Sold-To Name",rs.getString(11));
            map.put("Fab Name",rs.getString(12));
            map.put("Secondary Customer Name",rs.getString(13));
            map.put("Product Family",rs.getString(14));
            map.put("Build Product",rs.getString(15));
            map.put("Forecast Product",rs.getString(16));
            map.put("Product PN",rs.getString(17));
            map.put("Committed Ship $",rs.getInt(18));
            map.put("Ship Revenue (Int $)",rs.getInt(19));
            map.put("Ship Risk/Upside",rs.getString(20));
            map.put("Ship Risk Reason",rs.getString(21));
            map.put("MRP Date",rs.getString(22));
            map.put("Int. Ops Ship Readiness Date",rs.getString(23));
            map.put("MFG Commit Date",rs.getString(24));
            map.put("Ship Recog Date",rs.getString(25));
            map.put("Hand Off Date To DE",rs.getString(26));
            map.put("Hand Off Date Back To MFG",rs.getString(27));
            map.put("SAP Customer Req Date",rs.getString(28));
            map.put("Slot Request Date",rs.getString(29));
            map.put("Div Commit Date",rs.getString(30));
            map.put("Delta Days",rs.getInt(31));
            map.put("Region",rs.getString(32));
            map.put("SO Status",rs.getString(33));
            map.put("Caerus PO Qtr",rs.getString(34));
            map.put("Install Start Date",rs.getString(35));
            map.put("Cycle Time Days",rs.getInt(36));
            map.put("Slot Plan Note",rs.getString(37));
            map.put("Comment For $ Change",rs.getString(38));
            map.put("Configuration Note",rs.getString(39));
            map.put("Drop Ship",rs.getString(40));
            map.put("MFG Status",rs.getString(41));
            map.put("Quantity",rs.getInt(42));
            map.put("RMA Tool",rs.getInt(43));
            map.put("New/Used",rs.getString(44));
            map.put("Core Need Date",rs.getString(45));
            map.put("Core Arrival Date",rs.getString(46));
            map.put("Refurb Start Date",rs.getString(47));
            map.put("Refurb Complete Date",rs.getString(48));
            map.put("Donor Status",rs.getString(49));
            map.put("Core UTID",rs.getString(50));
            map.put("Core Notes",rs.getString(51));
            map.put("Fab ID",rs.getString(52));
            map.put("PGI Date",rs.getString(53));
            map.put("Flex 01",rs.getString(54));
            map.put("Flex 02",rs.getString(55));
            map.put("Flex 03",rs.getString(56));
            map.put("Flex 04",rs.getString(57));
            map.put("Build Qtr",rs.getString(58));
            map.put("Ship Qtr",rs.getString(59));
            map.put("Ship Recog Qtr",rs.getString(60));
            map.put("MFG Site",rs.getString(61));
            map.put("Division",rs.getString(62));
            map.put("Holds",rs.getString(63));
            map.put("IncoTerms",rs.getString(64));
            map.put("Sales Slot Identifier",rs.getString(65));
            map.put("MS Owner",rs.getString(66));
            map.put("BU",rs.getString(67));
            map.put("Book Risk Reason",rs.getString(68));
            map.put("Book Risk/Upside",rs.getString(69));
            map.put("Caerus Commit",rs.getString(70));
            map.put("Caerus Description",rs.getString(71));
            map.put("Caerus Product Type",rs.getString(72));
            map.put("Change Request #",rs.getString(73));
            map.put("Delivery #",rs.getString(74));
            map.put("Purchase Order #",rs.getString(75));
            map.put("SBU",rs.getString(76));
            map.put("Sales Ops Slot Notes",rs.getString(77));
            map.put("Segment",rs.getString(78));
            map.put("Slot Gap",rs.getString(79));
            map.put("Slot Request Qtr",rs.getString(80));
            map.put("Sold-To ID",rs.getString(81));
            map.put("Standard COGS",rs.getString(82));
            map.put("Created By",rs.getString(83));
            map.put("Created On",rs.getString(84));
            map.put("Changed On",rs.getString(85));
            map.put("Created Time",rs.getString(86));
            map.put("Changed By",rs.getString(87));
            map.put("Last Changed Time",rs.getString(88));
            list.add(map);
        }
        con.close();
        return list;
    }

    public boolean baselinePresentForUser(String staff_id) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();

        String query = "select * from baseline where staff_id = ? ";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, staff_id);

        ResultSet rs = pstmt.executeQuery();
        boolean presence = false;
        if (rs.next()) {
            presence = true;
        }
        con.close();
        return presence;
    }

    public boolean isThereMoreThanTenHistoryRecords(String staff_id) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();

        String query = "select COUNT(*) from history where staff_id = ? ";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, staff_id);

        ResultSet rs = pstmt.executeQuery();
        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }
        con.close();
        return (count > 10);
    }

    public void removeBaselineFromUser(String staff_id) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
        String query = "DELETE from baseline where staff_id = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, staff_id);
        int count = pstmt.executeUpdate();
        con.close();
    }

    public void removeHistoryFromUser(String staff_id) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
        String query = "DELETE from history where staff_id = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, staff_id);
        int count = pstmt.executeUpdate();
        con.close();
    }


    // public String addMassSlotUpload(ArrayList<main.java.history.MassSlotUploadDetails> data, int msuId) throws SQLException, ClassNotFoundException {

    //     Connection con = super.getConnection();
    //     // for (main.java.authentication.json.MassSlotUploadDetails row : data) {
    //     for (main.java.history.MassSlotUploadDetails row : data) {
    //         Statement stmt = con.createStatement();
    //         String defSql = "insert into mass_slot_upload (historyID, argo_id, slot_id, slot_status, ship_rev_type, build_category, build_product, slot_plan_notes, plan_product_type, ship_risk, ship_risk_reason, comment_for_change, committed_ship, secondary_customer_name, fab_id, sales_order, forecast_id, mfg_commit_date, ship_recognition_date, mrp_date, build_complete, int_ops_ship_ready_date, plant, category, core_need_date, core_arrival_date, refurb_start_date, refurb_complete_date, donor_status, core_utid, core_notes, mfg_status, qty, config_note, drop_ship, rma, product_pn, move_to_storage, off_date_to_de, off_date_to_mfg, install_start_date, cycle_time_days, flex, fulfilled) ";
    //         defSql += "values (";
    //         defSql += "'" + msuId + "',";
    //         defSql += "'" + row.getArgo_id() + "',";
    //         defSql += "'" + row.getSlot_id() + "',";
    //         defSql += "'" + row.getSlot_status() + "',";
    //         defSql += "'" + row.getShip_rev_type() + "',";
    //         defSql += "'" + row.getBuild_category() + "',";
    //         defSql += "'" + row.getBuild_product() + "',";
    //         defSql += "'" + row.getSlot_plan_notes() + "',";
    //         defSql += "'" + row.getPlan_product_type() + "',";
    //         defSql += "'" + row.getShip_risk() + "',";
    //         defSql += "'" + row.getShip_risk_reason() + "',";
    //         defSql += "'" + row.getComment_for_change() + "',";
    //         defSql += "'" + row.getCommited_ship() + "',";
    //         defSql += "'" + row.getSecondary_customer_name() + "',";
    //         defSql += row.getFab_id() == null ? "null ," : "'" + row.getFab_id() + "',";
    //         defSql += row.getSales_order() == null ? "null ," : "'" + row.getSales_order() + "',";
    //         defSql += row.getForecast_id() == null ? "null ," : "'" + row.getForecast_id() + "',";
    //         defSql += "'" + row.getMfg_commit_date() + "',";
    //         defSql += "'" + row.getShip_recognition_date() + "',";
    //         defSql += "'" + row.getMrp_date() + "',";
    //         defSql += "'" + row.getBuild_complete() + "',";
    //         defSql += "'" + row.getInt_ops_ship_ready_date() + "',";
    //         defSql += "'" + row.getPlant() + "',";
    //         defSql += "'" + row.getCategory() + "',";
    //         defSql += row.getCore_need_date() == null ? "null ," : "'" + row.getCore_need_date() + "',";
    //         defSql += row.getCore_arrival_date() == null ? "null ," : "'" + row.getCore_arrival_date() + "',";
    //         defSql += row.getRefurb_start_date() == null ? "null ," : "'" + row.getRefurb_start_date() + "',";
    //         defSql += row.getRefurb_complete_date() == null ? "null ," : "'" + row.getRefurb_complete_date() + "',";
    //         defSql += "'" + row.getDonor_status() + "',";
    //         defSql += row.getCore_utid() == null ? "null ," : "'" + row.getCore_utid() + "',";
    //         defSql += "'" + row.getCore_notes() + "',";
    //         defSql += "'" + row.getMfg_status() + "',";
    //         defSql += row.getQty() == null ? "null ," : "'" + row.getQty() + "',";
    //         defSql += "'" + row.getConfig_note() + "',";
    //         defSql += "'" + row.getDrop_ship() + "',";
    //         defSql += "'" + row.getRma() + "',";
    //         defSql += "'" + row.getProduct_pn() + "',";
    //         defSql += "null ,";
    //         defSql += row.getOff_date_to_de() == null ? "null ," : "'" + row.getOff_date_to_de() + "',";
    //         defSql += row.getOff_date_to_mfg() == null ? "null ," : "'" + row.getOff_date_to_mfg() + "',";
    //         defSql += row.getInstall_start_date() == null ? "null ," : "'" + row.getInstall_start_date() + "',";
    //         defSql += "'" + row.getCycle_time_days() + "',";
    //         // defSql += row.getFlex() == null ? "null ," : "'" + row.getFlex() + "',";
    //         // defSql += "'" + row.getFulfilled() + "'";
    //         defSql += "null ,";
    //         defSql += "0";

    //         defSql += ");";
    //         // return defSql;
    //         stmt.executeUpdate(defSql);
    //     }

    //     con.close();

    //     return "Success";
    // }
}
