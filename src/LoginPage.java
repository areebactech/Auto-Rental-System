import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {

    public LoginPage() {
        setupWindow();
        addComponents();
        setVisible(true);
    }

    private void setupWindow() {
        setTitle("Vehicle Rental Login");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        setBackground(new Color(0, 0, 0));

        // Try to load background image
        try {
            ImageIcon background = new ImageIcon(getClass().getResource("/images/background.png"));
            JLabel backgroundLabel = new JLabel(background);
            backgroundLabel.setLayout(null);
            setContentPane(backgroundLabel);
        } catch (Exception e) {
            System.out.println("Background image not found, using solid color");
        }
    }

    private void addComponents() {
        // Title
        JLabel title = new JLabel("Auto Rental System");
        title.setBounds(350, 40, 300, 40);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        add(title);

        // Subtitle
        JLabel subtitle = new JLabel("Start as");
        subtitle.setBounds(655, 115, 150, 40);
        subtitle.setFont(new Font("Arial", Font.BOLD, 20));
        subtitle.setForeground(Color.WHITE);
        add(subtitle);

        // Admin Button
        JButton adminBtn = new JButton("Admin");
        adminBtn.setBounds(630, 180, 150, 40);
        adminBtn.setBackground(new Color(100, 150, 200));
        adminBtn.setForeground(Color.WHITE);
        adminBtn.setFocusPainted(false);
        add(adminBtn);

        // User Button
        JButton userBtn = new JButton("User");
        userBtn.setBounds(630, 250, 150, 40);
        userBtn.setBackground(new Color(100, 200, 150));
        userBtn.setForeground(Color.WHITE);
        userBtn.setFocusPainted(false);
        add(userBtn);

        // Button actions
        adminBtn.addActionListener(e -> {
            try {
                new AdminLoginPage();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error opening Admin Login");
            }
        });

        userBtn.addActionListener(e -> {
            try {
                new BookingPage();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error opening User Page");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage());
    }
}


