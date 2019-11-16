package ru.ifmo.cs.bcomp.ui.components;

import javax.swing.*;
import java.awt.*;

import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;


public class CommutView extends JComponent {


    private  int x,y,wight,height;


    public CommutView(int x, int y, int width, int height) {

        this.x=x;this.y=y;this.wight=width;this.height=height;

        JLabel title = new JLabel("Commutator", JLabel.CENTER);
        title.setFont(FONT_COURIER_BOLD_21);
        title.setBounds(x, y, width, height);
        add(title);

        setBounds(x, y, width, height);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(COLOR_TITLE);
        g.fillRect(x, y, wight,height);
        g.setColor(COLOR_TEXT);
        g.drawRect(x, y,wight-1,height-1);
    }
}

