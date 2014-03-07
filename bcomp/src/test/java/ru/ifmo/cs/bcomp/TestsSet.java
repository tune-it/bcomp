/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Anastasia Prasolova <a-prasolova1507@yandex.ru>
 */
public class TestsSet { 
	public String[] testStrings = {
//		"CLA; cmds = INC; check = АCCUM = 0",
//		"CLC; data = 020 = 9000, 021 = 8000; cmds = CLA, ADD 020, ADD 021; flags = C = 0",
		"CMA; data = 020 = 8000; cmds = CLA, ADD 020; regs = АCCUM = 7FFF; flags = N = 0, Z = 0",//fail
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
	
	public String[] extendedTestStrings = {
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
	
	public String[] getTestStrings(String mp){
		if(mp.equals("base") || mp.equals("optimized"))
			return testStrings;
		else
			return extendedTestStrings;
	}
}
