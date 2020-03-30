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

    public ArrayList<JsonObject> getHistory(String staffId) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String my_string = "select * from history where staff_id = '" + staffId + "'";
        ResultSet rs = stmt.executeQuery(my_string);

        ArrayList<JsonObject> rv = new ArrayList<>();
        while (rs.next()) {
            rv.add(new HistoryDetails(rs.getString(1), rs.getString(5)));
        }
        con.close();
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

    public String removeHistory(String msuId) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String sqlStr = "delete from history where msu_id = '" + msuId + "'";
        stmt.executeUpdate(sqlStr);

        sqlStr = "delete from mass_slot_upload where msu_id = '" + msuId + "'";
        stmt.executeUpdate(sqlStr);

        con.close();

        return "Success";
    }

    // to be replaced by getbaseline occupancy and getbayoccupancy
    public List<Map<String,Object>> getMassSlotUpload(String msuId) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String my_string = "select * from mass_slot_upload where historyID = '" + msuId + "'";
        ResultSet rs = stmt.executeQuery(my_string);
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        
        List<Map<String,Object>> data = new ArrayList<>();
        ArrayList<main.java.history.MassSlotUploadDetails> rv2 = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        while (rs.next()) {
            HashMap row = new HashMap(columns);
            for (int i=3; i<=columns; i++){
                if (i==39 || i==44 || i==45 || i==46){
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
            // rv2.add(new MassSlotUploadDetails(row));
            
        }
        con.close();
        return data;
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
            // rv2.add(new MassSlotUploadDetails(row));
            
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
            // rv2.add(new MassSlotUploadDetails(row));
            
        }
        con.close();
        return data;
    }

    public int getMinGap(String msuId) throws SQLException, ClassNotFoundException{
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String defSql = "select min_gap from history where history_id = '" +msuId+ "';";
        ResultSet rs = stmt.executeQuery(defSql);
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }

    public int getMaxGap(String msuId) throws SQLException, ClassNotFoundException{
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String defSql = "select max_gap from history where history_id = '" +msuId+ "';";
        ResultSet rs = stmt.executeQuery(defSql);
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }
    public int getNumBay(String msuId) throws SQLException, ClassNotFoundException{
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String defSql = "select num_bay from history where history_id = '" +msuId+ "';";
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
        String defSql = "select MAX(history_id) from history;";
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
        String defSql = "select COUNT(history_id) from history;";
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
            pstmt.setInt(4, baseline.Build_Complete);
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
            pstmt.setInt(19, baseline.Committed_Ship_$);
            pstmt.setInt(20, baseline.Ship_Revenue_Int_$);
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
            pstmt.setInt(32, baseline.Delta_Days);
            pstmt.setString(33, baseline.Region);
            pstmt.setString(34, baseline.SO_Status);
            pstmt.setString(35, baseline.Caerus_PO_Qtr);
            pstmt.setString(36, baseline.Install_Start_Date);
            pstmt.setInt(37, baseline.Cycle_Time_Days);
            pstmt.setString(38, baseline.Slot_Plan_Note);
            pstmt.setString(39, baseline.Comment_For_$_Change);
            pstmt.setString(40, baseline.Configuration_Note);
            pstmt.setString(41, baseline.Drop_Ship);
            pstmt.setString(42, baseline.MFG_Status);
            pstmt.setInt(43, baseline.Quantity);
            pstmt.setInt(44, baseline.RMA_Tool);
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

    public void removeBaselineFromUser(String staff_id) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
        String query = "DELETE from baseline where staff_id = ?";
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
    //         String defSql = "insert into mass_slot_upload (history_id, argo_id, slot_id, slot_status, ship_rev_type, build_category, build_product, slot_plan_notes, plan_product_type, ship_risk, ship_risk_reason, comment_for_change, committed_ship, secondary_customer_name, fab_id, sales_order, forecast_id, mfg_commit_date, ship_recognition_date, mrp_date, build_complete, int_ops_ship_ready_date, plant, category, core_need_date, core_arrival_date, refurb_start_date, refurb_complete_date, donor_status, core_utid, core_notes, mfg_status, qty, config_note, drop_ship, rma, product_pn, move_to_storage, off_date_to_de, off_date_to_mfg, install_start_date, cycle_time_days, flex, fulfilled) ";
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
