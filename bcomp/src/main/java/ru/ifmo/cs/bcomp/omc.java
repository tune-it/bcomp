/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class omc {
	public final String label;
	private final long microcmd;
	private final CS[] signals;

	public omc(String label, CS[] signals) {
		long microcmd = 0L;

		this.label = label;

		for (CS cs : (this.signals = signals)) {
			microcmd |= 1L << cs.ordinal();
		}

		this.microcmd = microcmd;
	}

	public omc(CS[] signals) {
		this(null, signals);
	}

	public long getMicroCommand() {
		return microcmd;
	}
}
