import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminDashboard extends JFrame {
    private final int EXPANDED_WIDTH = 180, COLLAPSED_WIDTH = 60;
    private final String[] navItems = {"Dashboard", "View Bookings", "Add Vehicle", "Users", "Logout"};
    private final String[] iconPaths = {
            "/images/adb.png", "/images/bkg.png", "/images/adv.png",
            "/images/usr.png", "/images/lgt.png"
    };

    private JLayeredPane layeredPane;
    private JPanel sidebar;
    private boolean isExpanded = true;
    private JButton refreshBtn;
    private ViewBookings viewBookings;
    private AddVehicle addVehicle;
    private UserList userList;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        add(layeredPane, BorderLayout.CENTER);

        setupBackground();
        setupSidebar();
        setupRefreshButton();
        refreshDashboard();
        setVisible(true);
    }

    private void setupBackground() {
        JLabel background = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    ImageIcon bg = loadImageResource("/images/WM.png");
                    if (bg != null) {
                        g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
                    }
                } catch (Exception e) {
                    g.setColor(new Color(45, 45, 45));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        background.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(background, Integer.valueOf(0));

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                sidebar.setBounds(0, 0, isExpanded ? EXPANDED_WIDTH : COLLAPSED_WIDTH, getHeight());
                background.setSize(getWidth(), getHeight());
                refreshBtn.setBounds(getWidth() - 60, 20, 40, 40);
            }
        });
    }

    private ImageIcon loadImageResource(String path) {
        try {
            URL imageUrl = getClass().getResource(path);
            return imageUrl != null ? new ImageIcon(imageUrl) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private void setupSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(173, 216, 230, 220));
        sidebar.setBounds(0, 0, EXPANDED_WIDTH, getHeight());
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        layeredPane.add(sidebar, Integer.valueOf(1));
        updateSidebarButtons();
    }

    private void updateSidebarButtons() {
        sidebar.removeAll();
        sidebar.add(Box.createVerticalStrut(10));

        JButton toggle = new JButton();
        ImageIcon toggleIcon = loadImageResource("/images/dd.png");
        if (toggleIcon != null) {
            toggle.setIcon(new ImageIcon(toggleIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
        } else {
            toggle.setText("☰");
        }
        toggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggle.setBackground(new Color(45, 100, 180));
        toggle.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
        toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggle.setFocusPainted(false);
        toggle.addActionListener(e -> animateSidebar());
        sidebar.add(toggle);
        sidebar.add(Box.createVerticalStrut(20));

        for (int i = 0; i < navItems.length; i++) {
            JButton btn = createNavButton(navItems[i], iconPaths[i]);
            sidebar.add(Box.createVerticalStrut(10));
            sidebar.add(btn);
        }
        sidebar.revalidate();
        sidebar.repaint();
    }

    private JButton createNavButton(String text, String iconPath) {
        ImageIcon icon = loadImageResource(iconPath);
        JButton btn = new JButton(isExpanded ? text : "");
        if (icon != null) {
            btn.setIcon(new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        }

        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(160, 40));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setForeground(Color.DARK_GRAY);
        btn.setBackground(new Color(173, 216, 230));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(isExpanded ? SwingConstants.LEFT : SwingConstants.CENTER);
        btn.setIconTextGap(isExpanded ? 10 : 0);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(135, 206, 250)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(173, 216, 230)); }
        });

        btn.addActionListener(e -> handleNavigation(text));
        return btn;
    }

    public void refreshUserListIfOpen() {
        if (userList != null) {
            userList.loadUsers();  // or whatever method refreshes your JTable
        }
    }


    private void animateSidebar() {
        int start = sidebar.getWidth();
        int end = isExpanded ? COLLAPSED_WIDTH : EXPANDED_WIDTH;
        int step = (end - start) / 10;
        Timer timer = new Timer(15, null);

        timer.addActionListener(new ActionListener() {
            int current = start;
            public void actionPerformed(ActionEvent e) {
                current += step;
                if ((step > 0 && current >= end) || (step < 0 && current <= end)) {
                    current = end;
                    timer.stop();
                    isExpanded = !isExpanded;
                    updateSidebarButtons();
                }
                sidebar.setBounds(0, 0, current, getHeight());
            }
        });
        timer.start();
    }

    private void setupRefreshButton() {
        refreshBtn = new JButton();
        ImageIcon refreshIcon = loadImageResource("/images/refresh.png");
        if (refreshIcon != null) {
            refreshBtn.setIcon(new ImageIcon(refreshIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        } else {
            refreshBtn.setText("⟳");
        }
        refreshBtn.setToolTipText("Refresh Dashboard");
        refreshBtn.setContentAreaFilled(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshBtn.setBounds(getWidth() - 60, 20, 40, 40);
        refreshBtn.addActionListener(e -> refreshDashboard());
        layeredPane.add(refreshBtn, Integer.valueOf(3));
    }

    private void handleNavigation(String option) {
        closeAllWindows();
        switch (option) {
            case "Dashboard" -> refreshDashboard();
            case "View Bookings" -> viewBookings = new ViewBookings(this);
            case "Add Vehicle" -> addVehicle = new AddVehicle(this);
            case "Users" -> userList = new UserList(this);
            case "Logout" -> {
                dispose();
                new LoginPage();
            }
        }
    }

    private void closeAllWindows() {
        if (viewBookings != null) { viewBookings.dispose(); viewBookings = null; }
        if (addVehicle != null) { addVehicle.dispose(); addVehicle = null; }
        if (userList != null) { userList.dispose(); userList = null; }
    }

    public void refreshDashboard() {
        for (Component comp : layeredPane.getComponentsInLayer(2)) {
            if (comp instanceof JPanel || comp instanceof JLabel) layeredPane.remove(comp);
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

        String[] titles = {"Total Bookings", "Pending Approvals", "Active Vehicles", "Registered Users", "Revenue Today"};
        int[] values = fetchDashboardData();

        for (int i = 0; i < titles.length; i++) {
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
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));

            JLabel title = new JLabel(titles[i], SwingConstants.CENTER);
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

    private int[] fetchDashboardData() {
        int[] data = new int[5];
        String query = """
            SELECT 
                (SELECT COUNT(*) FROM Bookings) as total_bookings,
                (SELECT COUNT(*) FROM Bookings WHERE status = 'Pending') as pending_approvals,
                (SELECT COUNT(*) FROM Vehicles WHERE status = 'Available') as active_vehicles,
                (SELECT COUNT(*) FROM Users) as registered_users,
                (SELECT ISNULL(SUM(total_price), 0) FROM Bookings 
                 WHERE CAST(booking_date AS DATE) = CAST(GETDATE() AS DATE)) as revenue_today
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                data[0] = rs.getInt("total_bookings");
                data[1] = rs.getInt("pending_approvals");
                data[2] = rs.getInt("active_vehicles");
                data[3] = rs.getInt("registered_users");
                data[4] = rs.getInt("revenue_today");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}




















