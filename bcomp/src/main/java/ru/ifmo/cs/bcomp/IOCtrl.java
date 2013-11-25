/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import java.util.EnumMap;
import ru.ifmo.cs.elements.*;

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

	private Register flag;
	private Register data;
	private int addr;
	private Direction dir;
	private Valve valveSetFlag = new Valve(Consts.consts[1]);
	private EnumMap<ControlSignal, Valve[]> signals =
		new EnumMap<ControlSignal, Valve[]>(ControlSignal.class);

	public IOCtrl(int addr, Direction dir, CPU2IO cpu2io) {
		this.addr = addr;
		this.dir = dir;

		String name = "РД ВУ" + Integer.toString(addr);
		data = new Register(name, name, 8);

		DataComparer dc = new DataComparer(cpu2io.getAddr(), addr, cpu2io.getValveIO());
		ValveDecoder order = new ValveDecoder(cpu2io.getOrder(), dc);

		Valve valveClearFlag = new Valve(Consts.consts[0], 0, order);
		signals.put(ControlSignal.SETFLAG, new Valve[] { valveSetFlag, valveClearFlag });

		flag = new Register("Ф ВУ" + Integer.toString(addr), "Флаг ВУ" + Integer.toString(addr), 1, valveSetFlag, valveClearFlag);
		cpu2io.addIntrBusInput(flag);

		cpu2io.addIntrCtrlInput(valveClearFlag);
		cpu2io.addIntrCtrlInput(valveSetFlag);

		Valve checkFlag = new Valve(flag, 1, order);
		cpu2io.addFlagInput(checkFlag);
		signals.put(ControlSignal.CHKFLAG, new Valve[] { checkFlag });

		if (dir != Direction.IN) {
			Valve valveOut = new Valve(cpu2io.getOut(), 3, order);
			valveOut.addDestination(data);
			signals.put(ControlSignal.OUT, new Valve[] { valveOut });
		}

		if (dir != Direction.OUT) {
			Valve valveIn = new Valve(data, 2, order);
			cpu2io.addInInput(valveIn);
			signals.put(ControlSignal.IN, new Valve[] { valveIn });
		}
	}

	public int getFlag() {
		return flag.getValue();
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
		for (Valve valve : signals.get(cs))
			valve.addDestination(dest);
	}

	public void removeDestination(ControlSignal cs, DataDestination dest) {
		for (Valve valve : signals.get(cs))
			valve.removeDestination(dest);
	}
}
