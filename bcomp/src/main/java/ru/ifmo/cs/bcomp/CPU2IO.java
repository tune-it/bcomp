/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.components.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class CPU2IO {
	/*
	private final DummyValve valveio;
	private final Bus addr = new Bus(8);
	private final BusSplitter order;
	private final Bus in = new Bus(8);
	private final Bus out = new Bus(8);
	private final Bus intr;
	private final Bus flagstate = new Bus(1);
	private final DataHandler intrctrl;
	private final DummyValve valveIn = new DummyValve(in);
	private final DataHandler valveClearAll;

	public CPU2IO(Register accum, Register state, Bus intrReq, DataSource valveio, DataHandler valveClearAll, DataHandler intrctrl) {
		this.intr = intrReq;
		this.intrctrl = intrctrl;
		this.valveClearAll = valveClearAll;

		this.valveio = new DummyValve(valveio, valveio);
		addr.addInput(valveio);
		order = new BusSplitter(valveio, 8, 4);

		DummyValve valveSetState = new DummyValve(flagstate, valveio);
		valveSetState.addDestination(new PseudoRegister(state, StateReg.FLAG_READY));

		valveIn.addDestination(new PseudoRegister(accum, 0, 8));
		out.addInput(accum);

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

	public void addFlagInput(DataSource valve) {
		flagstate.addInput(valve);
	}

	public void addIntrBusInput(DataSource input) {
		intr.addInput(input);
	}

	public void addIntrCtrlInput(DataHandler ctrl) {
		ctrl.addDestination(intrctrl);
	}

	public void addValveClearFlag(DataDestination valve) {
		valveClearAll.addDestination(valve);
	} */
}
