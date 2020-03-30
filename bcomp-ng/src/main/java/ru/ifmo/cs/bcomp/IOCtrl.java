/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.components.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public abstract class IOCtrl {
	final Bus iodata;
	final Bus ioaddr;
	final CtrlBus ioctrl;
	final Register irqreg = new Register(3);
	private final Decoder chkregister;
	private final Control irqrqvalve;

	public IOCtrl(long addr, long width, long irq, CPU cpu) {
		Register devaddr = new Register(8 - width);
		devaddr.setValue(addr >> width);

		irqreg.setValue(irq);

		irqrqvalve = cpu.getIRQReqValve();

		iodata = cpu.getIOBuses().get(CPU.IOBuses.IOData);
		ioaddr = cpu.getIOBuses().get(CPU.IOBuses.IOAddr);
		ioctrl = (CtrlBus)cpu.getIOBuses().get(CPU.IOBuses.IOCtrl);
		ioctrl.addDestination(
			// Is set DI?
			new Not(IOControlSignal.DI.ordinal(), new Valve(ioctrl, 1, IOControlSignal.EI.ordinal(), 0,
				// Is set EI?
				new Not(0, new Valve(ioctrl, 1, IOControlSignal.IRQ.ordinal(), 0,
					// Is set IRQ?
					new Not(0, new Valve(ioctrl, 1, IOControlSignal.RDY.ordinal(), 0,
						// Is set RDY?
						new Not(0, new Valve(new InputBus(8 - width, width, ioaddr), 8 - width, 0, 0,
							// Requested my address?
							new Comparer(devaddr,
								// Ok, decode register
								chkregister = new Decoder(ioaddr, 0, width, 0)
							)
						))
					))
				))
			))
		);
	}

	public void setReady() {
		irqrqvalve.setValue(1);
	}

	public final void checkRegister(DataDestination ... dsts) {
		chkregister.addDestination(dsts);
	}

	public abstract Register[] getRegisters();
	public abstract DataDestination getIRQSC();
	public abstract boolean isReady();
	public abstract long getData();
	public abstract void setData(long value);

	void setIRQ(long irq) {
		irqreg.setValue(irq);
	}

	public long getIRQ() {
		return irqreg.getValue();
	}
}
