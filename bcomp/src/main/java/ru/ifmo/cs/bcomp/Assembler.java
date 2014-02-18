/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import java.util.ArrayList;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Assembler {
	private class Label {
		private final String label;
		private Integer addr;

		private Label(String label) throws Exception {
			this.label = label;

			if (label.equals(""))
				throw new Exception("Имя метки не может быть пустым");
		}

		private void setAddr(int addr) throws Exception {
			checkAddr(addr);
			this.addr = addr;
		}

		private boolean hasAddress() {
			return addr != null;
		}
	}

	protected class Command {
		private final Label arg;
		private final int cmd;
		private final int addr;
		private final int size;

		private Command(int addr, int cmd, Label arg, int size) throws Exception {
			this.addr = addr;
			this.cmd = cmd;
			this.arg = arg;
			this.size = size;
			checkAddr(addr);
		}

		private Command(int addr, int cmd, Label arg) throws Exception {
			this(addr, cmd, arg, 1);
		}

		private Command(int addr, int cmd) throws Exception {
			this(addr, cmd, null, 1);
		}

		protected int getCommand() {
			return arg == null ? cmd : cmd + arg.addr;
		}
	}

	private ArrayList<Label> labels;
	private ArrayList<Command> cmds;
	private Instruction[] instrset;

	public Assembler(Instruction[] instrset) {
		this.instrset = instrset;
	}

	public void compileProgram(String program) throws Exception {
		String[] prog = program.replace("\r", "").toUpperCase().split("\n");
		int addr = 0;
		int lineno = 0;

		labels = new ArrayList<Label>();
		cmds = new ArrayList<Command>();

		try {
			for (String l : prog) {
				lineno++;

				String[] line = l.trim().split("[#;]+");

				if ((line.length == 0) || line[0].equals(""))
					continue;

				line = line[0].trim().split("[ \t]+");

				if ((line.length == 0) || (line[0].equals("")))
					continue;

				if (line[0].equals("ORG")) {
					if (line.length != 2)
						throw new Exception("Директива ORG требует один и только один аргумент");

					checkAddr(addr = Integer.parseInt(line[1], 16));
					continue;
				}

				int col = 0;

				if (line[col].charAt(line[col].length() - 1) == ':') {
					String labelname = line[col].substring(0, line[col].length() - 1);

					Label label = getLabel(labelname);

					if (label.hasAddress())
						throw new Exception("Метка " + labelname + " объявлена повторно");

					label.setAddr(addr);
					col++;
				}

				if (col == line.length)
					continue;

				if (line[col].equals("WORD")) {
					if (col++ == line.length - 1)
						throw new Exception("Директива WORD должна иметь как минимум один аргумент");

					if ((line.length - col) == 3 && line[col + 1].equals("DUP")) {
						int size = Integer.parseInt(line[col], 16);
						if (size < 1 || (addr + size) > 0x7ff)
							throw new Exception("Указано недопустимое количество значений");

						col += 2;
						if (line[col].charAt(0) != '(' || line[col].charAt(line[col].length() - 1) != ')')
							throw new Exception("Значение после DUP должно быть в скобках");
						String value = line[col].substring(1, line[col].length() - 1);

						createWord(addr, value, size);
						addr += size;
						continue;
					}

					String v;
					for (v = line[col++]; col < line.length; v = v.concat(" ").concat(line[col++]));
					String[] values = v.split(",");

					for (String value : values)
						createWord(addr++, value.trim(), 1);

					continue;
				}

				Instruction instr = findInstruction(line[col]);

				if (instr == null)
					throw new Exception("Неизвестная команда " + line[col]);

				switch (instr.getType()) {
					case ADDR:
						if (col != line.length - 2)
							throw new Exception("Адресная команда " + line[col] + " требует один аргумент");

						String labelname = line[col + 1];
						int addrtype;

						if (labelname.charAt(0) == '(') {
							if (labelname.charAt(labelname.length() - 1) != ')')
								throw new Exception("Нет закрывающей скобки");

							labelname = labelname.substring(1, labelname.length() - 1);
							addrtype = 0x800;
						} else
							addrtype = 0;

						if (Utils.isHexNumeric(labelname))
							cmds.add(new Command(addr++, instr.getInstr() + addrtype + Integer.parseInt(labelname, 16)));
						else
							cmds.add(new Command(addr++, instr.getInstr() + addrtype, getLabel(labelname)));

						break;

					case NONADDR:
						if (col != line.length - 1)
							throw new Exception("Безадресная команда " + line[col] + " не требует аргументов");

						cmds.add(new Command(addr++, instr.getInstr()));
						break;

					case IO:
						if (col != line.length - 2)
							throw new Exception("Строка " + lineno + ": Команда ввода-вывода " + line[col] +
								" требует один и только один аргумент");

						cmds.add(new Command(addr++, instr.getInstr() + (Integer.parseInt(line[col + 1], 16) & 0xff)));
						break;
				}
			}

			for (Label label : labels)
				if (!label.hasAddress())
					throw new Exception("Ссылка на неопределённую метку " + label.label);
		} catch (Exception e) {
			throw new Exception("Строка " + lineno + ": " + e.getMessage());
		}
	}

	private Label findLabel(String labelname) {
		for (Label label : labels)
			if (label.label.equals(labelname))
				return label;

		return null;
	}

	private Label getLabel(String labelname) throws Exception {
		Label label = findLabel(labelname);

		if (label == null) {
			label = new Label(labelname);
			labels.add(label);
		}

		return label;
	}

	private void createWord(int addr, String value, int size) throws Exception {
		if (value.equals(""))
			throw new Exception("Пустое значение");

		if (value.equals("?"))
			return;

		int cmd = 0;
		Label arg = null;

		if (Utils.isHexNumeric(value))
			cmd = Integer.parseInt(value, 16);
		else
			arg = getLabel(value);

		cmds.add(new Command(addr, cmd, arg, size));
	}

	private void checkAddr(int addr) throws Exception {
		if (addr < 0 || addr > 0x7ff)
			throw new Exception("Адрес выходит из допустимых значений");
	}

	public Instruction findInstruction(String mnemonics) {
		for (Instruction instr : instrset)
			if (instr.getMnemonics().equals(mnemonics))
				return instr;

		return null;
	}

	public void loadProgram(CPU cpu) throws Exception {
		for (Command cmd : cmds) {
			cpu.setRegKey(cmd.addr);
			cpu.startFrom(ControlUnit.LABEL_ADDR);
			for (int i = 0; i < cmd.size; i++) {
				cpu.setRegKey(cmd.getCommand());
				cpu.startFrom(ControlUnit.LABEL_WRITE);
			}
		}

		cpu.setRegKey(getBeginAddr());
		cpu.startFrom(ControlUnit.LABEL_ADDR);
	}

	public int getLabelAddr(String labelname) throws Exception {
		Label label = findLabel(labelname);

		if (label == null)
			throw new Exception("Метка " + labelname + " не найдена");

		return label.addr;
	}

	public int getBeginAddr() throws Exception {
		return getLabelAddr("BEGIN");
	}

}
