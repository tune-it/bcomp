package ru.ifmo.cs.bcomp.ui.components;

import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.components.DataDestination;


import javax.swing.*;
import java.awt.*;

import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

public class FlagView extends JComponent  {
    JLabel title;

    private  int x,y,wight,height;
    boolean active;


    public FlagView(int x, int y, int wigth, int height) {

        this.x=x;this.y=y;this.wight=wigth;this.height=height;

         title = new JLabel("", JLabel.CENTER);
        title.setFont(FONT_COURIER_BOLD_21);
        title.setBounds(x, y, wigth, height);
        add(title);
        setBounds(x, y, wigth, height);

    }



    public void setTitle(String title) {
        this.title = new JLabel(title, JLabel.CENTER) ;
        this.title.setFont(FONT_COURIER_BOLD_21);
        this.title.setBounds(x, y, wight, height);
        add(this.title);
    }
    public void setActive(boolean active){
        this.active=active;
        setBackground(active?COLOR_TITLE:COLOR_VALUE);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(active?COLOR_TITLE:COLOR_VALUE);
        g.fillRect(x, y, wight,height);
        g.setColor(COLOR_TEXT);
        g.drawRect(x, y,wight-1,height-1);
    }

}
