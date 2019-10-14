/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import java.util.EnumMap;
import ru.ifmo.cs.components.Bus;
import ru.ifmo.cs.components.DataDestination;
import ru.ifmo.cs.components.Memory;
import ru.ifmo.cs.components.Register;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BasicComp {
	private final CPU cpu;
//	private final IOCtrl[] ioctrls;
//	private final IODevTimer timer;

	public BasicComp() throws Exception {
		cpu = new CPU();
		//cpu.startCPU();

/*		CPU2IO cpu2io = cpu.getCPU2IO();
		ioctrls = new IOCtrl[] {
			new IOCtrl(0, IOCtrl.Direction.OUT, cpu2io),
			new IOCtrl(1, IOCtrl.Direction.OUT, cpu2io),
			new IOCtrl(2, IOCtrl.Direction.IN, cpu2io),
			new IOCtrl(3, IOCtrl.Direction.INOUT, cpu2io),
			new IOCtrl(4, IOCtrl.Direction.OUT, cpu2io),
			new IOCtrl(5, IOCtrl.Direction.OUT, cpu2io),
			new IOCtrl(6, IOCtrl.Direction.OUT, cpu2io),
			new IOCtrl(7, IOCtrl.Direction.IN, cpu2io),
			new IOCtrl(8, IOCtrl.Direction.IN, cpu2io),
			new IOCtrl(9, IOCtrl.Direction.INOUT, cpu2io),
		};
		timer = new IODevTimer(ioctrls[0]);*/
	}

	public static void main(String[] args) {
		CPU cpu = new CPU();
		Memory mem = cpu.getMemory();
		Memory microcode = cpu.getMicroCode();
		EnumMap<Reg, Register> regs = cpu.getRegisters();
		EnumMap<CPU.Buses, Bus> buses = cpu.getBuses();

		microcode.setValue(0,
				(1L << CS.RDAC.ordinal()) |
				(1L << CS.LTOL.ordinal()) |
				(1L << CS.WRAR.ordinal()) |
				(1L << CS.COMR.ordinal()));
		regs.get(Reg.AC).setValue(0xDEAD);
		regs.get(Reg.PS).setValue(0x1);
		System.out.println("Before:\t" + Utils.toHex(regs.get(Reg.MR).getValue(), 40));
		cpu.step();
		System.out.println("After:\t" + Utils.toHex(regs.get(Reg.MR).getValue(), 40));

		System.out.println("Right:\t" + Utils.toHex(buses.get(CPU.Buses.RIGHT_INPUT).getValue(), 16));
		System.out.println("Left:\t" + Utils.toHex(buses.get(CPU.Buses.LEFT_INPUT).getValue(), 16));
		System.out.println("RightL:\t" + Utils.toHex(buses.get(CPU.Buses.RIGHT_COMPLEMENT).getValue(), 16));
		System.out.println("LeftL:\t" + Utils.toHex(buses.get(CPU.Buses.LEFT_COMPLEMENT).getValue(), 16));
		System.out.println("ALU_OUT:\t" + Utils.toHex(buses.get(CPU.Buses.ALU_OUT).getValue(), 19));
	}

/*
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

		cpu.tickLock();
		try {
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

				case IO4_TSF:
					iodev = 4;
					iocs = IOCtrl.ControlSignal.CHKFLAG;
					break;

				case IO5_TSF:
					iodev = 5;
					iocs = IOCtrl.ControlSignal.CHKFLAG;
					break;

				case IO6_TSF:
					iodev = 6;
					iocs = IOCtrl.ControlSignal.CHKFLAG;
					break;

				case IO7_TSF:
					iodev = 7;
					iocs = IOCtrl.ControlSignal.CHKFLAG;
					break;

				case IO7_IN:
					iodev = 7;
					iocs = IOCtrl.ControlSignal.IN;
					break;

				case IO8_TSF:
					iodev = 8;
					iocs = IOCtrl.ControlSignal.CHKFLAG;
					break;

				case IO8_IN:
					iodev = 8;
					iocs = IOCtrl.ControlSignal.IN;
					break;

				case IO9_TSF:
					iodev = 9;
					iocs = IOCtrl.ControlSignal.CHKFLAG;
					break;

				case IO9_IN:
					iodev = 9;
					iocs = IOCtrl.ControlSignal.IN;
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
		} finally {
			cpu.tickUnlock();
		}
	}
*/ /*
	public void addDestination(ControlSignal cs, DataDestination dest) {
		ctrlDestination(cs, dest, false);
	}

	public void removeDestination(ControlSignal cs, DataDestination dest) {
		ctrlDestination(cs, dest, true);
	}

	public void addDestination(SignalListener[] listeners) {
		cpu.tickLock();
		try {
			for (SignalListener listener : listeners)
				for (ControlSignal signal : listener.signals)
					addDestination(signal, listener.dest);
		} finally {
			cpu.tickUnlock();
		}
	}

	public void removeDestination(SignalListener[] listeners) {
		cpu.tickLock();
		try {
			for (SignalListener listener : listeners)
				for (ControlSignal signal : listener.signals)
					removeDestination(signal, listener.dest);
		} finally {
			cpu.tickUnlock();
		}
	}
*/
}