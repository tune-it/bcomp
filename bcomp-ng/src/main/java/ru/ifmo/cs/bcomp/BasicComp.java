/*
 * $Id$
 */
package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BasicComp {

    private final CPU cpu;
    private final IOCtrl[] ioctrls = new IOCtrl[10];
    private final IODevTimer timer;

    public BasicComp() throws Exception {
        cpu = new CPU();
        cpu.startCPU();

        cpu.addDestination(ControlSignal.INTS,
                (ioctrls[0] = new IOCtrlBasic(0x00, cpu, IOCtrlBasic.TYPE.INPUTOUTPUT,
                        (ioctrls[1] = new IOCtrlBasic(0x02, cpu, IOCtrlBasic.TYPE.OUTPUT,
                                (ioctrls[2] = new IOCtrlBasic(0x04, cpu, IOCtrlBasic.TYPE.INPUT,
                                        (ioctrls[3] = new IOCtrlBasic(0x06, cpu, IOCtrlBasic.TYPE.INPUTOUTPUT,
                                                (ioctrls[4] = new IOCtrlAdv(0x08, cpu,
                                                        (ioctrls[5] = new IOCtrlBasic(0x0C, cpu, IOCtrlBasic.TYPE.OUTPUT,
                                                                (ioctrls[6] = new IOCtrlBasic(0x10, cpu, IOCtrlBasic.TYPE.OUTPUT,
                                                                        (ioctrls[7] = new IOCtrlBasic(0x14, cpu, IOCtrlBasic.TYPE.OUTPUT,
                                                                                (ioctrls[8] = new IOCtrlBasic(0x18, cpu, IOCtrlBasic.TYPE.INPUT,
                                                                                        (ioctrls[9] = new IOCtrlBasic(0x1C, cpu, IOCtrlBasic.TYPE.INPUT)).getIRQSC()
                                                                                )).getIRQSC()
                                                                        )).getIRQSC()
                                                                )).getIRQSC()
                                                        )).getIRQSC()
                                                )).getIRQSC()
                                        )).getIRQSC()
                                )).getIRQSC()
                        )).getIRQSC()
                )).getIRQSC()
        );
        timer = new IODevTimer(ioctrls[0]);
    }

    public CPU getCPU() {
        return cpu;
    }

    public void addDestination(SignalListener[] listeners) {
        cpu.tickLock();
        try {
            for (SignalListener listener : listeners) {
                for (ControlSignal signal : listener.signals) {
                    cpu.addDestination(signal, listener.dest);
                }
            }
        } finally {
            cpu.tickUnlock();
        }
    }

    public void removeDestination(SignalListener[] listeners) {
        cpu.tickLock();
        try {
            for (SignalListener listener : listeners) {
                for (ControlSignal signal : listener.signals) {
                    cpu.removeDestination(signal, listener.dest);
                }
            }
        } finally {
            cpu.tickUnlock();
        }
    }

    public void loadProgram(ProgramBinary prog) throws RuntimeException {
        if (cpu.isLocked()) {
            throw new RuntimeException("Операция невозможна: выполняется программа");
        }
        if (!cpu.executeSetAddr(prog.load_address)) {
            throw new RuntimeException("Операция прервана: выполняется программа");
        }
        for (Integer cmd : prog.binary) {
            if (!cpu.executeWrite(cmd)) {
                throw new RuntimeException("Операция прервана: выполняется программа");
            }
        }
        if (!cpu.executeSetAddr(prog.start_address)) {
            throw new RuntimeException("Операция прервана: выполняется программа");
        }
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

    /*
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

	public void addDestination(ControlSignal cs, DataDestination dest) {
		ctrlDestination(cs, dest, false);
	}

	public void removeDestination(ControlSignal cs, DataDestination dest) {
		ctrlDestination(cs, dest, true);
	}
     */
}
