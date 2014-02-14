/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui;

import ru.ifmo.cs.bcomp.Utils;
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
		System.out.println(cpu.getClockState() ?
			"Адр Знчн  СК  РА  РК   РД    А  C Адр Знчн" :
			"Адр МК   СК  РА  РК   РД    А  C   БР  N Z СчМК");
	}

	private String getMemory(int addr) {
		return Utils.toHex(addr, 11) + " " + Utils.toHex(cpu.getMemoryValue(addr), 16);
	}

	private void printMicroMemoryTitle() {
		System.out.println("Адр МК");
	}

	private String getMicroMemory(int addr) {
		return Utils.toHex(addr, 8) + " " + Utils.toHex(cpu.getMicroMemoryValue(addr), 16);
	}

	private void printMicroMemory(int addr) {
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

	private void printRegs(int addr, String add) {
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

	private void cont(int count, boolean printtitle) {
		if (printtitle)
			printRegsTitle();

		for (int i = 0; i < count; i++) {
			int addr = getIP();
			String add;

			writelist.clear();

			try {
				cpu.start();
			} catch (Exception ex) {
				System.out.println("Программа не выполнена полностью: " + ex.getMessage());
			}

			if (writelist.isEmpty())
				add = "";
			else {
				add = " " + getMemory(writelist.get(0));
				writelist.remove(0);
			}

			printRegs(addr, add);

			for (Integer wraddr : writelist)
				System.out.println(String.format("%1$34s", " ") + getMemory(wraddr));
		}
	}

	private void cont() {
		cont(1, true);
	}

	private boolean checkCmd(String[] cmd, String check) {
		return cmd[0].equalsIgnoreCase(check.substring(0, Math.min(check.length(), cmd[0].length())));
	}

	private int getReqValue(String[] cmd, int index) throws Exception {
		if (index >= cmd.length)
			throw new Exception("Value required");

		return Integer.parseInt(cmd[index], 16);
	}

	private int getReqValue(String[] cmd) throws Exception {
		return getReqValue(cmd, 1);
	}

	private int getCountFromCmd(String[] cmd) throws Exception {
		if (cmd.length >= 2)
			return getReqValue(cmd);

		return 1;
	}

	private void printHelp() {
		System.out.println("Пультовые команды:\n" +
			"a[ddress] {value|label} - Ввод адреса\n" +
			"\tЗаписывает value в СК\n" +
			"w[rite] value ... - Запись\n" +
			"\tЗаписывает value в ячейку памяти по адресу в СК\n" +
			"r[ead] [count] - Чтение\n" +
			"\tЧитает 1 или count ячеек памяти по адресу в СК\n" +
			"s[tart] - Пуск\n" +
			"c[continue] [count] - Продолжить\n" +
			"\tВыполняет 1 или count тактов или команд или программ\n" +
			"ru[n] - Переключить режим Работа/Останов\n" +
			"cl[ock] - Переключить режим потактового выполнения\n" +
			"ma[ddress] value - Переход к микрокоманде\n" +
			"\tЗаписывает value в СчМК\n" +
			"mw[rite] value ... - Запись микрокоманды\n" +
			"\tЗаписывает value в память микрокоманд по адресу в СчМК\n" +
			"mr[ead] [count] - Чтение одной или count микрокоманд по адресу в СчМК\n" +
			"io [addr [value]] - Вывод состояния всех ВУ/указанного ВУ/запись value в ВУ\n" +
			"flag addr - Установить флаг готовности указанного ВУ\n" +
			"asm - Ввести программу на ассемблере\n");
	}

	private int parseAddress(String s) throws Exception {
		if (Utils.isHexNumeric(s))
			return Integer.parseInt(s, 16);
		else
			return asm.getLabelAddr(s.toUpperCase());
	}

	public void cli() {
		Scanner input = new Scanner(System.in);
		String line;

		bcomp.startTimer();

		System.out.println("Эмулятор Базовой ЭВМ. Версия r" + CLI.class.getPackage().getImplementationVersion() + "\n" +
			"Загружена " + cpu.getMicroProgramName() + " микропрограмма\n" +
			"Цикл прерывания начинается с адреса " + Utils.toHex(cpu.getIntrCycleStartAddr(), 8) + "\n" +
			"БЭВМ готова к работе.\n" +
			"Используйте ? или help для получения справки");

		for (;;) {
			try {
				line = input.nextLine();
			} catch(Exception ex) {
				break;
			}

			String[] cmd = line.split("[ \t]+");

			if ((cmd.length == 0) || cmd[0].equals(""))
				continue;

			if (cmd[0].charAt(0) == '#')
				continue;

			if (checkCmd(cmd, "?") || checkCmd(cmd, "help")) {
				printHelp();
				continue;
			}

			try {
				if (checkCmd(cmd, "address")) {
					if (cmd.length != 2)
						throw new Exception("Only one value required");

					cpu.setRegKey(parseAddress(cmd[1]));
					cpu.jump(ControlUnit.LABEL_ADDR);
					cont();
					continue;
				}

				if (checkCmd(cmd, "write")) {
					for (int i = 1; i < cmd.length; i++) {
						cpu.setRegKey(getReqValue(cmd, i));
						cpu.jump(ControlUnit.LABEL_WRITE);
						cont(1, i == 1);
					}
					continue;
				}

				if (checkCmd(cmd, "read")) {
					int count = getCountFromCmd(cmd);

					printRegsTitle();

					for (int i = 0; i < count; i++) {
						cpu.jump(ControlUnit.LABEL_READ);
						cont(1, false);
					}
					continue;
				}

				if (checkCmd(cmd, "start")) {
					cpu.jump(ControlUnit.LABEL_START);
					cont();
					continue;
				}

				if (checkCmd(cmd, "continue")) {
					int count = getCountFromCmd(cmd);
					cont(count, true);
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
					int addr = getReqValue(cmd);
					cpu.setRegKey(addr);
					cpu.jump();
					printMicroMemoryTitle();
					printMicroMemory(addr);
					continue;
				}

				if (checkCmd(cmd, "mwrite")) {
					printMicroMemoryTitle();

					for (int i = 1; i < cmd.length; i++) {
						int addr = cpu.getRegValue(CPU.Reg.MIP);
						cpu.setRegKey(getReqValue(cmd, i));
						cpu.setMicroMemory();
						printMicroMemory(addr);
					}
					continue;
				}

				if (checkCmd(cmd, "mread")) {
					int count = getCountFromCmd(cmd);

					printMicroMemoryTitle();

					for (int i = 0; i < count; i++) {
						int mip = cpu.getRegValue(CPU.Reg.MIP);
						printMicroMemory(mip);
						cpu.readMInstr();
					}
					continue;
				}

				if (checkCmd(cmd, "io")) {
					if (cmd.length == 1) {
						for (int ioaddr = 0; ioaddr < 4; ioaddr++)
							printIO(ioaddr);
						continue;
					}

					int ioaddr = getReqValue(cmd);

					if (cmd.length == 3) {
						int value = getReqValue(cmd, 2);
						System.out.println("Value: " + value);
						ioctrls[ioaddr].setData(value);

					}

					printIO(ioaddr);
					continue;
				}

				if (checkCmd(cmd, "flag")) {
					int ioaddr = getReqValue(cmd);
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

					asm.compileProgram(code);
					asm.loadProgram(cpu);

					System.out.println("Программа начинается с адреса " + Utils.toHex(asm.getBeginAddr(), 11));

					try {
						System.out.println("Результат по адресу " + Utils.toHex(asm.getLabelAddr("R"), 11));
					} catch (Exception ex) { }

					continue;
				}

				if (checkCmd(cmd, "exit") || checkCmd(cmd, "quit"))
					System.exit(0);
			} catch (Exception ex) {
				System.out.println("Ошибка: " + ex.getMessage());
				continue;
			}

			System.out.println("Unknown command");
		}

		System.exit(0);
	}
}
