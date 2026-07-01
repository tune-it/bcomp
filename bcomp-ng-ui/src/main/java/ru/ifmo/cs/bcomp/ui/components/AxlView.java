package ru.ifmo.cs.bcomp.ui.components;

import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ProgramBinary;
import ru.ifmo.cs.bcomp.axl.AxlEditorPanel;
import ru.ifmo.cs.bcomp.axl.AxlLoadHandler;
import ru.ifmo.cs.bcomp.ui.GUI;
import ru.ifmo.cs.components.Messages;

import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.util.List;

public class AxlView extends ActivateblePanel {

    private final GUI gui;
    private final CPU cpu;
    private final ComponentManager cmanager;
    private final AxlEditorPanel editor;

    public AxlView(final GUI gui) {
        this.gui = gui;
        this.cpu = gui.getCPU();
        this.cmanager = gui.getComponentManager();

        editor = new AxlEditorPanel(new AxlLoadHandler() {
            @Override
            public void loadBinary(List<Integer> binaryFormat) {
                loadIntoMachine(binaryFormat);
            }
        });

        setLayout(new BorderLayout());
        add(editor, BorderLayout.CENTER);
    }

    private void loadIntoMachine(List<Integer> binaryFormat) {
        if (cpu.isLocked()) {
            JOptionPane.showMessageDialog(gui, Messages.get("gui.axl.running"));
            return;
        }
        cmanager.saveDelay();
        boolean clock = cpu.getClockState();
        cpu.setClockState(true);
        gui.getBasicComp().loadProgram(new ProgramBinary(binaryFormat));
        cpu.setClockState(clock);
        cmanager.clearActiveSignals();
        cmanager.restoreDelay();
    }

    @Override
    public void panelActivate() {
        editor.focusEditor();
    }

    @Override
    public void panelDeactivate() {
    }

    @Override
    public String getPanelName() {
        return Messages.get("gui.app.axl");
    }

    @Override
    public void redrawArrows() {
    }
}
