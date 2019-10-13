/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import static ru.ifmo.cs.bcomp.CS.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class CMC extends omc {
	private final String labelto;
	private final long microcmd;

	public CMC(String label, CS[] signals, long startbit, long expected, String labelto) {
		super(label, signals);

		this.labelto = labelto;
		microcmd = (1L << TYPE.ordinal()); // !!! There also we should store startbit and expected value
	}

	public CMC(CS[] signals, long startbit, long expected, String labelto) {
		this(null, signals, startbit, expected, labelto);
	}

	@Override
	public long getMicroCommand() {
		return microcmd | super.getMicroCommand(); // !!! There also we should find address for label
	}
}
