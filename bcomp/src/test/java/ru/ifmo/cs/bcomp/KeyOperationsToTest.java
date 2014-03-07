/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Anastasia Prasolova <a-prasolova1507@yandex.ru>
 */
public class KeyOperationsToTest {

	CPU cpu;

	public KeyOperationsToTest(CPU cpu) {
		this.cpu = cpu;
	}

	void testingKeyOperations() throws Exception {
		int expectedValue;
		int realValue;

		int address = 0x0567;
		int valueOfDataReg;
		int valueOfInstrReg;
		int valueOfAddrReg;
		int valueOfAccum;
		
		valueOfDataReg = cpu.getRegValue(CPU.Reg.DATA);
		valueOfInstrReg = cpu.getRegValue(CPU.Reg.INSTR);
		valueOfAccum = cpu.getRegValue(CPU.Reg.ACCUM);
		
		cpu.setRegKey(address);
		cpu.jump(ControlUnit.LABEL_ADDR);
		cpu.start();
		
		realValue = cpu.getRegValue(CPU.Reg.IP);
		expectedValue = address;
		assertEquals(realValue, expectedValue);
		assertEquals(valueOfDataReg, cpu.getRegValue(CPU.Reg.DATA));
		assertEquals(valueOfInstrReg, cpu.getRegValue(CPU.Reg.INSTR));
		assertEquals(valueOfAccum, cpu.getRegValue(CPU.Reg.ACCUM));
		
		int value = 0xFA09;
		
		valueOfInstrReg = cpu.getRegValue(CPU.Reg.INSTR) + 1;
		valueOfAddrReg = cpu.getRegValue(CPU.Reg.ADDR);
		valueOfAccum = cpu.getRegValue(CPU.Reg.ACCUM);

		cpu.setRegKey(value);
		cpu.jump(ControlUnit.LABEL_WRITE);
		cpu.start();

		realValue = cpu.getMemoryValue(address);
		expectedValue = value;
		assertEquals(realValue, expectedValue);
		realValue = cpu.getRegValue(CPU.Reg.IP);
		expectedValue = ++address;
		assertEquals(realValue, expectedValue);
		assertEquals(value, cpu.getRegValue(CPU.Reg.DATA));
		assertEquals(valueOfInstrReg, cpu.getRegValue(CPU.Reg.INSTR));
		assertEquals(valueOfAddrReg, cpu.getRegValue(CPU.Reg.ADDR));
		assertEquals(valueOfAccum, cpu.getRegValue(CPU.Reg.ACCUM));

		cpu.setRegKey(address);
		cpu.jump(ControlUnit.LABEL_READ);
		cpu.start();

		realValue = cpu.getRegValue(CPU.Reg.DATA);
		expectedValue = value;
		assertEquals(realValue, expectedValue);
		realValue = cpu.getRegValue(CPU.Reg.IP);
		expectedValue = ++address;
		assertEquals(realValue, expectedValue);

		valueOfInstrReg = cpu.getRegValue(CPU.Reg.INSTR);
		valueOfAddrReg = cpu.getRegValue(CPU.Reg.ADDR);
		valueOfAccum = cpu.getRegValue(CPU.Reg.ACCUM);
		
		cpu.setRegKey(address);
		cpu.jump(ControlUnit.LABEL_START);
		cpu.start();

		expectedValue = 0;
		int regs[] = {StateReg.FLAG_C, StateReg.FLAG_N, StateReg.FLAG_EI};
		for (int reg : regs) {
			realValue = cpu.getStateValue(reg);
			assertEquals(realValue, expectedValue);
		}

		realValue = cpu.getRegValue(CPU.Reg.ACCUM);
		assertEquals(realValue, expectedValue);
		realValue = cpu.getRegValue(CPU.Reg.DATA);
		assertEquals(realValue, expectedValue);
		expectedValue = 1;
		realValue = cpu.getStateValue(1);
		assertEquals(realValue, expectedValue);
		assertEquals(valueOfInstrReg, cpu.getRegValue(CPU.Reg.INSTR));
		assertEquals(valueOfAddrReg, cpu.getRegValue(CPU.Reg.ADDR));
	}
	
}
