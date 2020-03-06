package connection;

import java.sql.*;
import java.util.*;

import main.java.authentication.json.users.*;
import main.java.authentication.json.HistoryDetails;
import main.java.authentication.json.JsonError;
import main.java.authentication.json.JsonObject;
import main.java.authentication.json.MassSlotUploadDetails;
import main.java.authentication.json.GetMassSlotUploadResult;
import main.java.authentication.json.FacilityUtil;
import main.java.authentication.json.GetFacilityUtilResult;

// import org.springframework.beans.factory.annotation.*;
// import org.springframework.stereotype.*;

// @Component
public class mysqlcon {

    // @Value("${port}")
    // private String port;

    private final String connectionPassword = "";
    private final String port = "3306";
    private final String databaseName = "baymaxdb";
    private final String connection = "jdbc:mysql://localhost:" + port + "/" + databaseName + "?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final String driverName = "com.mysql.cj.jdbc.Driver";

    public RegistrationDetails getUser(String username) throws SQLException, ClassNotFoundException {
        Class.forName(driverName);

        Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
        Statement stmt = con.createStatement();
        String my_string = "select * from users WHERE username = '" + username + "'";
        ResultSet rs = stmt.executeQuery(my_string);

        RegistrationDetails rv = null;

        while (rs.next()) {
            rv = new RegistrationDetails(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
        }
        con.close();
        return rv;
    }

    public ArrayList<User> getAllUsers() throws SQLException, ClassNotFoundException {
        Class.forName(driverName);

        Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
        Statement stmt = con.createStatement();
        String my_string = "select * from users";
        ResultSet rs = stmt.executeQuery(my_string);

        ArrayList<User> rv = new ArrayList<User>();
        while (rs.next()) {
            rv.add(new UserCredentials(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
        }
        con.close();
        return rv;
    }

    public void addUser(String username, String password, String firstname, String lastname, String department, String role) throws SQLException, ClassNotFoundException {

        Class.forName(driverName);

        Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
        Statement stmt = con.createStatement();
        String my_string = "insert into users (username, password, firstname, lastname, department, role) values ('" + username + "', '" + password + "', '" + firstname + "', '" + lastname + "', '" + department + "', '" + role + "')";
        stmt.executeUpdate(my_string);
        con.close();

    }

    public void changePassword(String username, String oldpassword, String newpassword) throws SQLException, ClassNotFoundException {

        Class.forName(driverName);

        Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
        Statement stmt = con.createStatement();
        String my_string = "update users set password = '" + newpassword + "' where username = '" + username + "' and password = '" + oldpassword + "';";
        stmt.executeUpdate(my_string);
        con.close();
    }

    public void resetPassword(String username) throws SQLException, ClassNotFoundException {

        Class.forName(driverName);

        Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
        Statement stmt = con.createStatement();
        String my_string = "update users set password = 'password' where username = '" + username + "';";
        stmt.executeUpdate(my_string);
        con.close();
    }

    public ArrayList<JsonObject> getAllHistory() {
        try {
            Class.forName(driverName);

            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            Statement stmt = con.createStatement();
            String my_string = "select * from history";
            ResultSet rs = stmt.executeQuery(my_string);

            ArrayList<JsonObject> rv = new ArrayList<>();
            while (rs.next()) {
                rv.add(new HistoryDetails(rs.getString(1), rs.getString(5)));
            }
            con.close();
            return rv;

        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<JsonObject>();
        }
    }

    public ArrayList<JsonObject> getHistory(String staffId) throws SQLException, ClassNotFoundException {
        Class.forName(driverName);

        Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
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

    public int addHistory(String staffId, String date) {
        try {
            Class.forName(driverName);

            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            Statement stmt = con.createStatement();

            String query = "insert into history (staff_id, date_generated)";
            query += " values (?,?)";
            PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, staffId);
            pstmt.setString(2, date);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }

            con.close();

            return id;
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        }
    }

    public String removeHistory(String msuId) {
        try {
            Class.forName(driverName);

            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            Statement stmt = con.createStatement();
            String sqlStr = "delete from history where msu_id = '" + msuId + "'";
            stmt.executeUpdate(sqlStr);

            sqlStr = "delete from mass_slot_upload where msu_id = '" + msuId + "'";
            stmt.executeUpdate(sqlStr);

            con.close();

            return "Success";
        } catch (Exception e) {
            System.out.println(e);
            return "Fail";
        }
    }

    public ArrayList<JsonObject> getMassSlotUpload(String msuId) {
        try {
            Class.forName(driverName);

            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            Statement stmt = con.createStatement();
            String my_string = "select * from mass_slot_upload where msu_id = '" + msuId + "'";
            ResultSet rs = stmt.executeQuery(my_string);

            ArrayList<JsonObject> rv = new ArrayList<>();
            while (rs.next()) {
                rv.add(new GetMassSlotUploadResult(
                        rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),
                        rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15),
                        rs.getString(16), rs.getString(17), rs.getString(18), rs.getString(19), rs.getString(20),
                        rs.getString(21), rs.getString(22), rs.getString(23), rs.getString(24), rs.getString(25),
                        rs.getString(26), rs.getString(27), rs.getString(28), rs.getString(29), rs.getString(30),
                        rs.getString(31), rs.getString(32), rs.getString(33), rs.getString(34), rs.getString(35),
                        rs.getString(36), rs.getString(37), rs.getString(38), rs.getString(39), rs.getString(40),
                        rs.getString(41), rs.getString(42), rs.getString(43)

                ));
            }
            con.close();
            return rv;

        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<JsonObject>();
        }
    }

