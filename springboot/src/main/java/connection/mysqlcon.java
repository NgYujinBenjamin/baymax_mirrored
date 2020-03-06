package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// import org.springframework.beans.factory.annotation.*;
// import org.springframework.stereotype.*;

// @Component
public class mysqlcon {

    private final String connectionPassword = "";
    private final String port = "3306";
    private final String databaseName = "baymaxdb";
    private final String connection = "jdbc:mysql://localhost:" + port + "/" + databaseName + "?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final String driverName = "com.mysql.cj.jdbc.Driver";
    private final String dbUser = "root";

    /**
     *
     * @return a new MySQL connection to the aforementioned database references
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(driverName);
        // the following is of type Connection
        return DriverManager.getConnection(connection, dbUser, connectionPassword);
    }

    public int checkUpdateCounts(int[] updateCounts) {
        for (int updateCount : updateCounts) {
            if (updateCount == Statement.EXECUTE_FAILED) {
                return -1;
            }
        }
        return 1;
    }


}