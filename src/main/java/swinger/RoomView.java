package swinger;

import postgresql.PostgresConnect;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class RoomView extends JFrame implements ActionListener {
    private PostgresConnect connection;
    private Object[] stats;
    private final ArrayList<Room> rooms;
    public RoomView(PostgresConnect c, Object[] s){
        super("Available Rooms");
        connection = c;
        stats = s;
        String q;
        try {
            q = getSQLquery();
            try {
                ResultSet result = connection.runSQL(q);
                while (result.next()){
                    System.out.println(result.getInt("roomID"));
                }
            } catch (SQLException e){
                System.out.println("smn else");
            }
        } catch (NullPointerException e){
            JOptionPane.showMessageDialog(this, "Check-in and Check-out Date Required.");
        }



        rooms = null;
        initComponents();

        setSize(1200,800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        return " AND roomID IN (SELECT roomID FROM rooms WHERE capacity = \'" + s + "\')";
    }

    private String getStreet(String s){
        return " AND roomID IN (SELECT roomID FROM rooms NATURAL JOIN hotels WHERE addressName = \'" + s + "\')";
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
        setLayout(new GridLayout(0,2,1,1));
        System.out.println(getSQLquery());
    }

    public void actionPerformed(ActionEvent event) {
        System.out.println("lmao");
    }
}
