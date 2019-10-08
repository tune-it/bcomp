/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MicroCommand {
	public final String label;
	private final long microcmd;

	public MicroCommand(String label, ControlSignal ... signals) {
		long microcmd = 0L;

		this.label = label;

		for (ControlSignal cs : signals) {
			microcmd |= 1L << cs.ordinal();
		}

		this.microcmd = microcmd;
	}

	public MicroCommand(ControlSignal ... signals) {
		this(null, signals);
	}

	public long getMicroCommand() {
		return microcmd;
	}
}
