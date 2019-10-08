/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.RunningCycle;
import ru.ifmo.cs.bcomp.StateReg;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class RunningCycleView extends BCompComponent {
	private CPU cpu;
	private static final String[] cycles = {
		"Выборка команды",
		"Выбора адреса",
		"Исполнение",
		"Прерывание",
		"Пультовая операция",
		"Программа"
	};
	private final JLabel[] labels = new JLabel[cycles.length];
	private RunningCycle lastcycle = RunningCycle.NONE;
	private int lastprogram = 0;

	public RunningCycleView(CPU cpu, int x, int y) {
		super("Устройство управления", cycles.length);

		this.cpu = cpu;

		setBounds(x, y, REG_16_WIDTH-20);

		for (int i = 0; i < cycles.length; i++) {
			labels[i] = addValueLabel(cycles[i]);
			labels[i].setBounds(1, getValueY(i), width - 2, CELL_HEIGHT);
		}
	}

	public void update() {
		RunningCycle newcycle = cpu.getRunningCycle();
		int newprogram = cpu.getStateValue(StateReg.FLAG_PROG);

		if (newcycle != lastcycle) {
			if (lastcycle != RunningCycle.NONE)
				labels[lastcycle.ordinal()].setForeground(COLOR_TEXT);
			if (newcycle != RunningCycle.NONE)
				labels[newcycle.ordinal()].setForeground(COLOR_ACTIVE);
			lastcycle = newcycle;
		}

		if (newprogram != lastprogram) {
			labels[labels.length - 1].setForeground(newprogram == 0 ? COLOR_TEXT : COLOR_ACTIVE);
			lastprogram = newprogram;
		}
	}
}
