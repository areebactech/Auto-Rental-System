import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddVehicle extends JFrame {

    public AddVehicle() {
        setTitle("Add New Vehicle");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.BLACK);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 15);

        JLabel title = new JLabel("Add New Vehicle");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBounds(280, 30, 300, 40);
        add(title);

        // Labels and Fields
        JTextField nameField = addField("Vehicle Name:", 100, labelFont, fieldFont);
        JTextField rentField = addField("Rent Per Day:", 160, labelFont, fieldFont);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(labelFont);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBounds(200, 220, 120, 30);
        add(statusLabel);

        String[] statuses = {"Available", "Unavailable"};
        JComboBox<String> statusBox = new JComboBox<>(statuses);
        statusBox.setFont(fieldFont);
        statusBox.setBounds(330, 220, 200, 30);
        statusBox.setBackground(Color.BLACK);
        statusBox.setForeground(Color.WHITE);
        statusBox.setFocusable(false);
        add(statusBox);

        // Buttons
        JButton addBtn = new JButton("Add Vehicle");
        styleButton(addBtn);
        addBtn.setBounds(250, 300, 150, 40);
        add(addBtn);

        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn);
        cancelBtn.setBounds(420, 300, 100, 40);
        add(cancelBtn);

        // Cancel action
        cancelBtn.addActionListener(e -> dispose());

        // Add vehicle logic
        addBtn.addActionListener((ActionEvent e) -> {
            String name = nameField.getText().trim();
            String rentText = rentField.getText().trim();
            String status = (String) statusBox.getSelectedItem();

            if (name.isEmpty() || rentText.isEmpty() || status == null) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int rent;
            try {
                rent = Integer.parseInt(rentText);
                if (rent <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid rent amount.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO Vehicles (vehicle_name, rent_per_day, status) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, name);
                    stmt.setInt(2, rent);
                    stmt.setString(3, status);

                    int result = stmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(this, "Vehicle added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        nameField.setText("");
                        rentField.setText("");
                        statusBox.setSelectedIndex(0);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to add vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error:\n" + ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private JTextField addField(String label, int y, Font labelFont, Font fieldFont) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(labelFont);
        jLabel.setForeground(Color.WHITE);
        jLabel.setBounds(200, y, 120, 30);
        add(jLabel);

        JTextField field = new JTextField();
        field.setFont(fieldFont);
        field.setBounds(330, y, 200, 30);
        field.setBackground(Color.BLACK);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        add(field);
        return field;
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(0, 123, 255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}

