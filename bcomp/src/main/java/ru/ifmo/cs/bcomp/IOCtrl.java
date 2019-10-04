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
public class IOCtrl {
	public enum Direction {
		IN, OUT, INOUT
	};
	public enum ControlSignal {
		SETFLAG, CHKFLAG, IN, OUT
	};

	private final Register flag;
	private final Register data;
	private final int addr;
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
}
