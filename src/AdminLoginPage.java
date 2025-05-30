import javax.swing.*;
import java.awt.*;

public class AdminLoginPage extends JFrame {

    public AdminLoginPage() {
        setTitle("Admin Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);
        setLayout(null);
        setResizable(false);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(60, 70, 100, 30);
        userLabel.setForeground(Color.WHITE);
        add(userLabel);

        JTextField username = new JTextField();
        username.setBounds(160, 70, 180, 30);
        add(username);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(60, 120, 100, 30);
        passLabel.setForeground(Color.WHITE);
        add(passLabel);

        JPasswordField password = new JPasswordField();
        password.setBounds(160, 120, 180, 30);
        add(password);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(190, 180, 120, 35);
        loginBtn.setBackground(new Color(100, 150, 200));
        loginBtn.setForeground(Color.WHITE);
        add(loginBtn);

        loginBtn.addActionListener(e -> {
            String user = username.getText();
            String pass = new String(password.getPassword());

            if (user.equals("admin") && pass.equals("1234")) {
                dispose();
                new AdminDashboard(); // Next step
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}

