/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Instruction {
	private int instr;
	private String mnemonics;
	private Type type;

	public enum Type {
		ADDR, NONADDR, IO
	};

	public Instruction(int instr, String mnemonics, Type type) {
		this.instr = instr;
		this.mnemonics = mnemonics;
		this.type = type;
	}

	public int getInstr() {
		return instr;
	}

	public String getMnemonics() {
		return mnemonics;
	}

	public Type getType() {
		return type;
	}
}
