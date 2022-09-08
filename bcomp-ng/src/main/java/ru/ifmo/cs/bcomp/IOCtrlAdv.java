/*
 * $Id$
 */
package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.components.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class IOCtrlAdv extends IOCtrl {

    private final int STATE = 2;
    private final int CONTROL = 3;

    private final Register[] registers = {
        new Register(8),
        new Register(8),
        new Register(8),
        new Register(8),};
    private final Control writeToRegister[] = new Control[registers.length];
    private final DataDestination irqsc;

    public IOCtrlAdv(long addr, CPU cpu, DataDestination... chainctrl) {
        super(addr, 2, cpu);

        And reqirq = new And(registers[STATE], READYBIT, registers[CONTROL], 3);
        cpu.addIRQReqInput(reqirq);

        irqsc = new Valve(reqirq, 1, 0, 0,
                new Valve(registers[CONTROL], 3, 0, 0, ioaddr),
                new Valve(Consts.consts[1], 1, 0, 0, new PartWriter(ioctrl, 1, IOControlSignal.IRQ.ordinal())),
                new Not(0, chainctrl)
        );

        Valve rdy = new Valve(Consts.consts[1], 1, 0, 0, new PartWriter(ioctrl, 1, IOControlSignal.RDY.ordinal()));

        for (int i = 0; i < registers.length; i++) {
            checkRegister(
                new Valve(ioctrl, 8, 0, i,
                    // Input
                    new Valve(Consts.consts[1], 1, 0, IOControlSignal.IN.ordinal(),
                            new Valve(registers[i], 8, 0, 0, iodata),
                            rdy
                    ),
                    // Output
                    new Valve(Consts.consts[1], 1, 0, IOControlSignal.OUT.ordinal(),
                            writeToRegister[i] = new Valve(iodata, 8, 0, 0, registers[i]),
                            rdy
                    )
                )
            );
        }

        writeToRegister[STATE].addDestination(cpu.getIRQReqValve());
        writeToRegister[CONTROL].addDestination(cpu.getIRQReqValve());
    }

    @Override
    public boolean isReady() {
        return registers[STATE].getValue(READYBIT) == 1;
    }

    @Override
    public void setReady() {
        registers[STATE].setValue(1, 1, READYBIT);
        updateStateIRQ();
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
        return registers[0].getValue();
    }

    @Override
    public void setData(long value) {
        registers[0].setValue(value);
    }

    @Override
    public String toString() {
        return "DR0 = " + registers[0]
                + " DR1 = " + registers[1]
                + " State = " + registers[2]
                + " Control = " + registers[3];
    }
}
