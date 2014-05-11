/*
 * $Id$
 */
package ru.ifmo.cs.bcomp;

import java.util.Arrays;
import java.util.List;
import java.util.EnumMap;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
 */
public class BasicCompTest {
	private static final String[] SHARED_TESTS = {
		// Пультовые операции
		"ADDR 100;chk:СК=100",
		"WRITE DEAD;chk:СК=011,РА=010,РД=DEAD,010=DEAD",
		"READ;mem:010=BEEF;chk:СК=011,РА=010,РД=BEEF",
		// Безадресные команды
		"HLT",
		"NOP",
		"CLA;run:DEC,DEC;chk:Акк=0000,Z=1,N=0", // C=1 after double DEC
		"CLC;run:DEC,DEC;chk:C=0",
		"CMA;mem:020=0000;run:SUB 020;chk:Акк=FFFF,N=1,Z=0",
		"CMA;run:DEC;chk:Акк=0000,N=0,Z=1",
		"CMC;chk:C=1",
		"CMC;run:DEC,DEC;chk:C=0",
		"ROL;run:CMC;chk:Акк=0001,C=0,N=0,Z=0",
		"ROL;mem:020=4000;run:ADD 020;chk:Акк=8000,C=0,N=1,Z=0",
		"ROL;mem:020=8000;run:ADD 020;chk:Акк=0000,C=1,N=0,Z=1",
		"ROR;run:CMC;chk:Акк=8000,C=0,N=1,Z=0",
		"ROR;mem:020=8000;run:ADD 020;chk:Акк=4000,C=0,N=0,Z=0",
		"ROR;run:INC;chk:Акк=0000,C=1,N=0,Z=1",
		"INC;chk:Акк=0001,C=0,N=0,Z=0",
		"INC;run:DEC;chk:Акк=0000,C=1,N=0,Z=1",
		"INC;mem:020=7FFF;run:ADD 020;chk:Акк=8000,C=0,N=1,Z=0",
		"DEC;chk:Акк=FFFF,C=0,N=1,Z=0",
		"DEC;run:INC;chk:Акк=0000,C=1,N=0,Z=1",
		"DEC;mem:020=8000;run:ADD 020;chk:Акк=7FFF,C=1,N=0,Z=0",
		// Адресные команды
		"ISZ 020;mem:020=8000;chk:РД=8001,РА=020,020=8001",
		"ISZ 020;mem:020=FFFF;chk:РД=0000,РА=020,СК=012,020=0000",
		"AND 020;mem:020=FFFF;run:DEC;chk:РД=FFFF,РА=020,Акк=FFFF,N=1,Z=0",
		"AND 020;mem:020=00FF;run:DEC;chk:РД=00FF,РА=020,Акк=00FF,N=0,Z=0",
		"AND 020;mem:020=0000;run:DEC;chk:РД=0000,РА=020,Акк=0000,N=0,Z=1",
		"MOV 020;run:DEC;chk:РД=FFFF,РА=020,020=FFFF",
		"ADD 020;mem:020=0001;run:DEC;chk:РД=0001,РА=020,Акк=0000,C=1,N=0,Z=1",
		"ADD 020;mem:020=FFFF;chk:РД=FFFF,РА=020,Акк=FFFF,C=0,N=1,Z=0",
		"ADC 020;mem:020=0000;run:DEC,CMC;chk:РД=0000,РА=020,Акк=0000,C=1,N=0,Z=1",
		"ADC 020;mem:020=FFFF;chk:РД=FFFF,РА=020,Акк=FFFF,C=0,N=1,Z=0",
		"SUB 020;mem:020=FFFF;run:DEC;chk:РД=FFFF,РА=020,Акк=0000,C=1,N=0,Z=1",
		"SUB 020;mem:020=0001;chk:РД=0001,РА=020,Акк=FFFF,C=0,N=1,Z=0",
		"BCS 020",
		"BCS 020;run:CMC;chk:СК=020",
		"BPL 020;run:DEC",
		"BPL 020;chk:СК=020",
		"BPL 020;run:INC;chk:СК=020",
		"BMI 020;run:DEC;chk:СК=020",
		"BMI 020",
		"BMI 020;run:INC",
		"BEQ 020;run:DEC",
		"BEQ 020;chk:СК=020",
		"BEQ 020;run:INC",
	};

