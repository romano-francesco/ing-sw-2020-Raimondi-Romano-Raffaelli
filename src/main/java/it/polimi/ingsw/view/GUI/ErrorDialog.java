package it.polimi.ingsw.view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErrorDialog extends GameDialog implements ActionListener {

    public ErrorDialog(JFrame frame,String errorMessage){
        super(frame,"MESSAGE");
        Font font = new Font("Impatto", Font.PLAIN, 18);
        JLabel labelGround = new JLabel("");
        Image imageGround = new ImageIcon(this.getClass().getResource("/odyssey_4.png")).getImage().getScaledInstance(520,315,Image.SCALE_DEFAULT);
        labelGround.setLayout(new BorderLayout());
        labelGround.setIcon(new ImageIcon(imageGround));
        JLabel label = new JLabel(errorMessage);
        label.setFont(font);
        label.setForeground(Color.BLACK);
        //label.setBounds(75,140,600,50);
        JButton buttonClose = new JButton("CLOSE");
        //buttonClose.setBounds(180,230,180,60);
        buttonClose.addActionListener(this);
        labelGround.add(label,BorderLayout.CENTER);
        labelGround.add(buttonClose,BorderLayout.PAGE_END);
        add(labelGround);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
    }
}
