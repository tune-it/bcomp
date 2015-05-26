/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import java.util.EnumMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import ru.ifmo.cs.elements.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class CPU {
	public enum Reg {
		ACCUM, BUF, DATA, ADDR, IP, INSTR, STATE, KEY, MIP, MINSTR
	}

	private final Bus aluOutput = new Bus(16);
	private final Bus intrReq = new Bus(1);
	private final Register regState = new Register("C", "РС", StateReg.WIDTH);
	private final ControlUnit cu = new ControlUnit(aluOutput);
	private final EnumMap<ControlSignal, DataHandler> valves =
		new EnumMap<ControlSignal, DataHandler> (ControlSignal.class);
	private final Register regAddr = new Register("РА", "Регистр адреса", 11, getValve(ControlSignal.BUF_TO_ADDR, aluOutput));
	private final Memory mem = new Memory("Память", 16, regAddr);
	private final Register regData = new Register("РД", "Регистр данных", 16,
		getValve(ControlSignal.BUF_TO_DATA, aluOutput),
		getValve(ControlSignal.MEMORY_READ, mem));
	private final Register regInstr = new Register("РК", "Регистр команд", 16, getValve(ControlSignal.BUF_TO_INSTR, aluOutput));
	private final Register regIP = new Register("СК", "Счётчик команд", 11, getValve(ControlSignal.BUF_TO_IP, aluOutput));
	private final Register regAccum = new Register("Акк", "Аккумулятор", 16, getValve(ControlSignal.BUF_TO_ACCUM, aluOutput));
	private final Register regKey = new Register("КР", "Клавишный регистр", 16);
	private final Register regBuf;
	private final DataHandler valveRunState;
	private final DataHandler valveSetProgram;
	private final CPU2IO cpu2io;
	private volatile boolean clock = true;
	private final MicroProgram mp;

	private final ReentrantLock tick = new ReentrantLock();
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition lockStart = lock.newCondition();
	private final Condition lockFinish = lock.newCondition();

	private Runnable tickStartListener = null;
	private Runnable tickFinishListener = null;
	private Runnable cpuStartListener = null;
	private Runnable cpuStopListener = null;

	private final Thread cpu = new Thread(new Runnable() {
		@Override
		public void run() {
			lock.lock();
			try {
				for (;;) {
					lockFinish.signalAll();
					lockStart.await();

					if (cpuStartListener != null)
						cpuStartListener.run();

					if (clock)
						valveSetProgram.setValue(1);

					do {
						if (tickStartListener != null)
							tickStartListener.run();

						tick.lock();
						try {
							cu.step();
						} finally {
							tick.unlock();
						}

						if (tickFinishListener != null)
							tickFinishListener.run();
					} while (regState.getValue(StateReg.FLAG_PROG) == 1);

					if (cpuStopListener != null)
						cpuStopListener.run();
				}
			} catch (InterruptedException e) {
			} finally {
				lock.unlock();
			}
		}
	}, "BComp");

	public CPU(MicroProgram mp) throws Exception {
		getValve(ControlSignal.MEMORY_WRITE, regData).addDestination(mem);

		regState.setValue(2);

		Bus aluRight = new Bus(
			getValve(ControlSignal.DATA_TO_ALU, regData),
			getValve(ControlSignal.INSTR_TO_ALU, regInstr),
			getValve(ControlSignal.IP_TO_ALU, regIP));

		Bus aluLeft = new Bus(
			getValve(ControlSignal.ACCUM_TO_ALU, regAccum),
			getValve(ControlSignal.STATE_TO_ALU, regState),
			getValve(ControlSignal.KEY_TO_ALU, regKey));

		DataSource notLeft = getValve(ControlSignal.INVERT_LEFT, aluLeft);
		DataSource notRight = getValve(ControlSignal.INVERT_RIGHT, aluRight);

		DataSource aluplus1 = getValve(ControlSignal.ALU_PLUS_1, Consts.consts[1]);
		regBuf = new Register("БР", "Буферный регистр", 17,
			getValve(ControlSignal.ALU_AND, notLeft, notRight, aluplus1),
			getValve(ControlSignal.SHIFT_RIGHT, regAccum, regState),
			getValve(ControlSignal.SHIFT_LEFT, regAccum, regState));
		aluOutput.addInput(regBuf);

		StateReg regStateEI = new StateReg(regState, StateReg.FLAG_EI,
			getValve(ControlSignal.DISABLE_INTERRUPTS, Consts.consts[0]),
			getValve(ControlSignal.ENABLE_INTERRUPTS, Consts.consts[1]));

		StateReg regStateC = new StateReg(regState, StateReg.FLAG_C,
			getValve(ControlSignal.BUF_TO_STATE_C, regBuf),
			getValve(ControlSignal.CLEAR_STATE_C, Consts.consts[0]),
			getValve(ControlSignal.SET_STATE_C, Consts.consts[1]));

		StateReg regStateN =
			new StateReg(regState, StateReg.FLAG_N, getValve(ControlSignal.BUF_TO_STATE_N, regBuf));
		StateReg regStateZ =
			new StateReg(regState, StateReg.FLAG_Z, getValve(ControlSignal.BUF_TO_STATE_Z, regBuf));

		StateReg regStateProg = new StateReg(regState, StateReg.FLAG_PROG,
			getValve(ControlSignal.HALT, Consts.consts[0]),
			valveSetProgram = getValve(ControlSignal.SET_PROGRAM));

		DataHandler intrctrl = getValve(ControlSignal.SET_REQUEST_INTERRUPT, regState, intrReq,
			getValve(ControlSignal.DISABLE_INTERRUPTS), getValve(ControlSignal.ENABLE_INTERRUPTS));
		StateReg intrwrite = new StateReg(regState, StateReg.FLAG_INTR, intrctrl);

		cpu2io = new CPU2IO(regAccum, regState, intrReq,
			getValve(ControlSignal.INPUT_OUTPUT, regData),
			getValve(ControlSignal.CLEAR_ALL_FLAGS, Consts.consts[1]),
			intrctrl);

		valveRunState = getValve(ControlSignal.SET_RUN_STATE);
		StateReg regStateRun = new StateReg(regState, StateReg.FLAG_RUN, valveRunState);

		cu.compileMicroProgram(this.mp = mp);
		cu.jump(ControlUnit.LABEL_STP);
	}

	void startCPU() throws InterruptedException {
		lock.lock();
		try {
			cpu.start();
			lockFinish.await();
		} finally {
			lock.unlock();
		}
	}

	private DataHandler getValve(ControlSignal cs, DataSource ... inputs) {
		DataHandler valve = valves.get(cs);

		if (valve == null)
			valves.put(cs, valve = cu.createValve(cs, inputs));

		return valve;
	}

	public CPU2IO getCPU2IO() {
		return cpu2io;
	}

	public void setTickStartListener(Runnable tickStartListener) {
		this.tickStartListener = tickStartListener;
	}

	public void setTickFinishListener(Runnable tickFinishListener) {
		this.tickFinishListener = tickFinishListener;
	}

	public void setCPUStartListener(Runnable cpuStartListener) {
		this.cpuStartListener = cpuStartListener;
	}

	public void setCPUStopListener(Runnable cpuStopListener) {
		this.cpuStopListener = cpuStopListener;
	}

	void tickLock() {
		tick.lock();
	}

	void tickUnlock() {
		tick.unlock();
	}

	/**
	 * Use tickLock() before call this function
	 */
	void addDestination(ControlSignal cs, DataDestination dest) {
		valves.get(cs).addDestination(dest);
	}

	/**
	 * Use tickLock() before call this function
	 */
	void removeDestination(ControlSignal cs, DataDestination dest) {
		valves.get(cs).removeDestination(dest);
	}

	public Register getRegister(Reg reg) {
		switch (reg) {
		case ACCUM:
			return regAccum;

		case BUF:
			return regBuf;

		case DATA:
			return regData;

		case ADDR:
			return regAddr;

		case IP:
			return regIP;

		case INSTR:
			return regInstr;

		case STATE:
			return regState;

		case KEY:
			return regKey;

		case MIP:
			return cu.getIP();

		case MINSTR:
			return cu.getInstr();
		}

		return null;
	}

	public Reg findRegister(String reg) {
		if (regAccum.name.equals(reg))
			return Reg.ACCUM;

		if (regBuf.name.equals(reg))
			return Reg.BUF;

		if (regData.name.equals(reg))
			return Reg.DATA;

		if (regAddr.name.equals(reg))
			return Reg.ADDR;

		if (regIP.name.equals(reg))
			return Reg.IP;

		if (regInstr.name.equals(reg))
			return Reg.INSTR;

		if (regState.name.equals(reg))
			return Reg.STATE;

		if (regKey.name.equals(reg))
			return Reg.KEY;

		if (cu.getIP().name.equals(reg))
			return Reg.MIP;

		if (cu.getInstr().name.equals(reg))
			return Reg.MINSTR;

		return null;
	}

	public int getRegValue(Reg reg) {
		return getRegister(reg).getValue();
	}

	public int getRegWidth(Reg reg) {
		return getRegister(reg).getWidth();
	}

	public int getStateValue(int startbit) {
		return regState.getValue(startbit);
	}

	public boolean isRunning() {
		return lock.isLocked();
	}

	public Memory getMemory() {
		return mem;
	}

	public int getMemoryValue(int addr) {
		return mem.getValue(addr);
	}

	public Memory getMicroMemory() {
		return cu.getMemory();
	}

	public int getMicroMemoryValue(int addr) {
		return cu.getMemoryValue(addr);
	}

	public void setRegKey(int value) {
		regKey.setValue(value);
	}

	public void invertRunState() {
		tick.lock();
		try {
			valveRunState.setValue(~regState.getValue(StateReg.FLAG_RUN));
		} finally {
			tick.unlock();
		}
	}

	public boolean getClockState() {
		return clock;
	}

	public void setClockState(boolean clock) {
		tick.lock();
		try {
			this.clock = clock;
			if (!clock)
				valveSetProgram.setValue(0);
		} finally {
			tick.unlock();
		}
	}

	public boolean invertClockState() {
		setClockState(!clock);
		return clock;
	}

	private void jump(int label) {
		if (label != ControlUnit.NO_LABEL)
			cu.jump(label);
	}

	private boolean startFrom(int label) {
		if (lock.tryLock()) {
			try {
				jump(label);
				lockStart.signal();
			} finally {
				lock.unlock();
			}
			return true;
		}
		return false;
	}

	public boolean startSetAddr() {
		return startFrom(ControlUnit.LABEL_ADDR);
	}

	public boolean startWrite() {
		return startFrom(ControlUnit.LABEL_WRITE);
	}

	public boolean startRead() {
		return startFrom(ControlUnit.LABEL_READ);
	}

	public boolean startStart() {
		return startFrom(ControlUnit.LABEL_START);
	}

	public boolean startContinue() {
		return startFrom(ControlUnit.NO_LABEL);
	}

	private boolean runFrom(int label) {
		if (lock.tryLock()) {
			try {
				jump(label);
				lockStart.signal();
				lockFinish.await();
			} catch (InterruptedException e) {
			} finally {
				lock.unlock();
			}
			return true;
		}
		return false;
	}

	public boolean runSetAddr() {
		return runFrom(ControlUnit.LABEL_ADDR);
	}

	public boolean runSetAddr(int addr) {
		setRegKey(addr);
		return runSetAddr();
	}

	public boolean runWrite() {
		return runFrom(ControlUnit.LABEL_WRITE);
	}

	public boolean runWrite(int value) {
		setRegKey(value);
		return runWrite();
	}

	public boolean runRead() {
		return runFrom(ControlUnit.LABEL_READ);
	}

	public boolean runStart() {
		return runFrom(ControlUnit.LABEL_START);
	}

	public boolean runContinue() {
		return runFrom(ControlUnit.NO_LABEL);
	}

	public boolean runSetMAddr() {
		if (lock.tryLock()) {
			try {
				cu.setIP(regKey.getValue());
			} finally {
				lock.unlock();
			}
			return true;
		}
		return false;
	}

	public boolean runMWrite() {
		if (lock.tryLock()) {
			try {
				cu.setMemory(regKey.getValue());
			} finally {
				lock.unlock();
			}
			return true;
		}
		return false;
	}

	public boolean runMRead() {
		if (lock.tryLock()) {
			try {
				cu.readInstr();
			} finally {
				lock.unlock();
			}
			return true;
		}
		return false;
	}

	public MicroProgram getMicroProgram() {
		return mp;
	}

	public String getMicroProgramName() {
		return mp.microprogramName;
	}

	public int getIntrCycleStartAddr() {
		return cu.getIntrCycleStartAddr();
	}

	public Instruction[] getInstructionSet() {
		return mp.instructionSet;
	}

	public RunningCycle getRunningCycle() {
		return cu.getCycle();
	}
}
