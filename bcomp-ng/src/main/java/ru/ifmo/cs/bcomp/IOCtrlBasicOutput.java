/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.components.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class IOCtrlBasicOutput extends IOCtrlBasic {
	public IOCtrlBasicOutput(long addr, long irq, CPU cpu, DataDestination chainctrl) {
		super(addr, irq, cpu, chainctrl);
	}

	@Override
	void doOutput(long reg) throws Exception {
		if (reg == 0)
			dr.setValue(iodata.getValue());
		else
			super.doOutput(reg);
	}
}
