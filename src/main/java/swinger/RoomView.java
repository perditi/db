package swinger;

import org.jdatepicker.impl.SqlDateModel;
import postgresql.PostgresConnect;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class RoomView extends JFrame implements ActionListener {
    private final PostgresConnect connection;
    private final Object[] stats;
    private final ArrayList<Integer> rooms;
    private final String query;
    private JRadioButton[] radios;
    private String selection;
    private final ButtonGroup group = new ButtonGroup();
    private JButton bookBtn;
    private JTextField customerSSN;
    private JTextField customerName;
    private JTextField customerAddress;

    public RoomView(PostgresConnect c, Object[] s){
        super("Available Rooms");
        connection = c;
        stats = s;
        rooms = new ArrayList<Integer>();

        setSize(1600,400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        query = getSQLquery();
        try {
            ResultSet result = connection.runSQL(query);
            while (result.next()){
                rooms.add(result.getInt("roomID"));
            }
            initComponents();
        } catch (SQLException e){
            System.out.println("smn else");
        }



    }

    private String getSQLquery(){
        StringBuilder str = new StringBuilder();
        String checkIn = "(SELECT roomID FROM rooms WHERE roomID NOT IN " +
                "(SELECT roomID FROM rooms NATURAL JOIN bookings WHERE " +
                "(bookings.endDate <= '%s')) " +
                "OR roomID NOT IN " +
                "(SELECT roomID FROM rooms NATURAL JOIN rentings WHERE " +
                "(rentings.endDate <= '%s')))";
        String checkInDate = stats[0].toString();
        checkIn = String.format(checkIn, checkInDate, checkInDate);

        String checkOut = "(SELECT roomID FROM rooms WHERE roomID NOT IN " +
                "(SELECT roomID FROM rooms NATURAL JOIN bookings WHERE " +
                "(bookings.startDate >= '%s')) " +
                "OR roomID NOT IN " +
                "(SELECT roomID FROM rooms NATURAL JOIN rentings WHERE " +
                "(rentings.startDate >= '%s')))";
        String checkOutDate = stats[1].toString();
        checkOut = String.format(checkOut, checkOutDate, checkOutDate);

        String capacity = "";
        if (!stats[2].toString().equals("ANY")){
            capacity = getCapacity(stats[2].toString());
        }

        String street = "";
        if (!stats[3].toString().equals("ANY")){
            street = getStreet(stats[3].toString());
        }

        String hotelChainID = "";
        if (!stats[4].toString().equals("ANY")){
            hotelChainID = getHotelChainID(stats[4].toString());
        }

        String category = "";
        if (!stats[5].toString().equals("ANY")){
            category = getCategory(stats[5].toString());
        }

        String nRooms = "";
        if (!stats[6].toString().equals("ANY")){
            nRooms = getNrooms(stats[6].toString());
        }

        String price = stats[7].toString();
        if (!price.equals("")){
            price = getPrice(price);
        }

        str.append("SELECT roomID FROM rooms WHERE roomID IN ");
        str.append(checkIn);
        str.append(" AND roomID IN ");
        str.append(checkOut);
        str.append(capacity);
        str.append(street);
        str.append(hotelChainID);
        str.append(category);
        str.append(nRooms);
        str.append(price);
        return str.toString();
    }

    private String getHotelChainID(String s){
        int r = 0;
        if (s.equals("First to Last")){
            r = 1;
        } else if (s.equals("Night to Remember")){
            r = 2;
        } else if (s.equals("Best of Three")){
            r = 3;
        } else if (s.equals("Foresight")){
            r = 4;
        } else if (s.equals("Five of Clubs")){
            r = 5;
        }
        return " AND roomID IN (SELECT roomID FROM rooms WHERE hotelChainID = " + r + ")";
    }

    private String getCapacity(String s){
        return " AND roomID IN (SELECT roomID FROM rooms WHERE capacity = '" + s + "')";
    }

    private String getStreet(String s){
        return " AND roomID IN (SELECT roomID FROM rooms NATURAL JOIN hotels WHERE addressName = '" + s + "')";
    }

    private String getCategory(String s){
        int r = 0;
        if (s.equals("1-star")){
            r = 1;
        } else if (s.equals("2-star")){
            r = 2;
        } else if (s.equals("3-star")){
            r = 3;
        } else if (s.equals("4-star")){
            r = 4;
        } else if (s.equals("5-star")){
            r = 5;
        }
        return " AND roomID IN (SELECT roomID FROM rooms NATURAL JOIN hotels WHERE stars = " + r + ")";
    }

    private String getNrooms(String s){
        return " AND roomID IN (SELECT roomID FROM rooms NATURAL JOIN hotels WHERE numOfRooms = " + s + ")";
    }

    private String getPrice(String s){
        return " AND roomID IN (SELECT roomID FROM rooms WHERE price <= " + s + ")";
    }

    private void initComponents(){
        setLayout(new GridLayout(0,1));
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.ipadx = 20;
        c.ipady = 5;
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBounds(0,0,1600, Integer.MAX_VALUE);
        getContentPane().add(scrollPane);
        radios = new JRadioButton[rooms.size()];
        for (int i = 0; i < rooms.size(); i++) {
            Room temp = new Room(connection, rooms.get(i));
            try {
                JPanel[] arr = temp.newRoomPanel();
                for (int j = 0; j < 10; j++){
                    c.ipadx = 20;
                    c.gridx = j;
                    c.gridy = i;
                    panel.add(arr[j], c);
                }
                c.gridx = 10;
                radios[i] = new JRadioButton();
                radios[i].setActionCommand(String.valueOf(rooms.get(i)));
                group.add(radios[i]);
                radios[i].addActionListener(this);
                panel.add(radios[i], c);

            } catch (SQLException e){
                System.out.println("Error processing SQL query...");
                e.printStackTrace();
                continue;
            }
        }


        c.gridx = 11;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel temp = new JLabel("SSN:  ");
        temp.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(temp, c);
        c.gridy = GridBagConstraints.RELATIVE;

        temp = new JLabel("Name:  ");
        temp.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(temp, c);
        c.gridy = GridBagConstraints.RELATIVE;

        temp = new JLabel("Address:  ");
        temp.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(temp, c);
        c.gridy = GridBagConstraints.RELATIVE;

        c.fill = GridBagConstraints.NONE;
        c.ipadx = 80;
        c.ipady = 7;
        c.gridx = 12;
        c.gridy = 1;
        customerSSN = new JTextField(5);
        customerSSN.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                String value = customerSSN.getText();
                int l = value.length();
                customerSSN.setEditable((ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') || ke.getKeyChar() == '\b');
            }
        });
        panel.add(customerSSN, c);

        c.gridy = GridBagConstraints.RELATIVE;
        customerName = new JTextField(5);
        customerName.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                String value = customerName.getText();
                int l = value.length();
                customerName.setEditable(ke.getKeyChar() < '0' || ke.getKeyChar() > '9');
            }
        });
        panel.add(customerName, c);
        c.gridy = GridBagConstraints.RELATIVE;
        customerAddress = new JTextField(5);

        panel.add(customerAddress, c);

        c.gridy = GridBagConstraints.RELATIVE;
        bookBtn = new JButton(new AbstractAction("Book Room") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selection != null) {
                    String ssn = customerSSN.getText();
                    if (ssn.length() == 9){
                        String name = customerName.getText();
                        String address = customerAddress.getText();

                        StringBuilder str = new StringBuilder();
                        str.append("INSERT INTO customers VALUES (");
                        str.append(ssn);
                        str.append(", ");
                        int r0 = 1;
                        try {
                            ResultSet h0 = connection.runSQL("SELECT hotelID FROM rooms WHERE roomID = " + selection);
                            h0.next();
                            r0 = h0.getInt("hotelID");
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        str.append(r0);
                        str.append(", ");
                        String r = "1";
                        try {
                            ResultSet h = connection.runSQL("SELECT hotelChainID FROM rooms WHERE roomID = " + selection);
                            h.next();
                            r = String.valueOf(h.getInt("hotelChainID"));
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        str.append(r);
                        str.append(", '");
                        str.append(name);
                        str.append("', ");
                        String[] temp = address.split(" ");
                        str.append(temp[0]);
                        str.append(", '");
                        String temp2 = "";
                        for (int i = 1; i < temp.length; i++) {
                            temp2 += temp[i];
                            if (i < temp.length - 1) {
                                temp2 += " ";
                            }
                        }
                        str.append(temp2);
                        str.append("', '");
                        str.append(Date.valueOf(LocalDate.now()));
                        str.append("')");

                        StringBuilder str2 = new StringBuilder();
                        str2.append("INSERT INTO bookings VALUES (");
                        int r1 = 1;
                        try {
                            ResultSet h1 = connection.runSQL("SELECT COUNT(bookingID) FROM bookings");
                            h1.next();
                            r1 += h1.getInt("count");
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        str2.append(r1);
                        str2.append(", ");
                        str2.append(ssn);
                        str2.append(", ");
                        str2.append(r0);
                        str2.append(", ");
                        str2.append(r);
                        str2.append(", ");
                        str2.append(selection);
                        str2.append(", '");
                        str2.append(stats[0].toString());
                        str2.append("', '");
                        str2.append(stats[1].toString());
                        str2.append("')");


                        try {
                            connection.runSQL(str.toString());
                            connection.runSQL(str2.toString());
                        } catch (SQLException e1){
                            e1.printStackTrace();
                        }
                    } else {
                        invalidSSN();
                    }
                } else {
                    missingSelection();
                }
            }
        });

        c.ipady = 5;
        c.ipadx = 10;
        panel.add(bookBtn, c);
        System.out.println(getSQLquery());
    }

    private void missingSelection(){
        JOptionPane.showMessageDialog(this, "Room selection required.");
    }

    private void invalidSSN(){
        JOptionPane.showMessageDialog(this, "Invalid SSN.");
    }

    public void actionPerformed(ActionEvent e) {
        selection = e.getActionCommand();
        System.out.println(selection);
    }
}
