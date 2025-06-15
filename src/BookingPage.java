import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class BookingPage extends JFrame {
    private JTextField nameField, phoneField, cnicField, addressField, dateField, durationField;
    private JRadioButton[] radioButtons;
    private ButtonGroup vehicleGroup;

    public BookingPage() {
        setupWindow();
        createUI();
        setVisible(true);
    }

    private void setupWindow() {
        setTitle("Vehicle Rental Booking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 950);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
    }

    private void createUI() {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        add(layeredPane, BorderLayout.CENTER);

        // Background
        JLabel background = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    ImageIcon bg = new ImageIcon(getClass().getResource("/images/book.png"));
                    g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    setBackground(new Color(30, 30, 50));
                    setOpaque(true);
                }
            }
        };
        background.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(background, Integer.valueOf(0));

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                background.setSize(getWidth(), getHeight());
            }
        });

        // Form Panel
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(new Color(0, 0, 0, 130));
        formPanel.setBounds(330, 220, 700, 500);
        layeredPane.add(formPanel, Integer.valueOf(1));

        createFormFields(formPanel);
        createVehicleOptions(formPanel);
        createButtons(formPanel);
    }

    private void createFormFields(JPanel panel) {
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        nameField = addField(panel, "Full Name:", labelFont, fieldFont, 40, 50);
        phoneField = addField(panel, "Phone No:", labelFont, fieldFont, 380, 50);
        cnicField = addField(panel, "CNIC:", labelFont, fieldFont, 40, 130);
        addressField = addField(panel, "Address:", labelFont, fieldFont, 380, 130);
        dateField = addField(panel, "Date(yyyy-MM-dd):", labelFont, fieldFont, 40, 190);
        durationField = addField(panel, "Duration (days):", labelFont, fieldFont, 380, 190);
    }

    private void createVehicleOptions(JPanel panel) {
        JLabel vehicleLabel = new JLabel("Select Vehicle:");
        vehicleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        vehicleLabel.setForeground(Color.WHITE);
        vehicleLabel.setBounds(30, 240, 120, 30);
        panel.add(vehicleLabel);

        String[] vehicles = {"Car", "Bike", "Truck", "Bus", "Cycle"};
        vehicleGroup = new ButtonGroup();
        radioButtons = new JRadioButton[vehicles.length];
        int x = 160;

        for (int i = 0; i < vehicles.length; i++) {
            JRadioButton rb = new JRadioButton(vehicles[i]);
            rb.setBounds(x, 240, 80, 30);
            styleRadioButton(rb);
            vehicleGroup.add(rb);
            panel.add(rb);
            radioButtons[i] = rb;
            x += 90;
        }
    }

    private void createButtons(JPanel panel) {
        JButton bookBtn = new JButton("Confirm Booking");
        styleButton(bookBtn);
        bookBtn.setBounds(250, 330, 200, 45);
        bookBtn.addActionListener(e -> handleBooking());
        panel.add(bookBtn);

        JButton logoutBtn = new JButton("Exit");
        styleButton(logoutBtn);
        logoutBtn.setBounds(250, 390, 200, 45);
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginPage();
        });
        panel.add(logoutBtn);
    }

    private void handleBooking() {
        // Get and validate input
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String cnic = cnicField.getText().trim();
        String address = addressField.getText().trim();
        String dateStr = dateField.getText().trim();
        String durationStr = durationField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || cnic.isEmpty() || address.isEmpty() ||
                dateStr.isEmpty() || durationStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String vehicle = getSelectedVehicle();
        if (vehicle == null) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle.", "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate duration and date
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
            bookingDate = LocalDate.parse(dateStr);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Enter booking date in yyyy-MM-dd format.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calculate price and save
        int total = calculatePrice(vehicle, days);
        saveToDatabase(name, phone, cnic, address, bookingDate, vehicle, days, total);
    }

    private String getSelectedVehicle() {
        for (JRadioButton rb : radioButtons) {
            if (rb.isSelected()) return rb.getText();
        }
        return null;
    }

    private int calculatePrice(String vehicle, int days) {
        int rentPerDay = switch (vehicle.toLowerCase()) {
            case "car" -> 1000;
            case "bike" -> 500;
            case "truck" -> 1500;
            case "bus" -> 2000;
            case "cycle" -> 200;
            default -> 1000;
        };
        return rentPerDay * days;
    }

    private void saveToDatabase(String name, String phone, String cnic, String address,
                                LocalDate date, String vehicle, int days, int total) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Insert user if doesn't exist
            String checkUserSql = "SELECT COUNT(*) FROM Users WHERE cnic = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkUserSql)) {
                checkStmt.setString(1, cnic);
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                if (rs.getInt(1) == 0) {
                    insertUser(conn, name, phone, cnic, address);
                }
            }

            // Insert booking
            String bookingSql = """
                    INSERT INTO Bookings (customer_name, phone_number, cnic, address, booking_date, rental_item, rental_duration_days, total_price)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)""";
            try (PreparedStatement pstmt = conn.prepareStatement(bookingSql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, phone);
                pstmt.setString(3, cnic);
                pstmt.setString(4, address);
                pstmt.setDate(5, java.sql.Date.valueOf(date));
                pstmt.setString(6, vehicle);
                pstmt.setInt(7, days);
                pstmt.setBigDecimal(8, new java.math.BigDecimal(total));

                if (pstmt.executeUpdate() > 0) {
                    conn.commit();
                    new ConfirmationMsg(this, name, vehicle, date.toString(), days, total).setVisible(true);
                    clearForm();
                } else {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "Booking failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error:\n" + ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void insertUser(Connection conn, String name, String phone, String cnic, String address) throws SQLException {
        String sql = "INSERT INTO Users (name, phone, cnic, address) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, cnic);
            stmt.setString(4, address);
            stmt.executeUpdate();
        }
    }

    private void clearForm() {
        nameField.setText("");
        phoneField.setText("");
        cnicField.setText("");
        addressField.setText("");
        dateField.setText("");
        durationField.setText("");
        vehicleGroup.clearSelection();
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







