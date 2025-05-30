import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args)
    {
        try (Connection conn = DBConnection.getConnection()) {
        System.out.println("Connected successfully!");

     } catch (SQLException e) {
        e.printStackTrace();
    }
        new LoginPage();
    }
}