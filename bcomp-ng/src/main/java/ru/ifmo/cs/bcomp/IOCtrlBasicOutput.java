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
public class IOCtrlBasicOutput extends IOCtrlBasic {
	public IOCtrlBasicOutput(long addr, long irq, EnumMap<CPU.IOBuses, Bus> buses, DataDestination chainctrl) {
		super(addr, irq, buses, chainctrl);
	}

	@Override
	void doOutput(long reg) throws Exception {
		if (reg == 0)
			dr.setValue(iodata.getValue());
		else
			super.doOutput(reg);
	}
}
