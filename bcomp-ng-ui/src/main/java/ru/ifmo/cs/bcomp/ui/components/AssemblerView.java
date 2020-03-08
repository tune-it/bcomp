/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ProgramBinary;
import ru.ifmo.cs.bcomp.assembler.AsmNg;
import ru.ifmo.cs.bcomp.assembler.Program;
import ru.ifmo.cs.bcomp.ui.GUI;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class AssemblerView extends BCompPanel implements ActionListener {
	private final GUI gui;
	private final CPU cpu;
	private final ComponentManager cmanager;
	private final JTextArea text;
	private final JTextArea errorarea;

	public AssemblerView(final GUI gui) {
                super (gui.getComponentManager(),null,null);
		this.gui = gui;
		this.cpu = gui.getCPU();
		this.cmanager = gui.getComponentManager();
                
                JPanel pane = new JPanel(new BorderLayout());
                
		text = new JTextArea();
		text.setFont(FONT_COURIER_BOLD_21);
		JScrollPane scroll = new JScrollPane(text);
		pane.add(scroll,BorderLayout.CENTER);

		JButton button = new JButton("Компилировать");
		button.setForeground(COLOR_TEXT);
		button.setFont(FONT_COURIER_PLAIN_12);
		button.setFocusable(false);
		button.addActionListener(this);
                JPanel buttonpane = new JPanel();
                //buttonpane.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                buttonpane.add(button);
                //button.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		pane.add(buttonpane,BorderLayout.PAGE_START);
                errorarea = new JTextArea();
                //errorarea.setRows(3);
                errorarea.setEditable(false);
                JScrollPane errscroll = new JScrollPane(errorarea);
                pane.add(errscroll,BorderLayout.SOUTH);
                
                JSplitPane splitpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pane, errscroll);
                splitpane.setDividerSize(4);
                splitpane.setDividerLocation((int)(PANE_HEIGHT*0.8)); // TODO FIX ALL Layouts
                add(splitpane);

	}

	@Override
	public void panelActivate() {
		text.requestFocus();
	}

	@Override
	public void panelDeactivate() { }

	@Override
	public String getPanelName() {
		return "Ассемблер";
	}

	private void showError(String msg) {
		JOptionPane.showMessageDialog(gui, msg, "Ошибка", JOptionPane.ERROR_MESSAGE);
	}

    @Override
    public void redrawArrows() {
        //no arrows no draw
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (cpu.isLocked()) {
            showError("Для компиляции остановите выполняющуюся программу");
            return;
        }

        cmanager.saveDelay();
        boolean clock = cpu.getClockState();
        cpu.setClockState(true);

        //try {
            AsmNg asm = new AsmNg(text.getText());
            Program pobj = asm.compile();
            String errors = new String();
            for (String err: asm.getErrors()) {
                errors = errors + err + '\n';
            }
            errorarea.setText(errors);
            if (pobj != null) {
                gui.getBasicComp().loadProgram(new ProgramBinary(pobj.getBinaryFormat()));
            }
        //} catch (Exception ex) {
        //    showError(ex.getMessage());
        //}
        cpu.setClockState(clock);
        cmanager.clearActiveSignals();
        cmanager.restoreDelay();
    }
}


