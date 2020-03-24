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
public class IOCtrlBasic extends IOCtrl {
	private final Register flag = new Register(1);
	final Register dr = new Register(8);

	public IOCtrlBasic(long addr, long irq, EnumMap<CPU.IOBuses, Bus> buses, DataDestination chainctrl) {
		super(addr, 1, irq, buses, chainctrl);
	}

	@Override
	void doInput(long reg) throws Exception {
		if (reg == 1)
			iodata.setValue(flag.getValue() == 0 ? 0 : 0x40);
		else
			super.doInput(reg);
	}

	@Override
	void doOutput(long reg) throws Exception {
		if (reg == 1)
			flag.setValue(0);
		else
			super.doInput(reg);
	}

	@Override
	public boolean isReady() {
		return flag.getValue() == 1;
	}

	public void setReady() {
		flag.setValue(1);
	}

	public Register getDR() {
		return dr;
	}
}