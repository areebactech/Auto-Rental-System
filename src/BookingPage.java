import javax.swing.*;
import java.awt.*;

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

        // Row 1
        JTextField nameField = addField(formPanel, "Full Name:", labelFont, fieldFont, 30, 60);
        JTextField phoneField = addField(formPanel, "Phone No:", labelFont, fieldFont, 370, 60);

        // Row 2
        JTextField cnicField = addField(formPanel, "CNIC:", labelFont, fieldFont, 30, 120);
        JTextField addressField = addField(formPanel, "Address:", labelFont, fieldFont, 370, 120);

        // Row 3
        JTextField dateField = addField(formPanel, "Booking Date:", labelFont, fieldFont, 30, 180);
        JTextField durationField = addField(formPanel, "Duration (days):", labelFont, fieldFont, 370, 180);

        // Vehicle selection using radio buttons
        JLabel vehicleLabel = new JLabel("Select Vehicle:");
        vehicleLabel.setFont(labelFont);
        vehicleLabel.setForeground(Color.WHITE);
        vehicleLabel.setBounds(30, 240, 120, 30);
        formPanel.add(vehicleLabel);

        String[] vehicles = {"Car", "Bike", "Truck", "Bus", "Cycle"};
        ButtonGroup vehicleGroup = new ButtonGroup();
        int x = 160;

        for (String v : vehicles) {
            JRadioButton rb = new JRadioButton(v);
            rb.setBounds(x, 240, 80, 30);
            styleRadioButton(rb); // Apply dark theme style
            formPanel.add(rb);
            vehicleGroup.add(rb);
            x += 90;
        }

        JButton bookBtn = new JButton("Confirm Booking");
        styleButton(bookBtn);
        bookBtn.setBounds(250, 330, 200, 45);
        formPanel.add(bookBtn);

        JButton logoutBtn = new JButton("Logout");
        styleButton(logoutBtn);
        logoutBtn.setBounds(1230, 710, 100, 35);
        background.add(logoutBtn);

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginPage();  // Open LoginPage on logout
        });

        bookBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String cnic = cnicField.getText().trim();
            String address = addressField.getText().trim();
            String date = dateField.getText().trim();
            String daysStr = durationField.getText().trim();

            String vehicle = null;
            for (Component c : formPanel.getComponents()) {
                if (c instanceof JRadioButton rb && rb.isSelected()) {
                    vehicle = rb.getText();
                    break;
                }
            }

            if (name.isEmpty() || phone.isEmpty() || cnic.isEmpty() || address.isEmpty() || date.isEmpty() || daysStr.isEmpty() || vehicle == null) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Missing Info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int days;
            try {
                days = Integer.parseInt(daysStr);
                if (days <= 0) {
                    JOptionPane.showMessageDialog(this, "Duration must be a positive number.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid duration entered.");
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

            new ConfirmationMsg(this, name, vehicle, date, days, total).setVisible(true);

        });

        setVisible(true);
    }

    private void styleRadioButton(JRadioButton rb) {
        rb.setOpaque(true);  // solid background
        rb.setBackground(Color.BLACK);
        rb.setForeground(Color.WHITE);
        rb.setFocusPainted(false);
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rb.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE)); // white underline
        rb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private JTextField addField(JPanel panel, String labelText, Font labelFont, Font fieldFont, int x, int y) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 120, 25);
        panel.add(label);

        JTextField field = new JTextField();
        field.setFont(fieldFont);
        field.setBounds(x + 120, y, 180, 30);

        field.setBackground(Color.BLACK);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));

        panel.add(field);
        return field;
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





