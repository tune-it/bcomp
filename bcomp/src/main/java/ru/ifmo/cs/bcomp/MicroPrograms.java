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
	public static final String DEFAULT_MICROPROGRAM = MicroCode.NAME;

	private static final HashMap<String, Class> microprograms =
		new HashMap<String, Class>();

	static {
		microprograms.put(MicroCode.NAME, MicroCode.class);
		microprograms.put(OptimizedMicroProgram.NAME, OptimizedMicroProgram.class);
		microprograms.put(ExtendedMicroProgram.NAME, ExtendedMicroProgram.class);
	}

	public static MicroCode getMicroProgram(String mptype) throws Exception {
		return (MicroCode)microprograms.get(mptype).newInstance();
	}

	public static Set<String> getMicroProgramsList() {
		return microprograms.keySet();
	}
}
