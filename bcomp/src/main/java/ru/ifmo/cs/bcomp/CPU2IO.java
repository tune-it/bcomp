/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.elements.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class CPU2IO {
	private final DataSource valveio;
	private final Bus addr = new Bus(8);
	private final BusSplitter order;
	private final Bus in = new Bus(8);
	private final Bus out = new Bus(8);
	private final Bus intr;
	private final PseudoRegister flag;
	private final DataHandler intrctrl;
	private final DummyValve valveIn = new DummyValve(in);

	public CPU2IO(Register accum, Register state, Bus intrReq, DataSource valveio, DataHandler intrctrl) {
		this.intr = intrReq;
		this.intrctrl = intrctrl;

		addr.addInput(this.valveio = valveio);
		order = new BusSplitter(valveio, 8, 4);

		valveIn.addDestination(new PseudoRegister(accum, 0, 8));
		out.addInput(accum);

		flag = new PseudoRegister(state, StateReg.FLAG_READY);
	}

	public DataSource getValveIO() {
		return valveio;
	}

	public Bus getAddr() {
		return addr;
	}

	public DataSource getOrder() {
		return order;
	}

	public Bus getOut() {
		return out;
	}

	public void addInInput(DataHandler ctrl) {
		in.addInput(ctrl);
		ctrl.addDestination(valveIn);
	}

	public void addFlagInput(DataHandler ctrl) {
		ctrl.addDestination(flag);
	}

	public void addIntrBusInput(DataSource input) {
		intr.addInput(input);
	}

	public void addIntrCtrlInput(DataHandler ctrl) {
		ctrl.addDestination(intrctrl);
	}
}
