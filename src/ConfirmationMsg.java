import javax.swing.*;
import java.awt.*;

public class ConfirmationMsg extends JDialog {

    public ConfirmationMsg(JFrame parent, String name, String vehicle, String date, int days, int rent) {
        super(parent, "Booking Confirmed", true);
        setSize(450, 300);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Color.BLACK);
        setLayout(null);

        Font titleFont = new Font("Arial", Font.BOLD, 22);
        Font textFont = new Font("Arial", Font.PLAIN, 16);

        addLabel("✔ Booking Confirmed!", titleFont, new Color(80, 130, 180), 100, 20, 300, 30);

        String[] texts = {
                "Name: " + name,
                "Vehicle: " + vehicle,
                "Date: " + date,
                "Duration: " + days + " day(s)",
                "Total Rent for " + days + " days: Rs " + rent
        };

        int y = 70;
        for (String text : texts) {
            addLabel(text, textFont, Color.WHITE, 50, y, 350, 25);
            y += 30; // increment y for next label
        }

        JButton ok = new JButton("OK");
        ok.setFont(textFont);
        ok.setBounds(170, 220, 100, 35);
        ok.setBackground(new Color(70, 130, 180));
        ok.setForeground(Color.WHITE);
        ok.setFocusPainted(false);
        ok.addActionListener(e -> dispose());
        add(ok);
    }

    private void addLabel(String text, Font font, Color color, int x, int y, int w, int h) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setBounds(x, y, w, h);
        add(label);
    }
}

