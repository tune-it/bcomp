/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui;

import ru.ifmo.cs.components.Utils;
import java.util.ArrayList;
import java.util.Scanner;
import ru.ifmo.cs.bcomp.*;
import ru.ifmo.cs.components.DataDestination;
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
            println("Адр    МК       Метка           Расшифровка");
            printMicroTitle = false;
        }
        println(MCDecoder.getFormattedMC(cpu, addr));
    }

    private Reg[] printRegs = new Reg[]{Reg.IP, Reg.CR, Reg.AR, Reg.DR, Reg.SP, Reg.BR, Reg.AC};

    private void printRegsTitle() {
        if (!printRegsTitle) {
            return;
        }

        print("Адр " + (cpu.getClockState() ? "Знчн" : "   МК    "));
        for (Reg reg : printRegs) {
            int width = (int) Math.ceil(cpu.getRegWidth(reg) / 4.0);
            int l = (int) Math.ceil((width - reg.name().length()) / 2.0);
            print(String.format(" %" + (l > 0 ? l : "") + "s%-" + (width - l) + "s", "", reg.name()));
        }
        println(" NZVC " + (cpu.getClockState() ? "Адр Знчн" : "СчМК"));

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
        println("ВУ" + ioaddr + " " + ioctrls[ioaddr]);
    }

    private boolean checkCmd(String cmd, String check) {
        return cmd.equalsIgnoreCase(check.substring(0, Math.min(check.length(), cmd.length())));
    }

    private void checkResult(boolean result) throws Exception {
        if (!result) {
            throw new Exception("операция не выполнена: выполняется программа");
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected void printHelp() {
        println("Доступные команды:\n"
                + "a[ddress]\t- Пультовая операция \"Ввод адреса\"\n"
                + "w[rite]\t\t- Пультовая операция \"Запись\"\n"
                + "r[ead]\t\t- Пультовая операция \"Чтение\"\n"
                + "s[tart]\t\t- Пультовая операция \"Пуск\"\n"
                + "c[continue]\t- Пультовая операция \"Продолжить\"\n"
                + "ru[n]\t\t- Переключение режима Работа/Останов\n"
                + "cl[ock]\t\t- Переключение режима потактового выполнения\n"
                + "ma[ddress]\t- Переход на микрокоманду\n"
                + "mw[rite] value\t- Запись микрокоманды\n"
                + "mr[ead]\t\t- Чтение микрокоманды\n"
                + "md[ecode]\t- Декодировать текущую микрокоманду\n"
                + "mdecodea[ll]\t- Декодировать всю микропрограмму\n"
				+ "stat[e]\t\t- Вывести регистр состояния БЭВМ\n"
                + "io\t\t- Вывод состояния всех ВУ\n"
                + "io addr\t\t- Вывод состояния указанного ВУ\n"
                + "io addr value\t- Запись value в указанное ВУ\n"
                + "flag addr\t- Установка флага готовности указанного ВУ\n"
                + "asm\t\t- Ввод программы на ассемблере\n"
                + "sleep value\t- Задержка между тактами при фоновом выполнении\n"
                + "{exit|quit}\t- Выход из эмулятора\n"
                + "(0000-FFFF)\t- Ввод шестнадцатеричного значения в клавишный регистр\n"
                + "labelname\t- Ввод адреса метки в клавишный регистр"
        );
    }

    private Scanner input = new Scanner(System.in);

    public void cli() {
        println("Эмулятор Базовой ЭВМ. Версия v1.45.10 " + CLI.class.getPackage().getImplementationVersion() + "\n"
                + "БЭВМ готова к работе.\n"
                + "Используйте ? или help для получения справки");

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
                    println("Такт: " + (cpu.invertClockState() ? "Нет" : "Да"));
                    continue;
                }

                if (checkCmd(cmd, "run")) {
                    cpu.invertRunState();
                    println("Режим работы: " + (cpu.getProgramState(State.W) == 1 ? "Работа" : "Останов"));
                    continue;
                }

                if (checkCmd(cmd, "maddress")) {
                    checkResult(cpu.executeSetMP());
                    printMicroMemory(cpu.getRegValue(Reg.MP));
                    continue;
                }

                if (checkCmd(cmd, "mwrite")) {
                    if (i == cmds.length - 1) {
                        throw new Exception("команда mwrite требует аргумент");
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
			        for (i = 0; i < (1L << cpu.getMicroCode().getAddrWidth()); i++)
						if (cpu.getMicroCode().getValue(i) != 0)
							printMicroMemory(i);
                    continue;
                }

				if (checkCmd(cmd, "state")) {
					for (State state : State.values())
						print(state.name() + ": " + cpu.getProgramState(state) + " ");

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
                        throw new Exception("команда flag требует аргумент");
                    }

                    int ioaddr = Integer.parseInt(cmds[++i], 16);
						ioctrls[ioaddr].setReady();
                    printIO(ioaddr);
                    continue;
                }

                if (checkCmd(cmd, "asm") || checkCmd(cmd, "assembler")) {
                    String code = "";

                    println("Введите текст программы. Для окончания введите END");

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
                        println("Программа начинается с адреса " + Utils.toHex(prog.start_address, 11));
                    } else {
                        for (String err : asm.getErrors()) {
                            println(err);
                        }
                        println("Программа содержит ошибки");
                    }
                    printOnStop = true;
                    continue;
                }

                if (checkCmd(cmd, "sleep")) {
                    if (i == cmds.length - 1) {
                        throw new Exception("команда sleep требует аргумент");
                    }

                    sleeptime = Integer.parseInt(cmds[++i], 16);
                    continue;
                }
            } catch (Exception e) {
                printOnStop = true;
                println("Ошибка: " + e.getMessage());
                continue;
            }

            try {
                if (Utils.isHexNumeric(cmd) && cmd.length() <= (cpu.getRegWidth(Reg.IR) / 4) + (cmd.charAt(0) == '-' ? 1 : 0)) {
                    value = Integer.parseInt(cmd, 16);
                    cpu.getRegister(Reg.IR).setValue(value);
                } else {
                    println("Неизвестная команда " + cmd);
                }
//					else
//						value = asm.getLabelAddr(cmd.toUpperCase());
            } catch (Exception e) {
                println("Неизвестная команда " + cmd);
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
