/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.IOCtrl;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.PANE_SIZE;
import ru.ifmo.cs.bcomp.ui.components.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class GUI extends JApplet {

    private ComponentManager cmanager;
    private JTabbedPane tabs;
    private ActivateblePanel activePanel = null;
    private final BasicComp bcomp;
    private final CPU cpu;
    private final boolean axlEnabled;

    public GUI(BasicComp bcomp) {
        this(bcomp, false);
    }

    public GUI(BasicComp bcomp, boolean axlEnabled) {
        this.bcomp = bcomp;
        this.cpu = bcomp.getCPU();
        this.axlEnabled = axlEnabled;
    }

    @Override
    public void init() {
        cmanager = new ComponentManager(this);

        final List<ActivateblePanel> panelList = new ArrayList<ActivateblePanel>();
        panelList.add(new BasicView(this));
        panelList.add(new AssemblerView(this));
        if (axlEnabled) {
            panelList.add(new AxlView(this));
        }
        final ActivateblePanel[] panels = panelList.toArray(new ActivateblePanel[0]);

        tabs = new JTabbedPane();
        tabs.addKeyListener(cmanager.getKeyListener());

        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (activePanel != null) {
                    activePanel.panelDeactivate();
                }

                activePanel = (ActivateblePanel) tabs.getSelectedComponent();
                activePanel.panelActivate();
            }
        });

        tabs.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                super.componentResized(componentEvent);
                for (ActivateblePanel panel : panels) {
                    panel.revalidate();
                    panel.repaint();
                    panel.redrawArrows();
                }
            }
        });
        for (ActivateblePanel pane : panels) {
            pane.setPreferredSize(PANE_SIZE);

            tabs.addTab(pane.getPanelName(), pane);
        }

        add(tabs);
    }

    @Override
    public void start() {
        cmanager.switchFocus();
    }

    public void gui() throws Exception {
        JFrame frame = new JFrame("BCompNG " + bcomp.getVersionBrief());

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        init();
        frame.pack();

        frame.setVisible(true);
        frame.setMinimumSize(frame.getSize());
        start();
    }

    public BasicComp getBasicComp() {
        return bcomp;
    }

    public CPU getCPU() {
        return cpu;
    }

    public IOCtrl[] getIOCtrls() {
        return bcomp.getIOCtrls();
    }

    public ComponentManager getComponentManager() {
        return cmanager;
    }

}
