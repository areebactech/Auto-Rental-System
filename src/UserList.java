import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserList extends JFrame {
    private DefaultTableModel model;

    public UserList(JFrame parentFrame) {
        setTitle("Registered Users");
        setSize(600, 400);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columnNames = {"User ID", "Name", "Phone", "CNIC", "Address"};
        model = new DefaultTableModel(columnNames, 0); // Move model to class-level
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadUsers(); // ← CALL HERE

        setVisible(true);
    }

    public void loadUsers() {
        model.setRowCount(0); // Clear previous rows

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT user_id, name, phone, cnic, address FROM Users")) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("cnic"),
                        rs.getString("address")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}



