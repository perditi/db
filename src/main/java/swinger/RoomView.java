package swinger;

import postgresql.PostgresConnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class RoomView extends JFrame implements ActionListener {
    private PostgresConnect connection;
    private String[] stats;
    private final ArrayList<Room> rooms;
    public RoomView(PostgresConnect c, String[] s){
        super("Available Rooms");
        connection = c;
        stats = s;
        rooms = null;
        initComponents();

        setSize(1200,800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents(){
        setLayout(new GridLayout(0,2,1,1));
        for (String s: stats) {
            JLabel test = new JLabel(s);
            add(test);
        }
    }

    public void actionPerformed(ActionEvent event) {
        System.out.println("lmao");
    }
}
