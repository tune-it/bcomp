package ru.ifmo.cs.bcomp.ui.io;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.ui.components.ComponentManager;
import ru.ifmo.cs.bcomp.ui.components.InputRegisterView;
import javax.swing.*;
import java.awt.*;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

public class SecondIO extends IODevice {
    private ComponentManager componentManager;
    private InputRegisterView input;

    public SecondIO(IOCtrl ioCtrl, ComponentManager componentManager) {
        super(ioCtrl, "input");
        this.componentManager = componentManager;
    }

    @Override
    protected Component getContent() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(500, 150));
        panel.setBackground(COLOR_BACKGROUND);
        input = new InputRegisterView(componentManager, ioctrl.getRegisters()[0]);
        input.setProperties(0, 0, false, false);
        input.setPreferredSize(input.getSize());
        input.setMinimumSize(input.getSize());
        input.setTitle("DR");
        ButtonReady button = new ButtonReady(ioctrl,getRes().getString("ready"));
        ioctrl.addDestination(1,button);
        GridBagConstraints constraints = new GridBagConstraints() {{
            gridy = 0;
            gridx = 3;
            gridwidth = GridBagConstraints.REMAINDER;
        }};
        panel.add(input, constraints);
        constraints.gridy++;
        constraints.insets.top += 30;
        panel.add(button, constraints);
        return panel;
    }

    @Override
    public void activate() {
        super.activate();
        input.setActive();
    }
}
