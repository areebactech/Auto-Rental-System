import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(1366, 768);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set background image
        JLabel background = new JLabel(new ImageIcon("src/WM.png"));
        background.setLayout(null);
        setContentPane(background);

        // Add components TO the background (not to the frame directly)
        JButton logoutButton = getJButton();
        background.add(logoutButton);

        JLabel label = new JLabel("Welcome, Admin!");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.WHITE);
        label.setBounds(590, 100, 400, 40);
        background.add(label);

        setVisible(true);
    }

    private JButton getJButton() {
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(100, 150, 200));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBounds(1230, 710, 100, 35);

        logoutButton.addActionListener(e -> {
            dispose();           // Close dashboard
            new LoginPage();     // Show login page
        });

        return logoutButton;
    }
}


