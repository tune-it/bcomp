/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.components.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class IOCtrlBasicInput extends IOCtrlBasic {
	public IOCtrlBasicInput(long addr, long irq, CPU cpu, DataDestination chainctrl) {
		super(addr, irq, cpu, chainctrl);
	}

	@Override
	void doInput(long reg) throws Exception {
		if (reg == 0)
			iodata.setValue(dr.getValue());
		else
			super.doInput(reg);
	}
}
