/*
 * $Id$
 */
package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.components.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public abstract class IOCtrl {

    public final int READYBIT = 6;

    final Bus iodata;
    final Bus ioaddr;
    final CtrlBus ioctrl;
    private final Decoder chkregister;
    private final Control irqrqvalve;

    public IOCtrl(long addr, long width, CPU cpu) {
        Register devaddr = new Register(8 - width);
        devaddr.setValue(addr >> width);

        irqrqvalve = cpu.getIRQReqValve();

        iodata = cpu.getIOBuses().get(IOBuses.IOData);
        ioaddr = cpu.getIOBuses().get(IOBuses.IOAddr);
        ioctrl = (CtrlBus) cpu.getIOBuses().get(IOBuses.IOCtrl);
        ioctrl.addDestination(
            // Is set DI?
            new Not(IOControlSignal.DI.ordinal(), new Valve(ioctrl, 1, IOControlSignal.EI.ordinal(), 0,
                // Is set EI?
                new Not(0, new Valve(ioctrl, 1, IOControlSignal.IRQ.ordinal(), 0,
                    // Is set IRQ?
                    new Not(0, new Valve(ioctrl, 1, IOControlSignal.RDY.ordinal(), 0,
                        // Is set RDY?
                        new Not(0, new Valve(new InputBus(8 - width, width, ioaddr), 8 - width, 0, 0,
                            // Requested my address?
                            new Comparer(devaddr,
                                    // Ok, decode register
                                    chkregister = new Decoder(ioaddr, 0, width, 0)
                            )
                        ))
                    ))
                ))
            ))
        );
    }

    public void updateStateIRQ() {
        irqrqvalve.setValue(1);
    }

    public final void checkRegister(DataDestination... dsts) {
        chkregister.addDestination(dsts);
    }

    public abstract Register[] getRegisters();

    public abstract DataDestination getIRQSC();

    public abstract void addDestination(int reg, DataDestination... dsts);

    public abstract boolean isReady();

    public abstract void setReady();

    public abstract long getData();

    public abstract void setData(long value);

    /**
     *
     * @deprecated
     */
    public void addDestination(Register reg, DataDestination... dsts) {
        Register registers[] = getRegisters();

        for (int i = 0; i < registers.length; i++) {
            if (registers[i] == reg) {
                addDestination(i, dsts);
                return;
            }
        }

    }
}
