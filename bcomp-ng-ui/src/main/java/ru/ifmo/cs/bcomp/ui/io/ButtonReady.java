package ru.ifmo.cs.bcomp.ui.io;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.components.DataDestination;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.COLOR_ACTIVE;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.COLOR_TEXT;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.FONT_COURIER_PLAIN_12;

public class ButtonReady extends JButton implements DataDestination {
    private IOCtrl ioCtrl;

    ButtonReady(IOCtrl ioCtrl, String title) {
        super(title);
        setFont(FONT_COURIER_PLAIN_12);
        setFocusable(false);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ioCtrl.setReady();
                setForeground(COLOR_ACTIVE);
            }
        });
        this.ioCtrl = ioCtrl;
    }

    @Override
    public void setValue(long value) {
        setForeground(ioCtrl.isReady() ? COLOR_ACTIVE : COLOR_TEXT);
    }
}
