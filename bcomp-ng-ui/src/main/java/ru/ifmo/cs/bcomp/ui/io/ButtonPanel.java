package ru.ifmo.cs.bcomp.ui.io;
import ru.ifmo.cs.bcomp.ui.components.RegisterView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.COLOR_VALUE;

 class ButtonPanel extends JPanel {
     private RegisterView reg;

     ButtonPanel(RegisterView reg) {
         setLayout(new GridBagLayout());
         this.reg = reg;
         GridBagConstraints constraints = new GridBagConstraints() {{
             gridy = 0;
             gridx = 1;
         }};

         for (int i = 7; i >= 0; i--) {
             JButton button = new JButton();
             addListener(button, i);
             button.setPreferredSize(new Dimension(14, 14));
             button.setBackground(COLOR_VALUE);
             add(button, constraints);
             constraints.gridx++;
         }
     }

     private void addListener(JButton button, int i) {
         button.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 reg.getReg().invertBit(i);
                 reg.setValue();
             }
         });

     }
 }
