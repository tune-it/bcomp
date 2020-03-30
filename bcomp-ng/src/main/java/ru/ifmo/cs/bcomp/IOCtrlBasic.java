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
	final Register state = new Register(1);
	final Register dr = new Register(8);
	private final Register[] registers = {dr, state};
	private final DataDestination irqsc;

	public IOCtrlBasic(long addr, long irq, CPU cpu, DataDestination ... chainctrl) {
		super(addr, 1, irq, cpu);
		cpu.addIRQReqInput(state);

		irqsc = new Valve(state, 1, 0, 0,
			new Valve(irqreg, 3, 0, 0, ioaddr),
			new Valve(Consts.consts[1], 1, 0, 0, new PartWriter(ioctrl, 1, IOControlSignal.IRQ.ordinal())),
			new Not(0, chainctrl)
		);
	}

	@Override
	public boolean isReady() {
		return state.getValue() == 1;
	}

	@Override
	public void setReady() {
		state.setValue(1);
		super.setReady();
	}

	@Override
	public DataDestination getIRQSC() {
		return irqsc;
	}

	@Override
	public Register[] getRegisters() {
		return registers;
	}
	/**
	 * 
	 * @deprecated 
	 */
	public Register getStateRegister() {
		return state;
	}

	/**
	 * 
	 * @deprecated 
	 */
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