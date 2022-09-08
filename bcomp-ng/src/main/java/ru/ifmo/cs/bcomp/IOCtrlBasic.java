/*
 * $Id$
 */
package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.components.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class IOCtrlBasic extends IOCtrl {

    public enum TYPE {
        INPUT,
        OUTPUT,
        INPUTOUTPUT
    }

    private final int DR = 0;
    private final int STATE = 1;
    private final int IRQ = 2;

    private final Register dr = new Register(8);
    private final Register state = new Register(1);
    private final Register irqreg = new Register(4);
    private final Register[] registers = {dr, state, irqreg};
    private final Control writeToRegister[] = new Control[registers.length];
    private final DataDestination irqsc;

    public IOCtrlBasic(long addr, CPU cpu, TYPE type, DataDestination... chainctrl) {
        super(addr, 1, cpu);

        And reqirq = new And(state, 0, irqreg, 3);
        cpu.addIRQReqInput(reqirq);

        irqsc = new Valve(reqirq, 1, 0, 0,
                new Valve(irqreg, 3, 0, 0, ioaddr),
                new Valve(Consts.consts[1], 1, 0, 0, new PartWriter(ioctrl, 1, IOControlSignal.IRQ.ordinal())),
                new Not(0, chainctrl)
        );

        Valve rdy = new Valve(Consts.consts[1], 1, 0, 0, new PartWriter(ioctrl, 1, IOControlSignal.RDY.ordinal()));
        writeToRegister[STATE] = new Control(1, 0, 0, state, cpu.getIRQReqValve());
        Valve clearFlag = new Valve(Consts.consts[0], 1, 0, 0, writeToRegister[STATE]);
        Valve r0;
        checkRegister(
            // Register 0
            r0 = new Valve(ioctrl, 8, 0, 0),
            // Register 1
            new Valve(ioctrl, 8, 0, 1,
                // Input - state into iodata 6th bit
                new Valve(Consts.consts[1], 1, 0, IOControlSignal.IN.ordinal(),
                        new Valve(state, 1, 0, 0, new PartWriter(iodata, 1, READYBIT)),
                        rdy
                ),
                // Output - set IRQ
                new Valve(Consts.consts[1], 1, 0, IOControlSignal.OUT.ordinal(),
                        writeToRegister[IRQ] = new Valve(iodata, irqreg.width, 0, 0, irqreg, cpu.getIRQReqValve()),
                        rdy
                )
            )
        );

        if (type == TYPE.INPUT || type == TYPE.INPUTOUTPUT) {
            r0.addDestination(
                    new Valve(Consts.consts[1], 1, 0, IOControlSignal.IN.ordinal(),
                            new Valve(dr, 8, 0, 0, iodata),
                            clearFlag,
                            rdy
                    )
            );
        }

        // Preventing NPE in dumb UI
        writeToRegister[DR] = new Valve(iodata, 8, 0, 0, dr);
        // Output to DR
        if (type == TYPE.OUTPUT || type == TYPE.INPUTOUTPUT) {
            r0.addDestination(
                    new Valve(Consts.consts[1], 1, 0, IOControlSignal.OUT.ordinal(),
                            writeToRegister[DR],
                            clearFlag,
                            rdy
                    )
            );
        }
    }

    @Override
    public boolean isReady() {
        return state.getValue() == 1;
    }

    @Override
    public void setReady() {
        writeToRegister[STATE].setValue(1);
    }

    @Override
    public DataDestination getIRQSC() {
        return irqsc;
    }

    @Override
    public Register[] getRegisters() {
        return registers;
    }

    @Override
    public void addDestination(int reg, DataDestination... dsts) {
        writeToRegister[reg].addDestination(dsts);
    }

    @Override
    public long getData() {
        return dr.getValue();
    }

    @Override
    public void setData(long value) {
        dr.setValue(value);
    }

    @Override
    public String toString() {
        return "IRQ = " + irqreg
                + " State = " + state
                + " Data = " + dr;
    }
}
