/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.RunningCycle;
import ru.ifmo.cs.bcomp.State;
import ru.ifmo.cs.components.Messages;

import java.awt.*;
import java.util.EnumMap;

import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class RunningCycleView extends BCompComponent {
	private CPU cpu;
	private static final String[] cycleslabels = {
		Messages.get("gui.cycle.infetch"),
		Messages.get("gui.cycle.adfetch"),
		Messages.get("gui.cycle.opfetch"),
		Messages.get("gui.cycle.exec"),
		Messages.get("gui.cycle.int"),
		Messages.get("gui.cycle.panel"),
		Messages.get("gui.cycle.program")
	};
	private EnumMap<RunningCycle, Integer> cycles= new EnumMap<RunningCycle, Integer>(RunningCycle.class);

	private final JLabel[] labels = new JLabel[cycleslabels.length];
	private RunningCycle lastcycle = RunningCycle.STOP;

	public RunningCycleView(CPU cpu, int x, int y) {
		super(Messages.get("gui.label.cu"), cycleslabels.length);

		this.cpu = cpu;

		setBounds(x, y, 7*MEM_WIDTH/4);

		for (int i = 0; i < cycleslabels.length; i++) {
			labels[i] = addValueLabel(cycleslabels[i]);
			labels[i].setBounds(1, getValueY(i), width - 2, CELL_HEIGHT);
		}
		cycles.put(RunningCycle.INFETCH,0);
		cycles.put(RunningCycle.ADFETCH,1);
		cycles.put(RunningCycle.OPFETCH,2);
		cycles.put(RunningCycle.EXEC,3);
		cycles.put(RunningCycle.INT,4);
		cycles.put(RunningCycle.START,5);
		cycles.put(RunningCycle.READ,5);
		cycles.put(RunningCycle.WRITE,5);
		cycles.put(RunningCycle.SETIP,5);
	}

	public void update() {
		RunningCycle newcycle = cpu.getRunningCycle();

		if (newcycle != lastcycle) {
			if (lastcycle != RunningCycle.STOP && lastcycle != RunningCycle.RESERVED)
				labels[cycles.get(lastcycle)].setForeground(COLOR_TEXT);
			if (newcycle != RunningCycle.STOP && newcycle != RunningCycle.RESERVED)
			labels[cycles.get(newcycle)].setForeground(COLOR_ACTIVE);
			lastcycle = newcycle;
		}

	}

	public void updateProg(boolean prog){
	labels[labels.length-1].setForeground(prog?COLOR_ACTIVE:COLOR_TEXT);
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