	private static final String[] BASE_SET_TESTS = {
		// Пультовые операции
		"START;run:DEC,CMC;chk:Акк=0000,C=0,N=0,Z=1,EI=0",
		// Безадресные команды
		"EI;chk:РД=F000,РА=011,СК=012,РК=F000,EI=1",
		"DI;run:EI;chk:РД=F000,РА=011,СК=012,РК=F000,EI=0",
		// Адресные команды
		"JSR 020;run:DEC;chk:РД=0011,РА=020,СК=021,РК=2021,020=0011",
		"JSR 020;chk:РД=0011,РА=020,СК=021,РК=2021,020=0011",
		"JSR 020;run:INC,CMC;chk:РД=0011,РА=020,СК=021,РК=2021,020=0011",
		"BR 020;run:DEC;chk:СК=020",
		"BR 020;chk:СК=020",
		"BR 020;run:INC,CMC;chk:СК=020",
	};

	private static final String[] EXTENDED_SET_TESTS = {
		// Пультовые операции
		"START;run:DEC,CMC;chk:РД=FFFF,РА=7FF,Акк=0000,C=0,Z=1,N=0,EI=0,7FF=FFFF",
		// Безадресные команды
		"EI;chk:EI=1",
		"DI;run:EI;chk:EI=0",
		"SHL;mem:020=4000;run:ADD 020,CMC;chk:Акк=8000,C=0,N=1,Z=0",
		"SHL;mem:020=8000;run:ADD 020;chk:Акк=0000,C=1,N=0,Z=1",
		"SHR;mem:020=8000;run:ADD 020,CMC;chk:Акк=4000,C=0,N=0,Z=0",
		"SHR;run:INC;chk:Акк=0000,C=1,N=0,Z=1",
		"SWAP;run:DEC,PUSH,DEC;chk:РД=FFFE,РА=7FE,РК=FFFF,Акк=FFFF,N=1,Z=0,7FE=FFFE",
		"SWAP;run:PUSH,DEC;chk:РД=FFFF,РА=7FE,РК=0000,Акк=0000,N=0,Z=1,7FE=FFFF",
		"PUSH;run:DEC;chk:РД=FFFF,РА=7FE,7FF=FFFE,7FE=FFFF",
		"PUSHF;chk:РД=0102,РА=7FE,7FF=FFFE,7FE=0102",
		"PUSHF;run:DEC,DEC;chk:РД=0105,РА=7FE,7FF=FFFE,7FE=0105",
		"POP;run:PUSH,DEC;chk:РД=0000,РА=7FE,Акк=0000,N=0,Z=1,7FF=FFFF",
		"POP;run:DEC,PUSH,CLA;chk:РД=FFFF,РА=7FE,Акк=FFFF,N=1,Z=0,7FF=FFFF",
		"POPF;run:PUSHF,DEC;chk:РД=0182,РА=7FE,C=0,N=0,Z=1,EI=0,7FF=FFFF",
		"POPF;run:DEC,DEC,EI,PUSHF,CLA,DI;chk:РД=0195,РА=7FE,C=1,N=1,Z=0,EI=1,7FF=FFFF",
		"RET;run:NOP,NOP,CALL 013;chk:СК=013,РД=0013,РА=7FE,7FF=FFFF",
		// IRET not tested because have double write to 7FF
		// Адресные команды
		"CALL 020;run:DEC;chk:СК=020,РД=0011,РА=7FE,7FF=FFFE,7FE=0011",
		"CALL 020;chk:СК=020,РД=0011,РА=7FE,7FF=FFFE,7FE=0011",
		"CALL 020;run:INC,CMC;chk:СК=020,РД=0011,РА=7FE,7FF=FFFE,7FE=0011",
		"JMP 020;run:DEC;chk:СК=020",
		"JMP 020;chk:СК=020",
		"JMP 020;run:INC,CMC;chk:СК=020",
		"CMP 020;mem:020=FFFF;run:DEC;chk:РД=FFFF,РА=020,C=1,N=0,Z=1",
		"CMP 020;mem:020=0001;chk:РД=0001,РА=020,C=0,N=1,Z=0",
		"LOOP 020;mem:020=0000;chk:РД=FFFF,РА=020,020=FFFF",
		"LOOP 020;mem:020=8000;chk:РД=7FFF,РА=020,СК=012,020=7FFF",
	};

