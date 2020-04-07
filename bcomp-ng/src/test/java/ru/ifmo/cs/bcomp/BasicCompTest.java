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
import static ru.ifmo.cs.components.Utils.toHex;
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
	private final State[] TEST_STATES = { C, V, Z, N, EI };
	private final long[] TEST_REG_VALUES = { 0xDEAD, 0x0ADB	};
	private final long[] TEST_FLAG_VALUES = { 0, 1 };
	private final Hexadecimal x100 = new Hexadecimal(0x100);
	private final Hexadecimal x101 = new Hexadecimal(0x101);

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

		"0580; AC=BEEF; AC=DF77,N=1,Z=0,V=0,C=1", // ASR
		"0580; AC=8000; AC=C000,N=1,Z=0,V=1,C=0", // ASR
		"0580; AC=0001; AC=0000,N=0,Z=1,V=1,C=1", // ASR
		"0580; AC=4444; AC=2222,N=0,Z=0,V=0,C=0", // ASR
		"0580; AC=5555; AC=2AAA,N=0,Z=0,V=1,C=1", // ASR

		"0600; AC=8888; AC=FF88,N=1,Z=0,V=0", // SXTB
		"0600; AC=4444; AC=0044,N=0,Z=0,V=0", // SXTB
		"0600; AC=0000; AC=0000,N=0,Z=1,V=0", // SXTB

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

		"0800; SP=7FF,7FF=1313; DR=1313,AR=7FF,SP=000,AC=1313,N=0,Z=0,V=0", // POP
		"0800; SP=600,600=BEEF; DR=BEEF,AR=600,SP=601,AC=BEEF,N=1,Z=0,V=0", // POP
		"0800; SP=400,400=0000; DR=0000,AR=400,SP=401,AC=0000,N=0,Z=1,V=0", // POP

		"0900; SP=600,600=" + states(P) + "; DR=" + states(P) + ",AR=600,SP=601,EI=0,N=0,Z=0,V=0,C=0", // POPF
		"0900; SP=7FF,7FF=" + states(P, EI, N, Z, V, C) + "; DR=" + states(P, EI, N, Z, V, C) +
			",AR=7FF,SP=000,EI=1,N=1,Z=1,V=1,C=1", // POPF

		"0A00; SP=600,600=FF00; DR=FF00,IP=700,AR=600,SP=601", // RET
		"0A00; SP=7FF,7FF=0013; DR=0013,IP=013,AR=7FF,SP=000", // RET

		"0B00; SP=600,600=" + states(P) + ",601=FF00; DR=FF00,IP=700,AR=601,SP=602,EI=0,N=0,Z=0,V=0,C=0", // IRET
		"0B00; SP=7FE,7FE=" + states(P, EI, N, Z, V, C) +
			",7FF=0013; DR=0013,IP=013,AR=7FF,SP=000,EI=1,N=1,Z=1,V=1,C=1", // IRET

		"0C00; AC=BEEF,SP=000; DR=BEEF,AR=7FF,SP=7FF,7FF=BEEF", // PUSH
		"0C00; AC=1313,SP=601; DR=1313,AR=600,SP=600,600=1313", // PUSH

		"0D00; SP=000,EI=0,F=0,N=0,Z=0,V=0,C=0; DR=" + states(P) + ",AR=7FF,SP=7FF,7FF=" + states(P), // PUSHF
		"0D00; SP=601,EI=1,F=1,N=1,Z=1,V=1,C=1; DR=" + states(P, EI, N, Z, V, C) +
			",AR=600,SP=600,600=" + states(P, EI, N, Z, V, C), // PUSHF

		"0E00; SP=7FF,AC=BEEF,7FF=1313; DR=BEEF,AC=1313,BR=1313,AR=7FF,N=0,Z=0,V=0,7FF=BEEF", // SWAP
		"0E00; SP=600,AC=0603,600=C0BA; DR=0603,AC=C0BA,BR=C0BA,AR=600,N=1,Z=0,V=0,600=0603", // SWAP
		"0E00; SP=400,AC=FEED,400=0000; DR=FEED,AC=0000,BR=0000,AR=400,N=0,Z=1,V=0,400=FEED", // SWAP

		"2200; AC=BEEF,200=FEED; DR=FEED,AC=BEED,AR=200,N=1,Z=0,V=0", // AND
		"2800; AC=AAAA,101=0400,400=5555; DR=5555,AC=0000,BR=0000,AR=400,N=0,Z=1,V=0", // AND
		"2AFE; AC=0000,0FF=0600,600=FFFF; DR=FFFF,AC=0000,BR=FFFE,AR=600,N=0,Z=1,V=0,0FF=0601", // AND
		"2B80; AC=FFFF,081=0011,010=0000; DR=0000,AC=0000,BR=FF80,AR=010,N=0,Z=1,V=0,081=0010", // AND
		"2C01; SP=700,AC=5555,701=6666; DR=6666,AC=4444,BR=0001,AR=701,N=0,Z=0,V=0", // AND
		"2E7F; AC=1313,180=3131; DR=3131,AC=1111,BR=007F,AR=180,N=0,Z=0,V=0", // AND
		"2F7F; AC=FFFF; DR=007F,AC=007F,BR=007F,N=0,Z=0,V=0", // AND
		"2F80; AC=FFFF; DR=FF80,AC=FF80,BR=FF80,N=1,Z=0,V=0", // AND

		"3F00; AC=0000; DR=0000,AC=0000,BR=FFFF,N=0,Z=1,V=0", // OR
		"3200; AC=5555,200=AAAA; DR=AAAA,AC=FFFF,BR=0000,AR=200,N=1,Z=0,V=0", // OR
		"3E00; AC=6666,101=5555; DR=5555,AC=7777,BR=8888,AR=101,N=0,Z=0,V=0", // OR

		"4F80; AC=0080; DR=FF80,AC=0000,BR=FF80,N=0,Z=1,V=0,C=1", // ADD
		"4200; AC=FFFF,200=FFFF; DR=FFFF,AC=FFFE,AR=200,N=1,Z=0,V=0,C=1", // ADD
		"4F13; AC=1300; DR=0013,AC=1313,BR=0013,N=0,Z=0,V=0,C=0", // ADD
		"4200; AC=6666,200=5555; DR=5555,AC=BBBB,AR=200,N=1,Z=0,V=1,C=0", // ADD
		"4FF0; AC=8000; DR=FFF0,AC=7FF0,BR=FFF0,N=0,Z=0,V=1,C=1", // ADD

		"5F80; AC=0080,C=0; DR=FF80,AC=0000,BR=FF80,N=0,Z=1,V=0,C=1", // ADC
		"5200; AC=FFFF,C=1,200=FFF0; DR=FFF0,AC=FFF0,AR=200,N=1,Z=0,V=0,C=1", // ADC
		"5F13; AC=1300,C=0; DR=0013,AC=1313,BR=0013,N=0,Z=0,V=0,C=0", // ADC
		"5200; AC=6666,C=1,200=5555; DR=5555,AC=BBBC,AR=200,N=1,Z=0,V=1,C=0", // ADC
		"5FF0; AC=8000,C=0; DR=FFF0,AC=7FF0,BR=FFF0,N=0,Z=0,V=1,C=1", // ADC

		"6F00; AC=0000; DR=0000,AC=0000,BR=0000,N=0,Z=1,V=0,C=1", // SUB
		"6200; AC=8000,200=4000; DR=4000,AC=4000,AR=200,N=0,Z=0,V=1,C=1", // SUB
		"6E00; AC=C000,101=E800; DR=E800,AC=D800,BR=0000,AR=101,N=1,Z=0,V=0,C=0", // SUB
		"6FF0; AC=7FFF; DR=FFF0,AC=800F,BR=FFF0,N=1,Z=0,V=1,C=0", // SUB

		"7F00; AC=0000; DR=0000,BR=0000,N=0,Z=1,V=0,C=1", // TST
		"7200; AC=8000,200=4000; DR=4000,AR=200,N=0,Z=0,V=1,C=1", // TST
		"7E00; AC=C000,101=E800; DR=E800,BR=0000,AR=101,N=1,Z=0,V=0,C=0", // TST
		"7FF0; AC=7FFF; DR=FFF0,BR=FFF0,N=1,Z=0,V=1,C=0", // TST

		"8200; 200=0000; AR=200,DR=FFFF,BR=FFFE,200=FFFF,IP=102", // LOOP
		"8E00; 101=FFFF; AR=101,DR=FFFE,BR=FFFD,101=FFFE,IP=102", // LOOP
		"8C08; SP=400,408=0BEC; AR=408,DR=0BEB,BR=0BEA,IP=101,408=0BEB", // LOOP

		"AF80; ; DR=FF80,AC=FF80,BR=FF80,N=1,Z=0,V=0", // LD
		"AE00; 101=7777; DR=7777,AC=7777,BR=0000,AR=101,N=0,Z=0,V=0", // LD
		"A200; 200=0000; DR=0000,AC=0000,AR=200,N=0,Z=1,V=0", // LD

		"B200; AC=BEEF,200=1313; DR=BEEF,AC=1313,BR=1313,AR=200,N=0,Z=0,V=0,200=BEEF", // SWAM
		"BE00; AC=4AD0,101=B0DA; DR=4AD0,AC=B0DA,BR=B0DA,AR=101,N=1,Z=0,V=0,101=4AD0", // SWAM
		"BC00; AC=6666,SP=600,600=0000; DR=6666,AC=0000,BR=0000,AR=600,N=0,Z=1,V=0,600=6666", // SWAM

		"C200; ; IP=200", // JUMP
		"CE80; ; DR=0081,IP=081,BR=FF80", // JUMP
		"C800; 101=F00F; DR=F00F,IP=00F,BR=0000,AR=101", // JUMP

		"D200; SP=000; DR=0101,IP=200,SP=7FF,BR=D200,AR=7FF,7FF=0101", // CALL
		"DE80; SP=601; DR=0101,IP=081,SP=600,BR=0081,AR=600,600=0101", // CALL
		"D800; SP=401,101=F00F; DR=0101,IP=00F,SP=400,BR=F00F,AR=400,400=0101", // CALL

		"E200; AC=BEEF; DR=BEEF,AR=200,200=BEEF", // ST
		"EE80; AC=1313; DR=1313,AR=081,BR=FF80,081=1313", // ST
		"E800; AC=0000,101=F00F; DR=0000,BR=0000,AR=00F,00F=0000", // ST
		"EC08; AC=0BEC,SP=400; DR=0BEC,BR=0008,AR=408,408=0BEC", // ST

		"F03F; Z=0;", // BEQ
		"F03F; Z=1; IP=140,BR=003F", // BEQ

		"F1EF; Z=0; IP=0F0,BR=FFEF", // BNE
		"F1EF; Z=1;", // BNE

		"F23F; N=0;", // BMI
		"F23F; N=1; IP=140,BR=003F", // BMI

		"F3EF; N=0; IP=0F0,BR=FFEF", // BPL
		"F3EF; N=1;", // BPL

		"F43F; C=0;", // BCS
		"F43F; C=1; IP=140,BR=003F", // BCS

		"F5EF; C=0; IP=0F0,BR=FFEF", // BCC
		"F5EF; C=1;", // BCC

		"F63F; V=0;", // BVS
		"F63F; V=1; IP=140,BR=003F", // BVS

		"F7EF; V=0; IP=0F0,BR=FFEF", // BVC
		"F7EF; V=1;", // BVC

		"F83F; N=0,V=0;", // BLT
		"F83F; N=0,V=1; IP=140,BR=003F", // BLT
		"F8EF; N=1,V=0; IP=0F0,BR=FFEF", // BLT
		"F8EF; N=1,V=1;", // BLT

		"F93F; N=0,V=1;", // BGE
		"F93F; N=0,V=0; IP=140,BR=003F", // BGE
		"F9EF; N=1,V=1; IP=0F0,BR=FFEF", // BGE
		"F9EF; N=1,V=0;", // BGE

		"1000; EI=0", // DI
		"1100; EI=1", // EI
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

	private static String states(State ... states) {
		long value = 0L;

		for (State state : states)
			value |= 1L << state.ordinal();

		return toHex(value, P.ordinal() + 1);
	}

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
						expectedRegs.put(r, value);
						continue;
					}

					State s = findState(pair[0]);
					if (s != null) {
						initialFlags.put(s, value);
						expectedFlags.put(s, value);
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

				run.run();

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
