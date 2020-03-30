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
	public IOCtrlBasicInput(long addr, long irq, CPU cpu, DataDestination ... chainctrl) {
		super(addr, irq, cpu, chainctrl);
	}
}
