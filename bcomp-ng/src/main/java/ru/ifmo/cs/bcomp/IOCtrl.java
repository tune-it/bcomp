/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import java.util.EnumMap;
import ru.ifmo.cs.components.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class IOCtrl implements DataDestination {
	private final long addr;
	private final long regwidth;
	private final long regmask;
	private long irq;
	final Bus iodata;
	final Bus ioaddr;
	private final CtrlBus ioctrl;
	private final DataDestination chaintctrl;

	public IOCtrl(long _addr, long width, long irq, EnumMap<CPU.IOBuses, Bus> buses, DataDestination chainctrl) {
		this.addr = _addr;
		this.regmask = ~BasicComponent.calculateMask(this.regwidth = width);
		this.irq = irq;
		this.iodata = buses.get(CPU.IOBuses.IOData);
		this.ioaddr = buses.get(CPU.IOBuses.IOAddr);
		this.ioctrl = (CtrlBus)buses.get(CPU.IOBuses.IOCtrl);
		this.chaintctrl = chainctrl;

		ioctrl.addDestination(new DataDestination() {
			@Override
			public void setValue(long value) {
				long reg = ioaddr.getValue();

				if ((reg & regmask) != addr)
					return;

				reg &= ~regmask;

				try {
					if (value == 1) {
						doInput(reg);
						ioctrl.setValue(1, 1, IOControlSignal.RDY.ordinal());
					} else if (value == 2) {
						doOutput(reg);
						ioctrl.setValue(1, 1, IOControlSignal.RDY.ordinal());
					}
				} catch (Exception e) {
					System.err.println("IOCtrl " + Utils.toHex(addr, 8) +
						" register " + Utils.toHex(reg, regwidth) +
						" can't be used for " + e.getMessage());
				}
			}
		});
	}

	@Override
	public synchronized void setValue(long value) {
		// isReady() { ... } else chainctrl.setValue(1) 
		System.out.println("ADDR: " + addr + "IRQ: " + Long.toHexString(value));
	}

	void doInput(long reg) throws Exception {
		throw new Exception("input");
	}

	void doOutput(long reg) throws Exception {
		throw new Exception("output");
	}

	public boolean isReady() {
		return false;
	}

	void setIRQ(long irq) {
		this.irq = irq;
	}

	/*
	public enum Direction {
		IN, OUT, INOUT
	};
	public enum ControlSignal {
		SETFLAG, CHKFLAG, IN, OUT
	};

	private final Register flag;
	private final Register data;
	private final Direction dir;
	private final Valve valveSetFlag = new Valve(Consts.consts[1]);
	private final EnumMap<ControlSignal, DataHandler[]> signals =
		new EnumMap<ControlSignal, DataHandler[]>(ControlSignal.class);

	public IOCtrl(int addr, Direction dir, CPU2IO cpu2io) {
		this.addr = addr;
		this.dir = dir;

		String name = "РД ВУ" + Integer.toString(addr);
		data = new Register(name, name, 8);

		DataComparer dc = new DataComparer(cpu2io.getAddr(), addr, cpu2io.getValveIO());
		ValveDecoder order = new ValveDecoder(cpu2io.getOrder(), dc);

		Valve valveClearFlag = new Valve(Consts.consts[0], 0, order);
		signals.put(ControlSignal.SETFLAG, new DataHandler[] { valveSetFlag, valveClearFlag });

		flag = new Register("Ф ВУ" + Integer.toString(addr), "Флаг ВУ" + Integer.toString(addr), 1, valveSetFlag, valveClearFlag);
		cpu2io.addIntrBusInput(flag);

		cpu2io.addIntrCtrlInput(valveClearFlag);
		cpu2io.addIntrCtrlInput(valveSetFlag);

		cpu2io.addValveClearFlag(valveClearFlag);

		ValveOnce checkFlag = new ValveOnce(flag, 1, order);
		cpu2io.addFlagInput(checkFlag);
		signals.put(ControlSignal.CHKFLAG, new DataHandler[] { checkFlag });

		if (dir != Direction.IN) {
			Valve valveOut = new Valve(cpu2io.getOut(), 3, order);
			valveOut.addDestination(data);
			signals.put(ControlSignal.OUT, new DataHandler[] { valveOut });
		}

		if (dir != Direction.OUT) {
			ValveOnce valveIn = new ValveOnce(data, 2, order);
			cpu2io.addInInput(valveIn);
			signals.put(ControlSignal.IN, new DataHandler[] { valveIn });
		}
	}

	public Direction getDirection() {
		return dir;
	}

	public int getFlag() {
		return flag.getValue();
	}

	public Register getRegFlag() {
		return flag;
	}

	public void setFlag() {
		valveSetFlag.setValue(1);
	}

	public int getData() {
		return data.getValue();
	}

	public Register getRegData() {
		return data;
	}

	public void setData(int value) throws Exception {
		if (dir != Direction.OUT)
			data.setValue(value);
		else
			throw new Exception("Attempt to write to the output device " + addr);
	}

	public void addDestination(ControlSignal cs, DataDestination dest) {
		for (DataHandler valve : signals.get(cs))
			valve.addDestination(dest);
	}

	public void removeDestination(ControlSignal cs, DataDestination dest) {
		for (DataHandler valve : signals.get(cs))
			valve.removeDestination(dest);
	}
*/
}
