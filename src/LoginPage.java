import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {

    public LoginPage() {
        setTitle("Vehicle Rental Login");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.BLACK);
        setLayout(null);
        setResizable(false);

        JLabel background = new JLabel(new ImageIcon("src/images/background.png"));
        background.setLayout(null);
        setContentPane(background);

        JLabel VRS = new JLabel("Auto Rental System");
        VRS.setBounds(350,40,300,40);
        VRS.setFont(new Font("Arial", Font.BOLD, 28));
        VRS.setForeground(Color.WHITE);
        add(VRS);

        JLabel title = new JLabel("Login As");
        title.setBounds(650, 100, 200, 40);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        add(title);

        JButton adminBtn = new JButton("Admin");
        adminBtn.setBounds(630, 180, 150, 40);
        adminBtn.setBackground(new Color(100, 150, 200));
        adminBtn.setForeground(Color.WHITE);
        add(adminBtn);

        JButton userBtn = new JButton("User");
        userBtn.setBounds(630, 250, 150, 40);
        userBtn.setBackground(new Color(100, 200, 150));
        userBtn.setForeground(Color.WHITE);
        add(userBtn);

        adminBtn.addActionListener(e -> {
            new AdminLoginPage();
        });

        userBtn.addActionListener(e -> {
            dispose();
            new BookingPage(); // This is already your user dashboard
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}

