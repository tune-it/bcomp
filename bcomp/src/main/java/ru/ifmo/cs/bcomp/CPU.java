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
	private int runLimit = 4 * 1024 * 1024;
	private final MicroProgram mp;

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

		cpu2io = new CPU2IO(regAccum, regState, intrReq, getValve(ControlSignal.INPUT_OUTPUT, regData), intrctrl);

		valveRunState = getValve(ControlSignal.SET_RUN_STATE);
		StateReg regStateRun = new StateReg(regState, StateReg.FLAG_RUN, valveRunState);

		cu.compileMicroProgram(this.mp = mp);
		cu.jump(ControlUnit.LABEL_STP);
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

	protected synchronized final void addDestination(ControlSignal cs, DataDestination dest) {
		valves.get(cs).addDestination(dest);
	}

	public synchronized void removeDestination(ControlSignal cs, DataDestination dest) {
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

	public Register getRegister(String reg) {
		if (regAccum.name.equals(reg))
			return regAccum;

		if (regBuf.name.equals(reg))
			return regBuf;

		if (regData.name.equals(reg))
			return regData;

		if (regAddr.name.equals(reg))
			return regAddr;

		if (regIP.name.equals(reg))
			return regIP;

		if (regInstr.name.equals(reg))
			return regInstr;

		if (regState.name.equals(reg))
			return regState;

		if (regKey.name.equals(reg))
			return regKey;

		if (cu.getIP().name.equals(reg))
			return cu.getIP();

		if (cu.getInstr().name.equals(reg))
			return cu.getInstr();

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

	// XXX: Hard check for synchronized
	public synchronized void setRegKey(int value) {
		regKey.setValue(value);
	}

	public synchronized void setMicroMemory() {
		cu.setMemory(regKey.getValue());
	}

	public synchronized void invertRunState() {
		valveRunState.setValue(~regState.getValue(StateReg.FLAG_RUN));
	}

	public synchronized void setRunState(int state) {
		valveRunState.setValue(state);
	}

	public synchronized void jump(int label) {
		cu.jump(label);
	}

	public synchronized void jump() {
		cu.setIP(regKey.getValue());
	}

	public synchronized void readMInstr() {
		cu.readInstr();
	}

	public synchronized void cont() {
		valveSetProgram.setValue(clock ? 1 : 0);
	}

	public synchronized boolean step() {
		cu.step();

		return regState.getValue(StateReg.FLAG_PROG) == 1;
	}

	public void start() throws Exception {
		int i = 0;

		cont();

		while (step())
			if ((++i) > runLimit)
				throw new Exception("Exceeded run limit");
	}

	public void startFrom(int label) throws Exception {
		jump(label);
		start();
	}

	public void runSetAddr(int addr) throws Exception {
		setRegKey(addr);
		startFrom(ControlUnit.LABEL_ADDR);
	}

	public void runWrite(int value) throws Exception {
		setRegKey(value);
		startFrom(ControlUnit.LABEL_WRITE);
	}

	public void runRead() throws Exception {
		startFrom(ControlUnit.LABEL_READ);
	}

	public void runStart() throws Exception {
		startFrom(ControlUnit.LABEL_START);
	}

	public boolean getClockState() {
		return clock;
	}

	public void setClockState(boolean clock) {
		this.clock = clock;
	}

	public boolean invertClockState() {
		return clock = !clock;
	}

	public int getRunLimit() {
		return runLimit;
	}

	public void setRunLimit(int runLimit) {
		this.runLimit = runLimit;
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
