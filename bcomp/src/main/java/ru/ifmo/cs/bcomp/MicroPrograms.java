/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MicroPrograms {
	public static final String DEFAULT_MICROPROGRAM = BaseMicroProgram.NAME;

	private static final HashMap<String, Class> microprograms =
		new HashMap<String, Class>();

	static {
		microprograms.put(BaseMicroProgram.NAME, BaseMicroProgram.class);
		microprograms.put(OptimizedMicroProgram.NAME, OptimizedMicroProgram.class);
		microprograms.put(ExtendedMicroProgram.NAME, ExtendedMicroProgram.class);
	}

	public static MicroProgram getMicroProgram(String mptype) throws Exception {
		return (MicroProgram)microprograms.get(mptype).newInstance();
	}

	public static Set<String> getMicroProgramsList() {
		return microprograms.keySet();
	}
}
