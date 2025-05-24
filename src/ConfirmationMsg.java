import javax.swing.*;
import java.awt.*;

public class ConfirmationMsg extends JDialog {

    public ConfirmationMsg(JFrame parent,String name, String vehicle, String date, int days, int rent) {
        super(parent, "Booking Confirmed", true);
        setSize(450, 300);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Color.BLACK);
        setLayout(null);

        Font titleFont = new Font("Arial", Font.BOLD, 22);
        Font textFont = new Font("Arial", Font.PLAIN, 16);

        JLabel success = new JLabel("✔ Booking Confirmed!");
        success.setFont(titleFont);
        success.setForeground(new Color(80, 130, 180));
        success.setBounds(100, 20, 300, 30);
        add(success);

        JLabel[] details = {
                new JLabel("Name: " + name),
                new JLabel("Vehicle: " + vehicle),
                new JLabel("Date: " + date),
                new JLabel("Duration: " + days + " day(s)"),
                new JLabel("Total Rent for "+ days +" days: Rs " + rent)
        };

        int y = 70;
        for (JLabel label : details) {
            label.setFont(textFont);
            label.setForeground(Color.WHITE);
            label.setBounds(50, y, 350, 25);
            add(label);
            y += 30;
        }

        JButton okButton = new JButton("OK");
        okButton.setFont(textFont);
        okButton.setBounds(170, 220, 100, 35);
        okButton.setBackground(new Color(70, 130, 180));
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> dispose());
        add(okButton);
    }
}