package ru.ifmo.cs.bcomp.ui.io;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.ui.components.RegisterView;
import javax.swing.*;
import java.awt.*;

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
        output.setTitle("DR");
        GridBagConstraints constraints = new GridBagConstraints() {{
            gridy = 0;
            gridx = 3;
            gridwidth = GridBagConstraints.REMAINDER;
        }};
        ButtonReady button = new ButtonReady(ioctrl,getRes().getString("ready"));
        ioctrl.addDestination(1, button);
        ioctrl.addDestination(0, output);
        panel.add(output, constraints);
        constraints.gridy++;
        constraints.insets.top += 30;
        panel.add(button, constraints);
        return panel;
    }
}
