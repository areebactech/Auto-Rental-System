import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminDashboard extends JFrame {
    private JFrame currentWindow = this;
    private JLayeredPane layeredPane;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        add(layeredPane, BorderLayout.CENTER);

        JLabel background = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = new ImageIcon("src/images/WM.png");
                g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        background.setBounds(0, 0, 1920, 1080);
        layeredPane.add(background, Integer.valueOf(0));

        // Bottom Navigation
        // Bottom navigation styled like sidebar but at bottom
        JPanel bottomNav = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 30, 30, 230));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);  // Rounded top corners
            }
        };
        bottomNav.setPreferredSize(new Dimension(0, 90)); // Slightly taller
        bottomNav.setLayout(new GridLayout(1, 6, 20, 0)); // 20px spacing between buttons
        bottomNav.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); // Padding around

// Common style for buttons
        String[] navOptions = {"Dashboard", "View Bookings", "Add Vehicle", "Users", "Logout"};
        for (String text : navOptions) {
            JButton btn = new JButton(text);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            btn.setBackground(text.equals("Logout") ? new Color(150, 40, 40) : new Color(40, 40, 40));
            btn.setForeground(Color.WHITE);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);

            // Hover effect
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(text.equals("Logout") ? new Color(170, 60, 60) : new Color(60, 60, 60));
                }

                public void mouseExited(MouseEvent e) {
                    btn.setBackground(text.equals("Logout") ? new Color(150, 40, 40) : new Color(40, 40, 40));
                }
            });

            // Action logic
            btn.addActionListener(e -> {
                switch (text) {
                    case "Dashboard" -> {
                        if (currentWindow != this) {
                            currentWindow.setVisible(false);
                            this.setVisible(true);
                            currentWindow = this;
                        }
                    }
                    case "View Bookings" -> {
                        if (currentWindow != this) this.setVisible(true);
                        ViewBookings vb = new ViewBookings(this);
                        currentWindow = vb;
                    }
                    case "Add Vehicle" -> {
                        // Placeholder or open add vehicle form
                        JOptionPane.showMessageDialog(this, "Add Vehicle clicked.");
                    }
                    case "Users" -> {
                        if (currentWindow != this) this.setVisible(true);
                        UserList userList = new UserList(this);
                        currentWindow = userList;
                    }
                    case "Logout" -> {
                        dispose();
                        new LoginPage();
                    }
                }
            });

            bottomNav.add(btn);
        }

// Refresh button (styled same)
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 16));
        refreshBtn.setBackground(new Color(0, 120, 200));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
        refreshBtn.setContentAreaFilled(false);
        refreshBtn.setOpaque(true);
        refreshBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                refreshBtn.setBackground(new Color(0, 140, 220));
            }

            public void mouseExited(MouseEvent e) {
                refreshBtn.setBackground(new Color(0, 120, 200));
            }
        });
        refreshBtn.addActionListener(e -> {
            this.dispose();
            new AdminDashboard();
        });

        bottomNav.add(refreshBtn);
        // Add to main frame
        add(bottomNav, BorderLayout.SOUTH);

        refreshDashboard();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                background.setSize(getWidth(), getHeight());
            }
        });

        setVisible(true);
    }

    private JButton createStyledNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(text.equals("Logout") ? new Color(150, 40, 40) : new Color(50, 50, 50));
        btn.setForeground(Color.WHITE);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        btn.addMouseListener(new HoverEffect(
                btn,
                btn.getBackground(),
                text.equals("Logout") ? new Color(180, 60, 60) : new Color(70, 70, 70)
        ));
        return btn;
    }

    private void refreshDashboard() {
        for (Component comp : layeredPane.getComponents()) {
            int z = layeredPane.getLayer(comp);
            if (z > 1) layeredPane.remove(comp);
        }

        JLabel welcome = new JLabel("Welcome, Admin!");
        welcome.setBounds(580, 90, 300, 30);
        welcome.setFont(new Font("Arial", Font.BOLD, 24));
        welcome.setForeground(Color.WHITE);
        layeredPane.add(welcome, Integer.valueOf(2));

        JLabel datetime = new JLabel(new SimpleDateFormat("EEEE, MMM dd yyyy - HH:mm").format(new Date()));
        datetime.setBounds(1000, 90, 400, 20);
        datetime.setFont(new Font("Arial", Font.PLAIN, 14));
        datetime.setForeground(Color.LIGHT_GRAY);
        layeredPane.add(datetime, Integer.valueOf(2));

        String[] cardTitles = {"Total Bookings", "Pending Approvals", "Active Vehicles", "Registered Users", "Revenue Today"};
        int[] values = new int[5];

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) FROM Bookings");
            if (rs1.next()) values[0] = rs1.getInt(1);

            ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) FROM Bookings WHERE status = 'Pending'");
            if (rs2.next()) values[1] = rs2.getInt(1);

            ResultSet rs3 = stmt.executeQuery("SELECT COUNT(*) FROM Vehicles WHERE status = 'Active'");
            if (rs3.next()) values[2] = rs3.getInt(1);

            ResultSet rs4 = stmt.executeQuery("SELECT COUNT(*) FROM Users");
            if (rs4.next()) values[3] = rs4.getInt(1);

            ResultSet rs5 = stmt.executeQuery("SELECT ISNULL(SUM(total_price), 0) FROM Bookings WHERE booking_date = CONVERT(date, GETDATE())");
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
            card.setBounds(250 + (i * 210), 170, 200, 90);
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

        JPanel bookingsPanel = new JPanel();
        bookingsPanel.setLayout(new BoxLayout(bookingsPanel, BoxLayout.Y_AXIS));
        bookingsPanel.setBackground(new Color(0, 0, 0, 100));

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT TOP 5 customer_name, rental_item, booking_date, status FROM Bookings ORDER BY booking_date DESC")) {

            while (rs.next()) {
                String bookingStr = rs.getString("customer_name") + " | " +
                        rs.getString("rental_item") + " | " +
                        rs.getDate("booking_date") + " | " +
                        rs.getString("status");

                JLabel booking = new JLabel(bookingStr);
                booking.setForeground(Color.WHITE);
                booking.setFont(new Font("Arial", Font.PLAIN, 14));
                booking.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                bookingsPanel.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load bookings: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scroll = new JScrollPane(bookingsPanel);
        scroll.setBounds(250, 290, 500, 200);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Recent Bookings",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                Color.WHITE
        ));
        layeredPane.add(scroll, Integer.valueOf(2));

        layeredPane.repaint();
        layeredPane.revalidate();
    }

    // Utility class for hover effect
    private static class HoverEffect extends MouseAdapter {
        private final JButton button;
        private final Color baseColor, hoverColor;

        public HoverEffect(JButton button, Color baseColor, Color hoverColor) {
            this.button = button;
            this.baseColor = baseColor;
            this.hoverColor = hoverColor;
        }

        public void mouseEntered(MouseEvent e) {
            button.setBackground(hoverColor);
        }

        public void mouseExited(MouseEvent e) {
            button.setBackground(baseColor);
        }
    }
}











