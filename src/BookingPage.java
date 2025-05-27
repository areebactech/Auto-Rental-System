import javax.swing.*;
import java.awt.*;

public class BookingPage extends JFrame {

    public BookingPage() {
        setTitle("Vehicle Rental Booking");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.BLACK);

        // Background
        JLabel background = new JLabel(new ImageIcon("src/background.png"));
        background.setLayout(null);
        setContentPane(background);

        // Shared font
        Font labelFont = new Font("Arial", Font.PLAIN, 16);

        // Title
        background.add(createLabel("Book Your Vehicle", new Font("Arial", Font.BOLD, 30), 600, 100, 400, 40));

        // Name
        background.add(createLabel("Your Name:", labelFont, 550, 170, 120, 30));
        JTextField nameField = createTextField(labelFont, 680, 170, 220, 30);
        background.add(nameField);

        // Vehicle
        background.add(createLabel("Select Vehicle:", labelFont, 550, 220, 120, 30));
        JComboBox<String> vehicleCombo = new JComboBox<>(new String[]{"Car", "Bike", "Truck"});
        vehicleCombo.setFont(labelFont);
        vehicleCombo.setBounds(680, 220, 220, 30);
        background.add(vehicleCombo);

        // Book button
        JButton bookButton = new JButton("Book Now");
        bookButton.setFont(new Font("Arial", Font.BOLD, 16));
        bookButton.setBackground(new Color(70, 130, 180));
        bookButton.setForeground(Color.WHITE);
        bookButton.setBounds(680, 280, 120, 35);
        bookButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String vehicle = (String) vehicleCombo.getSelectedItem();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else {
                new BookingDetails(name, vehicle);
            }
        });
        background.add(bookButton);

        setVisible(true);
    }

    private JLabel createLabel(String text, Font font, int x, int y, int w, int h) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, w, h);
        return label;
    }

    private JTextField createTextField(Font font, int x, int y, int w, int h) {
        JTextField field = new JTextField();
        field.setFont(font);
        field.setBounds(x, y, w, h);
        return field;
    }
}

