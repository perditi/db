package swinger;

import postgresql.PostgresConnect;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Room extends JPanel implements ActionListener {
    private final String[] content;
    public Room(String[] c){
        super(false);
        content = c;


    }

    public void actionPerformed(ActionEvent event) {
        System.out.println("lmao");
    }
}

