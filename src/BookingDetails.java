import javax.swing.*;
import java.awt.*;

public class BookingDetails extends JFrame {
    public BookingDetails(String name, String vehicle) {
        setTitle("Finalize Your Booking");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);
        setLayout(null);

        addLabel("Booking for: " + name + " (" + vehicle + ")", 30, 30, 500, 30);
        addLabel("Start Date (dd-mm-yyyy):", 30, 90, 250, 30);
        JTextField dateField = addTextField(290, 90, 250, 35);

        addLabel("Duration (days):", 30, 150, 250, 30);
        JTextField durationField = addTextField(290, 150, 250, 35);

        JButton confirmBtn = new JButton("Confirm Booking");
        confirmBtn.setBounds(200, 230, 200, 40);
        confirmBtn.setBackground(new Color(80, 130, 180));
        confirmBtn.setForeground(Color.WHITE);
        add(confirmBtn);

        confirmBtn.addActionListener(e -> {
            String date = dateField.getText();
            String daysStr = durationField.getText();

            if (date.isEmpty() || daysStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            int days = Integer.parseInt(daysStr);
            int rentPerDay = switch (vehicle.toLowerCase()) {
                case "car" -> 1000;
                case "bike" -> 500;
                case "truck" -> 1500;
                default -> 1000;
            };

            int rent = days * rentPerDay;
            new ConfirmationMsg(this, name, vehicle, date, days, rent).setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    private void addLabel(String text, int x, int y, int w, int h) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, w, h);
        label.setForeground(Color.WHITE);
        add(label);
    }

    private JTextField addTextField(int x, int y, int w, int h) {
        JTextField field = new JTextField();
        field.setBounds(x, y, w, h);
        add(field);
        return field;
    }
}

