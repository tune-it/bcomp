/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui.components;

import ru.ifmo.cs.bcomp.*;

import ru.ifmo.cs.bcomp.ui.GUI;
import ru.ifmo.cs.bcomp.ui.io.*;
import ru.ifmo.cs.components.DataDestination;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.EnumMap;

import static ru.ifmo.cs.bcomp.ControlSignal.*;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;
import static ru.ifmo.cs.bcomp.ui.components.BusNames.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BasicView extends BCompPanel {

    private final CPU cpu;
    private final RunningCycleView cycleview;
    private final ALUView alu;
    private final CommutView commutView;
    private IOCtrl[] ioctrls;
    private FirstIO firstIO;
    private SecondIO secondIO;
    private TextPrinter textPrinter = null;
    private Ticker ticker = null;
    private SevenSegmentDisplay ssd = null;
    private Keyboard kbd = null;
    private Numpad numpad = null;
    private ThirdIO thirdIO;
    private JToggleButton[] ioButtons = new JToggleButton[9];
    private ItemListener[] actionListeners = new ItemListener[]{new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            switch (e.getStateChange()) {
                case ItemEvent.SELECTED:
                    if (firstIO == null) {
                        firstIO = new FirstIO(ioctrls[1]);
                        activateAndAddListener(firstIO, 0);
                    } else {
                        firstIO.activate();
                    }
                    break;
                case ItemEvent.DESELECTED:
                    firstIO.getFrame().setVisible(false);
            }
        }
    }, new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            switch (e.getStateChange()) {
                case ItemEvent.SELECTED:
                    if (secondIO == null) {
                        secondIO = new SecondIO(ioctrls[2], cmanager);
                        activateAndAddListener(secondIO, 1);
                    } else {
                        secondIO.activate();
                    }
                    break;
                case ItemEvent.DESELECTED:
                    secondIO.getFrame().setVisible(false);
            }
        }
    }, new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            switch (e.getStateChange()) {
                case ItemEvent.SELECTED:
                    if (thirdIO == null) {
                        thirdIO = new ThirdIO(ioctrls[3]);
                        activateAndAddListener(thirdIO, 2);
                    } else {
                        thirdIO.activate();
                    }
                    break;
                case ItemEvent.DESELECTED:
                    thirdIO.getFrame().setVisible(false);
            }
        }
    }, null,
        new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        if (textPrinter == null) {
                            textPrinter = new TextPrinter(ioctrls[5]);
                            activateAndAddListener(textPrinter, 4);
                        } else {
                            textPrinter.activate();
                        }
                        break;
                    case ItemEvent.DESELECTED:
                        textPrinter.getFrame().setVisible(false);
                }
            }
        },
        new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        if (ticker == null) {
                            ticker = new Ticker(ioctrls[6]);
                            activateAndAddListener(ticker, 5);
                        } else {
                            ticker.activate();
                        }
                        break;
                    case ItemEvent.DESELECTED:
                        ticker.getFrame().setVisible(false);
                }
            }
        },
        new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        if (ssd == null) {
                            ssd = new SevenSegmentDisplay(ioctrls[7]);
                            activateAndAddListener(ssd, 6);
                        } else {
                            ssd.activate();
                        }
                        break;
                    case ItemEvent.DESELECTED:
                        ssd.getFrame().setVisible(false);
                }
            }
        },
        new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        if (kbd == null) {
                            kbd = new Keyboard(ioctrls[8]);
                            activateAndAddListener(kbd, 7);
                        } else {
                            kbd.activate();
                        }
                        break;
                    case ItemEvent.DESELECTED:
                        kbd.getFrame().setVisible(false);
                }
            }
        },
        new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        if (numpad == null) {
                            numpad = new Numpad(ioctrls[9]);
                            activateAndAddListener(numpad, 8);
                        } else {
                            numpad.activate();
                        }
                        break;
                    case ItemEvent.DESELECTED:
                        numpad.getFrame().setVisible(false);
                }
            }
        }
    };

    public BasicView(GUI gui) {

        super(gui.getComponentManager(),
                new RegisterProperties[]{
                    new RegisterProperties(Reg.AR, REG_ACCUM_X_BV, REG_ADDR_Y_BV, false, false,
                            new GridBagConstraints() {
                        {
                            fill = GridBagConstraints.NONE;
                            gridy = 5;
                            gridx = 5;
                            weightx = 0;
                            weighty = 0;
                            gridwidth = 1;
                            gridheight = 1;
                            anchor = GridBagConstraints.NORTHWEST;
                            insets = new Insets(156, 0, 0, 0);
                        }
                    }),
                    new RegisterProperties(Reg.DR, REG_ACCUM_X_BV, REG_DATA_Y_BV, false, false,
                            new GridBagConstraints() {
                        {
                            fill = GridBagConstraints.NONE;
                            gridy = 1;
                            gridx = 4;
                            weightx = 0.5;
                            weighty = 0.5;
                            insets = new Insets(60, 40, 0, 0);

                        }
                    }
                    ),
                    new RegisterProperties(Reg.IP, REG_IP_X_BV, REG_IP_Y_BV, false, false,
                            new GridBagConstraints() {
                        {
                            fill = GridBagConstraints.NONE;
                            gridy = 3;
                            gridx = 4;
                            weightx = 0.5;
                            weighty = 0.5;
                            insets = new Insets(0, 0, 0, 33);

                        }
                    }
                    ),
                    new RegisterProperties(Reg.CR, REG_INSTR_X_BV, REG_ADDR_Y_BV, false, false,
                            new GridBagConstraints() {
                        {
                            fill = GridBagConstraints.NONE;
                            gridy = 2;
                            gridx = 4;
                            weightx = 0.5;
                            weighty = 0.5;
                            insets = new Insets(0, 40, 0, 0);
                        }
                    }
                    ),
                    new RegisterProperties(Reg.AC, REG_ACCUM_X_BV, REG_ACCUM_Y_BV, false, true,
                            new GridBagConstraints() {
                        {
                            fill = GridBagConstraints.NONE;
                            gridy = 1;
                            gridx = 3;
                            weighty = 0.5;
                            weightx = 0.5;
                            insets = new Insets(60, 23, 0, 60);
                        }
                    }),
                    new RegisterProperties(Reg.SP, REG_ACCUM_X_BV, 0, false, false,
                            new GridBagConstraints() {
                        {
                            fill = GridBagConstraints.NONE;
                            gridy = 4;
                            gridx = 4;
                            weighty = 0.5;
                            insets = new Insets(0, 0, 80, 33);
                        }
                    }),
                    new RegisterProperties(Reg.BR, 0, 0, false, true,
                            new GridBagConstraints() {
                        {
                            gridy = 2;
                            gridx = 3;
                            insets = new Insets(0, 3, 0, 40);
                        }
                    }),
                    new RegisterProperties(Reg.PS, 0, 0, false, true,
                            new GridBagConstraints() {
                        {
                            gridy = 3;
                            gridx = 3;
                            insets = new Insets(0, 3, 0, 40 - REG_16_WIDTH + REG_9_WIDTH);
                        }
                    }),
                    new RegisterProperties(Reg.IR, 0, 0, false, true,
                            new GridBagConstraints() {
                        {
                            gridy = 4;
                            gridx = 3;
                            insets = new Insets(0, 3, 80, 40);
                        }
                    })
                },
                new EnumMap<BusNames, BusView>(BusNames.class) {
            {
                put(BR_ALU, new BusView(RDBR));
                put(COMM_BR, new BusView(WRBR));
                put(PS_ALU, new BusView(RDPS));
                put(COMM_PS, new BusView(WRPS));
                put(COMM_ALL, new BusView(WRBR, WRAC, WRIP, WRCR, WRDR, WRAR, WRPS, WRSP));
                put(ALU_COMM, new BusView(WRBR, WRAC, WRIP, WRCR, WRDR, WRAR, WRPS, WRSP, TYPE));
                put(DR_ALU, new BusView(RDDR));
                put(CR_ALU, new BusView(RDCR));
                put(IP_ALU, new BusView(RDIP));
                put(SP_ALU, new BusView(RDSP));
                put(AC_ALU, new BusView(RDAC));
                put(IR_ALU, new BusView(RDIR));
                put(COMM_AR, new BusView(WRAR));
                put(COMM_DR, new BusView(WRDR));
                put(COMM_CR, new BusView(WRCR));
                put(COMM_IP, new BusView(WRIP));
                put(COMM_SP, new BusView(WRSP));
                put(COMM_AC, new BusView(WRAC));
                put(MEM_IO, new BusView(LOAD, STOR));
                put(MEM_R, new BusView(LOAD));
                put(MEM_W, new BusView(STOR));
                put(CU, new BusView(TYPE));
            }
        }
        );
        cpu = gui.getCPU();
        ioctrls = gui.getIOCtrls();
        add(regPanel, BorderLayout.CENTER);

        setSignalListeners(new SignalListener[]{
            new SignalListener(new DataDestination() {
                @Override
                public void setValue(long value) {
                    cycleview.updateProg(cpu.getProgramState(State.P) == 1);
                }
            }, HALT, SET_PROGRAM)
        });

        GridBagConstraints constraintsALU = new GridBagConstraints() {
            {
                gridx = 3;
                gridy = 5;
                gridwidth = 2;
                weightx = 0.5;
                weighty = 0.5;
                anchor = GridBagConstraints.NORTH;
                insets = new Insets(0, 30, 100, 0);
            }
        };
        alu = new ALUView(REG_C_X_BV, ALU_Y, ALU_WIDTH, ALU_HEIGHT);
        alu.setPreferredSize(alu.getSize());
        regPanel.add(alu, constraintsALU);

        GridBagConstraints constraintsComm = new GridBagConstraints() {
            {
                gridx = 3;
                gridy = 5;
                gridwidth = 2;
                weightx = 0.5;
                weighty = 0.5;

                anchor = GridBagConstraints.NORTH;
                insets = new Insets(ALU_HEIGHT + 17, 30, 0, 0);
            }
        };
        commutView = new CommutView(0, 0, 150, 30);
        commutView.setPreferredSize(commutView.getSize());
        regPanel.add(commutView, constraintsComm);

        GridBagConstraints constraintsCycle = new GridBagConstraints() {
            {
                gridx = 4;
                gridy = 4;
                gridheight = 2;
                anchor = GridBagConstraints.CENTER;
                insets = new Insets(0, 90, 0, 0);
            }
        };
        cycleview = new RunningCycleView(cpu, REG_INSTR_X_BV, CYCLEVIEW_Y);
        cycleview.setPreferredSize(cycleview.getSize());
        regPanel.add(cycleview, constraintsCycle);

        GridBagConstraints constraintMem = new GridBagConstraints() {
            {
                gridx = 5;
                gridy = 1;
                gridheight = 5;
                weighty = 0;
                anchor = GridBagConstraints.WEST;
                insets = new Insets(0, 40, 60, 0);
            }
        };
        regPanel.add(cmanager.getMem(), constraintMem);

        GridBagConstraints constraintsF = new GridBagConstraints() {
            {
                fill = GridBagConstraints.NONE;
                gridy = 5;
                gridx = 3;
                gridwidth = 2;
                weightx = 0.5;
                weighty = 0;
                anchor = NORTH;
                insets = new Insets(101 + CELL_HEIGHT, 0, 0, 20);
            }
        };

        regPanel.add(cmanager.getFlagView(1), constraintsF);
        constraintsF.insets.right += 60;
        regPanel.add(cmanager.getFlagView(0), constraintsF);
        constraintsF.insets.right = 0;
        constraintsF.insets.left = 140;
        regPanel.add(cmanager.getFlagView(3), constraintsF);
        constraintsF.insets = new Insets(101 + CELL_HEIGHT, 80, 0, 0);
        regPanel.add(cmanager.getFlagView(2), constraintsF);

        GridBagConstraints constraintsButton = new GridBagConstraints() {
            {
                fill = GridBagConstraints.NONE;
                gridy = 0;
                gridx = 0;
                insets = new Insets(0, 0, 50, 0);
            }
        };
        GridBagConstraints constraintsButtonPanel = new GridBagConstraints() {
            {
                fill = GridBagConstraints.NONE;
                gridy = 0;
                gridx = 0;
                gridheight = 2;
                gridwidth = 5;
                insets = new Insets(30, 0, 0, 50);
            }
        };
        RegPanel buttonsPanel = new RegPanel();
        for (int i = 1; i < 10; i++) {
            ioButtons[i - 1] = new JToggleButton(cmanager.getRes().getString("DEV-" + i));
            ioButtons[i - 1].setFont(FONT_COURIER_PLAIN_12);
            ioButtons[i - 1].setFocusable(false);
            ioButtons[i - 1].addItemListener(actionListeners[i - 1]);
            buttonsPanel.add(ioButtons[i - 1], constraintsButton);
            constraintsButton.gridx++;
        }
        regPanel.add(buttonsPanel, constraintsButtonPanel);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                redrawArrows();
            }
        });
        redrawArrows();
    }

    @Override
    public void stepStart() {
        cycleview.update();
        super.stepStart();
    }

    @Override
    public String getPanelName() {
        return cmanager.getRes().getString("basename");
    }

    @Override
    public void stepFinish() {
        super.stepFinish();
    }

    public void redrawArrows() {
        for (BusNames key : busesMap.keySet()) {
            BusView bus = busesMap.get(key);
            RegisterView data = cmanager.getRegisterView(Reg.DR);
            RegisterView addr = cmanager.getRegisterView(Reg.AR);
            RegisterView instr = cmanager.getRegisterView(Reg.CR);
            RegisterView accum = cmanager.getRegisterView(Reg.AC);
            RegisterView irReg = cmanager.getRegisterView(Reg.IR);
            RegisterView ipReg = cmanager.getRegisterView(Reg.IP);
            RegisterView spReg = cmanager.getRegisterView(Reg.SP);
            RegisterView buf = cmanager.getRegisterView(Reg.BR);
            RegisterView rs = cmanager.getRegisterView(Reg.PS);
            int regPanelX = regPanel.getX();
            int aluX = alu.getX();
            int aluY = alu.getY();
            int commY = commutView.getY();
            int accumX = accum.getX();
            switch (key) {
                case DR_ALU:
                    bus.calcBounds(new int[][]{
                        {regPanelX + data.getX() - 5, data.getY() + REG_HEIGHT / 2},
                        {regPanelX + aluX + ALU_WIDTH * 6 / 8, data.getY() + REG_HEIGHT / 2},
                        {regPanelX + aluX + ALU_WIDTH * 6 / 8, aluY - 13},});
                    break;
                case CR_ALU:
                    bus.calcBounds(new int[][]{
                        {regPanelX + instr.getX() - 5, instr.getY() + REG_HEIGHT / 2},
                        {regPanelX + aluX + ALU_WIDTH * 6 / 8, instr.getY() + REG_HEIGHT / 2},
                        {regPanelX + aluX + ALU_WIDTH * 6 / 8, aluY - 13},});
                    break;
                case IP_ALU:
                    bus.calcBounds(new int[][]{
                        {regPanelX + ipReg.getX() - 5, ipReg.getY() + REG_HEIGHT / 2},
                        {regPanelX + aluX + ALU_WIDTH * 6 / 8, ipReg.getY() + REG_HEIGHT / 2},
                        {regPanelX + aluX + ALU_WIDTH * 6 / 8, aluY - 13},});
                    break;
                case SP_ALU:
                    bus.calcBounds(new int[][]{
                        {regPanelX + spReg.getX() - 5, spReg.getY() + REG_HEIGHT / 2},
                        {regPanelX + aluX + ALU_WIDTH * 6 / 8, spReg.getY() + REG_HEIGHT / 2},
                        {regPanelX + aluX + ALU_WIDTH * 6 / 8, aluY - 13},});
                    break;
                case AC_ALU:
                    bus.calcBounds(new int[][]{
                        {regPanelX + accumX + REG_16_WIDTH - 6, accum.getY() + REG_HEIGHT / 2},
                        {regPanelX + aluX + ALU_WIDTH * 2 / 8, accum.getY() + REG_HEIGHT / 2},
                        {regPanelX + aluX + ALU_WIDTH * 2 / 8, aluY - 13},});
                    break;
                case BR_ALU:
                    bus.calcBounds(new int[][]{
                        {regPanelX + buf.getX() + REG_16_WIDTH - 6, buf.getY() + REG_HEIGHT / 2},
                        {regPanelX + aluX + ALU_WIDTH * 2 / 8, buf.getY() + REG_HEIGHT / 2},
                        {regPanelX + aluX + ALU_WIDTH * 2 / 8, aluY - 13},});
                    break;
                case PS_ALU:
                    bus.calcBounds(new int[][]{
                        {regPanelX + rs.getX() + REG_9_WIDTH - 6, rs.getY() + REG_HEIGHT / 2},
                        {regPanelX + aluX + ALU_WIDTH * 2 / 8, rs.getY() + REG_HEIGHT / 2},
                        {regPanelX + aluX + ALU_WIDTH * 2 / 8, aluY - 13},});
                    break;
                case IR_ALU:
                    bus.calcBounds(new int[][]{
                        {regPanelX + irReg.getX() + REG_16_WIDTH - 6, irReg.getY() + REG_HEIGHT / 2},
                        {regPanelX + aluX + ALU_WIDTH * 2 / 8, irReg.getY() + REG_HEIGHT / 2},
                        {regPanelX + aluX + ALU_WIDTH * 2 / 8, aluY - 13},});
                    break;
                case COMM_AR:
                    bus.calcBounds(new int[][]{
                        {regPanelX + aluX + ALU_WIDTH / 2, commY + commutView.getHeight() + 4},
                        {regPanelX + aluX + ALU_WIDTH / 2, addr.getY() + REG_HEIGHT / 2},
                        {addr.getX() - 13, addr.getY() + REG_HEIGHT / 2},});
                    break;
                case COMM_DR:
                    bus.calcBounds(new int[][]{
                        {regPanelX + aluX + ALU_WIDTH / 2, commY + commutView.getHeight() + 4},
                        {regPanelX + aluX + ALU_WIDTH / 2, aluY + ALU_HEIGHT + 90},
                        {regPanelX + data.getX() + REG_16_WIDTH + 10, aluY + ALU_HEIGHT + 90},
                        {regPanelX + data.getX() + REG_16_WIDTH + 10, data.getY() + REG_HEIGHT / 2},
                        {regPanelX + data.getX() + REG_16_WIDTH + 3, data.getY() + REG_HEIGHT / 2}
                    });
                    break;
                case COMM_CR:
                    bus.calcBounds(new int[][]{
                        {regPanelX + aluX + ALU_WIDTH / 2, commY + commutView.getHeight() + 4},
                        {regPanelX + aluX + ALU_WIDTH / 2, aluY + ALU_HEIGHT + 90},
                        {regPanelX + instr.getX() + REG_16_WIDTH + 10, aluY + ALU_HEIGHT + 90},
                        {regPanelX + instr.getX() + REG_16_WIDTH + 10, instr.getY() + REG_HEIGHT / 2},
                        {regPanelX + instr.getX() + REG_16_WIDTH + 3, instr.getY() + REG_HEIGHT / 2}
                    });
                    break;
                case COMM_IP:
                    bus.calcBounds(new int[][]{
                        {regPanelX + aluX + ALU_WIDTH / 2, commY + commutView.getHeight() + 4},
                        {regPanelX + aluX + ALU_WIDTH / 2, aluY + ALU_HEIGHT + 90},
                        {regPanelX + instr.getX() + REG_16_WIDTH + 10, aluY + ALU_HEIGHT + 90},
                        {regPanelX + instr.getX() + REG_16_WIDTH + 10, ipReg.getY() + REG_HEIGHT / 2},
                        {regPanelX + ipReg.getX() + REG_11_WIDTH + 3, ipReg.getY() + REG_HEIGHT / 2}
                    });
                    break;
                case COMM_SP:
                    bus.calcBounds(new int[][]{
                        //   {regPanelX + aluX + ALU_WIDTH / 2, aluY + ALU_HEIGHT + 4},
                        // {regPanelX + aluX +  ALU_WIDTH / 2, commY},
                        {regPanelX + aluX + ALU_WIDTH / 2, commY + commutView.getHeight() + 4},
                        {regPanelX + aluX + ALU_WIDTH / 2, aluY + ALU_HEIGHT + 90},
                        {regPanelX + instr.getX() + REG_16_WIDTH + 10, aluY + ALU_HEIGHT + 90},
                        {regPanelX + instr.getX() + REG_16_WIDTH + 10, spReg.getY() + REG_HEIGHT / 2},
                        {regPanelX + spReg.getX() + REG_11_WIDTH + 3, spReg.getY() + REG_HEIGHT / 2}
                    });
                    break;
                case COMM_ALL:
                    bus.calcBounds(new int[][]{
                        {regPanelX + aluX + ALU_WIDTH / 2, commY + commutView.getHeight() + 4},
                        {regPanelX + aluX + ALU_WIDTH / 2, aluY + ALU_HEIGHT + 74},});
                    break;
                case COMM_AC:
                    bus.calcBounds(new int[][]{
                        {regPanelX + aluX + ALU_WIDTH / 2, commY + commutView.getHeight() + 4},
                        {regPanelX + aluX + ALU_WIDTH / 2, aluY + ALU_HEIGHT + 90},
                        {regPanelX + accumX - 20, aluY + ALU_HEIGHT + 90},
                        {regPanelX + accumX - 20, accum.getY() + REG_HEIGHT / 2},
                        {regPanelX + accumX - 13, accum.getY() + REG_HEIGHT / 2}
                    });
                    break;
                case ALU_COMM:
                    bus.calcBounds(new int[][]{
                        {regPanelX + aluX + ALU_WIDTH / 2, aluY + ALU_HEIGHT + 4},
                        {regPanelX + aluX + ALU_WIDTH / 2, commY - 13}
                    });
                    break;
                case COMM_BR:
                    bus.calcBounds(new int[][]{
                        {regPanelX + aluX + ALU_WIDTH / 2, commY + commutView.getHeight() + 4},
                        {regPanelX + aluX + ALU_WIDTH / 2, aluY + ALU_HEIGHT + 90},
                        {regPanelX + accumX - 20, aluY + ALU_HEIGHT + 90},
                        {regPanelX + accumX - 20, buf.getY() + REG_HEIGHT / 2},
                        {regPanelX + accumX - 13, buf.getY() + REG_HEIGHT / 2}
                    });
                    break;
                case COMM_PS:
                    bus.calcBounds(new int[][]{
                        {regPanelX + aluX + ALU_WIDTH / 2, commY + commutView.getHeight() + 4},
                        {regPanelX + aluX + ALU_WIDTH / 2, aluY + ALU_HEIGHT + 90},
                        {regPanelX + accumX - 20, aluY + ALU_HEIGHT + 90},
                        {regPanelX + accumX - 20, rs.getY() + REG_HEIGHT / 2},
                        {regPanelX + rs.getX() - 13, rs.getY() + REG_HEIGHT / 2}
                    });
                    break;
                case MEM_IO:
                    bus.calcBounds(new int[][]{
                        {regPanelX + cmanager.getMem().getX() + MEM_WIDTH / 4, addr.getY() - 5},
                        {regPanelX + cmanager.getMem().getX() + MEM_WIDTH / 4, cmanager.getMem().getY() + CELL_HEIGHT * 17 + 15},});
                    break;
                case MEM_R:
                    bus.calcBounds(cmanager.getMem().getY() < 45
                            ? new int[][]{
                                {regPanelX + cmanager.getMem().getX() - 5, cmanager.getMem().getY() + 10},
                                {regPanelX + data.getX() + 3 * REG_16_WIDTH / 4 - 10, cmanager.getMem().getY() + 10},
                                {regPanelX + data.getX() + 3 * REG_16_WIDTH / 4 - 10, data.getY() - 13},}
                            : new int[][]{
                                {regPanelX + cmanager.getMem().getX() + MEM_WIDTH / 2 + 10, cmanager.getMem().getY() - 5},
                                {regPanelX + cmanager.getMem().getX() + MEM_WIDTH / 2 + 10, data.getY() - 65},
                                {regPanelX + data.getX() + 3 * REG_16_WIDTH / 4 - 10, data.getY() - 65},
                                {regPanelX + data.getX() + 3 * REG_16_WIDTH / 4 - 10, data.getY() - 13}
                            });
                    break;
                case MEM_W:
                    bus.calcBounds(
                            cmanager.getMem().getY() < 45
                            ? new int[][]{{regPanelX + data.getX() + 3 * REG_16_WIDTH / 4 + 10, data.getY() - 5},
                            {regPanelX + data.getX() + 3 * REG_16_WIDTH / 4 + 10, cmanager.getMem().getY() + 25},
                            {regPanelX + cmanager.getMem().getX() - 13, cmanager.getMem().getY() + 25},}
                            : new int[][]{
                                {regPanelX + data.getX() + 3 * REG_16_WIDTH / 4 + 10, data.getY() - 5},
                                {regPanelX + data.getX() + 3 * REG_16_WIDTH / 4 + 10, data.getY() - 50},
                                {regPanelX + cmanager.getMem().getX() + MEM_WIDTH / 2 - 10, data.getY() - 50},
                                {regPanelX + cmanager.getMem().getX() + MEM_WIDTH / 2 - 10, cmanager.getMem().getY() - 13},});
                    break;
                case CU:
                    bus.calcBounds(new int[][]{
                        {regPanelX + aluX + ALU_WIDTH / 2, commY + commutView.getHeight() + 4},
                        {regPanelX + aluX + ALU_WIDTH / 2, aluY + ALU_HEIGHT + 90},
                        {regPanelX + cycleview.getX() + cycleview.getWidth() / 2, aluY + ALU_HEIGHT + 90},
                        {regPanelX + cycleview.getX() + cycleview.getWidth() / 2, cycleview.getY() + CELL_HEIGHT * 8 + 15},});
                    break;
            }
        }
    }

    private void activateAndAddListener(IODevice ioDevice, int i) {
        ioDevice.activate();
        ioDevice.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                ioButtons[i].setSelected(false);
            }
        });
    }
}
