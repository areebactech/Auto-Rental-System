import javax.swing.*;
import java.awt.*;

import static java.awt.Color.black;

public class LoginPage extends JFrame {

    public LoginPage() {
        initializeFrame();
        setBackgroundImage();
        addTitleLabels();
        addRoleButtons();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Vehicle Rental Login");
        setSize(1000, 600);
        setBackground(black);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
    }

    private void setBackgroundImage() {
        // Use getResource() in production builds for JAR compatibility
        JLabel background = new JLabel(new ImageIcon("src/images/background.png"));
        background.setLayout(null);
        setContentPane(background);
    }

    private void addTitleLabels() {
        JLabel appTitle = new JLabel("Auto Rental System");
        appTitle.setBounds(350, 40, 300, 40);
        appTitle.setFont(new Font("Arial", Font.BOLD, 28));
        appTitle.setForeground(Color.WHITE);
        add(appTitle);

        JLabel roleTitle = new JLabel("Start As");
        roleTitle.setBounds(650, 100, 200, 40);
        roleTitle.setFont(new Font("Arial", Font.BOLD, 24));
        roleTitle.setForeground(Color.WHITE);
        add(roleTitle);
    }

    private void addRoleButtons() {
        JButton adminBtn = new JButton("Admin");
        adminBtn.setBounds(630, 180, 150, 40);
        adminBtn.setBackground(new Color(100, 150, 200));
        adminBtn.setForeground(Color.WHITE);
        adminBtn.setFocusPainted(false);
        add(adminBtn);

        JButton userBtn = new JButton("User");
        userBtn.setBounds(630, 250, 150, 40);
        userBtn.setBackground(new Color(100, 200, 150));
        userBtn.setForeground(Color.WHITE);
        userBtn.setFocusPainted(false);
        add(userBtn);

        // Event handlers
        adminBtn.addActionListener(e -> {
            new AdminLoginPage(); // Keeping LoginPage open intentionally for demo/testing
        });

        userBtn.addActionListener(e -> {
            dispose(); // Close this window before opening user dashboard
            new BookingPage();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}


