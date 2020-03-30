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
	public enum TYPE {
		INPUT,
		OUTPUT,
		INPUTOUTPUT
	}

	private final Register state = new Register(1);
	private final Register dr = new Register(8);
	private final Register[] registers = {dr, state};
	private final DataDestination irqsc;

	public IOCtrlBasic(long addr, long irq, CPU cpu, TYPE type, DataDestination ... chainctrl) {
		super(addr, 1, irq, cpu);
		cpu.addIRQReqInput(state);

		irqsc = new Valve(state, 1, 0, 0,
			new Valve(irqreg, 3, 0, 0, ioaddr),
			new Valve(Consts.consts[1], 1, 0, 0, new PartWriter(ioctrl, 1, IOControlSignal.IRQ.ordinal())),
			new Not(0, chainctrl)
		);

		Valve rdy = new Valve(Consts.consts[1], 1, 0, 0, new PartWriter(ioctrl, 1, IOControlSignal.RDY.ordinal()));
		Valve clearFlag = new Valve(Consts.consts[0], 1, 0, 0, state);
		Valve r0;
		checkRegister(
			// Register 0
			r0 = new Valve(ioctrl, 8, 0, 0),
			// Register 1
			new Valve(ioctrl, 8, 0, 1,
				// Input - state into iodata 6th bit
				new Valve(Consts.consts[1], 1, 0, IOControlSignal.IN.ordinal(),
					new Valve(state, 1, 0, 0, new PartWriter(iodata, 1, 6)),
					rdy
				),
				// Output - set IRQ
				new Valve(Consts.consts[1], 1, 0, IOControlSignal.OUT.ordinal(),
					new Valve(iodata, irqreg.width, 0, 0, irqreg),
					rdy
				)
			)
		);

		if (type == TYPE.INPUT || type == TYPE.INPUTOUTPUT)
			r0.addDestination(
				new Valve(Consts.consts[1], 1, 0, IOControlSignal.IN.ordinal(),
					new Valve(dr, 8, 0, 0, iodata),
					clearFlag,
					rdy
				)
			);

		if (type == TYPE.OUTPUT || type == TYPE.INPUTOUTPUT)
			r0.addDestination(
				new Valve(Consts.consts[1], 1, 0, IOControlSignal.OUT.ordinal(),
					new Valve(iodata, 8, 0, 0, dr),
					clearFlag,
					rdy
				)
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