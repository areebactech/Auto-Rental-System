import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminDashboard extends JFrame {
    private JLayeredPane layeredPane;
    private JPanel sidebar;
    private boolean isSidebarExpanded = true;
    private final int SIDEBAR_EXPANDED_WIDTH = 180;
    private final int SIDEBAR_COLLAPSED_WIDTH = 60;

    private final String[] navOptions = {"Dashboard", "View Bookings", "Add Vehicle", "Users", "Logout"};

    private JButton refreshBtn;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        add(layeredPane, BorderLayout.CENTER);

        // Background
        JLabel background = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = new ImageIcon("src/images/WM.png");
                g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        background.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(background, Integer.valueOf(0));

        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(173, 216, 230, 220));
        sidebar.setBounds(0, 0, SIDEBAR_EXPANDED_WIDTH, getHeight());
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        updateSidebarButtons();
        layeredPane.add(sidebar, Integer.valueOf(1));

        // Sidebar & background resizing
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                sidebar.setBounds(0, 0, isSidebarExpanded ? SIDEBAR_EXPANDED_WIDTH : SIDEBAR_COLLAPSED_WIDTH, getHeight());
                background.setSize(getWidth(), getHeight());
                refreshBtn.setBounds(getWidth() - 60, 20, 40, 40);
            }
        });

        // Refresh button with image icon
        ImageIcon refreshIcon = new ImageIcon("src/images/refresh.png");
        Image scaledRefresh = refreshIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        refreshBtn = new JButton(new ImageIcon(scaledRefresh));
        refreshBtn.setToolTipText("Refresh Dashboard");
        refreshBtn.setContentAreaFilled(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshBtn.setBounds(getWidth() - 60, 20, 40, 40);
        refreshBtn.addActionListener(e -> {
            dispose();
            new AdminDashboard();
        });
        layeredPane.add(refreshBtn, Integer.valueOf(3));

        refreshDashboard();
        setVisible(true);
    }

    private void updateSidebarButtons() {
        sidebar.removeAll();
        sidebar.add(Box.createVerticalStrut(10));

        // Toggle button
        ImageIcon icon = new ImageIcon("src/images/dd.png");
        Image scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JButton toggle = new JButton(new ImageIcon(scaled));

        toggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        toggle.setFocusPainted(false);
        toggle.setForeground(Color.DARK_GRAY);
        toggle.setBackground(new Color(45, 100, 180)); // Darker blue
        toggle.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
        toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggle.addActionListener(e -> {
            isSidebarExpanded = !isSidebarExpanded;
            sidebar.setBounds(0, 0, isSidebarExpanded ? SIDEBAR_EXPANDED_WIDTH : SIDEBAR_COLLAPSED_WIDTH, getHeight());
            updateSidebarButtons();
        });
        sidebar.add(toggle);
        sidebar.add(Box.createVerticalStrut(20));

        for (String option : navOptions) {
            JButton btn = new JButton(isSidebarExpanded ? option : option.substring(0, 1));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(160, 40));
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setForeground(Color.DARK_GRAY);
            btn.setBackground(new Color(173, 216, 230));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(new Color(135, 206, 250));
                }

                public void mouseExited(MouseEvent e) {
                    btn.setBackground(new Color(173, 216, 230));
                }
            });

            btn.addActionListener(e -> handleNavClick(option));
            sidebar.add(Box.createVerticalStrut(10));
            sidebar.add(btn);
        }

        sidebar.revalidate();
        sidebar.repaint();
    }

    private void handleNavClick(String option) {
        switch (option) {
            case "Dashboard" -> refreshDashboard();
            case "View Bookings" -> new ViewBookings(this);
            case "Add Vehicle" -> JOptionPane.showMessageDialog(this, "Add Vehicle clicked.");
            case "Users" -> new UserList(this);
            case "Logout" -> {
                dispose();
                new LoginPage();
            }
        }
    }

    private void refreshDashboard() {
        for (Component comp : layeredPane.getComponentsInLayer(2)) {
            if (comp instanceof JPanel || comp instanceof JLabel) {
                layeredPane.remove(comp);
            }
        }

        JLabel welcome = new JLabel("Welcome, Admin!");
        welcome.setBounds(590, 90, 300, 30);
        welcome.setFont(new Font("Arial", Font.BOLD, 24));
        welcome.setForeground(Color.WHITE);
        layeredPane.add(welcome, Integer.valueOf(2));

        JLabel datetime = new JLabel(new SimpleDateFormat("EEEE, MMM dd yyyy - HH:mm").format(new Date()));
        datetime.setBounds(1000, 90, 400, 20);
        datetime.setFont(new Font("Arial", Font.PLAIN, 16));
        datetime.setForeground(Color.LIGHT_GRAY);
        layeredPane.add(datetime, Integer.valueOf(2));

        String[] cardTitles = {"Total Bookings", "Pending Approvals", "Active Vehicles", "Registered Users", "Revenue Today"};
        int[] values = new int[5];

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            // Total Bookings
            ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) FROM Bookings");
            if (rs1.next()) values[0] = rs1.getInt(1);

            // Pending Approvals
            ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) FROM Bookings WHERE status = 'Pending'");
            if (rs2.next()) values[1] = rs2.getInt(1);

            // Active Vehicles
            ResultSet rs3 = stmt.executeQuery("SELECT COUNT(*) FROM Vehicles WHERE status = 'Active'");
            if (rs3.next()) values[2] = rs3.getInt(1);

            // Registered Users
            ResultSet rs4 = stmt.executeQuery("SELECT COUNT(*) FROM Users");
            if (rs4.next()) values[3] = rs4.getInt(1);

            // Revenue Today (only today's bookings)
            ResultSet rs5 = stmt.executeQuery("SELECT ISNULL(SUM(total_price), 0) FROM Bookings WHERE CAST(booking_date AS DATE) = CAST(GETDATE() AS DATE)");
            if (rs5.next()) values[4] = rs5.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < cardTitles.length; i++) {
            JPanel card = new JPanel() {
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    g2.setColor(Color.BLACK);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            card.setLayout(new BorderLayout());
            card.setBounds(200 + (i * 210), 170, 200, 90);
            card.setOpaque(false);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 1, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel title = new JLabel(cardTitles[i], SwingConstants.CENTER);
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Arial", Font.BOLD, 14));

            JLabel count = new JLabel(String.valueOf(values[i]), SwingConstants.CENTER);
            count.setForeground(Color.WHITE);
            count.setFont(new Font("Arial", Font.BOLD, 22));

            card.add(title, BorderLayout.NORTH);
            card.add(count, BorderLayout.CENTER);
            layeredPane.add(card, Integer.valueOf(2));
        }

        layeredPane.repaint();
        layeredPane.revalidate();
    }
}
















