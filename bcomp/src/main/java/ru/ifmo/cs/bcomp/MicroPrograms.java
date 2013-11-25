/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MicroPrograms {
	public static final String DEFAULT_MICROPROGRAM = "base";

	public static MicroProgram getMicroProgram(String mptype) {
		if (mptype.equals(DEFAULT_MICROPROGRAM))
			return new BaseMicroProgram();

		if (mptype.equals("optimized"))
			return new OptimizedMicroProgram();

		return null;
	}
}
