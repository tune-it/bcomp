/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui;

import java.util.ArrayList;
import java.util.Scanner;
import ru.ifmo.cs.bcomp.*;
import ru.ifmo.cs.elements.DataDestination;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class CLI {
	private final BasicComp bcomp;
	private final MicroProgram mp;
	private final CPU cpu;
	private final IOCtrl[] ioctrls;
	private final Assembler asm;
	private final ArrayList<Integer> writelist = new ArrayList<Integer>();

	private final static int SLEEP_TIME = 1;
	private volatile int addr;
	private volatile boolean printOnStop = true;
	private volatile boolean printRegsTitle = false;
	private volatile boolean printMicroTitle = false;
	private volatile int sleep = 0;

	public CLI(MicroProgram mp) throws Exception {
		bcomp = new BasicComp(this.mp = mp);

		cpu = bcomp.getCPU();
		bcomp.addDestination(ControlSignal.MEMORY_WRITE, new DataDestination() {
			@Override
			public void setValue(int value) {
				int addr = cpu.getRegValue(CPU.Reg.ADDR);

				if (!writelist.contains(addr))
					writelist.add(addr);
			}
		});

		cpu.setCPUStartListener(new Runnable() {
			@Override
			public void run() {
				if (printOnStop) {
					writelist.clear();
					addr = getIP();
					printRegsTitle();
				}
			}
		});

		cpu.setCPUStopListener(new Runnable() {
			@Override
			public void run() {
				String add;

				sleep = 0;

				if (printOnStop) {
					if (writelist.isEmpty())
						add = "";
					else {
						add = " " + getMemory(writelist.get(0));
						writelist.remove(0);
					}

					printRegs(add);

					for (Integer wraddr : writelist)
						System.out.println(String.format("%1$34s", " ") + getMemory(wraddr));
				}
			}
		});

		cpu.setTickFinishListener(new Runnable() {
			@Override
			public void run() {
				if (sleep > 0)
					try {
						Thread.sleep(sleep);
					} catch (InterruptedException e) { }
			}
		});

		asm = new Assembler(cpu.getInstructionSet());
		ioctrls = bcomp.getIOCtrls();
	}

	private String getReg(CPU.Reg reg) {
		return Utils.toHex(cpu.getRegValue(reg), cpu.getRegWidth(reg));
	}

	private String getFormattedState(int flag) {
		return Utils.toBinaryFlag(cpu.getStateValue(flag));
	}

	// XXX: Получать имена регистров из их свойств
	private void printRegsTitle() {
		if (printRegsTitle) {
			System.out.println(cpu.getClockState() ?
				"Адр Знчн  СК  РА  РК   РД    А  C Адр Знчн" :
				"Адр МК   СК  РА  РК   РД    А  C   БР  N Z СчМК");
			printRegsTitle = false;
		}
	}

	private String getMemory(int addr) {
		return Utils.toHex(addr, 11) + " " + Utils.toHex(cpu.getMemoryValue(addr), 16);
	}

	private String getMicroMemory(int addr) {
		return Utils.toHex(addr, 8) + " " + Utils.toHex(cpu.getMicroMemoryValue(addr), 16);
	}

	private void printMicroMemory(int addr) {
		if (printMicroTitle) {
			System.out.println("Адр МК");
			printMicroTitle = false;
		}
		System.out.println(getMicroMemory(addr) + " " + mp.decodeCmd(cpu.getMicroMemoryValue(addr)));
	}

	private String getRegs() {
		return getReg(CPU.Reg.IP) + " " +
			getReg(CPU.Reg.ADDR) + " " +
			getReg(CPU.Reg.INSTR) + " " +
			getReg(CPU.Reg.DATA) + " " +
			getReg(CPU.Reg.ACCUM) + " " +
			getFormattedState(StateReg.FLAG_C);
	}

	private void printRegs(String add) {
		System.out.println(cpu.getClockState() ?
			getMemory(addr) + " " + getRegs() + add :
			getMicroMemory(addr) + " " +
				getRegs() + " " +
				getReg(CPU.Reg.BUF) + " " +
				getFormattedState(StateReg.FLAG_N) + " " +
				getFormattedState(StateReg.FLAG_Z) + "  " +
				getReg(CPU.Reg.MIP));
	}

	private void printIO(int ioaddr) {
		System.out.println("ВУ" + ioaddr +
			": Флаг = " + Utils.toBinaryFlag(ioctrls[ioaddr].getFlag()) +
			" РДВУ = " + Utils.toHex(ioctrls[ioaddr].getData(), 8));
	}

	private int getIP() {
		return cpu.getClockState() ? cpu.getRegValue(CPU.Reg.IP) : cpu.getRegValue(CPU.Reg.MIP);
	}

	private boolean checkCmd(String cmd, String check) {
		return cmd.equalsIgnoreCase(check.substring(0, Math.min(check.length(), cmd.length())));
	}

	private void checkResult(boolean result) throws Exception {
		if (!result)
			throw new Exception("операция не выполнена: выполняется программа");
	}

	private void printHelp() {
		System.out.println("Доступные команды:\n" +
			"a[ddress]\t- Пультовая операция \"Ввод адреса\"\n" +
			"w[rite]\t\t- Пультовая операция \"Запись\"\n" +
			"r[ead]\t\t- Пультовая операция \"Чтение\"\n" +
			"s[tart]\t\t- Пультовая операция \"Пуск\"\n" +
			"c[continue]\t- Пультовая операция \"Продолжить\"\n" +
			"ru[n]\t\t- Переключение режима Работа/Останов\n" +
			"cl[ock]\t\t- Переключение режима потактового выполнения\n" +
			"ma[ddress]\t- Переход на микрокоманду\n" +
			"mw[rite]\t- Запись микрокоманды\n" +
			"mr[ead]\t\t- Чтение микрокоманды\n" +
			"io\t\t- Вывод состояния всех ВУ\n" +
			"io addr\t\t- Вывод состояния указанного ВУ\n" +
			"io addr value\t- Запись value в указанное ВУ\n" +
			"flag addr\t- Установка флага готовности указанного ВУ\n" +
			"asm\t\t- Ввод программы на ассемблере\n" +
			"{exit|quit}\t- Выход из эмулятора\n" +
			"label\t\t- Ввод адреса метки в клавишный регистр\n" +
			"value\t\t- Ввод шестнадцатеричного значения в клавишный регистр");
	}

	public void cli() {
		Scanner input = new Scanner(System.in);
		String line;
		int i, value;

		bcomp.startTimer();

		System.out.println("Эмулятор Базовой ЭВМ. Версия r" + CLI.class.getPackage().getImplementationVersion() + "\n" +
			"Загружена " + cpu.getMicroProgramName() + " микропрограмма\n" +
			"Цикл прерывания начинается с адреса " + Utils.toHex(cpu.getIntrCycleStartAddr(), 8) + "\n" +
			"БЭВМ готова к работе.\n" +
			"Используйте ? или help для получения справки");

		for (;;) {
			try {
				line = input.nextLine();
			} catch(Exception e) {
				break;
			}

			String[] cmds = line.split("[ \t]+");

			if (cmds.length == 0)
				continue;

			for (i = 0, printRegsTitle = printMicroTitle = true; i < cmds.length; i++) {
				String cmd = cmds[i];

				if (cmd.equals(""))
					continue;

				if (cmd.charAt(0) == '#')
					break;

				if (checkCmd(cmd, "exit") || checkCmd(cmd, "quit"))
					System.exit(0);

				if (checkCmd(cmd, "?") || checkCmd(cmd, "help")) {
					printHelp();
					continue;
				}

				try {
					if (checkCmd(cmd, "address")) {
						checkResult(cpu.runSetAddr());
						continue;
					}

					if (checkCmd(cmd, "write")) {
						checkResult(cpu.runWrite());
						continue;
					}

					if (checkCmd(cmd, "read")) {
						checkResult(cpu.runRead());
						continue;
					}

					if (checkCmd(cmd, "start")) {
						if (i == cmds.length - 1) {
							sleep = SLEEP_TIME;
							checkResult(cpu.startStart());
						} else
							checkResult(cpu.runStart());
						continue;
					}

					if (checkCmd(cmd, "continue")) {
						if (i == cmds.length - 1) {
							sleep = SLEEP_TIME;
							checkResult(cpu.startContinue());
						} else
							checkResult(cpu.runContinue());
						continue;
					}

					if (checkCmd(cmd, "clock")) {
						System.out.println("Такт: " + (cpu.invertClockState() ? "Нет" : "Да"));
						continue;
					}

					if (checkCmd(cmd, "run")) {
						cpu.invertRunState();
						System.out.println("Режим работы: " + (
							cpu.getStateValue(StateReg.FLAG_RUN) == 1 ? "Работа" : "Останов"));
						continue;
					}

					if (checkCmd(cmd, "maddress")) {
						checkResult(cpu.runSetMAddr());
						printMicroMemory(cpu.getRegValue(CPU.Reg.MIP));
						continue;
					}

					if (checkCmd(cmd, "mwrite")) {
						int addr = cpu.getRegValue(CPU.Reg.MIP);
						checkResult(cpu.runMWrite());
						printMicroMemory(addr);
						continue;
					}

					if (checkCmd(cmd, "mread")) {
						int addr = cpu.getRegValue(CPU.Reg.MIP);
						checkResult(cpu.runMRead());
						printMicroMemory(addr);
						continue;
					}

					if (checkCmd(cmd, "io")) {
						if (i == cmds.length - 1) {
							for (int ioaddr = 0; ioaddr < 4; ioaddr++)
								printIO(ioaddr);
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
						if (i == cmds.length - 1)
							throw new Exception("команда flag требует аргумент");

						int ioaddr = Integer.parseInt(cmds[++i], 16);
						ioctrls[ioaddr].setFlag();
						printIO(ioaddr);
						continue;
					}

					if (checkCmd(cmd, "asm") || checkCmd(cmd, "assembler")) {
						String code = "";

						System.out.println("Введите текст программы. Для окончания введите END");

						for (;;) {
							line = input.nextLine();

							if (line.equalsIgnoreCase("END"))
								break;

							code = code.concat(line.concat("\n"));
						}

						printOnStop = false;
						asm.compileProgram(code);
						asm.loadProgram(cpu);
						System.out.println("Программа начинается с адреса " + Utils.toHex(asm.getBeginAddr(), 11));
						printOnStop = true;

						try {
							System.out.println("Результат по адресу " + Utils.toHex(asm.getLabelAddr("R"), 11));
						} catch (Exception e) { }

						continue;
					}
				} catch (Exception e) {
					printOnStop = true;
					System.out.println("Ошибка: " + e.getMessage());
					continue;
				}

				try {
					if (Utils.isHexNumeric(cmd))
						value = Integer.parseInt(cmd, 16);
					else
						value = asm.getLabelAddr(cmd.toUpperCase());

					cpu.setRegKey(value);
				} catch (Exception e) {
					System.out.println("Неизвестная команда " + cmd);
				}
			}
		}

		System.exit(0);
	}
}
