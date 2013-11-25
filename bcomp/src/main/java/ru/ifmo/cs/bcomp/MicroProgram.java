/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public interface MicroProgram {
	public String[][] getMicroProgram();
	public String getMicroProgramName();
	public Instruction[] getInstructionSet();
}
