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

public class userscon extends main.java.connection.mysqlcon {


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
        String my_string = "update users set password = '5f4dcc3b5aa765d61d8327deb882cf99' where username = '" + username + "';";
        stmt.executeUpdate(my_string);
        con.close();
    }

    public boolean resetPasswordWithStaffid(String staffid) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
        String defaultPass = "5f4dcc3b5aa765d61d8327deb882cf99";
        String query = "UPDATE users SET password = ? where staff_id = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, defaultPass);
        pstmt.setString(2, staffid);

        int count = pstmt.executeUpdate();
        con.close();

        return count > 0;
    }

    /**
     * Remove user related data from all the database tables
     *
     * @param staffid
     * @param historyDetails
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean deleteUser(String staffid, ArrayList<HistoryDetails> historyDetails) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();

        con.setAutoCommit(false);

        // delete mass slot upload data
        String queryDeleteMassSlotUpload = "DELETE FROM mass_slot_upload where historyID = ?";
        PreparedStatement pstmtDeleteMSU = con.prepareStatement(queryDeleteMassSlotUpload);
        for (HistoryDetails historyDetail : historyDetails) {
            pstmtDeleteMSU.setString(1, historyDetail.getHistID());
            pstmtDeleteMSU.addBatch();
        }

        // delete baseline data
        String queryDeleteBaseline = "DELETE FROM baseline where staff_id = ?";
        PreparedStatement pstmtDeleteBaseline = con.prepareStatement(queryDeleteBaseline);
        pstmtDeleteBaseline.setString(1, staffid);
        pstmtDeleteBaseline.addBatch();

        // delete history data
        String queryDeleteHistory = "DELETE FROM history where staff_id = ?";
        PreparedStatement pstmtDeleteHistory = con.prepareStatement(queryDeleteHistory);
        pstmtDeleteHistory.setString(1, staffid);
        pstmtDeleteHistory.addBatch();

        // delete user data
        String queryDeleteUser = "DELETE FROM users where staff_id = ?";
        PreparedStatement pstmtDeleteUser = con.prepareStatement(queryDeleteUser);
        pstmtDeleteUser.setString(1, staffid);
        pstmtDeleteUser.addBatch();

        int[] deleteCount = pstmtDeleteMSU.executeBatch();
        int deleteMSUStatus = super.checkUpdateCounts(deleteCount);

        int[] deleteBaselineCount = pstmtDeleteBaseline.executeBatch();
        int deleteBaselineStatus = super.checkUpdateCounts(deleteBaselineCount);

        int[] deleteHistoryCount = pstmtDeleteHistory.executeBatch();
        int deleteHistoryStatus = super.checkUpdateCounts(deleteHistoryCount);

        int[] deleteUserCount = pstmtDeleteUser.executeBatch();
        int deleteUserStatus = super.checkUpdateCounts(deleteUserCount);

        if (deleteMSUStatus == 1 && deleteBaselineStatus == 1 && deleteHistoryStatus == 1 && deleteUserStatus == 1){
            con.commit();
            con.close();
            return true;
        } else{
            con.rollback();
            con.close();
            return false;
        }
    }

    public UserCredentials getUserByStaffId(String staff_id) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
        String query = "select * from users WHERE staff_id = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, staff_id);
        ResultSet rs = pstmt.executeQuery();

        UserCredentials rv = null;

        while (rs.next()) {
            rv = new UserCredentials(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
        }
        con.close();
        return rv;
    }

    public UserCredentials convertStaffToAdmin(String staffid) throws SQLException, ClassNotFoundException {
        Connection con = super.getConnection();
        String newRole = "admin";
        String query = "UPDATE users SET role = ? where staff_id = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, newRole);
        pstmt.setString(2, staffid);
        pstmt.executeUpdate();
        con.close();

        return getUserByStaffId(staffid);
    }
}
