package swinger;

import postgresql.PostgresConnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;

public class Room/*implements ActionListener*/ {
    private final PostgresConnect connection;
    private final int ID;
    private Object[] data;
    private JPanel[] arr;
    public Room(PostgresConnect c, int i){
        ID = i;
        connection = c;
        initComponents();
    }

    private void initComponents(){
        data = new Object[8];
        try {
            ResultSet result = connection.runSQL("SELECT * FROM rooms WHERE roomID = " + ID);
            while (result.next()){
                data[0] = result.getInt("hotelID");
                data[1] = result.getInt("hotelChainID");
                data[2] = result.getObject("price");
                data[3] = result.getString("capacity");
                data[4] = result.getString("viewType");
                data[5] = result.getArray("amenities");
                data[6] = result.getBoolean("extendable");
                data[7] = result.getString("damages");
            }
        } catch (SQLException e){
            System.out.println("yo myabe fuck uofofof");
        }
    }

    public JPanel[] newRoomPanel() throws SQLException{
        if (data != null){
            arr = new JPanel[10];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = new JPanel();
            }
            JLabel temp;
            ResultSet h = connection.runSQL("SELECT * FROM hotel_chains NATURAL JOIN rooms WHERE roomID = " + ID);
            h.next();
            String chainName = h.getString("name");
            temp = new JLabel(chainName);
            temp.setHorizontalAlignment(0);
            arr[0].add(temp);

            h = connection.runSQL("SELECT * FROM hotels NATURAL JOIN rooms WHERE roomID = " + ID);
            h.next();
            String address = String.valueOf(h.getInt("addressNumber")) + " " + h.getString("addressName");
            temp = new JLabel(address);
            temp.setHorizontalAlignment(0);
            arr[1].add(temp);

            String stars = "⭐";
            for (int i = 1; i < h.getInt("stars"); i++){
                stars += "⭐";
            }
            temp = new JLabel(stars);
            temp.setHorizontalAlignment(0);
            arr[2].add(temp);

            temp = new JLabel(String.valueOf(h.getLong("phone")));
            temp.setHorizontalAlignment(0);
            arr[7].add(temp);

            temp = new JLabel(h.getString("email"));
            temp.setHorizontalAlignment(0);
            arr[8].add(temp);

            h = connection.runSQL("SELECT * FROM rooms WHERE roomID = " + ID);
            h.next();

            temp = new JLabel(h.getString("capacity"));
            temp.setHorizontalAlignment(0);
            arr[3].add(temp);

            temp = new JLabel(h.getString("viewType"));
            temp.setHorizontalAlignment(0);
            arr[4].add(temp);

            String tempAmenities = h.getArray("amenities").toString();
            tempAmenities = tempAmenities.substring(1, tempAmenities.length()-1);
            String amenities = "";
            if (tempAmenities.length() >= 2){
                String[] tempAmenities2 = tempAmenities.split(",");
                for (int i = 0; i < tempAmenities2.length; i++){
                    amenities += tempAmenities2[i];
                    if (i < tempAmenities2.length - 1) {
                        amenities += ", ";
                    }
                }
            } else {
                amenities = "No amenities";
            }
            temp = new JLabel(amenities);
            temp.setHorizontalAlignment(0);
            arr[5].add(temp);

            String extendable;
            if (h.getBoolean("extendable")){
                extendable = "Extendable";
            } else {
                extendable = "Not Extendable";
            }
            temp = new JLabel(extendable);
            temp.setHorizontalAlignment(0);
            arr[6].add(temp);

            temp = new JLabel("$" + h.getString("price") + "/night");
            temp.setHorizontalAlignment(0);
            arr[9].add(temp);
        }
        return arr;
    }

    /*public void actionPerformed(ActionEvent event) {
        System.out.println("lmao");
    }*/
}

