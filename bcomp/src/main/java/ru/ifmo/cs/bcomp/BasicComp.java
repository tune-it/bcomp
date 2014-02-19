/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.elements.DataDestination;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BasicComp {
	private final CPU cpu;
	private final IOCtrl[] ioctrls;
	private final IODevTimer timer;

	public BasicComp(MicroProgram mp) throws Exception {
		cpu = new CPU(mp);

		CPU2IO cpu2io = cpu.getCPU2IO();
		ioctrls = new IOCtrl[] {
			new IOCtrl(0, IOCtrl.Direction.OUT, cpu2io),
			new IOCtrl(1, IOCtrl.Direction.OUT, cpu2io),
			new IOCtrl(2, IOCtrl.Direction.IN, cpu2io),
			new IOCtrl(3, IOCtrl.Direction.INOUT, cpu2io)
		};

		timer = new IODevTimer(ioctrls[0]);
	}

	public CPU getCPU() {
		return cpu;
	}

	public IOCtrl[] getIOCtrls() {
		return ioctrls;
	}

	public void startTimer() {
		timer.start("IO0");
	}

	public void stopTimer() {
		timer.done();
	}

	private void ctrlDestination(ControlSignal cs, DataDestination dest, boolean remove) {
		int iodev;
		IOCtrl.ControlSignal iocs;

		switch (cs) {
			case IO0_TSF:
				iodev = 0;
				iocs = IOCtrl.ControlSignal.CHKFLAG;
				break;

			case IO1_TSF:
				iodev = 1;
				iocs = IOCtrl.ControlSignal.CHKFLAG;
				break;

			case IO1_SETFLAG:
				iodev = 1;
				iocs = IOCtrl.ControlSignal.SETFLAG;
				break;

			case IO1_OUT:
				iodev = 1;
				iocs = IOCtrl.ControlSignal.OUT;
				break;

			case IO2_TSF:
				iodev = 2;
				iocs = IOCtrl.ControlSignal.CHKFLAG;
				break;

			case IO2_SETFLAG:
				iodev = 2;
				iocs = IOCtrl.ControlSignal.SETFLAG;
				break;

			case IO2_IN:
				iodev = 2;
				iocs = IOCtrl.ControlSignal.IN;
				break;

			case IO3_TSF:
				iodev = 3;
				iocs = IOCtrl.ControlSignal.CHKFLAG;
				break;

			case IO3_SETFLAG:
				iodev = 3;
				iocs = IOCtrl.ControlSignal.SETFLAG;
				break;

			case IO3_IN:
				iodev = 3;
				iocs = IOCtrl.ControlSignal.IN;
				break;

			case IO3_OUT:
				iodev = 3;
				iocs = IOCtrl.ControlSignal.OUT;
				break;

			default:
				if (remove)
					cpu.removeDestination(cs, dest);
				else
					cpu.addDestination(cs, dest);
				return;
		}

		if (remove)
			ioctrls[iodev].removeDestination(iocs, dest);
		else
			ioctrls[iodev].addDestination(iocs, dest);
	}

	public void addDestination(ControlSignal cs, DataDestination dest) {
		ctrlDestination(cs, dest, false);
	}

	public void removeDestination(ControlSignal cs, DataDestination dest) {
		ctrlDestination(cs, dest, true);
	}
}