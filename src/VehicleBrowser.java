import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleBrowser extends JFrame {

    private final BookingPage parent;         // to send chosen vehicle back
    private final Color CARD_BG   = new Color(30,30,30);
    private final Color PRIMARY   = new Color(45,100,180);

    public VehicleBrowser(BookingPage parent) {
        this.parent = parent;

        setTitle("Browse Vehicles");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setLayout(new BorderLayout(20,20));

        /* --- heading --- */
        JLabel title = new JLabel("Select Your Vehicle",SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI",Font.BOLD,28));
        title.setForeground(Color.WHITE);
        add(title,BorderLayout.NORTH);

        /* --- dark background --- */
        getContentPane().setBackground(new Color(15,15,15));

        /* --- scrollable card grid --- */
        JPanel grid = new JPanel(new GridLayout(0,2,30,30));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(20,40,20,40));

        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        /* fetch vehicle data */
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT vehicle_name, rent_per_day, vehicle_type FROM Vehicles WHERE status = 'Available'");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("vehicle_name");
                String type = rs.getString("vehicle_type");
                String rent = "PKR " + rs.getInt("rent_per_day") + " / day";
                grid.add(makeCard(new String[]{name, type, rent}));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching vehicles:\n" + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }

        setVisible(true);
    }

    /* single vehicle card */
    private JPanel makeCard(String[] v){
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setPreferredSize(new Dimension(350, 300));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel name = new JLabel(v[0], SwingConstants.CENTER);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        name.setFont(new Font("Segoe UI", Font.BOLD, 16));
        name.setForeground(Color.WHITE);

        JLabel type = new JLabel(v[1], SwingConstants.CENTER);
        type.setAlignmentX(Component.CENTER_ALIGNMENT);
        type.setForeground(Color.GRAY);

        JLabel price = new JLabel(v[2], SwingConstants.CENTER);
        price.setAlignmentX(Component.CENTER_ALIGNMENT);
        price.setForeground(new Color(0, 200, 100));

        JButton book = new JButton("Book Now");
        book.setAlignmentX(Component.CENTER_ALIGNMENT);
        book.setBackground(PRIMARY);
        book.setForeground(Color.WHITE);
        book.setFocusPainted(false);
        book.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        book.setMaximumSize(new Dimension(150, 35));
        book.addActionListener(e -> {
            String priceStr = v[2].replaceAll("[^0-9]", "");
            int perDay = Integer.parseInt(priceStr);
            parent.setSelectedVehicle(v[0], perDay);
            dispose();
        });

        card.add(name);
        card.add(Box.createVerticalStrut(10));
        card.add(type);
        card.add(Box.createVerticalStrut(10));
        card.add(price);
        card.add(Box.createVerticalStrut(15));
        card.add(book);

        return card;
    }
}

