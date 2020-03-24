/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import java.util.EnumMap;
import ru.ifmo.cs.components.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class IOCtrlBasicInput extends IOCtrlBasic {
	public IOCtrlBasicInput(long addr, long irq, EnumMap<CPU.IOBuses, Bus> buses, DataDestination chainctrl) {
		super(addr, irq, buses, chainctrl);
	}

	@Override
	void doInput(long reg) throws Exception {
		if (reg == 0)
			iodata.setValue(dr.getValue());
		else
			super.doInput(reg);
	}
}
