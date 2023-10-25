/*
 * $Id$
 */
package ru.ifmo.cs.bcomp;

import java.util.EnumMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import ru.ifmo.cs.components.*;
import static ru.ifmo.cs.bcomp.ControlSignal.*;
import static ru.ifmo.cs.bcomp.State.*;
import static ru.ifmo.cs.bcomp.RunningCycle.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class CPU {

    private enum Buses {
        RIGHT_INPUT,
        LEFT_INPUT,
        RIGHT_COMPLEMENT,
        LEFT_COMPLEMENT,
        ALU_OUT,
        SWITCH_OUT,
        VV,
        EXPECTED,
        NEWMP,
    }

    private static final long MR_WIDTH = TYPE.ordinal() + 1;
    private static final long VR_WIDTH = MR_WIDTH - 17;
    private static final long MP_WIDTH = 8;
    private static final long AR_WIDTH = 11;
    private static final long DATA_WIDTH = 16;
    private static final long IO_WIDTH = 8;
    private static final long IOCMD_WIDTH = 3;
    private static final long PS_WIDTH = P.ordinal() + 1;

    private final EnumMap<Reg, Register> regs = new EnumMap<Reg, Register>(Reg.class);
    private final EnumMap<ControlSignal, Control> valves = new EnumMap<ControlSignal, Control>(ControlSignal.class);
    private final EnumMap<Buses, Bus> buses = new EnumMap<Buses, Bus>(Buses.class);
    private final EnumMap<IOBuses, Bus> iobuses = new EnumMap<IOBuses, Bus>(IOBuses.class);
    private final EnumMap<RunningCycle, Integer> labels = new EnumMap<RunningCycle, Integer>(RunningCycle.class);
    private final MicroCode mc = new MicroCode();
    private final Memory mem;
    private final Memory microcode;
    private final Register ps;
    private final Register ir;
    private final Register mp;
    private final Bus vv;
    private final Bus expected;
    private final Bus newmp;
    private final InputBus irqreq = new InputBus(1);
    private volatile boolean clock = true;
    private volatile long debuglevel = 0;

    private final ReentrantLock tick = new ReentrantLock();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition lockStart = lock.newCondition();
    private final Condition lockFinish = lock.newCondition();

    private volatile Runnable tickStartListener = null;
    private volatile Runnable tickFinishListener = null;
    private volatile Runnable cpuStartListener = null;
    private volatile Runnable cpuStopListener = null;

    private final Thread cpu = new Thread(new Runnable() {
        @Override
        public void run() {
            lock.lock();

            try {
                for (;;) {
                    lockFinish.signalAll();
                    lockStart.await();

                    if (cpuStartListener != null) {
                        cpuStartListener.run();
                    }

                    if (clock) {
                        valves.get(SET_PROGRAM).setValue(1);
                    }

                    do {
                        if (tickStartListener != null) {
                            tickStartListener.run();
                        }

                        tick.lock();
                        try {
                            step();
                        } finally {
                            tick.unlock();
                        }

                        if (tickFinishListener != null) {
                            tickFinishListener.run();
                        }
                    } while (ps.getValue(P.ordinal()) == 1);

                    if (cpuStopListener != null) {
                        cpuStopListener.run();
                    }
                }
            } catch (InterruptedException e) {
                return;
            } finally {
                lock.unlock();
            }
        }
    }, "BComp");

    protected CPU() throws Exception {
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
        regs.put(Reg.PS, ps = new Register(PS_WIDTH));
        // Input Register
        regs.put(Reg.IR, ir = new Register(DATA_WIDTH));
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
        valves.put(CLOCK0, new Valve(microcode, MR_WIDTH, 0, 0, mr));

        // Internal buses
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
        // IO buses
        Bus iodata = new Bus(IO_WIDTH);
        iobuses.put(IOBuses.IOData, iodata);
        Bus ioaddr = new Bus(IO_WIDTH);
        iobuses.put(IOBuses.IOAddr, ioaddr);
        CtrlBus ioctrl = new CtrlBus(IO_WIDTH);
        iobuses.put(IOBuses.IOCtrl, ioctrl);
        // Update INT flag
        ValveAnd irqrq = new ValveAnd(ps, EI.ordinal(), irqreq, new PartWriter(ps, 1, State.INT.ordinal()));

        // Execute microcommand
        Control clock1 = new Valve(mr, MR_WIDTH, 0, 0,
                newValve(dr, DATA_WIDTH, 0, RDDR, right),
                newValve(cr, DATA_WIDTH, 0, RDCR, right),
                newValve(ip, DATA_WIDTH, 0, RDIP, right),
                newValve(sp, DATA_WIDTH, 0, RDSP, right),
                newValve(ac, DATA_WIDTH, 0, RDAC, left),
                newValve(br, DATA_WIDTH, 0, RDBR, left),
                newValve(ps, DATA_WIDTH, 0, RDPS, left),
                newValve(ir, DATA_WIDTH, 0, RDIR, left)
        );
        valves.put(CLOCK1, clock1);

        // Pass normal value when COMR is off
        clock1.addDestination(new Not(COMR.ordinal(), new Valve(right, DATA_WIDTH, 0, 0, rcom)));
        // Pass complemented value when COMR is on
        clock1.addDestination(c = new Complement(right, DATA_WIDTH, 0, COMR.ordinal(), rcom));
        valves.put(COMR, c);

        // Pass normal value when COML is off
        clock1.addDestination(new Not(COML.ordinal(), new Valve(left, DATA_WIDTH, 0, 0, lcom)));
        // Pass complemented value when COML is on
        clock1.addDestination(c = new Complement(left, DATA_WIDTH, 0, COML.ordinal(), lcom));
        valves.put(COMR, c);

        // Plus 1
        ValveValue carry = new ValveValue(PLS1.ordinal());
        clock1.addDestination(carry);
        valves.put(PLS1, carry);

        // AND
        clock1.addDestination(c = new DataAnd(lcom, rcom, DATA_WIDTH, SORA.ordinal(), aluout));
        valves.put(SORA, c);

        // SUM
        PartWriter writetoH = new PartWriter(swout, 8, 8);
        clock1.addDestination(new Not(SORA.ordinal(),
                new DataAdd(lcom, rcom, carry, DATA_WIDTH, 0, aluout),
                new Valve(ps, 1, 0, 0, new PartWriter(aluout, 1, DATA_WIDTH + 2))));

        clock1.addDestination(newValve(aluout, 8, 0, LTOL, swout));
        clock1.addDestination(newValve(aluout, 8, 0, LTOH,
                writetoH));
        clock1.addDestination(newValve(aluout, 8, 8, HTOL, swout));
        clock1.addDestination(newValve(aluout, 10, 8, HTOH,
                new PartWriter(swout, 10, 8)));

        // Control Micro Command
        Control vr0 = newValve(mr, VR_WIDTH, 16, TYPE,
                new DataDestination() {
            @Override
            public synchronized void setValue(long value) {
                newmp.setValue((value >> 8) & BasicComponent.calculateMask(8));
                expected.setValue((value >> 16) & 1L);
            }
        }
        );
        clock1.addDestination(vr0);
        for (long i = 0; i < 8; i++) {
            vr0.addDestination(new Valve(swout, 1, i, i, vv));
        }

        // Operating Micro Command
        Control shrf;
        Control setv;
        PartWriter writeto15 = new PartWriter(swout, 1, DATA_WIDTH - 1);
        PartWriter writeto17 = new PartWriter(swout, 1, DATA_WIDTH + 1);
        PartWriter stateProgram = new PartWriter(ps, 1, P.ordinal());
        valves.put(SEXT, c = new Extender(aluout, 8, 7, SEXT.ordinal() - 16, writetoH));

        clock1.addDestination(new Not(TYPE.ordinal(),
                new Valve(mr, VR_WIDTH, 16, 0,
                        c,
                        new Valve(aluout, 1, 14, SHLT.ordinal() - 16, writeto17),
                        newValveH(aluout, DATA_WIDTH, 0, SHLT, new PartWriter(swout, DATA_WIDTH, 1)),
                        newValveH(aluout, 1, DATA_WIDTH + 2, SHL0, swout),
                        newValveH(aluout, DATA_WIDTH - 1, 1, SHRT, swout),
                        new Valve(aluout, 1, 0, SHRT.ordinal() - 16, new PartWriter(swout, 1, DATA_WIDTH)),
                        new ValveTwo(SHRT.ordinal() - 16, SHRF.ordinal() - 16,
                                shrf = new Valve(aluout, 1, DATA_WIDTH + 2, 0, writeto15, writeto17),
                                new Not(0, new Valve(aluout, 1, DATA_WIDTH - 1, 0, writeto15, writeto17))
                        ),
                        newValveH(swout, 1, DATA_WIDTH, SETC, new PartWriter(ps, 1, C.ordinal())),
                        setv = new Xor(swout, 2, DATA_WIDTH, SETV.ordinal() - 16, new PartWriter(ps, 1, V.ordinal())),
                        new DataCheckZero(swout, DATA_WIDTH, STNZ.ordinal() - 16, new PartWriter(ps, 1, Z.ordinal())),
                        newValveH(swout, 1, DATA_WIDTH - 1, STNZ, new PartWriter(ps, 1, N.ordinal())),
                        newValveH(swout, DATA_WIDTH, 0, WRDR, dr),
                        newValveH(swout, DATA_WIDTH, 0, WRCR, cr),
                        newValveH(swout, AR_WIDTH, 0, WRIP, ip),
                        newValveH(swout, AR_WIDTH, 0, WRSP, sp),
                        newValveH(swout, DATA_WIDTH, 0, WRAC, ac),
                        newValveH(swout, DATA_WIDTH, 0, WRBR, br),
                        newValveH(swout, PS_WIDTH, 0, WRPS, new PartWriter(ps, 6, 0), irqrq),
                        newValveH(swout, AR_WIDTH, 0, WRAR, ar),
                        newValveH(mem, DATA_WIDTH, 0, LOAD, dr),
                        newValveH(dr, DATA_WIDTH, 0, STOR, mem),
                        newValveH(Consts.consts[1], 1, 0, IO,
                                new Valve(cr, IO_WIDTH, 0, 0, ioaddr),
                                new Decoder(cr, IO_WIDTH, IOCMD_WIDTH, 0, ioctrl)
                        ),
                        newValveH(Consts.consts[1], 1, 0, INTS),
                        newValveH(Consts.consts[0], 1, 0, HALT, stateProgram)
                )
        ));
        valves.put(SHRF, shrf);
        valves.put(SETV, setv);
        valves.put(SET_PROGRAM, new Valve(Consts.consts[1], 1, 0, 0, stateProgram));

        clock1.addDestination(new DataDestination() {
            @Override
            public void setValue(long value) {
                mp.setValue(vv.getValue() == expected.getValue() ? newmp.getValue() : 0);
            }
        });

        for (int i = 0; i < mc.getMicroCodeLength(); i++) {
            microcode.setValue(i, mc.getMicroCommand(i));
        }

        for (RunningCycle cycle : RunningCycle.values()) {
            labels.put(cycle, findLabel(cycle.name()));
        }

        mp.setValue(labels.get(STOP) + 1);

        // IO specific staff
        valves.put(SET_REQUEST_INTERRUPT, irqrq);
        Control ei = new Control(1, 0, 0, new PartWriter(ps, 1, EI.ordinal()), irqrq);
        valves.put(SET_EI, ei);

        ioctrl.addDestination(
                // Output: open AC to IO data
                new ValveTwo(IOControlSignal.OUT.ordinal(), IOControlSignal.RDY.ordinal(),
                        new Not(0, new Valve(ac, IO_WIDTH, 0, 0, iodata))
                ),
                // Input: IO data to AC
                new ValveTwo(IOControlSignal.IN.ordinal(), IOControlSignal.RDY.ordinal(),
                        new Valve(iodata, IO_WIDTH, 0, 0, new PartWriter(ac, IO_WIDTH, 0))
                ),
                // Disable interrupts
                new Valve(Consts.consts[0], 1, 0, IOControlSignal.DI.ordinal(), ei),
                // Enable interrupts
                new Valve(Consts.consts[1], 1, 0, IOControlSignal.EI.ordinal(), ei),
                // INT SC
                new Valve(ioaddr, 3, 0, IOControlSignal.IRQ.ordinal(), new PartWriter(cr, 8, 0))
        );
    }

    private Control newValve(DataSource input, long width, long startbit, ControlSignal cs, DataDestination... dsts) {
        Control valve = new Valve(input, width, startbit, cs.ordinal(), dsts);

        valves.put(cs, valve);
        return valve;
    }

    private Control newValveH(DataSource input, long width, long startbit, ControlSignal cs, DataDestination... dsts) {
        Control valve = new Valve(input, width, startbit, cs.ordinal() - 16, dsts);

        valves.put(cs, valve);
        return valve;
    }

    public EnumMap<Reg, Register> getRegisters() {
        return regs;
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

    public MicroCode getMicroCodeSource() {
        return mc;
    }

    public EnumMap<IOBuses, Bus> getIOBuses() {
        return iobuses;
    }

    public void addIRQReqInput(DataSource... inputs) {
        irqreq.addInput(inputs);
    }

    public Control getIRQReqValve() {
        return valves.get(SET_REQUEST_INTERRUPT);
    }

    public synchronized void step() {
        if ((debuglevel & 1) == 1) {
            System.out.println(MCDecoder.getFormattedMC(this, getRegister(Reg.MP).getValue()));
        }

        for (Buses bus : Buses.values()) {
            buses.get(bus).resetValue();
        }
        for (IOBuses bus : IOBuses.values()) {
            iobuses.get(bus).resetValue();
        }

        valves.get(CLOCK0).setValue(1);
        valves.get(CLOCK1).setValue(1);
    }

    /**
     * Start CPU thread
     *
     * @throws InterruptedException
     */
    void startCPU() throws InterruptedException {
        lock.lock();
        try {
            cpu.start();
            lockFinish.await();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Stop CPU thread
     */
    public void stopCPU() {
        cpu.interrupt();
    }

    public boolean isLocked() {
        return lock.isLocked();
    }

    public void tickLock() {
        tick.lock();
    }

    public void tickUnlock() {
        tick.unlock();
    }

    /**
     * Add listener for specified control signal
     * <p>
     * Use tickLock() before call this method
     */
    public void addDestination(ControlSignal cs, DataDestination dest) {
        valves.get(cs).addDestination(dest);
    }

    /**
     * Remove listener for specified control signal
     * <p>
     * Use tickLock() before call this method
     */
    public void removeDestination(ControlSignal cs, DataDestination dest) {
        valves.get(cs).removeDestination(dest);
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

    public void setRunState(boolean state) {
        tick.lock();
        try {
            ps.setValue(state ? 1 : 0, 1, State.W.ordinal());
        } finally {
            tick.unlock();
        }
    }

    public void invertRunState() {
        tick.lock();
        try {
            ps.invertBit(State.W.ordinal());
        } finally {
            tick.unlock();
        }
    }

    public long getRegValue(Reg reg) {
        return regs.get(reg).getValue();
    }

    public long getRegWidth(Reg reg) {
        return regs.get(reg).width;
    }

    public long getProgramState(State state) {
        return ps.getValue(state.ordinal());
    }

    public boolean getClockState() {
        return clock;
    }

    public void setClockState(boolean clock) {
        tick.lock();
        try {
            this.clock = clock;
            if (!clock) {
                valves.get(HALT).setValue(1L << (HALT.ordinal() - 16));
            }
        } finally {
            tick.unlock();
        }
    }

    public boolean invertClockState() {
        setClockState(!clock);
        return clock;
    }

    public void setDebugLevel(long debuglevel) {
        this.debuglevel = debuglevel;
    }

    public final int findLabel(String label) throws Exception {
        return mc.findLabel(label);
    }

    /**
     * Jump to specified address
     * <p>
     * lock should be acquired before calling
     */
    private void jump(long addr) {
        if (addr > 0) {
            mp.setValue(addr);
        }
    }

    private boolean startFrom(long addr) {
        if (lock.tryLock()) {
            try {
                jump(addr);
                lockStart.signal();
            } finally {
                lock.unlock();
            }
            return true;
        }
        return false;
    }

    public boolean startSetAddr() {
        return startFrom(labels.get(SETIP));
    }

    public boolean startWrite() {
        return startFrom(labels.get(WRITE));
    }

    public boolean startRead() {
        return startFrom(labels.get(READ));
    }

    public boolean startStart() {
        return startFrom(labels.get(START));
    }

    public boolean startContinue() {
        return startFrom(0);
    }

    private boolean executeFrom(long label) {
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

    public boolean executeSetAddr() {
        return executeFrom(labels.get(SETIP));
    }

    public boolean executeSetAddr(long value) {
        ir.setValue(value);
        return executeSetAddr();
    }

    public boolean executeWrite() {
        return executeFrom(labels.get(WRITE));
    }

    public boolean executeWrite(long value) {
        ir.setValue(value);
        return executeWrite();
    }

    public boolean executeRead() {
        return executeFrom(labels.get(READ));
    }

    public boolean executeStart() {
        return executeFrom(labels.get(START));
    }

    public boolean executeContinue() {
        return executeFrom(0);
    }

    public boolean executeSetMP() {
        if (lock.tryLock()) {
            try {
                mp.setValue(ir.getValue());
            } finally {
                lock.unlock();
            }
            return true;
        }
        return false;
    }

    public boolean executeMCWrite(long value) {
        if (lock.tryLock()) {
            try {
                microcode.setValue(value);
                mp.setValue(0);
            } finally {
                lock.unlock();
            }
            return true;
        }
        return false;
    }

    public boolean executeMCRead() {
        if (lock.tryLock()) {
            try {
                valves.get(CLOCK0).setValue(1);
                mp.setValue(0);
            } finally {
                lock.unlock();
            }
            return true;
        }
        return false;
    }

    public RunningCycle getRunningCycle() {
        long addr = mp.getValue();
        RunningCycle[] cycles = RunningCycle.values();
        int i;

        for (i = cycles.length - 1; i > 0; i--) {
            if (addr >= labels.get(cycles[i])) {
                return cycles[i];
            }
        }

        return cycles[i];
    }
}
