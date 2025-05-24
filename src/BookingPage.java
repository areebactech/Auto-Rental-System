import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookingPage extends JFrame {

    public BookingPage() {
        // Set up frame
        setTitle("Vehicle Rental Booking");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.black);


        // Load background image
        ImageIcon backgroundIcon = new ImageIcon("src/background.png");
        JLabel background = new JLabel(backgroundIcon);
        background.setLayout(null);
        setContentPane(background);

        // Title
        JLabel titleLabel = new JLabel("Book Your Vehicle");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(600, 100, 400, 40);
        background.add(titleLabel);

        // Name
        JLabel nameLabel = new JLabel("Your Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(550, 170, 120, 30);
        background.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        nameField.setBounds(680, 170, 220, 30);
        background.add(nameField);

        // Vehicle selection
        JLabel vehicleLabel = new JLabel("Select Vehicle:");
        vehicleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        vehicleLabel.setForeground(Color.WHITE);
        vehicleLabel.setBounds(550, 220, 120, 30);
        background.add(vehicleLabel);

        String[] vehicles = {"Car", "Bike", "Truck"};
        JComboBox<String> vehicleCombo = new JComboBox<>(vehicles);
        vehicleCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        vehicleCombo.setBounds(680, 220, 220, 30);
        background.add(vehicleCombo);

        // Book Now Button
        JButton bookButton = new JButton("Book Now");
        bookButton.setFont(new Font("Arial", Font.BOLD, 16));
        bookButton.setBackground(new Color(70, 130, 180));
        bookButton.setForeground(Color.WHITE);
        bookButton.setBounds(680, 280, 120, 35);
        background.add(bookButton);


        // Button click behavior
        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String vehicle = (String) vehicleCombo.getSelectedItem();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(BookingPage.this, "Please enter your name.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Open next page
                    new BookingDetails(name, vehicle);
                }
            }
        });

        setVisible(true);
    }
}
