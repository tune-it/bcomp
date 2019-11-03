/*
 * $Id$
 */
package ru.ifmo.cs.bcomp;

import java.util.Arrays;
import java.util.EnumMap;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Iterator;
import static ru.ifmo.cs.bcomp.Utils.toHex;
import ru.ifmo.cs.components.DataDestination;
import ru.ifmo.cs.components.Register;
import static ru.ifmo.cs.bcomp.ControlSignal.*;
import static ru.ifmo.cs.bcomp.Reg.*;
import static ru.ifmo.cs.bcomp.State.*;
import ru.ifmo.cs.components.Memory;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BasicCompTest {
	private final Reg[] TEST_REGISTERS = { DR, CR, IP, SP, AC, BR, AR };
	private final State[] TEST_STATES = { C, V, Z, N, F, EI };
	private final long[] TEST_REG_VALUES = { 0xDEAD, 0x0ADB	};
	private final long[] TEST_FLAG_VALUES = { 0, 1 };
	private Hexadecimal x100 = new Hexadecimal(0x100);
	private Hexadecimal x101 = new Hexadecimal(0x101);

	private static final String[] TESTS = {
		"START; ; DR=0,CR=0,SP=0,AC=0,BR=0,AR=0,C=0,V=0,Z=1,N=0,EI=0",
		"SETIP; IR=100; IP=100",
		"SETIP; IR=7FF; IP=7FF",
		"WRITE; IR=BEEF,IP=100; IP=101,AR=100,DR=BEEF,100=BEEF",
		"WRITE; IR=1313,IP=7FF; IP=000,AR=7FF,DR=1313,7FF=1313",
		"READ; IP=100,100=BEEF; IP=101,AR=100,DR=BEEF",
		"READ; IP=7FF,7FF=1313; IP=000,AR=7FF,DR=1313",

		"0000", // NOP

		"0100", // HLT

		"0200; ; AC=0000,N=0,Z=1,V=0", // CLA

		"0280; AC=BEEF; AC=4110,V=0,Z=0,N=0", // NOT
		"0280; AC=FFFF; AC=0000,V=0,Z=1,N=0", // NOT
		"0280; AC=0000; AC=FFFF,V=0,Z=0,N=1", // NOT

		"0300; ; C=0", // CLC

		"0380; C=1; C=0", // CMC
		"0380; C=0; C=1", // CMC

		"0400; AC=BEEF,C=1; AC=7DDF,Z=0,N=0,V=1,C=1", // ROL
		"0400; AC=5555,C=0; AC=AAAA,Z=0,N=1,V=1,C=0", // ROL
		"0400; AC=8000,C=0; AC=0000,Z=1,N=0,V=1,C=1", // ROL
		"0400; AC=0000,C=1; AC=0001,Z=0,N=0,V=0,C=0", // ROL

		"0480; AC=BEEF,C=1; AC=DF77,Z=0,N=1,V=0,C=1", // ROR
		"0480; AC=8000,C=0; AC=4000,Z=0,N=0,V=0,C=0", // ROR
		"0480; AC=0001,C=0; AC=0000,Z=1,N=0,V=1,C=1", // ROR

		"0500; AC=BEEF; DR=BEEF,AC=7DDE,N=0,Z=0,V=1,C=1", // ASL
		"0500; AC=4444; DR=4444,AC=8888,N=1,Z=0,V=1,C=0", // ASL
		"0500; AC=2222; DR=2222,AC=4444,N=0,Z=0,V=0,C=0", // ASL
		"0500; AC=FFFF; DR=FFFF,AC=FFFE,N=1,Z=0,V=0,C=1", // ASL
		"0500; AC=0000; DR=0000,AC=0000,N=0,Z=1,V=0,C=0", // ASL

		"0580; AC=BEEF; AC=DF77,N=1,Z=0,V=0", // ASR
		"0580; AC=8000; AC=C000,N=1,Z=0,V=1", // ASR
		"0580; AC=0001; AC=0000,N=0,Z=1,V=1", // ASR
		"0580; AC=4444; AC=2222,N=0,Z=0,V=0", // ASR
		"0580; AC=5555; AC=2AAA,N=0,Z=0,V=1", // ASR

		"0600; AC=8888; AC=FF88,N=1,Z=0,V=0", // SXTB
		"0600; AC=4444; AC=0044,N=0,Z=0,V=0", // SXTB
		"0600; AC=0000; AC=0000,N=0,Z=1,V=0", // SXTB

		"0640; AC=BEEF; AC=BEEF,N=1,Z=0,V=0", // TST
		"0640; AC=1313; AC=1313,N=0,Z=0,V=0", // TST
		"0640; AC=0000; AC=0000,N=0,Z=1,V=0", // TST

		"0680; AC=BEEF; AC=EFBE,N=1,Z=0,V=0", // SWAB
		"0680; AC=7654; AC=5476,N=0,Z=0,V=0", // SWAB
		"0680; AC=0000; AC=0000,N=0,Z=1,V=0", // SWAB

		"0700; AC=BEEF; AC=BEF0,N=1,Z=0,V=0,C=0", // INC
		"0700; AC=7FFF; AC=8000,N=1,Z=0,V=1,C=0", // INC
		"0700; AC=1313; AC=1314,N=0,Z=0,V=0,C=0", // INC
		"0700; AC=FFFF; AC=0000,N=0,Z=1,V=0,C=1", // INC

		"0740; AC=BEEF; AC=BEEE,N=1,Z=0,V=0,C=1", // DEC
		"0740; AC=8000; AC=7FFF,N=0,Z=0,V=1,C=1", // DEC
		"0740; AC=0000; AC=FFFF,N=1,Z=0,V=0,C=0", // DEC
		"0740; AC=0001; AC=0000,N=0,Z=1,V=0,C=1", // DEC

		"0780; AC=BEEF; AC=4111,N=0,Z=0,V=0,C=0", // NEG
		"0780; AC=1313; AC=ECED,N=1,Z=0,V=0,C=0", // NEG
		"0780; AC=0000; AC=0000,N=0,Z=1,V=0,C=1", // NEG

		"0800; SP=600,600=BEEF; DR=BEEF,AR=600,SP=601,AC=BEEF", // POP
		"0800; SP=7FF,7FF=1313; DR=1313,AR=7FF,SP=000,AC=1313", // POP

		"0900; SP=600,600=0400; DR=0400,AR=600,SP=601,EI=0,F=0,N=0,Z=0,V=0,C=0", // POPF
		"0900; SP=7FF,7FF=045F; DR=045F,AR=7FF,SP=000,EI=1,F=1,N=1,Z=1,V=1,C=1", // POPF
	};

	private final BasicComp bcomp;
	private final CPU cpu;
	private final Memory memory;
	private final EnumMap<Reg, Register> regs;

	private EnumMap<Reg, Hexadecimal> initialRegs;
	private EnumMap<State, Hexadecimal> initialFlags;
	private ArrayList<MemoryValue> initialMemory;
	private EnumMap<Reg, Hexadecimal> expectedRegs;
	private EnumMap<State, Hexadecimal> expectedFlags;
	private ArrayList<MemoryValue> expectedWrites;
	private Iterator<MemoryValue> nextWrite;

	public BasicCompTest() throws Exception {
		this.bcomp = new BasicComp();
		this.cpu = bcomp.getCPU();
		this.regs = cpu.getRegisters();
		this.memory = cpu.getMemory();

		cpu.addDestination(STOR, new DataDestination() {
			@Override
			public void setValue(long value) {
				long ar = regs.get(AR).getValue();

				assertTrue("Unexpected write to " + toHex(ar, regs.get(AR).width), nextWrite.hasNext());

				MemoryValue exp = nextWrite.next();

				assertEquals("Write at address", exp.addr, new Hexadecimal(ar));
				assertEquals("Write to " + exp.addr + " value", exp.value, new Hexadecimal(memory.getValue(ar)));
			}
		});
	}

	@Test
	public void testBComp() {
		assertNotNull(bcomp);
		assertNotNull(cpu);
		assertNotNull(regs);
	}

	@Test
	public void runTests() {
		for (String _test : TESTS) {
			String[] test = _test.replace(" ", "").split(";");

			initialRegs = new EnumMap<Reg, Hexadecimal>(Reg.class);
			initialFlags = new EnumMap<State, Hexadecimal>(State.class);
			initialMemory = new ArrayList<MemoryValue>();
			expectedRegs = new EnumMap<Reg, Hexadecimal>(Reg.class);
			expectedFlags = new EnumMap<State, Hexadecimal>(State.class);
			expectedWrites = new ArrayList<MemoryValue>();

			if (test.length > 1)
				for (String init : test[1].split(",")) {
					if (init.equals(""))
						continue;

					String[] pair = init.split("=");
					Hexadecimal value = new Hexadecimal(pair[1]);

					Reg r = findRegister(pair[0]);
					if (r != null) {
						initialRegs.put(r, value);
						continue;
					}

					State s = findState(pair[0]);
					if (s != null) {
						initialFlags.put(s, value);
						continue;
					}

					initialMemory.add(new MemoryValue(Long.parseLong(pair[0], 16), value));
				}

			if (test.length > 2)
				for (String exp : test[2].split(",")) {
					if (exp.equals(""))
						continue;

					String[] pair = exp.split("=");
					Hexadecimal value = new Hexadecimal(pair[1]);

					Reg r = findRegister(pair[0]);
					if (r != null) {
						expectedRegs.put(r, value);
						continue;
					}

					State s = findState(pair[0]);
					if (s != null) {
						expectedFlags.put(s, value);
						continue;
					}

					expectedWrites.add(new MemoryValue(Long.parseLong(pair[0], 16), value));
				}

			if (test[0].equals("START"))
				runTest(new Runnable() {
					@Override
					public void run() {
						cpu.executeStart();
					}
				});
			else if (test[0].equals("SETIP"))
				runTest(new Runnable() {
					@Override
					public void run() {
						cpu.executeSetAddr();
					}
				});
			else if (test[0].equals("WRITE"))
				runTest(new Runnable() {
					@Override
					public void run() {
						cpu.executeWrite();
					}
				});
			else if (test[0].equals("READ"))
				runTest(new Runnable() {
					@Override
					public void run() {
						cpu.executeRead();
					}
				});
			else {
				Hexadecimal cmd = new Hexadecimal(test[0]);
				initialRegs.put(IP, x100);
				initialMemory.add(new MemoryValue(0x100, cmd));
				expectedRegs.put(CR, cmd);
				if (!expectedRegs.containsKey(DR))
					expectedRegs.put(DR, cmd);
				if (!expectedRegs.containsKey(BR))
					expectedRegs.put(BR, x100);
				if (!expectedRegs.containsKey(IP))
					expectedRegs.put(IP, x101);
				if (!expectedRegs.containsKey(AR))
					expectedRegs.put(AR, x100);

				runTest(new Runnable() {
					@Override
					public void run() {
						cpu.executeContinue();
					}
				});
			}
		}
	}

	private void runTest(Runnable run) {
		for (long rvalue : TEST_REG_VALUES)
			for (long svalue : TEST_FLAG_VALUES) {
				for (Reg r : Reg.values())
					if (initialRegs.containsKey(r))
						regs.get(r).setValue(initialRegs.get(r).value);
					else if (Arrays.asList(TEST_REGISTERS).contains(r))
						regs.get(r).setValue(rvalue);
						
				for (State s : TEST_STATES)
					regs.get(PS).setValue(initialFlags.containsKey(s) ? initialFlags.get(s).value : svalue, 1, s.ordinal());

				for (MemoryValue mem : initialMemory)
					memory.setValue(mem.addr.value, mem.value.value);
				nextWrite = expectedWrites.listIterator();

				System.out.println("=== BEFORE " + Long.toHexString(regs.get(PS).getValue()));
				run.run();
				System.out.println("=== AFTER " + Long.toHexString(regs.get(PS).getValue()));

				for (Reg r : TEST_REGISTERS)
					assertEquals("Register " + r.name(),
						expectedRegs.containsKey(r) ? expectedRegs.get(r) : new Hexadecimal(rvalue & regs.get(r).mask),
						new Hexadecimal(regs.get(r).getValue()));
						
				for (State s : TEST_STATES)
					assertEquals("State " + s.name(),
						expectedFlags.containsKey(s) ? expectedFlags.get(s) : new Hexadecimal(svalue),
						new Hexadecimal(regs.get(PS).getValue(s.ordinal())));

				assertFalse("Not all writes done", nextWrite.hasNext());
			}
	}

	private Reg findRegister(String name) {
		for (Reg r : Reg.values())
			if (name.equals(r.name()))
				return r;

		return null;
	}

	private State findState(String name) {
		for (State s : TEST_STATES)
			if (name.equals(s.name()))
				return s;

		return null;
	}

	private class Hexadecimal {
		private final long value;

		public Hexadecimal(long value) {
			this.value = value;
		}

		public Hexadecimal(String value) {
			this(Long.parseLong(value, 16));
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Hexadecimal)
				return value == ((Hexadecimal) obj).value;

			return false;
		}

		@Override
		public String toString() {
			return Long.toHexString(value).toUpperCase();
		}
	}

	private class MemoryValue {
		private final Hexadecimal addr;
		private final Hexadecimal value;

		public MemoryValue(long addr, Hexadecimal value) {
			this.addr = new Hexadecimal(addr);
			this.value = value;
		}
	}
}
