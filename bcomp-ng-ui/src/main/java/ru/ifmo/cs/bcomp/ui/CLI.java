/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui;

import ru.ifmo.cs.components.Utils;
import java.util.ArrayList;
import java.util.Scanner;
import ru.ifmo.cs.bcomp.*;
import ru.ifmo.cs.components.DataDestination;
import ru.ifmo.cs.components.Messages;
import ru.ifmo.cs.bcomp.assembler.AsmNg;
import ru.ifmo.cs.bcomp.assembler.Program;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class CLI {

    private final BasicComp bcomp;
    private final CPU cpu;
    private final IOCtrl[] ioctrls;
    private final ArrayList<Long> writelist = new ArrayList<Long>();

    private int sleeptime = 1;
    private volatile long savedPointer;
    private volatile boolean printOnStop = true;
    private volatile boolean printRegsTitle = false;
    private volatile boolean printMicroTitle = false;
    private volatile int sleep = 0;

    public CLI(BasicComp bcomp) {
        this.bcomp = bcomp;

        cpu = bcomp.getCPU();
        cpu.addDestination(ControlSignal.STOR, new DataDestination() {
            @Override
            public void setValue(long value) {
                long addr = cpu.getRegValue(Reg.AR);

                if (!writelist.contains(addr)) // Saving changed mem addr to print later
                {
                    writelist.add(addr);
                }
            }
        });

        cpu.setCPUStartListener(new Runnable() {
            @Override
            public void run() {
                if (!printOnStop) {
                    return;
                }

                writelist.clear();
                // Saving IP/MP to print registers later
                savedPointer = cpu.getRegValue(cpu.getClockState() ? Reg.IP : Reg.MP);
                printRegsTitle();
            }
        });

        cpu.setCPUStopListener(new Runnable() { // Print changed mem
            @Override
            public void run() {
                sleep = 0;

                if (!printOnStop) {
                    return;
                }

                printRegs(writelist.isEmpty() ? "" : " " + getMemory(writelist.remove(0)));

                for (Long wraddr : writelist) {
                    println(String.format("%1$46s", " ") + getMemory(wraddr));
                }
            }
        });

        cpu.setTickFinishListener(new Runnable() {
            @Override
            public void run() {
                if (sleep <= 0) {
                    return;
                }

                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    /*totally not empty*/
                }
            }
        });

        ioctrls = bcomp.getIOCtrls();
    }

    private String getReg(Reg reg) {
        return Utils.toHex(cpu.getRegValue(reg), cpu.getRegWidth(reg));
    }

    private String getMemory(long addr) {
        return Utils.toHex(addr, 11) + " " + Utils.toHex(cpu.getMemory().getValue(addr), 16);
    }

    private void printMicroMemory(long addr) {
        if (printMicroTitle) {
            println(Messages.get("cli.micro.header"));
            printMicroTitle = false;
        }
        println(MCDecoder.getFormattedMC(cpu, addr));
    }

    private Reg[] printRegs = new Reg[]{Reg.IP, Reg.CR, Reg.AR, Reg.DR, Reg.SP, Reg.BR, Reg.AC};

    private void printRegsTitle() {
        if (!printRegsTitle) {
            return;
        }

        print(Messages.get("cli.regs.header.addr")
                + (cpu.getClockState() ? Messages.get("cli.regs.header.value") : Messages.get("cli.regs.header.mc")));
        for (Reg reg : printRegs) {
            int width = (int) Math.ceil(cpu.getRegWidth(reg) / 4.0);
            int l = (int) Math.ceil((width - reg.name().length()) / 2.0);
            print(String.format(" %" + (l > 0 ? l : "") + "s%-" + (width - l) + "s", "", reg.name()));
        }
        println(Messages.get("cli.regs.header.nzvc")
                + (cpu.getClockState() ? Messages.get("cli.regs.header.addr_value") : Messages.get("cli.regs.header.mp_counter")));

        printRegsTitle = false;
    }

    private void printRegs(String add) {
        print((cpu.getClockState() ? getMemory(savedPointer) : Utils.toHex(savedPointer, 8)
                + ' ' + Utils.toHex(cpu.getMicroCode().getValue(savedPointer), 40)));
        for (Reg reg : printRegs) {
            print(' ' + getReg(reg));
        }
        println(' ' + Utils.toBinary(cpu.getRegValue(Reg.PS) & 0xF, 4)
                + (cpu.getClockState() ? add : "  " + getReg(Reg.MP)));
    }

    private void printIO(int ioaddr) {
        println(Messages.format("cli.io.entry", ioaddr, ioctrls[ioaddr]));
    }

    private boolean checkCmd(String cmd, String check) {
        return cmd.equalsIgnoreCase(check.substring(0, Math.min(check.length(), cmd.length())));
    }

    private void checkResult(boolean result) throws Exception {
        if (!result) {
            throw new Exception(Messages.get("cli.op_failed"));
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected void printHelp() {
        println(Messages.get("cli.help"));
    }

    private Scanner input = new Scanner(System.in);

    public void cli() {
        println(Messages.format("cli.banner.title", bcomp.getVersionBrief()) + "\n"
                + Messages.get("cli.banner.ready") + "\n"
                + Messages.get("cli.banner.help_hint"));

        String line;
        for (;;) {
            try {
                line = fetchLine();
            } catch (Exception e) {
                break;
            }

            processLine(line);
        }

        Runtime.getRuntime().exit(0); // System.exit(0);
    }

    @SuppressWarnings("WeakerAccess")
    protected void processLine(String line) {
        int i, value;
        String[] cmds = line.split("[ \t]+");

        if (cmds.length == 0) {
            return;
        }

        for (i = 0, printRegsTitle = printMicroTitle = true; i < cmds.length; i++) {
            String cmd = cmds[i];

            if (cmd.equals("")) {
                continue;
            }

            if (cmd.charAt(0) == '#') {
                break;
            }

            if (checkCmd(cmd, "exit") || checkCmd(cmd, "quit")) {
                Runtime.getRuntime().exit(0); // System.exit(0);
            }
            if (checkCmd(cmd, "?") || checkCmd(cmd, "help")) {
                printHelp();
                continue;
            }

            try {
                if (checkCmd(cmd, "address")) {
                    checkResult(cpu.executeSetAddr());
                    continue;
                }

                if (checkCmd(cmd, "write")) {
                    checkResult(cpu.executeWrite());
                    continue;
                }

                if (checkCmd(cmd, "read")) {
                    checkResult(cpu.executeRead());
                    continue;
                }

                if (checkCmd(cmd, "start")) {
                    if (i == cmds.length - 1) {
                        sleep = sleeptime;
                        checkResult(cpu.startStart());
                    } else {
                        checkResult(cpu.executeStart());
                    }
                    continue;
                }

                if (checkCmd(cmd, "continue")) {
                    if (i == cmds.length - 1) {
                        sleep = sleeptime;
                        checkResult(cpu.startContinue());
                    } else {
                        checkResult(cpu.executeContinue());
                    }
                    continue;
                }

                if (checkCmd(cmd, "clock")) {
                    println(Messages.format("cli.clock.label",
                            cpu.invertClockState() ? Messages.get("cli.no") : Messages.get("cli.yes")));
                    continue;
                }

                if (checkCmd(cmd, "run")) {
                    cpu.invertRunState();
                    println(Messages.format("cli.run_mode.label",
                            cpu.getProgramState(State.W) == 1
                                    ? Messages.get("cli.run_mode.running")
                                    : Messages.get("cli.run_mode.stopped")));
                    continue;
                }

                if (checkCmd(cmd, "maddress")) {
                    checkResult(cpu.executeSetMP());
                    printMicroMemory(cpu.getRegValue(Reg.MP));
                    continue;
                }

                if (checkCmd(cmd, "mwrite")) {
                    if (i == cmds.length - 1) {
                        throw new Exception(Messages.get("cli.cmd.mwrite_needs_arg"));
                    }

                    long mc = Long.parseLong(cmds[++i], 16);
                    long addr = cpu.getRegValue(Reg.MP);
                    checkResult(cpu.executeMCWrite(mc));
                    printMicroMemory(addr);
                    continue;
                }

                if (checkCmd(cmd, "mread")) {
                    long addr = cpu.getRegValue(Reg.MP);
                    checkResult(cpu.executeMCRead());
                    printMicroMemory(addr);
                    continue;
                }

                if (checkCmd(cmd, "mdecode")) {
                    printMicroMemory(cpu.getRegValue(Reg.MP));
                    continue;
                }

                if (checkCmd(cmd, "mdecodeall")) {
                    for (i = 0; i < (1L << cpu.getMicroCode().getAddrWidth()); i++) {
                        if (cpu.getMicroCode().getValue(i) != 0) {
                            printMicroMemory(i);
                        }
                    }
                    continue;
                }

                if (checkCmd(cmd, "state")) {
                    for (State state : State.values()) {
                        print(state.name() + ": " + cpu.getProgramState(state) + " ");
                    }

                    println("");
                    continue;
                }

                if (checkCmd(cmd, "io")) {
                    if (i == cmds.length - 1) {
                        for (int ioaddr = 0; ioaddr < 4; ioaddr++) {
                            printIO(ioaddr);
                        }
                        continue;
                    }

                    int ioaddr = Integer.parseInt(cmds[++i], 16);

                    if (i < cmds.length - 1) {
                        value = Integer.parseInt(cmds[++i], 16);
                        ioctrls[ioaddr].setData(value);
                    }

                    printIO(ioaddr);
                    continue;
                }

                if (checkCmd(cmd, "flag")) {
                    if (i == cmds.length - 1) {
                        throw new Exception(Messages.get("cli.cmd.flag_needs_arg"));
                    }

                    int ioaddr = Integer.parseInt(cmds[++i], 16);
                    ioctrls[ioaddr].setReady();
                    printIO(ioaddr);
                    continue;
                }

                if (checkCmd(cmd, "asm") || checkCmd(cmd, "assembler")) {
                    String code = "";

                    println(Messages.get("cli.asm.prompt"));

                    for (;;) {
                        line = fetchLine();

                        if (line.equalsIgnoreCase("END")) {
                            break;
                        }

                        code = code.concat(line.concat("\n"));
                    }

                    printOnStop = false;
                    AsmNg asm = new AsmNg(code);
                    Program pobj = asm.compile();
                    if (asm.getErrors().isEmpty()) {
                        ProgramBinary prog = new ProgramBinary(pobj.getBinaryFormat());
                        bcomp.loadProgram(prog);
                        println(Messages.format("cli.asm.start_address", Utils.toHex(prog.start_address, 11)));
                    } else {
                        for (String err : asm.getErrors()) {
                            println(err);
                        }
                        println(Messages.get("cli.asm.has_errors"));
                    }
                    printOnStop = true;
                    continue;
                }

                if (checkCmd(cmd, "sleep")) {
                    if (i == cmds.length - 1) {
                        throw new Exception(Messages.get("cli.cmd.sleep_needs_arg"));
                    }

                    sleeptime = Integer.parseInt(cmds[++i], 16);
                    continue;
                }
            } catch (Exception e) {
                printOnStop = true;
                println(Messages.format("cli.error", e.getMessage()));
                continue;
            }

            try {
                if (Utils.isHexNumeric(cmd) && cmd.length() <= (cpu.getRegWidth(Reg.IR) / 4) + (cmd.charAt(0) == '-' ? 1 : 0)) {
                    value = Integer.parseInt(cmd, 16);
                    cpu.getRegister(Reg.IR).setValue(value);
                } else {
                    println(Messages.format("cli.unknown_cmd", cmd));
                }
//					else
//						value = asm.getLabelAddr(cmd.toUpperCase());
            } catch (Exception e) {
                println(Messages.format("cli.unknown_cmd", cmd));
            }
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected String fetchLine() throws Exception {
        return input.nextLine();
    }

    @SuppressWarnings("WeakerAccess")
    protected void print(String str) {
        System.out.print(str);
    }

    @SuppressWarnings("WeakerAccess")
    protected void println(String str) {
        System.out.println(str);
    }
}
