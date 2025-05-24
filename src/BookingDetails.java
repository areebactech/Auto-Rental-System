import javax.swing.*;
import java.awt.*;

public class BookingDetails extends JFrame {
    public BookingDetails(String name, String vehicle) {
        setTitle("Finalize Your Booking");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Set black background
        getContentPane().setBackground(Color.BLACK);

        // Booking info
        JLabel infoLabel = new JLabel("Booking for: " + name + " (" + vehicle + ")");
        infoLabel.setBounds(30, 30, 500, 30);
        infoLabel.setForeground(Color.WHITE);
        add(infoLabel);

        // Start Date
        JLabel dateLabel = new JLabel("Start Date (dd-mm-yyyy):");
        dateLabel.setBounds(30, 90, 250, 30);
        dateLabel.setForeground(Color.WHITE);
        add(dateLabel);

        JTextField dateField = new JTextField();
        dateField.setBounds(290, 90, 250, 35);
        dateField.setForeground(Color.black);
        add(dateField);

        // Duration
        JLabel durationLabel = new JLabel("Duration (days):");
        durationLabel.setBounds(30, 150, 250, 30);
        durationLabel.setForeground(Color.WHITE);
        add(durationLabel);

        JTextField durationField = new JTextField();
        durationField.setBounds(290, 150, 250, 35);
        durationField.setBackground(Color.WHITE);

        add(durationField);

        // Confirm Button
        JButton confirmBtn = new JButton("Confirm Booking");
        confirmBtn.setBounds(200, 230, 200, 40);
        confirmBtn.setBackground(new Color(80, 130, 180));
        confirmBtn.setForeground(Color.WHITE);
        add(confirmBtn);

        // Confirm Button Action
        confirmBtn.addActionListener(e -> {
            String date = dateField.getText();
            String durationText = durationField.getText();

            if (date.isEmpty() || durationText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
            } else {
                int days = Integer.parseInt(durationText);
                int rentPerDay;

                // Determine rent based on vehicle type
                if (vehicle.equalsIgnoreCase("Car")) {
                    rentPerDay = 1000;
                } else if (vehicle.equalsIgnoreCase("Bike")) {
                    rentPerDay = 500;
                } else if (vehicle.equalsIgnoreCase("Truck")) {
                    rentPerDay = 1500;
                } else {
                    rentPerDay = 1000;
                }

                int rent = days * rentPerDay;

                // Show custom confirmation window
                new ConfirmationMsg(this, name, vehicle, date, days, rent).setVisible(true);

                dispose(); // Close this booking details window
            }
        });

        setVisible(true);
    }
}
