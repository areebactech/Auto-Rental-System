import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class BookingPage extends JFrame {

    private JTextField nameField, phoneField, cnicField,
            addressField, dateField, durationField;
    private int selectedVehiclePrice = 0;
    private String  selectedVehicle = null;
    private JTextField selectedVehicleLabel;

    public BookingPage() {
        setTitle("Vehicle Rental Booking");
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        buildUI();
        setVisible(true);
    }

    //UI
    private void buildUI() {
        JLayeredPane layer = new JLayeredPane();
        layer.setLayout(null);
        add(layer, BorderLayout.CENTER);

        /* background image */
        JLabel bg = new JLabel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try{
                    ImageIcon img=new ImageIcon(getClass().getResource("/images/book.png"));
                    g.drawImage(img.getImage(),0,0,getWidth(),getHeight(),this);
                }catch(Exception ex){ g.setColor(Color.DARK_GRAY); g.fillRect(0,0,getWidth(),getHeight()); }
            }
        };
        bg.setBounds(0,0,getWidth(),getHeight());
        layer.add(bg,Integer.valueOf(0));
        addComponentListener(new java.awt.event.ComponentAdapter(){
            public void componentResized(java.awt.event.ComponentEvent e){ bg.setSize(getSize()); }
        });

        /* form panel  */
        JPanel form=new JPanel(null);
        form.setBounds(330,220,700,500);
        form.setBackground(new Color(0,0,0,130));
        layer.add(form,Integer.valueOf(1));

        createFormFields(form);
        createVehicleSelector(form);
        createButtons(form);
    }

    /* create input fields */
    private void createFormFields(JPanel p){
        Font lbl = new Font("Segoe UI", Font.BOLD, 14),
                fld = new Font("Segoe UI", Font.PLAIN, 14);

        nameField     = addField(p,"Full Name:",        lbl,fld,40 ,50);
        phoneField    = addField(p,"Phone No:",         lbl,fld,380,50);
        cnicField     = addField(p,"CNIC:",             lbl,fld,40 ,130);
        addressField  = addField(p,"Address:",          lbl,fld,380,130);
        dateField     = addField(p,"Date (yyyy-MM-dd):",lbl,fld,40 ,190);
        durationField = addField(p,"Duration (days):",  lbl,fld,380,190);
    }

    /* vehicle selector block */
    private void createVehicleSelector(JPanel p) {
        JLabel l = new JLabel("Vehicle:");
        l.setBounds(40, 250, 160, 25);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        p.add(l);

        selectedVehicleLabel = new JTextField("None selected");
        selectedVehicleLabel.setBounds(200, 250, 220, 25);
        selectedVehicleLabel.setEditable(false);
        selectedVehicleLabel.setBackground(Color.BLACK);
        selectedVehicleLabel.setForeground(new Color(0, 200, 255));
        selectedVehicleLabel.setBorder(BorderFactory.createEmptyBorder());
        selectedVehicleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        p.add(selectedVehicleLabel);


        JButton choose = new JButton("Select Vehicle");
        choose.setBounds(420, 245, 180, 35);
        styleButton(choose);
        choose.addActionListener(e -> new VehicleBrowser(this));
        p.add(choose);
    }

    /* action buttons */
    private void createButtons(JPanel p){
        JButton confirm=new JButton("Confirm Booking");
        styleButton(confirm); confirm.setBounds(250,330,200,45);
        confirm.addActionListener(e->handleBooking());
        p.add(confirm);

        JButton exit=new JButton("Exit");
        styleButton(exit); exit.setBounds(250,390,200,45);
        exit.addActionListener(e->{ dispose(); new LoginPage(); });
        p.add(exit);
    }
    private void insertUserIfNotExists(String name, String phone, String cnic, String address) {
        try (Connection con = DBConnection.getConnection()) {
            String checkQuery = "SELECT * FROM users WHERE cnic = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);
            checkStmt.setString(1, cnic);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                String insertQuery = "INSERT INTO users (name, phone, cnic, address) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                insertStmt.setString(1, name);
                insertStmt.setString(2, phone);
                insertStmt.setString(3, cnic);
                insertStmt.setString(4, address);
                insertStmt.executeUpdate();
                insertStmt.close();
            }

            rs.close();
            checkStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            msg("User insert error: " + e.getMessage());
        }
    }

    /* LOGIC */
    private void handleBooking(){
        String name=nameField.getText().trim(),
                phone=phoneField.getText().trim(),
                cnic =cnicField.getText().trim(),
                addr =addressField.getText().trim(),
                dateS=dateField.getText().trim(),
                durS =durationField.getText().trim();

        if(name.isEmpty()||phone.isEmpty()||cnic.isEmpty()||addr.isEmpty()
                ||dateS.isEmpty()||durS.isEmpty()){
            msg("Please fill all fields."); return;
        }
        if(selectedVehicle==null){
            msg("Please select a vehicle first."); return;
        }

        int days;
        try{ days=Integer.parseInt(durS); if(days<=0)throw new NumberFormatException(); }
        catch(NumberFormatException ex){ msg("Invalid duration."); return; }

        LocalDate date;
        try{ date=LocalDate.parse(dateS); }
        catch(DateTimeParseException ex){ msg("Date format yyyy-MM-dd"); return; }

        int total = selectedVehiclePrice * days;
        insertUserIfNotExists(name, phone, cnic, addr);
        saveToDatabase(name,phone,cnic,addr,date,selectedVehicle,days,total);

    }

    /* ===================================== HELPERS ===================================== */
    private JTextField addField(JPanel p,String txt,Font lf,Font ff,int x,int y){
        JLabel lab=new JLabel(txt); lab.setFont(lf); lab.setForeground(Color.WHITE);
        lab.setBounds(x,y,160,25); p.add(lab);

        JTextField f=new JTextField(); f.setFont(ff);
        f.setBounds(x+160,y,180,30); f.setBackground(Color.BLACK);
        f.setForeground(Color.WHITE); f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createMatteBorder(0,0,2,0,Color.WHITE));
        p.add(f); return f;
    }
    private void styleButton(AbstractButton b){
        b.setBackground(new Color(0,123,255));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI",Font.BOLD,15));
        b.setBorder(BorderFactory.createEmptyBorder());
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    private void msg(String s){ JOptionPane.showMessageDialog(this,s); }

    /* --- called by VehicleBrowser when user clicks “Book Now” --- */
    public void setSelectedVehicle(String v, int price) {
        selectedVehicle = v;
        selectedVehiclePrice = price;
        selectedVehicleLabel.setText((v.length() > 20 ? v.substring(0, 20) + "..." : v));
        selectedVehicleLabel.repaint();
        System.out.println("DEBUG: Vehicle selected = " + v + " | Price: " + price);
    }

    private void saveToDatabase(String name, String phone, String cnic, String address,
                                LocalDate date, String vehicle, int days, int total) {
        try (Connection con = DBConnection.getConnection()) {

            String query = """
            INSERT INTO bookings (customer_name, phone_number, cnic, address,
                                  booking_date, rental_item, rental_duration_days, total_price, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, phone);
            pst.setString(3, cnic);
            pst.setString(4, address);
            pst.setString(5, date.toString());
            pst.setString(6, vehicle);
            pst.setInt(7, days);
            pst.setInt(8, total);
            pst.setString(9, "Pending");

            int inserted = pst.executeUpdate();
            if (inserted > 0) {
                // pop‑up confirmation dialog
                new ConfirmationMsg(this, name, vehicle, date.toString(), days, total)
                        .setVisible(true);
            } else {
                msg("Booking failed. Please try again.");
            }

            pst.close();

        } catch (Exception e) {
            e.printStackTrace();
            msg("Database error: " + e.getMessage());
        }
    }

    public static void main(String[] a){
        SwingUtilities.invokeLater(BookingPage::new);
    }
}









