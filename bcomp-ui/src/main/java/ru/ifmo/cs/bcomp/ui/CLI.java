/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui;

import java.util.ArrayList;
import java.util.Scanner;
import ru.ifmo.cs.bcomp.*;
import ru.ifmo.cs.components.DataDestination;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class CLI {
	private final BasicComp bcomp;
	private final CPU cpu;
//	private final IOCtrl[] ioctrls; //TODO IOCtrl
//	protected final Assembler asm; //TODO Assembler
	private final ArrayList<Long> writelist = new ArrayList<Long>();

	private int sleeptime = 1;
	private volatile long addr;
	protected volatile boolean printOnStop = true;
	private volatile boolean printRegsTitle = false;
	private volatile boolean printMicroTitle = false;
	private volatile int sleep = 0;

	public CLI() throws Exception {
		bcomp = new BasicComp();

		cpu = bcomp.getCPU();
		cpu.addDestination(ControlSignal.STOR, new DataDestination() {
			@Override
			public void setValue(long value) {
				long addr = cpu.getRegValue(Reg.AR);

				if (!writelist.contains(addr)) // Save changed mem addr to print later
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

		cpu.setCPUStopListener(new Runnable() { // Print changed mem
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

					for (Long wraddr : writelist)
						println(String.format("%1$34s", " ") + getMemory(wraddr));
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

//		asm = new Assembler(cpu.getInstructionSet());
//		ioctrls = bcomp.getIOCtrls();
	}

	private String getReg(Reg reg) {
		return Utils.toHex(cpu.getRegValue(reg), cpu.getRegWidth(reg));
	}

	// XXX: Получать имена регистров из их свойств
	private void printRegsTitle() {
		if (printRegsTitle) {
			//Адр Знчн  IR  AR  SP  CR   DR   BR   AC  NZVC Адр Знчн
			//Адр МК   IR  AR  SP  CR   DR   BR   AC  NZVC СчМК

			String space = "  ";
			String hdr = "Адр ";
			hdr += (cpu.getClockState() ? "Знчн" : "МК ") + space;
			hdr += Reg.IP.name() + space;
			hdr += Reg.AR.name() + space;
			hdr += Reg.SP.name() + space;
			space = "   ";
			hdr += Reg.CR.name() + space;
			hdr += Reg.DR.name() + space;
			hdr += Reg.BR.name() + space;
			hdr += Reg.AC.name() + "  ";
			hdr += "NZVC ";
			hdr += (cpu.getClockState() ? "Адр Знчн" : "СчМК");
			println(hdr);

			printRegsTitle = false;
		}
	}

	private String getMemory(long addr) {
		return Utils.toHex(addr, 11) + " " + Utils.toHex(cpu.getMemory().getValue(addr), 16);
	}

	private String getMicroMemory(long addr) {
		return Utils.toHex(addr, 8) + " " + Utils.toHex(cpu.getMicroCode().getValue(addr), 16);
	}

	private void printMicroMemory(long addr) {
		if (printMicroTitle) {
			println("Адр МК");
			printMicroTitle = false;
		}
//		println(getMicroMemory(addr) + " " + mp.decodeCmd(cpu.getMicroCode().getValue(addr)));
		println(getMicroMemory(addr) + " " + MCDecoder.decodeMC(cpu, addr)[2]);
	}

	private void printRegs(String add) {
		String line = (cpu.getClockState() ? getMemory(addr) : getMicroMemory(addr)) + " ";
		line += getReg(Reg.IP) + " ";
		line += getReg(Reg.AR) + " ";
		line += getReg(Reg.SP) + " ";
		line += getReg(Reg.CR) + " ";
		line += getReg(Reg.DR) + " ";
		line += getReg(Reg.BR) + " ";
		line += getReg(Reg.AC) + " ";
		line += Utils.toBinary(cpu.getRegValue(Reg.PS) & 0xF,4);
		line += (cpu.getClockState() ? add : "  " + getReg(Reg.MP));
		println(line);
	}

	private void printIO(int ioaddr) {
		println("ВУ" + ioaddr +
			": Флаг = " +
			" РДВУ = ");
//		println("ВУ" + ioaddr +
//			": Флаг = " + Utils.toBinaryFlag(ioctrls[ioaddr].getFlag()) +
//			" РДВУ = " + Utils.toHex(ioctrls[ioaddr].getData(), 8));
	}

	private long getIP() {
		return cpu.getClockState() ? cpu.getRegValue(Reg.IP) : cpu.getRegValue(Reg.MP);
	}

	private boolean checkCmd(String cmd, String check) {
		return cmd.equalsIgnoreCase(check.substring(0, Math.min(check.length(), cmd.length())));
	}

	private void checkResult(boolean result) throws Exception {
		if (!result)
			throw new Exception("операция не выполнена: выполняется программа");
	}

	protected void printHelp() {
		println("Доступные команды:\n" +
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
			"sleep value\t- Задержка между тактами при фоновом выполнении\n" +
			"{exit|quit}\t- Выход из эмулятора\n" +
//			"value\t\t- Ввод шестнадцатеричного значения в клавишный регистр\n" +
//			"label\t\t- Ввод адреса метки в клавишный регистр");
			"(0000-FFFF)\t- Ввод шестнадцатеричного значения в клавишный регистр\n" +
			"(метка)\t\t- Ввод адреса метки в клавишный регистр"
		);
	}

	private Scanner input = new Scanner(System.in);
	public void cli() {
		String line;


//		bcomp.startTimer(); //TODO ? IODevTimer

		println("Эмулятор Базовой ЭВМ. Версия r" + CLI.class.getPackage().getImplementationVersion() + "\n" +
//			"Загружена " + cpu.getMicroProgramName() + " микропрограмма\n" +
//			"Цикл прерывания начинается с адреса " + Utils.toHex(cpu.getIntrCycleStartAddr(), 8) + "\n" +
			"БЭВМ готова к работе.\n" +
			"Используйте ? или help для получения справки");

		for (;;) {
			try {
				line = fetchLine();
			} catch(Exception e) {
				break;
			}

			processLine(line);
		}

//		System.exit(0);
		Runtime.getRuntime().exit(0);
	}

	protected void processLine(String line){
		int i, value;
		String[] cmds = line.split("[ \t]+");

		if(cmds.length == 0)
			return;

		for (i = 0, printRegsTitle = printMicroTitle = true; i < cmds.length; i++) {
			String cmd = cmds[i];

			if (cmd.equals(""))
				continue;

			if (cmd.charAt(0) == '#')
				break;

			if (checkCmd(cmd, "exit") || checkCmd(cmd, "quit"))
				Runtime.getRuntime().exit(0); // System.exit(0);

			if (checkCmd(cmd, "?") || checkCmd(cmd, "help")) {
				printHelp();
				continue;
			}

			try{
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
					} else
						checkResult(cpu.executeStart());
					continue;
				}

				if (checkCmd(cmd, "continue")) {
					if (i == cmds.length - 1) {
						sleep = sleeptime;
						checkResult(cpu.startContinue());
					} else
						checkResult(cpu.executeContinue());
					continue;
				}

				if (checkCmd(cmd, "clock")) {
					println("Такт: " + (cpu.invertClockState() ? "Нет" : "Да"));
					continue;
				}

				if (checkCmd(cmd, "run")) {
					cpu.invertRunState();
					println("Режим работы: " + (
							cpu.getProgramState(State.RUN) == 1 ? "Работа" : "Останов"));
					continue;
				}

				if (checkCmd(cmd, "maddress")) {
					checkResult(cpu.executeSetMP());
					printMicroMemory(cpu.getRegValue(Reg.MP));
					continue;
				}

				if (checkCmd(cmd, "mwrite")) {
					long addr = cpu.getRegValue(Reg.MP);
					checkResult(cpu.executeMCWrite());
					printMicroMemory(addr);
					continue;
				}

				if (checkCmd(cmd, "mread")) {
					long addr = cpu.getRegValue(Reg.MP);
					checkResult(cpu.executeMCRead());
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
//							ioctrls[ioaddr].setData(value);
					}

					printIO(ioaddr);
					continue;
				}

				if (checkCmd(cmd, "flag")) {
					if (i == cmds.length - 1)
						throw new Exception("команда flag требует аргумент");

					int ioaddr = Integer.parseInt(cmds[++i], 16);
//						ioctrls[ioaddr].setFlag();
					printIO(ioaddr);
					continue;
				}

				if (checkCmd(cmd, "asm") || checkCmd(cmd, "assembler")) {
					String code = "";

					println("Введите текст программы. Для окончания введите END");

					for (;;) {
						line = fetchLine(); // line = input.nextLine();


						if (line.equalsIgnoreCase("END"))
							break;

						code = code.concat(line.concat("\n"));
					}

					printOnStop = false;
//						asm.compileProgram(code);
//						asm.loadProgram(cpu);
//						println("Программа начинается с адреса " + Utils.toHex(asm.getBeginAddr(), 11));
					printOnStop = true;

//					try {
//							println("Результат по адресу " + Utils.toHex(asm.getLabelAddr("R"), 11));
//					} catch (Exception e) { }

					continue;
				}

				if (checkCmd(cmd, "sleep")) {
					if (i == cmds.length - 1)
						throw new Exception("команда sleep требует аргумент");

					sleeptime = Integer.parseInt(cmds[++i], 16);
					continue;
				}
			} catch (Exception e) {
				printOnStop = true;
				println("Ошибка: " + e.getMessage());
				continue;
			}

			try {
				if (Utils.isHexNumeric(cmd)) {
					value = Integer.parseInt(cmd, 16);
					cpu.getRegister(Reg.IR).setValue(value); //cpu.setRegKey(value);
				} else
					println("Неизвестная команда " + cmd);
//					else
//						value = asm.getLabelAddr(cmd.toUpperCase());
			} catch (Exception e) {
				println("Неизвестная команда " + cmd);
			}
		}
	}

	protected String fetchLine() throws Exception {
		return input.nextLine();
	}

	protected void println(String str){
		System.out.println(str);
	}
}
