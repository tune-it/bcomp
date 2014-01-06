/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ExtendedInstrSet {
	public static final Instruction[] instructions = {
		new Instruction(0x0000, "NOP", Instruction.Type.NONADDR),
		new Instruction(0x0001, "HLT", Instruction.Type.NONADDR),
		new Instruction(0x0002, "EI", Instruction.Type.NONADDR),
		new Instruction(0x0003, "DI", Instruction.Type.NONADDR),
		new Instruction(0x0004, "CLC", Instruction.Type.NONADDR),
		new Instruction(0x0005, "CMC", Instruction.Type.NONADDR),
		new Instruction(0x0006, "ROL", Instruction.Type.NONADDR),
		new Instruction(0x0016, "SHL", Instruction.Type.NONADDR),
		new Instruction(0x0007, "ROR", Instruction.Type.NONADDR),
		new Instruction(0x0017, "SHR", Instruction.Type.NONADDR),
		new Instruction(0x0008, "CLA", Instruction.Type.NONADDR),
		new Instruction(0x0009, "CMA", Instruction.Type.NONADDR),
		new Instruction(0x000A, "INC", Instruction.Type.NONADDR),
		new Instruction(0x000B, "DEC", Instruction.Type.NONADDR),
		new Instruction(0x000C, "SWAP", Instruction.Type.NONADDR),
		new Instruction(0x000D, "PUSH", Instruction.Type.NONADDR),
		new Instruction(0x000E, "POP", Instruction.Type.NONADDR),
		new Instruction(0x000F, "RET", Instruction.Type.NONADDR),
		new Instruction(0x1000, "CLF", Instruction.Type.IO),
		new Instruction(0x1100, "TSF", Instruction.Type.IO),
		new Instruction(0x1200, "IN", Instruction.Type.IO),
		new Instruction(0x1300, "OUT", Instruction.Type.IO),
		new Instruction(0x2000, "CALL", Instruction.Type.ADDR),
		new Instruction(0x3000, "JMP", Instruction.Type.ADDR),
		new Instruction(0x4000, "BCS", Instruction.Type.ADDR),
		new Instruction(0x5000, "BPL", Instruction.Type.ADDR),
		new Instruction(0x6000, "BMI", Instruction.Type.ADDR),
		new Instruction(0x7000, "BEQ", Instruction.Type.ADDR),
		new Instruction(0x8000, "MOV", Instruction.Type.ADDR),
		new Instruction(0x9000, "CMP", Instruction.Type.ADDR),
		new Instruction(0xA000, "LOOP", Instruction.Type.ADDR),
		new Instruction(0xB000, "ISZ", Instruction.Type.ADDR),
		new Instruction(0xC000, "SUB", Instruction.Type.ADDR),
		new Instruction(0xD000, "ADC", Instruction.Type.ADDR),
		new Instruction(0xE000, "ADD", Instruction.Type.ADDR),
		new Instruction(0xF000, "AND", Instruction.Type.ADDR),
	};
}
