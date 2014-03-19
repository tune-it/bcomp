/*
 * $Id$
 */
package ru.ifmo.cs.bcomp;

import java.util.Arrays;
import java.util.List;
import java.util.EnumMap;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import static java.lang.Integer.parseInt;
import static ru.ifmo.cs.bcomp.Utils.isHexNumeric;
import static ru.ifmo.cs.bcomp.Utils.toHex;
import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.Register;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 * @author Anastasia Prasolova <a-prasolova1507@yandex.ru>
 */
public class BasicCompTest {
	private class HexInt {
		private final int value;

		public HexInt(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof HexInt) {
				return value == ((HexInt) obj).getValue();
			}
			return false;
		}

		@Override
		public String toString() {
			return toHex(value, 16);
		}
	}

	private class MemoryListener implements DataDestination {
		private final Register regAddr;
		private final String testinfo;
		private final HashMap<Integer, HexInt> mem;

		public MemoryListener(CPU cpu,String testinfo, HashMap<Integer, HexInt> mem) {
			this.regAddr = cpu.getRegister(CPU.Reg.ADDR);
			this.testinfo = testinfo;
			this.mem = mem;
		}

		public void setValue(int value) {
			int addr = regAddr.getValue();
			if (!mem.containsKey(addr))
				fail(testinfo + "неожиданная запись в ячейку " + toHex(addr, 11));
		}
	}

	private static final CPU.Reg[] REGS_TO_TEST = {
		CPU.Reg.ACCUM, CPU.Reg.ADDR, CPU.Reg.DATA, CPU.Reg.IP, CPU.Reg.INSTR
	};

	private static final int[] FLAGS_TO_TEST = {
		StateReg.FLAG_C, StateReg.FLAG_Z, StateReg.FLAG_N, StateReg.FLAG_EI, StateReg.FLAG_READY
	};

	private static final List<String> CONTROL_OPS =
		Arrays.asList("ADDR", "WRITE", "READ", "START");

	private static final String[] SHARED_TESTS = {
		// Пультовые операции
		"ADDR 100;chk:СК=100",
		"WRITE DEAD;chk:СК=011,РА=010,РД=DEAD,010=DEAD",
		"READ;mem:010=BEEF;chk:СК=011,РА=010,РД=BEEF",
		// Безадресные команды
		"HLT",
		"NOP",
		"CLA;run:DEC,DEC;chk:Акк=0000,Z=1,N=0", // C=1 after double DEC
		"CLA;run:INC;chk:Акк=0000,Z=1,N=0",
		"CLC;chk:C=0",
		"CLC;run:DEC,DEC;chk:C=0",
		"CMA;mem:020=8000;run:ADD 020;chk:Акк=7FFF,N=0",
		"CMA;chk:Акк=FFFF,N=1,Z=0",
		"CMA;run:DEC;chk:Акк=0000,N=0,Z=1",
		"CMC;chk:C=1",
		"CMC;run:DEC,DEC;chk:C=0",
		"ROL;run:CMC;chk:Акк=0001,Z=0,C=0",
		"ROL;mem:020=4000;run:ADD 020;chk:Акк=8000,N=1",
		"ROL;mem:020=8000;run:ADD 020;chk:Акк=0000,Z=1,N=0,C=1",
		"ROR;run:CMC;chk:Акк=8000,Z=0,N=1,C=0",
		"ROR;mem:020=8000;run:ADD 020;chk:Акк=4000,N=0",
		"ROR;run:INC;chk:Акк=0000,Z=1,C=1",
		"INC;chk:Акк=0001,Z=0",
		"INC;run:DEC;chk:Акк=0000,Z=1,N=0,C=1",
		"INC;mem:020=7FFF;run:ADD 020;chk:Акк=8000,N=1",
		"DEC;chk:Акк=FFFF,Z=0,N=1",
		"DEC;run:INC;chk:Акк=0000,Z=1,C=1",
		"DEC;mem:020=8000;run:ADD 020;chk:Акк=7FFF,N=0,C=1",
		// Адресные команды
		"ISZ 020;mem:020=8000;chk:РД=8001,РА=020,020=8001",
		"ISZ 020;mem:020=FFFF;chk:РД=0000,РА=020,СК=012,020=0000",
	};

	private static final String[] BASE_SET_TESTS = {
		// Пультовые операции
		"START;run:DEC,CMC;chk:Акк=0000,C=0,Z=1,N=0",
		// Безадресные команды
		"EI;chk:РД=F000,РА=011,СК=012,РК=F000,EI=1",
		"DI;run:EI;chk:РД=F000,РА=011,СК=012,РК=F000,EI=0",
		//		"CLC; data = 020 = 9000, 021 = 8000; cmds = CLA, ADD 020, ADD 021; flags = C = 0",
	//		"CMC; data = 020 = 9000, 021 = 8000; cmds = CLA, ADD 020, ADD 021; flags = C = 0",
	//		"ROL; data = 020 = 0004; cmds = CLA, CLC, ADD 020; regs = ACCUM = 0008; flags = C = 0",
	//		"ROL; data = 020 = 8000; cmds = CLA, CLC, ADD 020; regs = ACCUM = 0; flags = C = 1",
	//		"ROR; data = 020 = 0001; cmds = CLA, CLC, ADD 020; regs = ACCUM = 0; flags = C = 1",
	//		"ROR; data = 020 = 8000; cmds = CLA, CLC, ADD 020; regs = ACCUM = 4000; flags = C = 0",
	//		"INC; cmds = CLA; regs = ACCUM = 1; flags = C = 0, Z = 0, N = 0",//FAIL
	//		"DEC; cmds = CLA; regs = ACCUM = FFFF; flags = Z = 0, C = 0, N = 1",//FAIL
	//		"HLT",
	//		"NOP",
	//		"ADD 020; data = 020 = 9234; cmds = CLA; regs = ACCUM = 9234; flags = C = 0, N = 1, Z = 0",
	//		"ADD 020; data = 020 = 9000, 021 = 8000; cmds = CLA, ADD 021; regs = ACCUM = 8000; flags = C = 1, N = 0, Z = 1",
	//		"MOV 020; cmds = CLA, INC; memory = 020 = 1",
	//		"AND 020; data = 020 = ABCD, 021 = FFFF; cmds = CLA, ADD 021; regs = ACCUM = ABCD; flags = N = 1, Z = 0",
	//		"ADC 020; data = 020 = 1234, 021 = 9000, 022 = 8000; cmds = CLA, ADD 021, ADD 022; regs = ACCUM = 1235; flags = C = 0, Z = 0, N = 0",
	//		"SUB 020; data = 020 = 0088, 021 = 1488; cmds = CLA, ADD 021; regs = ACCUM = 1400; flags = C = 0, N = 0, Z = 0",
	//		"SUB 020; data = 020 = FFFF, 021 = 1234; cmds = CLA, ADD 021; regs = ACCUM = 1235; flags = C = 1, Z = 0, N = 0",
	//		"ISZ 00B; data = 00B = FFFE; regs = INSTR = 011, DATA = FFFF; memory = 00B = FFFF",
	//		"ISZ 00B; data = 00B = 0002; regs = INSTR = 012, DATA = 0003; memory = 00B = 0003",
	//		"BCS 00B; data = 020 = 9000, 021 = 8000; cmds = CLA, ADD 020, ADD 021; regs = INSTR = 00B; flags = C = 1",
	//		"BPL 00B; cmds = CLA, INC; regs = INSTR = 00B; flags = N = 0",
	//		"BPL 00B; cmds = CLA; regs = INSTR = 00B; flags = Z = 1",
	//		"BMI 00B; cmds = CLA, DEC; regs = INSTR = 00B; flags = N = 1",
	//		"BEQ 00B; cmds = CLA; regs = INSTR = 00B; flags = Z = 1",
	//		"BR 00B; regs = INSTR = 00B",
	//		"JSR 00B; regs = INSTR = 00C; memory = 00B = 010",
	//		"EI; flags = EI = 1",
	//		"DI; flags = EI = 0",
	//		"TSF 1; cmds = CLF 1; regs = IP = 011; flags = READY = 0",//fail
	//		"CLF 1; flags = READY = 0",//fail
	//		"IN 1; data = 020 = 6789; cmds = CLA, MOV 020, OUT 1; regs = ACCUM = 6789; flags = READY = 0",//fail
	//		"OUT 1; data = 020 = 7890; cmds = CLA, MOV 020; regs = IODATA = 7890, ACCUM = 7890; flags = READY = 0"//fail
	};

	private static final String[] EXTENDED_SET_TESTS = {
		// Пультовые операции
		"START;run:DEC,CMC;chk:Акк=0000,РА=7FF,РД=FFFF,C=0,Z=1,N=0,7FF=FFFF",
		// Безадресные команды
		"EI;chk:EI=1",
		"DI;run:EI;chk:EI=0",
	//		"CLA; cmds = INC; check = АCCUM = 0",
	//		"CLC; data = 020 = 9000, 021 = 8000; cmds = CLA, ADD 020, ADD 021; flags = C = 0",
	//		"CMA; data = 020 = 8000; cmds = CLA, CLC, ADD 020; regs = АCCUM = 7FFF; flags = N = 0, Z = 0", //FAIL
	//		"CMC; data = 020 = 9000, 021 = 8000; cmds = CLA, ADD 020, ADD 021; flags = C = 0",
	//		"ROL; data = 020 = 0004; cmds = CLA, CLC, ADD 020; regs = ACCUM = 0008; flags = C = 0",
	//		"ROL; data = 020 = 8000; cmds = CLA, CLC, ADD 020; regs = ACCUM = 0; flags = C = 1",
	//		"SHL; data = 020 = FFFF, 021 = 0001; cmds = CLA, ADD 020, ADD 021; regs = ACCUM = FFFE; flags = C = 0",
	//		"ROR; data = 020 = 0001; cmds = CLA, CLC, ADD 020; regs = ACCUM = 0; flags = C = 1",
	//		"ROR; data = 020 = 8000; cmds = CLA, CLC, ADD 020; regs = ACCUM = 4000; flags = C = 0",
	//		"SHR; data = 020 = FFFF, 021 = 0001; cmds = CLA, ADD 020, ADD 021; regs = ACCUM = 7FFF; flags = C = 0",
	//		"INC; cmds = CLA, CLC; regs = ACCUM = 1; flags = C = 0, Z = 0, N = 0",//FAIL
	//		"DEC; cmds = CLA; regs = ACCUM = FFFF; flags = C = 0, Z = 0, N = 1",//fail
	//		"HLT",
	//		"NOP",
	//		"ADD 020; data = 020 = 9234; cmds = CLA; regs = ACCUM = 9234; flags = C = 0, N = 1, Z = 0",
	//		"ADD 020; data = 020 = 9000, 021 = 8000; cmds = CLA, ADD 021; regs = ACCUM = 8000; flags = C = 1, N = 0, Z = 1",
	//		"MOV 020; cmds = CLA, INC; memory = 020 = 1",
	//		"AND 020; data = 020 = ABCD, 021 = FFFF; cmds = CLA, ADD 021; regs = ACCUM = ABCD; flags = N = 1, Z = 0",
	//		"ADC 020; data = 020 = 1234, 021 = 9000, 022 = 8000; cmds = CLA, ADD 021, ADD 022; regs = ACCUM = 1235; flags = C = 0, Z = 0, N = 0",
	//		"SUB 020; data = 020 = 0088, 021 = 1488; cmds = CLA, ADD 021; regs = ACCUM = 1400; flags = C = 0, N = 0, Z = 0",
	//		"SUB 020; data = 020 = FFFF, 021 = 1234; cmds = CLA, ADD 021; regs = ACCUM = 1235; flags = C = 1, z = 0, N = 0",
	//		"ISZ 00B; data = 00B = FFFE; regs = INSTR = 011, DATA = FFFF; memory = 00B = FFFF",
	//		"ISZ 00B; data = 00B = 0002; regs = INSTR = 012, DATA = 0003; memory = 00B = 0003",
	//		"BCS 00B; data = 020 = 9000, 021 = 8000; cmds = CLA, ADD 020, ADD 021; regs = INSTR = 00B; flags = C = 1",
	//		"BPL 00B; cmds = CLA, INC; regs = INSTR = 00B; flags = N = 0",
	//		"BPL 00B; cmds = CLA; regs = INSTR = 00B; flags = Z = 1",
	//		"BMI 00B; cmds = CLA, DEC; regs = INSTR = 00B; flags = N = 1",
	//		"BEQ 00B; cmds = CLA; regs = INSTR  = 00B; flags = Z = 1",
	//		"JUMP 00B; regs = INSTR = 00B",//unknown command
	//		"SWAP; data = 7FE = 1234, 020 = 5678; cmds = CLA, ADD 020; regs = ACCUM = 1234; flags = C = 0, N = 0, Z = 0; memory = 7FE = 5678",
	//		"PUSH; cmds = CLA, INC; regs = ACCUM = 1; memory = 7FE = 0001, 7FF = 7FE",
	//		"PUSHF; cmds = CLA; flags = C = 0, Z = 1, N = 0; memory = 7FE = 0040, 7FF = 7FE",
	//		"POP; data = 020 = 2345; cmds = CLA, ADD 020, MOV 7FE; regs = ACCUM = 2345; flags = C = 0, Z = 0, N = 0; memory = 7FF = 7FE",
	//		"POPF; data = 7FE = 0020; flags = C = 0, N = 1, Z = 0",
	//		"RET; cmds = CALL 00B; regs = INSTR = 011",
	//		"CALL 00B; regs = INSTR = 00B; memory = 7FE = 011",
	//		"EI; flags = EI = 1",
	//		"DI; flags = EI = 0",
	//		"CMP 021; data = 020 = 5678, 021 = FFFF; cmds = CLA, ADD 020; flags = C = 0, Z = 0, N = 1",
	//		"CMP 021; data = 020 = 1234, 021 =  1234; cmds = CLA, ADD 020; flags = C = 0, Z = 1, N = 0",
	//		"CMP 021; data = 020 = FFFF, 021 = FFFF; cmds = CLA, ADD 020; flags = C = 1, N = 0, Z = 0",
	//		"LOOP 00B; data = 00B = A001; regs = INSTR = 011, DATA = A000; memory = 00B = A000",
	//		"LOOP 00B; data = 00B = 8000; regs = INSTR = 012, DATA = 7FFF; memory = 00B = 7FFF",
	//		"TSF 1; flags = READY = 1", //fail
	//		"CLF 1; flags = READY = 0",//fail
	//		"IN 1; data = 020 = 6789; cmds = CLA, MOV 020, OUT 1; regs = ACCUM = 6789; flags = READY = 0",//fail
	//		"OUT 1; data = 020 = 7890; cmds = CLA, MOV 020; regs = IODATA = 7890, ACCUM = 7890; flags = READY = 0"//fail
	};

	private static final HashMap<Instruction[], String[]> TEST_SETS
		= new HashMap<Instruction[], String[]>();

	static {
		TEST_SETS.put(BaseInstrSet.instructions, BASE_SET_TESTS);
		TEST_SETS.put(ExtendedInstrSet.instructions, EXTENDED_SET_TESTS);
	}

	@Test
	public void testBasicComp() throws Exception {
		for (String microprogram : MicroPrograms.getMicroProgramsList()) {
			testMicroProgram(microprogram);
		}
	}

	private void testMicroProgram(String microprogram) throws Exception {
		MicroProgram mp = MicroPrograms.getMicroProgram(microprogram);
		BasicComp bcomp = new BasicComp(mp);
		Assembler asm = new Assembler(mp.instructionSet);

		for (String test : SHARED_TESTS)
			runTest(test, bcomp, asm);
		for (String test : TEST_SETS.get(bcomp.getCPU().getInstructionSet()))
			runTest(test, bcomp, asm);
	}

	private void prepareProgram(String cmds, CPU cpu, Assembler asm) throws Exception {
		asm.compileProgram("ORG 10\nBEGIN:\n" + cmds + "\nHLT");
		asm.loadProgram(cpu);
	}

	private void runTest(String test, BasicComp bcomp, Assembler asm) throws Exception {
		CPU cpu = bcomp.getCPU();
		String mpname = "Микропрограмма " + cpu.getMicroProgramName() + " Тест ";
		String[] fields = test.split(";");
		String cmd = fields[0];
		String[] cmdfields = cmd.split(" ");
		boolean isControlOp = CONTROL_OPS.contains(cmdfields[0]);
		String run = null;
		String[] data = null;
		String[] checks = null;
		EnumMap<CPU.Reg, HexInt> regs = new EnumMap<CPU.Reg, HexInt>(CPU.Reg.class);
		HashMap<Integer, Integer> flags = new HashMap<Integer, Integer>();
		HashMap<Integer, HexInt> mem = new HashMap<Integer, HexInt>();
		String testinfo = mpname + test + ": ";
		DataDestination memoryListener;

		for (String field : Arrays.copyOfRange(fields, 1, fields.length)) {
			String[] params = field.split(":");

			if (params[0].equals("run")) {
				run = params[1].replace(",", "\n");
			} else if (params[0].equals("mem")) {
				data = params[1].split(",");
			} else if (params[0].equals("chk")) {
				checks = params[1].split(",");
			} else {
				fail(mpname + "неизвестный параметр " + params[0]);
			}
		}

		if (data != null) {
			for (String field : data) {
				String[] pair = field.split("=");
				cpu.runSetAddr(parseInt(pair[0], 16));
				cpu.runWrite(parseInt(pair[1], 16));
			}
		}

		cpu.runStart();

		if (run != null) {
			prepareProgram(run, cpu, asm);
			cpu.setRunState(1);
			cpu.start();
			cpu.setRunState(0);
		}

		if (isControlOp) {
			cpu.runSetAddr(0x10);

			for (CPU.Reg reg : REGS_TO_TEST)
				regs.put(reg, new HexInt(cpu.getRegValue(reg)));
		} else {
			prepareProgram(cmd, cpu, asm);

			regs.put(CPU.Reg.ACCUM, new HexInt(cpu.getRegValue(CPU.Reg.ACCUM)));
			int ip = cpu.getRegValue(CPU.Reg.IP);
			int command = cpu.getMemoryValue(ip);
			regs.put(CPU.Reg.ADDR, new HexInt(ip));
			regs.put(CPU.Reg.DATA, new HexInt(command));
			regs.put(CPU.Reg.IP, new HexInt(ip + 1));
			regs.put(CPU.Reg.INSTR, new HexInt(command));
		}

		for (int flag : FLAGS_TO_TEST)
			flags.put(flag, cpu.getStateValue(flag));

		if (checks != null)
			for (String chk : checks) {
				String[] pair = chk.split("=");
				int value = parseInt(pair[1], 16);

				if (StateReg.isFlag(pair[0]))
					flags.put(StateReg.getFlag(pair[0]), value);
				else if (isHexNumeric(pair[0]))
					mem.put(parseInt(pair[0], 16), new HexInt(value));
				else {
					CPU.Reg reg = cpu.findRegister(pair[0]);

					if (reg != null && Arrays.asList(REGS_TO_TEST).contains(reg))
						regs.put(reg, new HexInt(value));
					else
						fail("Неизвестная проверка " + pair[0]);
				}
			}

		bcomp.addDestination(ControlSignal.MEMORY_WRITE,
			memoryListener =new MemoryListener(cpu, testinfo, mem));

		if (isControlOp) {
			if (cmdfields[0].equals("ADDR"))
				cpu.runSetAddr(parseInt(cmdfields[1], 16));
			else if (cmdfields[0].equals("WRITE"))
				cpu.runWrite(parseInt(cmdfields[1], 16));
			else if (cmdfields[0].equals("READ"))
				cpu.runRead();
			else if (cmdfields[0].equals("START"))
				cpu.runStart();
		} else
			cpu.start();

		bcomp.removeDestination(ControlSignal.MEMORY_WRITE, memoryListener);

		for (CPU.Reg reg : regs.keySet())
			assertEquals(
				testinfo + cpu.getRegister(reg).fullname,
				regs.get(reg), new HexInt(cpu.getRegValue(reg)));

		for (int flag : flags.keySet())
			assertEquals(
				testinfo + "флаг " + StateReg.NAME[flag],
				flags.get(flag).intValue(), cpu.getStateValue(flag));

		for (int addr : mem.keySet())
			assertEquals(
				testinfo + "ОП(" + toHex(addr, 11) + ")",
				mem.get(addr), new HexInt(cpu.getMemoryValue(addr)));
	}
}
