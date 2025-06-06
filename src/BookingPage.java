import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class BookingPage extends JFrame {

    public BookingPage() {
        setTitle("Vehicle Rental Booking");
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);

        JLabel background = new JLabel(new ImageIcon("src/book.png"));
        background.setLayout(null);
        setContentPane(background);

        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(new Color(0, 0, 0, 130));
        formPanel.setBounds(330, 200, 700, 500);
        background.add(formPanel);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Fields
        JTextField nameField = addField(formPanel, "Full Name:", labelFont, fieldFont, 30, 60);
        JTextField phoneField = addField(formPanel, "Phone No:", labelFont, fieldFont, 370, 60);
        JTextField cnicField = addField(formPanel, "CNIC:", labelFont, fieldFont, 30, 120);
        JTextField addressField = addField(formPanel, "Address:", labelFont, fieldFont, 370, 120);
        JTextField dateField = addField(formPanel, "Book Date(yyyy-MM-dd):", labelFont, fieldFont, 30, 180);
        JTextField durationField = addField(formPanel, "Duration (days):", labelFont, fieldFont, 370, 180);

        // Vehicle options
        JLabel vehicleLabel = new JLabel("Select Vehicle:");
        vehicleLabel.setFont(labelFont);
        vehicleLabel.setForeground(Color.WHITE);
        vehicleLabel.setBounds(30, 240, 120, 30);
        formPanel.add(vehicleLabel);

        String[] vehicles = {"Car", "Bike", "Truck", "Bus", "Cycle"};
        ButtonGroup vehicleGroup = new ButtonGroup();
        int x = 160;
        JRadioButton[] radioButtons = new JRadioButton[vehicles.length];

        for (int i = 0; i < vehicles.length; i++) {
            JRadioButton rb = new JRadioButton(vehicles[i]);
            rb.setBounds(x, 240, 80, 30);
            styleRadioButton(rb);
            vehicleGroup.add(rb);
            formPanel.add(rb);
            radioButtons[i] = rb;
            x += 90;
        }

        // Confirm Button
        JButton bookBtn = new JButton("Confirm Booking");
        styleButton(bookBtn);
        bookBtn.setBounds(250, 330, 200, 45);
        formPanel.add(bookBtn);

        // Logout Button
        JButton logoutBtn = new JButton("Logout");
        styleButton(logoutBtn);
        logoutBtn.setBounds(1230, 710, 100, 35);
        background.add(logoutBtn);

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        // Booking Logic
        bookBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String cnic = cnicField.getText().trim();
            String address = addressField.getText().trim();
            String dateStr = dateField.getText().trim();
            String durationStr = durationField.getText().trim();

            // Validate input
            if (name.isEmpty() || phone.isEmpty() || cnic.isEmpty() || address.isEmpty() ||
                    dateStr.isEmpty() || durationStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Missing Info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String vehicle = null;
            for (JRadioButton rb : radioButtons) {
                if (rb.isSelected()) {
                    vehicle = rb.getText();
                    break;
                }
            }

            if (vehicle == null) {
                JOptionPane.showMessageDialog(this, "Please select a vehicle.", "Missing Info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int days;
            try {
                days = Integer.parseInt(durationStr);
                if (days <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid duration in days.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate bookingDate;
            try {
                bookingDate = LocalDate.parse(dateStr); // yyyy-MM-dd format expected
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Enter booking date in yyyy-MM-dd format.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int rentPerDay = switch (vehicle.toLowerCase()) {
                case "car" -> 1000;
                case "bike" -> 500;
                case "truck" -> 1500;
                case "bus" -> 2000;
                case "cycle" -> 200;
                default -> 1000;
            };

            int total = rentPerDay * days;

            // Save booking to DB
            try (Connection conn = DBConnection.getConnection()) {
                String sql = """
                        INSERT INTO Bookings (customer_name, phone_number, cnic, address, booking_date, rental_item, rental_duration_days, total_price)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)""";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, name);
                    pstmt.setString(2, phone);
                    pstmt.setString(3, cnic);
                    pstmt.setString(4, address);
                    pstmt.setDate(5, java.sql.Date.valueOf(bookingDate));
                    pstmt.setString(6, vehicle);
                    pstmt.setInt(7, days);
                    pstmt.setBigDecimal(8, new java.math.BigDecimal(total));

                    int rows = pstmt.executeUpdate();
                    if (rows > 0) {

                        new ConfirmationMsg(this, name, vehicle, dateStr, days, total).setVisible(true);
                        nameField.setText("");
                        phoneField.setText("");
                        cnicField.setText("");
                        addressField.setText("");
                        dateField.setText("");
                        durationField.setText("");
                        vehicleGroup.clearSelection();
                    } else {
                        JOptionPane.showMessageDialog(this, "Booking failed!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error:\n" + ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private JTextField addField(JPanel panel, String labelText, Font labelFont, Font fieldFont, int x, int y) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 160, 25);
        panel.add(label);

        JTextField field = new JTextField();
        field.setFont(fieldFont);
        field.setBounds(x + 160, y, 180, 30);
        field.setBackground(Color.BLACK);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        panel.add(field);
        return field;
    }

    private void styleRadioButton(JRadioButton rb) {
        rb.setOpaque(true);
        rb.setBackground(Color.BLACK);
        rb.setForeground(Color.WHITE);
        rb.setFocusPainted(false);
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rb.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
        rb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}






