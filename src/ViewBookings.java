import javax.swing.*;
import java.awt.*;


public class ViewBookings extends JFrame {
    private final AdminDashboard dashboard;

    public ViewBookings(AdminDashboard dashboard) {
        this.dashboard = dashboard;

        setTitle("All Bookings");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("All Bookings", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        add(label, BorderLayout.NORTH);

        setVisible(true);
    }
}


