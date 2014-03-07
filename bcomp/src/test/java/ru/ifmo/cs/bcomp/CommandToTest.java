/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.elements.*;
import java.util.*;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Anastasia Prasolova <a-prasolova1507@yandex.ru>
 */
public class CommandToTest {

	private final BasicComp bcomp;
	private final CPU cpu;
	private final Assembler asm;
	String[] inputString;

	String command;
	Data[] input;
	String cmdsToRun;
	HashMap<Register, Integer> registersToCheck = new HashMap<Register, Integer>();
	HashMap<Integer, Integer> flagsToCheck = new HashMap<Integer, Integer>();
	Data[] memoryToCheck;

	public CommandToTest(BasicComp bcomp, Assembler asm, String testString) throws Exception {
		this.cpu = (this.bcomp = bcomp).getCPU();
		this.asm = asm;
		String[] splittedString;

		splittedString = testString.split(";");

		this.command = splittedString[0];
		if (splittedString.length > 1) {
			String[] restString = Arrays.copyOfRange(splittedString, 1, splittedString.length - 1);
			for (String info : restString) {

				if (info.substring(0, info.indexOf(" ")).equals("data")) {
					inputString = info.split(",");
					for (int i = 0; i < inputString.length; i++) {
						input[i].address = Integer.parseInt(inputString[i].substring(0, 2), 16);
						input[i].value = Integer.parseInt(inputString[i].substring(4, 7), 16);
					}
					continue;
				}

				if (info.substring(0, info.indexOf(" ")).equals("cmds")) {
					cmdsToRun = info.replace(" ", "\n");
					continue;
				}

				if (info.substring(0, info.indexOf(" ")).equals("regs")) {
					inputString = splittedString[3].split(",");
					Register reg;
					Integer value;
					for (String str : inputString) {
						if (str.substring(0, str.indexOf(" ")).equals("IODATA")) {

							int ioctrl = Integer.parseInt((command.split(" "))[1]);
							IOCtrl ctrl = (bcomp.getIOCtrls())[ioctrl];
							reg = ctrl.getRegData();
							value = Integer.parseInt(str.substring(str.indexOf(" ") + 1, str.length() - 1), 16);
						} else {
							reg = cpu.getRegister(str.substring(0, str.indexOf(" ")));
							value = Integer.parseInt(str.substring(str.indexOf(" ") + 1, str.length() - 1), 16);
						}
						registersToCheck.put(reg, value);
					}
					continue;
				}

				if (info.substring(0, info.indexOf(" ")).equals("flags")) {
					inputString = info.split(",");
					String flag;
					String value;

					for (String str : inputString) {
						flag = str.substring(0, str.indexOf(" "));
						value = str.substring(str.indexOf(" ") + 1, inputString.length - 1);
						flagsToCheck.put(cpu.getStateValue(Integer.parseInt(flag)), Integer.parseInt(value));
					}
					continue;
				}
				if (info.substring(0, info.indexOf(" ")).equals("memory")) {
					inputString = info.split(",");

					for (int i = 0; i < inputString.length; i++) {
						memoryToCheck[i].address = Integer.parseInt(inputString[i].substring(0, inputString[i].indexOf("=")), 16);
						memoryToCheck[i].value = Integer.parseInt(inputString[i].substring(inputString[i].indexOf("=") + 1, inputString[i].length()), 16);
					}
				}
			}
		}
	}

	void testingCommand() throws Exception {

		HashMap<Integer, Integer> savedFlags = new HashMap<Integer, Integer>();
		int length = 0;
		if (cmdsToRun != null) {
			length = cmdsToRun.split("\n").length;
		}
		int[] regs = {StateReg.FLAG_C, StateReg.FLAG_Z, StateReg.FLAG_N, StateReg.FLAG_EI, StateReg.FLAG_READY};

		if (input != null) {
			for (Data data : input) {
				cpu.setRegKey(data.address);
				cpu.jump(ControlUnit.LABEL_ADDR);
				cpu.start();

				cpu.setRegKey(data.value);
				cpu.jump(ControlUnit.LABEL_WRITE);
				cpu.start();
			}
		}
		if (cmdsToRun != null) {
			asm.compileProgram("ORG 10\nbegin:\n" + cmdsToRun + "\n" + command);
		} else {
			asm.compileProgram("ORG 10\nbegin:\n" + "\n" + command);
		}
		asm.loadProgram(cpu);

		for (int i = 0; i < length; i++) {
			cpu.start();
		}

		for (int reg : regs) {
			savedFlags.put(reg, cpu.getStateValue(reg));
		}

		String name = command.split(" ")[0];
		Instruction instr = asm.findInstruction(name);
		if ((instr.getType() == Instruction.Type.NONADDR) || (instr.getType() == Instruction.Type.ADDR)) {
			if (!(registersToCheck.containsKey(cpu.getRegister(CPU.Reg.ACCUM)))) {
				registersToCheck.put(cpu.getRegister(CPU.Reg.ACCUM), cpu.getRegister(CPU.Reg.ACCUM).getValue());
			}
		}

		if (instr.getType() == Instruction.Type.IO) {
			if (!(registersToCheck.containsKey(cpu.getRegister(CPU.Reg.ACCUM)))) {
				registersToCheck.put(cpu.getRegister(CPU.Reg.ACCUM), cpu.getRegister(CPU.Reg.ACCUM).getValue());
			}
			if (!(registersToCheck.containsKey(cpu.getRegister(CPU.Reg.DATA)))) {
				registersToCheck.put(cpu.getRegister(CPU.Reg.DATA), asm.findInstruction(name).getInstr());
			}
			if (!(registersToCheck.containsKey(cpu.getRegister(CPU.Reg.ADDR)))) {
				registersToCheck.put(cpu.getRegister(CPU.Reg.ADDR), 0x010);
			}
			if (!(registersToCheck.containsKey(cpu.getRegister(CPU.Reg.INSTR)))) {
				registersToCheck.put(cpu.getRegister(CPU.Reg.INSTR), asm.findInstruction(name).getInstr());
			}
		}

		cpu.start();
		//проверяем всё		

		int expectedValue;
		int realValue;
		//проверка регистров
		for (Register reg : registersToCheck.keySet()) {
			expectedValue = registersToCheck.get(reg);
			realValue = reg.getValue();

			if (instr.getType() == Instruction.Type.IO) {
				int ioctrl = Integer.parseInt((command.split(" "))[1]);
				IOCtrl ctrl = (bcomp.getIOCtrls())[ioctrl];
				if (command.split(" ")[0].equals("OUT")) {
					realValue = ctrl.getData();
				}
			}
			assertEquals(expectedValue, realValue);
		}
		//проверка флагов
		for (Integer value : flagsToCheck.keySet()) {
			expectedValue = flagsToCheck.get(value);
			realValue = cpu.getStateValue(value);

			if (instr.getType() == Instruction.Type.IO) {
				int ioctrl = Integer.parseInt((command.split(" "))[1]);
				IOCtrl ctrl = (bcomp.getIOCtrls())[ioctrl];
				realValue = ctrl.getFlag();
			}
			assertEquals(expectedValue, realValue);
		}
		//проверка ячеек памяти
		if (memoryToCheck != null) {
			for (Data d : memoryToCheck) {

				expectedValue = d.value;
				realValue = cpu.getMemoryValue(d.address);
				assertEquals(expectedValue, realValue);
			}
		}

	}
}