    public String addMassSlotUpload(ArrayList<MassSlotUploadDetails> data, int msuId) {
        try {
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            for (MassSlotUploadDetails row : data) {
                Statement stmt = con.createStatement();
                String defSql = "insert into mass_slot_upload (msu_id, argo_id, slot_id, slot_status, ship_rev_type, build_category, build_product, slot_plan_notes, plan_product_type, ship_risk, ship_risk_reason, comment_for_change, committed_ship_$, secondary_customer_name, fab_id, sales_order, forecast_id, mfg_commit_date, ship_recognition_date, mrp_date, build_complete, int_ops_ship_ready_date, plant, category, core_need_date, core_arrival_date, refurb_start_date, refurb_complete_date, donor_status, core_utid, core_notes, mfg_status, qty, config_note, drop_ship, rma, product_pn, off_date_to_de, off_date_to_mfg, install_start_date, cycle_time_days, flex, fulfilled) ";
                defSql += "values (";
                defSql += "'" + msuId + "',";
                defSql += "'" + row.getArgo_id() + "',";
                defSql += "'" + row.getSlot_id() + "',";
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
                defSql += row.getFab_id().equals("") ? "null ," : "'" + row.getFab_id() + "',";
                defSql += row.getSales_order().equals("") ? "null ," : "'" + row.getSales_order() + "',";
                defSql += row.getForecast_id().equals("") ? "null ," : "'" + row.getForecast_id() + "',";
                defSql += "'" + row.getMfg_commit_date() + "',";
                defSql += "'" + row.getShip_recognition_date() + "',";
                defSql += "'" + row.getMrp_date() + "',";
                defSql += "'" + row.getBuild_complete() + "',";
                defSql += "'" + row.getInt_ops_ship_ready_date() + "',";
                defSql += "'" + row.getPlant() + "',";
                defSql += "'" + row.getCategory() + "',";
                defSql += row.getCore_need_date().equals("") ? "null ," : "'" + row.getCore_need_date() + "',";
                defSql += row.getCore_arrival_date().equals("") ? "null ," : "'" + row.getCore_arrival_date() + "',";
                defSql += row.getRefurb_start_date().equals("") ? "null ," : "'" + row.getRefurb_start_date() + "',";
                defSql += row.getRefurb_complete_date().equals("") ? "null ," : "'" + row.getRefurb_complete_date() + "',";
                defSql += "'" + row.getDonor_status() + "',";
                defSql += row.getCore_utid().equals("") ? "null ," : "'" + row.getCore_utid() + "',";
                defSql += "'" + row.getCore_notes() + "',";
                defSql += "'" + row.getMfg_status() + "',";
                defSql += row.getQty().equals("") ? "null ," : "'" + row.getQty() + "',";
                defSql += "'" + row.getConfig_note() + "',";
                defSql += "'" + row.getDrop_ship() + "',";
                defSql += "'" + row.getRma() + "',";
                defSql += "'" + row.getProduct_pn() + "',";
                defSql += row.getOff_date_to_de().equals("") ? "null ," : "'" + row.getOff_date_to_de() + "',";
                defSql += row.getOff_date_to_mfg().equals("") ? "null ," : "'" + row.getOff_date_to_mfg() + "',";
                defSql += row.getInstall_start_date().equals("") ? "null ," : "'" + row.getInstall_start_date() + "',";
                defSql += "'" + row.getCycle_time_days() + "',";
                defSql += row.getFlex().equals("") ? "null ," : "'" + row.getFlex() + "',";
                defSql += "'" + row.getFulfilled() + "'";
                defSql += ")";
                stmt.executeUpdate(defSql);
            }

            con.close();

            return "Success";
        } catch (Exception e) {
            System.out.println(e);
            return "Fail";
        }
    }

