/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import java.util.EnumMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import ru.ifmo.cs.components.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class CPU {
	// !!! TEMPORARY PUBLIC !!! BUSES SHOULD BE NOT VISIBLE TO OTHERS !!!
	public enum Buses {
		RIGHT_INPUT,
		LEFT_INPUT,
		RIGHT_COMPLEMENT,
		LEFT_COMPLEMENT,
		ALU_OUT,
		SWITCH_OUT,
		VV,
		EXPECTED,
		NEWMP
	}

	private static final long MR_WIDTH = CS.TYPE.ordinal() + 1;
	private static final long VR_WIDTH = MR_WIDTH - 17;
	private static final long MP_WIDTH = 8;
	private static final long AR_WIDTH = 11;
	private static final long DATA_WIDTH = 16;
	private static final long PS_WIDTH = 16; // !!! FIX WIDTH !!! //

	private final EnumMap<Reg, Register> regs = new EnumMap<Reg, Register>(Reg.class);
	private final EnumMap<CS, Control> valves = new EnumMap<CS, Control>(CS.class);
	private final EnumMap<Buses, Bus> buses = new EnumMap<Buses, Bus>(Buses.class);
	private final Memory mem;
	private final Memory microcode;
	private final Register mp ;
	private final Bus vv;
	private final Bus expected;
	private final Bus newmp;

	protected CPU() {
		Control c;

		// Data Register
		Register dr = new Register(DATA_WIDTH);
		regs.put(Reg.DR, dr);
		// Command Register
		Register cr = new Register(DATA_WIDTH);
		regs.put(Reg.CR, cr);
		// Instruction Pointer
		Register ip = new Register(AR_WIDTH);
		regs.put(Reg.IP, ip);
		// Stack Pointer
		Register sp = new Register(AR_WIDTH);
		regs.put(Reg.SP, sp);
		// ACcumulator
		Register ac = new Register(DATA_WIDTH);
		regs.put(Reg.AC, ac);
		// Buffer Register
		Register br = new Register(DATA_WIDTH);
		regs.put(Reg.BR, br);
		// Program State
		Register ps = new Register(PS_WIDTH);
		regs.put(Reg.PS, ps);
		// Input Register
		Register ir = new Register(DATA_WIDTH);
		regs.put(Reg.IR, ir);
		// Address Register
		Register ar = new Register(AR_WIDTH);
		regs.put(Reg.AR, ar);
		// Microcommand Register
		Register mr = new Register(MR_WIDTH);
		regs.put(Reg.MR, mr);
		// Microcommand Pointer
		regs.put(Reg.MP, mp = new AutoIncRegister(MP_WIDTH));

		mem = new Memory(DATA_WIDTH, ar);
		microcode = new Memory(MR_WIDTH, mp);

		// Read microcommand
		valves.put(CS.CLOCK0, new Valve(microcode, MR_WIDTH, 0, 0, mr));

		Bus right = new Bus(DATA_WIDTH);
		buses.put(Buses.RIGHT_INPUT, right);
		Bus left = new Bus(DATA_WIDTH);
		buses.put(Buses.LEFT_INPUT, left);
		Bus rcom = new Bus(DATA_WIDTH);
		buses.put(Buses.RIGHT_COMPLEMENT, rcom);
		Bus lcom = new Bus(DATA_WIDTH);
		buses.put(Buses.LEFT_COMPLEMENT, lcom);
		Bus aluout = new Bus(DATA_WIDTH + 3);
		buses.put(Buses.ALU_OUT, aluout);
		Bus swout = new Bus(DATA_WIDTH + 2);
		buses.put(Buses.SWITCH_OUT, swout);
		buses.put(Buses.VV, vv = new Bus(1));
		buses.put(Buses.EXPECTED, expected = new Bus(1));
		buses.put(Buses.NEWMP, newmp = new Bus(MP_WIDTH));

		// Execute microcommand
		Control clock = new Valve(mr, MR_WIDTH, 0, 0,
			newValve(dr, DATA_WIDTH, 0, CS.RDDR, right),
			newValve(cr, DATA_WIDTH, 0, CS.RDCR, right),
			newValve(ip, DATA_WIDTH, 0, CS.RDIP, right),
			newValve(sp, DATA_WIDTH, 0, CS.RDSP, right),
			newValve(ac, DATA_WIDTH, 0, CS.RDAC, left),
			newValve(br, DATA_WIDTH, 0, CS.RDBR, left),
			newValve(ps, DATA_WIDTH, 0, CS.RDPS, left),
			newValve(ir, DATA_WIDTH, 0, CS.RDIR, left)
		);
		valves.put(CS.CLOCK1, clock);

		// Pass normal value when CS.COMR is off
		clock.addDestination(new Not(CS.COMR.ordinal(), new Valve(right, DATA_WIDTH, 0, 0, rcom)));
		// Pass complemented value when CS.COMR is on
		clock.addDestination(c = new Complement(right, DATA_WIDTH, 0, CS.COMR.ordinal(), rcom));
		valves.put(CS.COMR, c);

		// Pass normal value when CS.COML is off
		clock.addDestination(new Not(CS.COML.ordinal(), new Valve(left, DATA_WIDTH, 0, 0, lcom)));
		// Pass complemented value when CS.COML is on
		clock.addDestination(c = new Complement(left, DATA_WIDTH, 0, CS.COML.ordinal(), lcom));
		valves.put(CS.COMR, c);

		// Plus 1
		ValveValue carry = new ValveValue(CS.PLS1.ordinal());
		clock.addDestination(carry);
		valves.put(CS.PLS1, carry);

		// AND
		clock.addDestination(c = new DataAnd(lcom, rcom, DATA_WIDTH, CS.SORA.ordinal(), aluout));
		valves.put(CS.SORA, c);

		// SUM
		clock.addDestination(new Not(CS.SORA.ordinal(),
			new DataAdd(lcom, rcom, carry, DATA_WIDTH, 0, aluout),
			new Valve(ps, 1, 0, 0, new PartWriter(aluout, 1, DATA_WIDTH + 2))));

		clock.addDestination(newValve(aluout, 8, 0, CS.LTOL, swout));
		clock.addDestination(newValve(aluout, 8, 0, CS.LTOH,
			new PartWriter(swout, 8, 8)));
		clock.addDestination(newValve(aluout, 8, 8, CS.HTOL, swout));
		clock.addDestination(newValve(aluout, 10, 8, CS.HTOH,
			new PartWriter(swout, 10, 8)));

		// Control Micro Command
		Control vr0 = newValve(mr, VR_WIDTH, 16, CS.TYPE,
			new DataDestination() {
				public synchronized void setValue(long value) {
					newmp.setValue((value >> 24) & Component.calculateMask(8));
					expected.setValue((value >> 32) & 1L);
				}
			}
		);
		clock.addDestination(vr0);
		for (long i = 0; i < 8; i++)
			vr0.addDestination(new Valve(swout, 1, i, i, vv));

		// Operating Micro Command
		Control shrf;
		Control setv;
		PartWriter writeto15 = new PartWriter(swout, 1, DATA_WIDTH - 1);
		PartWriter ei = new PartWriter(ps, 1, State.EI.ordinal());
		valves.put(CS.SEXT, c = new SignExtender(aluout, 8, CS.SEXT.ordinal() - 16, swout));
		clock.addDestination(new Not(CS.TYPE.ordinal(),
			new Valve(mr, VR_WIDTH, 16, 0,
				c,
				newValveH(aluout, DATA_WIDTH, 0, CS.SHLT, new PartWriter(swout, DATA_WIDTH, 1)),
				newValveH(aluout, 1, DATA_WIDTH + 2, CS.SHL0, swout),
				newValveH(aluout, DATA_WIDTH - 1, 1, CS.SHRT, swout),
				new ValveTwo(CS.SHRT.ordinal() - 16, CS.SHRF.ordinal() - 16,
					shrf = new Valve(aluout, 1, DATA_WIDTH - 1, 0, writeto15),
					new Not(0,
						new Valve(aluout, 1, 0, 0, new PartWriter(swout, 1, DATA_WIDTH)),
						new Valve(aluout, 1, DATA_WIDTH + 2, 0, writeto15)
					)
				),
				newValveH(swout, 1, DATA_WIDTH, CS.SETC, new PartWriter(ps, 1, State.C.ordinal())),
				newValveH(swout, 1, DATA_WIDTH - 1, CS.STNZ, new PartWriter(ps, 1, State.N.ordinal())),
				new DataCheckZero(swout, DATA_WIDTH, CS.STNZ.ordinal() - 16, new PartWriter(ps, 1, State.Z.ordinal())),
				setv = new Xor(swout, 2, DATA_WIDTH, CS.SETV.ordinal() - 16, new PartWriter(ps, 1, State.V.ordinal())),
				newValveH(swout, DATA_WIDTH, 0, CS.WRDR, dr),
				newValveH(swout, DATA_WIDTH, 0, CS.WRCR, cr),
				newValveH(swout, AR_WIDTH, 0, CS.WRIP, ip),
				newValveH(swout, AR_WIDTH, 0, CS.WRSP, sp),
				newValveH(swout, DATA_WIDTH, 0, CS.WRAC, ac),
				newValveH(swout, DATA_WIDTH, 0, CS.WRBR, br),
				newValveH(swout, PS_WIDTH, 0, CS.WRPS, ps),
				newValveH(swout, AR_WIDTH, 0, CS.WRAR, ar),
				newValveH(mem, DATA_WIDTH, 0, CS.LOAD, dr),
				newValveH(dr, DATA_WIDTH, 0, CS.STOR, mem),
				newValveH(dr, DATA_WIDTH, 0, CS.IO),
				newValveH(Consts.consts[1], 1, 0, CS.CLRF),
				newValveH(Consts.consts[0], 1, 0, CS.DINT, ei),
				newValveH(Consts.consts[1], 1, 0, CS.EINT, ei),
				newValveH(Consts.consts[0], 1, 0, CS.HALT, new PartWriter(ps, 1, State.PROG.ordinal()))
			)
		));
		valves.put(CS.SHRF, shrf);
		valves.put(CS.SETV, setv);

		clock.addDestination(new DataDestination() {
			public void setValue(long value) {
				mp.setValue(vv.getValue() == expected.getValue() ? newmp.getValue() : 0);
			}
		});
	}

	private Control newValve(DataSource input, long width, long startbit, CS cs, DataDestination ... dsts) {
		Control valve = new Valve(input, width, startbit, cs.ordinal(), dsts);

		valves.put(cs, valve);
		return valve;
	}

	private Control newValveH(DataSource input, long width, long startbit, CS cs, DataDestination ... dsts) {
		Control valve = new Valve(input, width, startbit, cs.ordinal() - 16, dsts);

		valves.put(cs, valve);
		return valve;
	}

	public EnumMap<Reg, Register> getRegisters() {
		return regs;
	}

	// !!! TEMPORARY PUBLIC !!! BUSES SHOULD BE NOT VISIBLE TO OTHERS !!!
	public EnumMap<Buses, Bus> getBuses() {
		return buses;
	}
	
	public Register getRegister(Reg reg) {
		return regs.get(reg);
	}

	public Memory getMemory() {
		return mem;
	}

	public Memory getMicroCode() {
		return microcode;
	}

	public synchronized void step() {
		for (Buses bus: Buses.values())
			buses.get(bus).resetValue();

		valves.get(CS.CLOCK0).setValue(1);
		valves.get(CS.CLOCK1).setValue(1);
	}
/*
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
				return;
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

	public void stopCPU() {
		cpu.interrupt();
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
*/
	/**
	 * Use tickLock() before call this function
	 */
/*	void addDestination(ControlSignal cs, DataDestination dest) {
		valves.get(cs).addDestination(dest);
	}
*/
	/**
	 * Use tickLock() before call this function
	 */
/*	void removeDestination(ControlSignal cs, DataDestination dest) {
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

	public void setRunState(boolean state) {
		tick.lock();
		try {
			valveRunState.setValue(state ? 1 : 0);
		} finally {
			tick.unlock();
		}
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

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		stopCPU();
	}
*/
}
