import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlite:rental_system.db";  // The DB file will be in your project folder
        return DriverManager.getConnection(url);
    }
}


