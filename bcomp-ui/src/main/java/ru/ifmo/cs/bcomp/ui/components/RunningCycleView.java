/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.RunningCycle;
import ru.ifmo.cs.bcomp.State;

import java.awt.*;

import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class RunningCycleView extends BCompComponent {
	private CPU cpu;
	private static final String[] cycles = {
		"Instruction fetch",
		"Address fetch",
		"Operand fetch",
		"Execution",
		"Interrupt",
		"Remote operanion",
		"Program"
	};
	private final JLabel[] labels = new JLabel[cycles.length];
	private RunningCycle lastcycle = null;
	private long lastprogram = 0;

	public RunningCycleView(CPU cpu, int x, int y) {
		super("Control Unit", cycles.length);

		this.cpu = cpu;

		setBounds(x, y, 7*MEM_WIDTH/4);

		for (int i = 0; i < cycles.length; i++) {
			labels[i] = addValueLabel(cycles[i]);
			labels[i].setBounds(1, getValueY(i), width - 2, CELL_HEIGHT);
		}
	}

	public void update() {
//		RunningCycle newcycle = cpu.getRunningCycle();
//		long newprogram = cpu.getProgramState(State.PROG);
//
//		if (newcycle != lastcycle) {
//			if (lastcycle != null)
//				labels[lastcycle.ordinal()].setForeground(COLOR_TEXT);
//			if (newcycle != null)
//				labels[newcycle.ordinal()].setForeground(COLOR_ACTIVE);
//			lastcycle = newcycle;
//		}
//
//		if (newprogram != lastprogram) {
//			labels[labels.length - 1].setForeground(newprogram == 0 ? COLOR_TEXT : COLOR_ACTIVE);
//			lastprogram = newprogram;
//		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawLine(1, CELL_HEIGHT + 1, width - 2, CELL_HEIGHT + 1);
	}

	@Override
	protected JLabel addValueLabel(String value) {
		return addLabel(value,FONT_COURIER_BOLD_18,COLOR_VALUE);
	}
}
