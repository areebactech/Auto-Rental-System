import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class ViewBookings extends JFrame {
    private JTable bookingsTable;

    public ViewBookings(AdminDashboard dashboard) {
        setTitle("All Bookings");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("All Bookings", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        bookingsTable = createStyledTable();
        add(new JScrollPane(bookingsTable), BorderLayout.CENTER);

        JButton statusBtn = createStatusButton();
        add(statusBtn, BorderLayout.SOUTH);

        loadBookings();
        setVisible(true);
    }

    private JTable createStyledTable() {
        JTable table = new JTable();
        table.setBackground(Color.BLACK);
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setGridColor(Color.DARK_GRAY);

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.DARK_GRAY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, center);

        return table;
    }

    private JButton createStatusButton() {
        JButton btn = new JButton("Change Selected Booking Status");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBackground(Color.DARK_GRAY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);

        btn.addActionListener(e -> {
            int row = bookingsTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a booking first.");
                return;
            }
            int bookingId = (int) bookingsTable.getValueAt(row, 0);
            String currentStatus = (String) bookingsTable.getValueAt(row, 9);
            String[] options = {"Pending", "Confirmed", "Completed", "Cancelled"};

            String newStatus = (String) JOptionPane.showInputDialog(
                    this, "Select new status:", "Change Status",
                    JOptionPane.PLAIN_MESSAGE, null, options, currentStatus);

            if (newStatus != null && !newStatus.equals(currentStatus)) {
                updateStatus(bookingId, newStatus);
                bookingsTable.setValueAt(newStatus, row, 9);
            }
        });

        return btn;
    }

    private void loadBookings() {
        String[] columns = {"Booking ID", "Customer Name", "Phone", "CNIC", "Address", "Booking Date", "Vehicle", "Duration", "Total Price", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        bookingsTable.setModel(model);

        String sql = "SELECT booking_id, customer_name, phone_number, cnic, address, booking_date, rental_item, rental_duration_days, total_price, status FROM Bookings";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("booking_id"),
                        rs.getString("customer_name"),
                        rs.getString("phone_number"),
                        rs.getString("cnic"),
                        rs.getString("address"),
                        rs.getDate("booking_date"),
                        rs.getString("rental_item"),
                        rs.getInt("rental_duration_days"),
                        rs.getBigDecimal("total_price"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading bookings:\n" + e.getMessage());
        }
    }

    private void updateStatus(int bookingId, String status) {
        String sql = "UPDATE Bookings SET status = ? WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, bookingId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to update status:\n" + e.getMessage());
        }
    }
}




