/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.MicroProgram;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MicroMemoryView extends MemoryView {
	private final MicroProgram mp;

	public MicroMemoryView(CPU cpu, int x, int y) {
		super(cpu.getMicroMemory(), x, y);
		this.mp = cpu.getMicroProgram();
	}

	@Override
	void updateValue(JLabel label, int value) {
		super.updateValue(label, value);
		label.setToolTipText(mp.decodeCmd(value));
	}
}
