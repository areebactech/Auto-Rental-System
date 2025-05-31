import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Connected successfully!");

            // Insert a test booking
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO Bookings (customer_name, booking_date, rental_item, rental_duration_days, total_price) " +
                    "VALUES ('Test User', '2025-06-01', 'Bike', 2, 1000.00)";
            int rowsAffected = stmt.executeUpdate(sql);

            if (rowsAffected > 0) {
                System.out.println("Booking inserted successfully.");
            } else {
                System.out.println("Booking insertion failed.");
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Launch Login Page
        new LoginPage();
    }
}

