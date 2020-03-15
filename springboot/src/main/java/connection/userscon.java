package main.java.connection;

import main.java.authentication.json.users.RegistrationDetails;
import main.java.authentication.json.users.User;
import main.java.authentication.json.users.UserCredentials;
import main.java.authentication.json.JsonObject;
import main.java.authentication.json.HistoryDetails;
// import main.java.authentication.Token;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class userscon extends mysqlcon {

    public RegistrationDetails getUser(String username) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
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

    public UserCredentials getUserCredentials(String username) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String my_string = "select * from users WHERE username = '" + username + "'";
        ResultSet rs = stmt.executeQuery(my_string);

        UserCredentials rv = null;

        while (rs.next()) {
            rv = new UserCredentials(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
        }
        con.close();
        return rv;
    }


    public ArrayList<User> getAllUsers() throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
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
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        String my_string = "insert into users (username, password, firstname, lastname, department, role) values ('" + username + "', '" + password + "', '" + firstname + "', '" + lastname + "', '" + department + "', '" + role + "')";
        stmt.executeUpdate(my_string);
        con.close();
    }
    
    /*
    public void deleteUser(String username) throws SQLException, ClassNotFoundException {
        if(getUser(username)) {
          Connection con = super.getConnection();
          Statement stmt = con.createStatement();
          //Verify Admin token for extra security if needed
          String my_string = "DELETE FROM users WHERE username = '" + username + "'";
          stmt.executeUpdate(my_string);
          con.close();
        } else {
          throw new Exception("User not found!");
        }
    }
    */

    public boolean verifyPassword(String username, String password) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();

        String query = "select password from users where username = ?;";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next())
            return password.contains(rs.getString(1));

        return false;
    }

    public void changePassword(String username, String oldpassword, String newpassword) throws SQLException, ClassNotFoundException, Exception {

        if (verifyPassword(username, oldpassword)) {
            Connection con = super.getConnection();
            Statement stmt = con.createStatement();
            String my_string = "update users set password = '" + newpassword + "' where username = '" + username + "';";
            stmt.executeUpdate(my_string);
            con.close();
        } else {
            throw new Exception("Invalid old password");
        }
    }

    public void resetPassword(String username) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
        Statement stmt = con.createStatement();
        // String pw = TOKEN.generateMD5Hash("password");
        String my_string = "update users set password = '5f4dcc3b5aa765d61d8327deb882cf99' where username = '" + username + "';";
        stmt.executeUpdate(my_string);
        con.close();
    }

    // following sections of code will have to be redone completely for baseline table
    // please use the following for reference
//    public ArrayList<main.java.authentication.json.GetFacilityUtilResult> readFacilityUtil(String staffId) throws SQLException, ClassNotFoundException {
//        Connection con = super.getConnection();
//        Statement stmt = con.createStatement();
//        String my_string = "select * from facility_util where staff_id = '" + staffId + "'";
//        ResultSet rs = stmt.executeQuery(my_string);
//
//        ArrayList<main.java.authentication.json.GetFacilityUtilResult> rv = new ArrayList<>();
//        while (rs.next()) {
//            rv.add(new main.java.authentication.json.GetFacilityUtilResult(
//                    rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
//                    rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),
//                    rs.getString(11)
//            ));
//        }
//        con.close();
//
//        if (rv.size() == 0) {
//            return new ArrayList<main.java.authentication.json.GetFacilityUtilResult>();
//        }
//
//        return rv;
//    }
//
//    /**
//     * @return -1 if there is an error
//     */
//    public int getNextFaciId() {
//        try {
//            Connection con = super.getConnection();
//            Statement stmt = con.createStatement();
//            String my_string = "select max(faci_id) from facility_util";
//            ResultSet rs = stmt.executeQuery(my_string);
//
//            if (rs.next()) {
//                return rs.getInt(1) + 1;
//            }
//
//            return 1;
//        } catch (Exception e) {
//            return -1;
//        }
//    }
//
//    public String createFacilityUtil(ArrayList<main.java.authentication.json.FacilityUtil> data, String staff_id, int faci_id) throws SQLException, ClassNotFoundException {
//        Connection con = super.getConnection();
//        con.setAutoCommit(false);
//        Statement stmt = con.createStatement();
//        String sql = "insert into facility_util (faci_id, staff_id, slot_id_utid, sales_order, customer, configuration, model, tool_start, mfg_commit_ship_date, bay, week_of_friday) ";
//        sql += "values (?,?,?,?,?,?,?,?,?,?,?)";
//        PreparedStatement pstmt = con.prepareStatement(sql);
//
//        for (main.java.authentication.json.FacilityUtil row : data) {
//            pstmt.setString(1, Integer.toString(faci_id));
//            pstmt.setString(2, staff_id);
//            pstmt.setString(3, row.getSlot_id_utid().equals("") ? null : row.getSlot_id_utid());
//            pstmt.setString(4, row.getSales_order());
//            pstmt.setString(5, row.getCustomer());
//            pstmt.setString(6, row.getConfiguration().equals("") ? null : row.getConfiguration());
//            pstmt.setString(7, row.getModel());
//            pstmt.setString(8, row.getTool_start());
//            pstmt.setString(9, row.getMfg_commit_ship_date());
//            pstmt.setString(10, row.getBay());
//            pstmt.setString(11, row.getWeek_of_friday());
//            pstmt.addBatch();
//        }
//
//        // execute the batch
//        int[] updateCounts = pstmt.executeBatch();
//        int status = super.checkUpdateCounts(updateCounts);
//
//        pstmt.close();
//        con.close();
//        if (status == -1) {
//            return "Failed adding facility usage";
//        }
//        return "Successfully added facility usage";
//    }
//
//    public String removeUsage(String staff_id) throws SQLException, ClassNotFoundException {
//        Connection con = super.getConnection();
//        Statement stmt = con.createStatement();
//        String sqlStr = "delete from facility_util where staff_id = '" + staff_id + "'";
//        stmt.executeUpdate(sqlStr);
//        con.close();
//
//        return "Successfully deleted facility usage";
//    }
}
