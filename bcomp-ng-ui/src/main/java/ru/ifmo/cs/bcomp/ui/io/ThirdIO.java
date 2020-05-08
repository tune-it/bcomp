package ru.ifmo.cs.bcomp.ui.io;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.ui.components.RegisterView;
import javax.swing.*;
import java.awt.*;

import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

public class ThirdIO extends IODevice {
    private RegisterView output;

    public ThirdIO(IOCtrl ioCtrl) {
        super(ioCtrl, "IO");
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
        output.setTitle("DR");
        ButtonPanel input = new ButtonPanel(output);
        ButtonReady buttonReady = new ButtonReady(ioctrl,getRes().getString("ready"));
        ioctrl.addDestination(1,buttonReady);
        ioctrl.addDestination(0, output);
       GridBagConstraints constraints = new GridBagConstraints() {{
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
}