    public ArrayList<GetFacilityUtilResult> readFacilityUtil(String staffId) {
        try {
            Class.forName(driverName);

            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            Statement stmt = con.createStatement();
            String my_string = "select * from facility_util where staff_id = '" + staffId + "'";
            ResultSet rs = stmt.executeQuery(my_string);

            ArrayList<GetFacilityUtilResult> rv = new ArrayList<>();
            while (rs.next()) {
                rv.add(new GetFacilityUtilResult(
                        rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),
                        rs.getString(11)
                ));
            }
            con.close();

            if (rv.size() == 0) {
                return new ArrayList<GetFacilityUtilResult>();
            }

            return rv;
        } catch (Exception e) {
            return new ArrayList<GetFacilityUtilResult>();
        }
    }

    /**
     * @return -1 if there is an error
     */
    public int getNextFaciId() {
        try {
            Class.forName(driverName);

            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            Statement stmt = con.createStatement();
            String my_string = "select max(faci_id) from facility_util";
            ResultSet rs = stmt.executeQuery(my_string);

            if (rs.next()) {
                return rs.getInt(1) + 1;
            }

            return 1;
        } catch (Exception e) {
            return -1;
        }
    }

//    public String createFacilityUtil(ArrayList<FacilityUtil> data, String staff_id, int faci_id) {
//        String defSql = "";
//        try {
//            Class.forName(driverName);
//            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
//
//            for (FacilityUtil row : data) {
//                Statement stmt = con.createStatement();
//                defSql = "insert into facility_util (faci_id, staff_id, slot_id_utid, sales_order, customer, configuration, model, tool_start, mfg_commit_ship_date, bay, week_of_friday) ";
//                defSql += "values (";
//                defSql += "'" + faci_id + "',";
//                defSql += "'" + staff_id + "',";
//                defSql += row.getSlot_id_utid().equals("") ? "null ," : "'" + row.getSlot_id_utid() + "',";
//                defSql += "'" + row.getSales_order() + "',";
//                defSql += "'" + row.getCustomer() + "',";
//                defSql += row.getConfiguration().equals("") ? "null ," : "'" + row.getConfiguration() + "',";
//                defSql += "'" + row.getModel() + "',";
//                defSql += "'" + row.getTool_start() + "',";
//                defSql += "'" + row.getMfg_commit_ship_date() + "',";
//                defSql += "'" + row.getBay() + "',";
//                defSql += "'" + row.getWeek_of_friday() + "'";
//                defSql += ")";
//                stmt.executeUpdate(defSql);
//            }
//
//            con.close();
//            return "Successfully added facility usage";
//        } catch (Exception e) {
//            return "Failed adding facility usage";
//        }
//    }

    public String createFacilityUtil(ArrayList<FacilityUtil> data, String staff_id, int faci_id) {
        try {
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            String sql = "insert into facility_util (faci_id, staff_id, slot_id_utid, sales_order, customer, configuration, model, tool_start, mfg_commit_ship_date, bay, week_of_friday) ";
            sql += "values (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);

            for (FacilityUtil row : data) {
                pstmt.setString(1, Integer.toString(faci_id));
                pstmt.setString(2, staff_id);
                pstmt.setString(3, row.getSlot_id_utid().equals("") ? null : row.getSlot_id_utid());
                pstmt.setString(4, row.getSales_order());
                pstmt.setString(5, row.getCustomer());
                pstmt.setString(6, row.getConfiguration().equals("") ? null : row.getConfiguration());
                pstmt.setString(7, row.getModel());
                pstmt.setString(8, row.getTool_start());
                pstmt.setString(9, row.getMfg_commit_ship_date());
                pstmt.setString(10, row.getBay());
                pstmt.setString(11, row.getWeek_of_friday());
                pstmt.addBatch();
            }

            // execute the batch
            int[] updateCounts = pstmt.executeBatch();
            int status = checkUpdateCounts(updateCounts);

            pstmt.close();
            con.close();
            if (status == -1) {
                return "Failed adding facility usage";
            }
            return "Successfully added facility usage";
        } catch (Exception e) {
            return "Failed adding facility usage";
        }
    }

    public int checkUpdateCounts(int[] updateCounts) {
        for (int i = 0; i < updateCounts.length; i++) {
//            if (updateCounts[i] >= 0) {
//                System.out.println("OK; updateCount=" + updateCounts[i]);
//            } else if (updateCounts[i] == Statement.SUCCESS_NO_INFO) {
//                System.out.println("OK; updateCount=Statement.SUCCESS_NO_INFO");
//            } else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
//                return -1;
//            }
            if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                return -1;
            }
        }
        return 1;
    }

    public String removeUsage(String staff_id) {
        try {
            Class.forName(driverName);

            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            Statement stmt = con.createStatement();
            String sqlStr = "delete from facility_util where staff_id = '" + staff_id + "'";
            stmt.executeUpdate(sqlStr);
            con.close();

            return "Successfully deleted facility usage";
        } catch (Exception e) {
            System.out.println(e);
            return "Failed deleting facility usage";
        }
    }


}