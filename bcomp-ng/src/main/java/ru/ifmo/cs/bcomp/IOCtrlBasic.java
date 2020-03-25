/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.components.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class IOCtrlBasic extends IOCtrl {
	private final Register state = new Register(1);
	final Register dr = new Register(8);

	public IOCtrlBasic(long addr, long irq, CPU cpu, DataDestination chainctrl) {
		super(addr, 1, irq, cpu, chainctrl);
		cpu.addIRQReqInput(state);
	}

	@Override
	void doInput(long reg) throws Exception {
		if (reg == 1)
			iodata.setValue(state.getValue() == 0 ? 0 : 0x40);
		else
			super.doInput(reg);
	}

	@Override
	void doOutput(long reg) throws Exception {
		System.out.println("reg: " + reg);
		if (reg == 1) {
			state.setValue(0);
			callbackIRQRq();
		} else
			super.doInput(reg);
	}

	@Override
	public boolean isReady() {
		return state.getValue() == 1;
	}

	@Override
	public void setReady() {
		state.setValue(1);
		callbackIRQRq();
	}

	public Register getStateRegister() {
		return state;
	}

	public Register getDataRegister() {
		return dr;
	}

	public long getData() {
		return dr.getValue();
	}

	public void setData(long value) {
		dr.setValue(value);
	}
}