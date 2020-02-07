package connection;

import java.sql.*;
import java.util.*;

import main.java.authentication.json.User;
import main.java.authentication.json.HistoryDetails;
import main.java.authentication.json.JsonObject;
import main.java.authentication.json.MassSlotUploadDetails;
import main.java.authentication.json.GetMassSlotUploadResult;

public class mysqlcon {

    private final String connectionPassword = "";
    private final String port = "3307";
    private final String databaseName = "baymaxdb";
    private final String connection = "jdbc:mysql://localhost:" + port + "/" + databaseName + "?useSSL=false&allowPublicKeyRetrieval=true";
    private final String driverName = "com.mysql.jdbc.Driver";

    public User getUser(String username) {
        try {
            Class.forName(driverName);

            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            Statement stmt = con.createStatement();
            String my_string = "select * from users WHERE username = '" + username + "'";
            ResultSet rs = stmt.executeQuery(my_string);

            User rv = null;

            while (rs.next()) {
                rv = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
            }
            con.close();
            return rv;
        } catch (Exception e) {
            System.out.println(e);
            //to be changed
            return new User("error", "error", "error", "error", "error");
        }
    }

    public ArrayList<User> getAllUsers() {
        try {
            Class.forName(driverName);

            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            Statement stmt = con.createStatement();
            String my_string = "select * from users";
            ResultSet rs = stmt.executeQuery(my_string);

            ArrayList<User> rv = new ArrayList<User>();
            while (rs.next()) {
                rv.add(new User(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
            }
            con.close();
            return rv;
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<User>();
        }
    }

    public String addUser(String username, String password, String firstname, String lastname, String department, String role) {
        try {
            Class.forName(driverName);

            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            Statement stmt = con.createStatement();
            String my_string = "insert into users (username, password, firstname, lastname, department, role) values ('" + username + "', '" + password + "', '" + firstname + "', '" + lastname + "', '" + department + "', '" + role + "')";
            stmt.executeUpdate(my_string);
            con.close();
            return "200";
        } catch (Exception e) {
            System.out.println(e);
            return "400";
        }
    }

    public String changePassword(String username, String oldpassword, String newpassword) {
        try {
            Class.forName(driverName);

            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            Statement stmt = con.createStatement();
            String my_string = "update users set password = '" + newpassword + "' where username = '" + username + "' and password = '" + oldpassword + "';";
            stmt.executeUpdate(my_string);
            con.close();

            return "200";
        } catch (Exception e) {
            System.out.println(e);
            return "400";
        }
    }

    public String resetPassword(String username) {
        try {
            Class.forName(driverName);

            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            Statement stmt = con.createStatement();
            String my_string = "update users set password = 'password' where username = '" + username + "';";
            stmt.executeUpdate(my_string);
            con.close();

            return "200";
        } catch (Exception e) {
            System.out.println(e);
            return "400";
        }
    }

    public ArrayList<JsonObject> getHistory(String staffId) {
        try {
            Class.forName(driverName);

            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            Statement stmt = con.createStatement();
            String my_string = "select * from history";
            ResultSet rs = stmt.executeQuery(my_string);

            ArrayList<JsonObject> rv = new ArrayList<>();
            while (rs.next()) {
                rv.add(new HistoryDetails(rs.getString(1), rs.getString(2), rs.getString(3)));
            }
            con.close();
            return rv;

        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<JsonObject>();
        }
    }

    public int addHistory(String staffId) {
        try {
            Class.forName(driverName);

            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            Statement stmt = con.createStatement();
            String my_string = "insert into history (staff_id) values ('" + staffId + "')";
            stmt.executeUpdate(my_string, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
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
            String sqlStr = "delete from history where msu_id = '"+msuId+"'";
            stmt.executeUpdate(sqlStr);

            sqlStr = "delete from mass_slot_upload where msu_id = '"+msuId+"'";
            stmt.executeUpdate(sqlStr);

            con.close();

            return "Success";
        } catch (Exception e) {
            System.out.println(e);
            return "Fail";
        }
    }

    public ArrayList<JsonObject> getMassSlotUpload(String msuId){
        try {
            Class.forName(driverName);

            Connection con = DriverManager.getConnection(connection, "root", connectionPassword);
            Statement stmt = con.createStatement();
            String my_string = "select * from mass_slot_upload where msu_id = '"+msuId+"'";
            ResultSet rs = stmt.executeQuery(my_string);

            ArrayList<JsonObject> rv = new ArrayList<>();
            while (rs.next()) {
                rv.add(new GetMassSlotUploadResult(
                        rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),rs.getString(10),
                        rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14),rs.getString(15),
                        rs.getString(16), rs.getString(17), rs.getString(18), rs.getString(19),rs.getString(20),
                        rs.getString(21), rs.getString(22), rs.getString(23), rs.getString(24),rs.getString(25),
                        rs.getString(26), rs.getString(27), rs.getString(28), rs.getString(29),rs.getString(30),
                        rs.getString(31), rs.getString(32), rs.getString(33), rs.getString(34),rs.getString(35),
                        rs.getString(36), rs.getString(37), rs.getString(38), rs.getString(39),rs.getString(40),
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



}