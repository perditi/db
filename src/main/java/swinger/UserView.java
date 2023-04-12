package swinger;

import postgresql.PostgresConnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.jdatepicker.impl.*;

import java.sql.*;
import java.util.*;

public class UserView extends JFrame{
    private final String[] chains = {"ANY", "First to Last", "Night to Remember", "Best of Three", "Foresight", "Five of Clubs"};
    private final String[] caps = {"ANY", "Single", "Double", "Triple", "Quad"};
    private final String[] labels = {"Check-in Date", "Check-out Date",
            "Capacity", "Street", "Hotel Chain", "Category", "Total Rooms",
            "Max Price ($)"};
    private Stack<String> addresses;
    private final String[] stars = {"ANY", "1-star", "2-star", "3-star", "4-star", "5-star"};
    private Stack<String> numOfRooms;
    private PostgresConnect connection;
    private JComboBox<String> hotelChainDropDown;
    private JButton btn;
    private JDatePickerImpl checkInDate;
    private JDatePickerImpl checkOutDate;
    private JComboBox<String> roomCapacitiesDropDown;
    private JComboBox<String> addressesDropDown;
    private JComboBox<String> categoriesDropDown;
    private JComboBox<String> numOfRoomsDropDown;
    private JTextField priceInput;

    public UserView(PostgresConnect c){
        super("eHotel Manager (User View)");
        connection = c;
        initComponents();

        setSize(1400,300);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                connection.disconnect();
            }
        });
    }

    private void blankLine(){
        for (int i = 0; i < 8; i++){
            add((Component) new JPanel());
        }
    }

    private void initComponents(){//start date, end date, room capactiy, area, chain, stars, total rooms, price range
        GridLayout l = new GridLayout(0,8, 8, 15);
        setLayout(l);
        for (String s : labels){
            JLabel title = new JLabel(s);
            title.setHorizontalAlignment(0);
            add(title);
        }

        SqlDateModel model = new SqlDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        checkInDate = new JDatePickerImpl(new JDatePanelImpl(model, p), new DateComponentFormatter());
        add(checkInDate);

        SqlDateModel model2 = new SqlDateModel();
        Properties p2 = new Properties();
        p2.put("text.today", "Today");
        p2.put("text.month", "Month");
        p2.put("text.year", "Year");
        checkOutDate = new JDatePickerImpl(new JDatePanelImpl(model2, p2), new DateComponentFormatter());
        add(checkOutDate);

        roomCapacitiesDropDown = new JComboBox<>(caps);
        add(roomCapacitiesDropDown);

        addresses = new Stack<String>();
        addresses.push("ANY");
        try{
            ResultSet result = connection.runSQL("SELECT addressName FROM hotels");
            while (result.next()){
                String tempstr = result.getString("addressName");
                if (!tempstr.equals(addresses.peek())) {
                    addresses.push(tempstr);
                }
            }
        } catch (SQLException e){
            System.out.println("SQL error");
        }
        addressesDropDown = new JComboBox<>(addresses);
        add(addressesDropDown);

        hotelChainDropDown = new JComboBox<>(chains);
        add(hotelChainDropDown);

        categoriesDropDown = new JComboBox<>(stars);
        add(categoriesDropDown);

        numOfRooms = new Stack<String>();
        numOfRooms.push("ANY");
        try{
            ResultSet result = connection.runSQL("SELECT numOfRooms FROM hotels");
            while (result.next()){
                String temp = String.valueOf(result.getInt("numOfRooms"));
                if (!numOfRooms.contains(temp)) {
                    numOfRooms.push(temp);
                }
            }
        } catch (SQLException e){
            System.out.println("SQL error");
        }
        numOfRoomsDropDown = new JComboBox<>(numOfRooms);
        add(numOfRoomsDropDown);


        priceInput = new JTextField(5);
        priceInput.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                String value = priceInput.getText();
                int l = value.length();
                if ((ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') || ke.getKeyChar() == '\b') {
                    priceInput.setEditable(true);
                } else {
                    priceInput.setEditable(false);
                }
            }
        });
        add(priceInput);

        blankLine();
        blankLine();
        blankLine();
        for (int i = 0; i < 7; i++){
            add((Component) new JPanel());
        }

        btn = new JButton(new AbstractAction("Get Available Rooms") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] query = new Object[8];
                query[0] = checkInDate.getModel().getValue();
                query[1] = checkOutDate.getModel().getValue();
                query[2] = roomCapacitiesDropDown.getSelectedItem();
                query[3] = addressesDropDown.getSelectedItem();
                query[4] = hotelChainDropDown.getSelectedItem();
                query[5] = categoriesDropDown.getSelectedItem();
                query[6] = numOfRoomsDropDown.getSelectedItem();
                query[7] = priceInput.getText();
                new RoomView(connection, query).setVisible(true);
            }
        });
        add(btn);
    }
}
