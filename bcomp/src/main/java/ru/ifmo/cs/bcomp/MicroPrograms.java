/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import java.util.HashMap;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MicroPrograms {
	public static final String DEFAULT_MICROPROGRAM = "base";

	private static final HashMap<String, Class> microprograms =
		new HashMap<String, Class>();

	static {
		microprograms.put(DEFAULT_MICROPROGRAM, BaseMicroProgram.class);
		microprograms.put("optimized", OptimizedMicroProgram.class);
		microprograms.put("extended", ExtendedMicroProgram.class);
	}

	public static MicroProgram getMicroProgram(String mptype) throws Exception {
		return (MicroProgram)microprograms.get(mptype).newInstance();
	}
}