	private static final CPU.Reg[] REGS_TO_TEST = {
		CPU.Reg.ACCUM, CPU.Reg.ADDR, CPU.Reg.DATA, CPU.Reg.IP, CPU.Reg.INSTR
	};

	private static final List<String> CONTROL_OPS =
		Arrays.asList("ADDR", "WRITE", "READ", "START");

	private static final HashMap<Instruction[], String[]> TEST_SETS =
		new HashMap<Instruction[], String[]>();

	private static final HashMap<Integer, ControlSignal[]> FLAGS_TO_TEST =
		new HashMap<Integer, ControlSignal[]>();

	static {
		TEST_SETS.put(BaseInstrSet.instructions, BASE_SET_TESTS);
		TEST_SETS.put(ExtendedInstrSet.instructions, EXTENDED_SET_TESTS);

		FLAGS_TO_TEST.put(StateReg.FLAG_C, new ControlSignal[] {
			ControlSignal.SET_STATE_C,
			ControlSignal.CLEAR_STATE_C,
			ControlSignal.BUF_TO_STATE_C
		});
		FLAGS_TO_TEST.put(StateReg.FLAG_Z, new ControlSignal[] {
			ControlSignal.BUF_TO_STATE_Z
		});
		FLAGS_TO_TEST.put(StateReg.FLAG_N, new ControlSignal[] {
			ControlSignal.BUF_TO_STATE_N
		});
		FLAGS_TO_TEST.put(StateReg.FLAG_EI, new ControlSignal[] 	{
			ControlSignal.ENABLE_INTERRUPTS, 
			ControlSignal.DISABLE_INTERRUPTS
		});
		FLAGS_TO_TEST.put(StateReg.FLAG_READY, new ControlSignal[] {
			ControlSignal.INPUT_OUTPUT
		});
	};

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
			return value == ((HexInt) obj).getValue();
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

		public MemoryListener(CPU cpu, String testinfo, HashMap<Integer, HexInt> mem) {
			this.regAddr = cpu.getRegister(CPU.Reg.ADDR);
			this.testinfo = testinfo;
			this.mem = mem;
		}

		public void setValue(int value) {
			int addr = regAddr.getValue();
			HexInt expected = mem.get(addr);

			if (expected == null)
				fail(testinfo + "неожиданная запись в ячейку " + toHex(addr, 11));

			assertEquals(
				testinfo + "ОП(" + toHex(addr, 11) + ")",
				expected, new HexInt(value));
		}
	}

	private class FlagListener implements DataDestination {
		private final CPU cpu;
		private final String testinfo;
		private final Integer flag;
		private final HashMap<Integer, Integer> flags;

		public FlagListener(CPU cpu, String testinfo, int flag, HashMap<Integer, Integer> flags) {
			this.cpu = cpu;
			this.testinfo = testinfo;
			this.flag = flag;
			this.flags = flags;
		}

		@Override
		public void setValue(int value) {
			assertNotNull(testinfo + "неожиданная запись во флаг " + StateReg.NAME[flag], flags.get(flag));
		}
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
		HashMap<Integer, DataDestination> flagListener = new HashMap<Integer, DataDestination>();
		String testinfo = mpname + test + ": ";

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

		DataDestination memoryListener = new MemoryListener(cpu, testinfo, mem);
		bcomp.addDestination(ControlSignal.MEMORY_WRITE, memoryListener);

		for (Integer flag : FLAGS_TO_TEST.keySet()) {
			DataDestination lsnr = new FlagListener(cpu, testinfo, flag, flags);
			flagListener.put(flag, lsnr);

			for (ControlSignal cs : FLAGS_TO_TEST.get(flag))
				bcomp.addDestination(cs, lsnr);
		}

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

		for (Integer flag : FLAGS_TO_TEST.keySet()) {
			DataDestination lsnr = flagListener.get(flag);

			for (ControlSignal cs : FLAGS_TO_TEST.get(flag))
				bcomp.removeDestination(cs, lsnr);
		}

		for (CPU.Reg reg : regs.keySet())
			assertEquals(
				testinfo + cpu.getRegister(reg).fullname,
				regs.get(reg), new HexInt(cpu.getRegValue(reg)));

		for (int flag : flags.keySet())
			assertEquals(
				testinfo + "флаг " + StateReg.NAME[flag],
				flags.get(flag).intValue(), cpu.getStateValue(flag));
	}
}
