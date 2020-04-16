package ru.ifmo.cs.bcomp.ui.io;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.ui.components.RegisterView;
import ru.ifmo.cs.components.DataDestination;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

public class ThirdIO extends IODevice {
    private RegisterView output;

    public ThirdIO(IOCtrl ioCtrl) {
        super(ioCtrl, "IO");
    }

    @Override
    protected Component getContent() {
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel input = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(500, 150));
        panel.setBackground(COLOR_BACKGROUND);
        output = new RegisterView(ioctrl.getRegisters()[0]);
        output.setProperties(0, 0, false, false);
        output.setPreferredSize(output.getSize());
        output.setMinimumSize(output.getSize());
        output.setTitle("ВУ");
        JButton buttonReady = new JButton(getRes().getString("ready"));
        buttonReady.setFont(FONT_COURIER_PLAIN_12);
        buttonReady.setFocusable(false);
        GridBagConstraints constraints = new GridBagConstraints() {{
            gridy = 0;
            gridx = 1;
        }};

        for (int i = 7; i >= 0; i--) {
            JButton button = new JButton();
            addListener(button, i);
            button.setPreferredSize(new Dimension(14, 14));
            button.setBackground(COLOR_VALUE);
            input.add(button, constraints);
            constraints.gridx++;
        }

        buttonReady.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ioctrl.setReady();
            }
        });
        ioctrl.addDestination(ioctrl.getRegisters()[1], new DataDestination() {
            @Override
            public void setValue(long value) {
                buttonReady.setForeground(value == 1 ? COLOR_ACTIVE : COLOR_TEXT);
            }
        });
        ioctrl.addDestination(ioctrl.getRegisters()[0], new DataDestination() {
            @Override
            public void setValue(long value) {
                output.setValue(value);
            }
        });
        constraints = new GridBagConstraints() {{
            gridy = 0;
            gridx = 3;
            gridwidth = GridBagConstraints.REMAINDER;
        }};
        panel.add(output, constraints);
        constraints.gridy++;
        constraints.insets.left = REG_TITLE_WIDTH;
        constraints.insets.top += 10;
        panel.add(input, constraints);
        constraints.gridy++;
        constraints.insets.top += 15;
        constraints.insets.left = 0;
        panel.add(buttonReady, constraints);
        return panel;
    }

    private void addListener(JButton button, int i) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ioctrl.getRegisters()[0].invertBit(i);
                output.setValue();
            }
        });

    }
}
