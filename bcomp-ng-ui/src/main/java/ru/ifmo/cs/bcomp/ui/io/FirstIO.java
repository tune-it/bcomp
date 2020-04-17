package ru.ifmo.cs.bcomp.ui.io;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.ui.components.RegisterView;
import ru.ifmo.cs.components.DataDestination;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

public class FirstIO extends IODevice {
    private RegisterView output;

    public FirstIO(IOCtrl ioCtrl) {
        super(ioCtrl, "output");
    }

    @Override
    protected Component getContent() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(500, 150));
        panel.setBackground(COLOR_BACKGROUND);
        output = new RegisterView(ioctrl.getRegisters()[0]);
        output.setProperties(0, 0, false, false);
        output.setPreferredSize(output.getSize());
        output.setMinimumSize(output.getSize());
        output.setTitle("ВУ");
        GridBagConstraints constraints = new GridBagConstraints() {{
            gridy = 0;
            gridx = 3;
            gridwidth = GridBagConstraints.REMAINDER;
        }};
        JButton button = new JButton(getRes().getString("ready"));
        button.setFont(FONT_COURIER_PLAIN_12);
        button.setFocusable(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ioctrl.setReady();
            }
        });
        ioctrl.addDestination(ioctrl.getRegisters()[1], new DataDestination() {
            @Override
            public void setValue(long value) {
                button.setForeground(value == 1 ? COLOR_ACTIVE : COLOR_TEXT);
            }

        });
        ioctrl.addDestination(ioctrl.getRegisters()[0], output);
        panel.add(output, constraints);
        constraints.gridy++;
        constraints.insets.top += 30;
        panel.add(button, constraints);
        return panel;
    }
}
