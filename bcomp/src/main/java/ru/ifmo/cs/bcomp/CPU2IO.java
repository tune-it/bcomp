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
	private DataSource valveio;
	private Bus addr = new Bus(8);
	private BusSplitter order;
	private Bus out = new Bus(8);
	private Bus intr;
	private PseudoRegister flag;
	private PseudoRegister in;
	private DataHandler intrctrl;

	public CPU2IO(Register accum, Register state, Bus intrReq, DataSource valveio, DataHandler intrctrl) {
		this.intr = intrReq;
		this.intrctrl = intrctrl;

		addr.addInput(this.valveio = valveio);
		order = new BusSplitter(valveio, 8, 4);

		out.addInput(accum);

		flag = new PseudoRegister(state, StateReg.FLAG_READY);
		in = new PseudoRegister(accum, 0, 8);
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
		ctrl.addDestination(in);
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
