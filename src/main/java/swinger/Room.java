package swinger;

import postgresql.PostgresConnect;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Room extends JPanel implements ActionListener {
    private final PostgresConnect connection;
    private final int ID;
    public Room(PostgresConnect c, int i){
        super(false);
        ID = i;
        connection = c;


    }

    public void actionPerformed(ActionEvent event) {
        System.out.println("lmao");
    }
}

