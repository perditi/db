package helper_stuff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class testSwing extends JFrame implements ActionListener{

    private JLabel labelQuestion;
    private JLabel labelWeight;
    private JTextField fieldWeight;
    private JButton buttonTellMe;
    public testSwing(){
        super("eHotel Manager");

        initComponents();

        setSize(1200,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.out.println("test2");
            }
        });
    }

    private void initComponents(){
        labelQuestion = new JLabel("How much water should I drink?");
        labelWeight = new JLabel("My weight (kg):");
        fieldWeight = new JTextField(5);
        buttonTellMe = new JButton("Tell Me");

        setLayout(new FlowLayout());

        add(labelQuestion);
        add(labelWeight);
        add(fieldWeight);
        add(buttonTellMe);

        buttonTellMe.addActionListener(this);
    }
    public void actionPerformed(ActionEvent event) {
        String message = "Buddy, you should drink %.1f L of water a day!";

        float weight = Float.parseFloat(fieldWeight.getText());
        float waterAmount = calculateWaterAmount(weight);

        message = String.format(message, waterAmount);

        JOptionPane.showMessageDialog(this, message);
    }

    private float calculateWaterAmount(float weight) {
        return (weight / 10f) * 0.4f;
    }

    public static void main(String[] args) {
        new testSwing().setVisible(true);
        System.out.println("test");
    }
}
