/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BaseInstrSet {
	private static final Instruction[] instrs = {
		new Instruction(0x0000, "ISZ", Instruction.Type.ADDR),
		new Instruction(0x1000, "AND", Instruction.Type.ADDR),
		new Instruction(0x2000, "JSR", Instruction.Type.ADDR),
		new Instruction(0x3000, "MOV", Instruction.Type.ADDR),
		new Instruction(0x4000, "ADD", Instruction.Type.ADDR),
		new Instruction(0x5000, "ADC", Instruction.Type.ADDR),
		new Instruction(0x6000, "SUB", Instruction.Type.ADDR),
		new Instruction(0x8000, "BCS", Instruction.Type.ADDR),
		new Instruction(0x9000, "BPL", Instruction.Type.ADDR),
		new Instruction(0xA000, "BMI", Instruction.Type.ADDR),
		new Instruction(0xB000, "BEQ", Instruction.Type.ADDR),
		new Instruction(0xC000, "BR", Instruction.Type.ADDR),
		new Instruction(0xE000, "CLF", Instruction.Type.IO),
		new Instruction(0xE100, "TSF", Instruction.Type.IO),
		new Instruction(0xE200, "IN", Instruction.Type.IO),
		new Instruction(0xE300, "OUT", Instruction.Type.IO),
		new Instruction(0xF000, "HLT", Instruction.Type.NONADDR),
		new Instruction(0xF100, "NOP", Instruction.Type.NONADDR),
		new Instruction(0xF200, "CLA", Instruction.Type.NONADDR),
		new Instruction(0xF300, "CLC", Instruction.Type.NONADDR),
		new Instruction(0xF400, "CMA", Instruction.Type.NONADDR),
		new Instruction(0xF500, "CMC", Instruction.Type.NONADDR),
		new Instruction(0xF600, "ROL", Instruction.Type.NONADDR),
		new Instruction(0xF700, "ROR", Instruction.Type.NONADDR),
		new Instruction(0xF800, "INC", Instruction.Type.NONADDR),
		new Instruction(0xF900, "DEC", Instruction.Type.NONADDR),
		new Instruction(0xFA00, "EI", Instruction.Type.NONADDR),
		new Instruction(0xFB00, "DI", Instruction.Type.NONADDR)
	};

	public static Instruction[] getInstructionSet() {
		return instrs;
	}
}
