/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import static ru.ifmo.cs.bcomp.ControlSignal.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ControlMicroCommand extends MicroCommand {
	private final String labelto;
	private final long microcmd;

	public ControlMicroCommand(String label, String labelto, long startbit, long expected,
			ControlSignal ... signals) {
		super(label, signals);

		this.labelto = labelto;
		microcmd = (1L << TYPE.ordinal()); // !!! There also we should store startbit and expected value
	}

	public ControlMicroCommand(String labelto, long startbit, long expected, ControlSignal ... signals) {
		this(null, labelto, startbit, expected, signals);
	}

	@Override
	public long getMicroCommand() {
		return microcmd | super.getMicroCommand(); // !!! There also we should find address for label
	}
}
