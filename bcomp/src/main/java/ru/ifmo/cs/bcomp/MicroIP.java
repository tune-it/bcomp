/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.elements.Register;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MicroIP extends Register {
	public MicroIP(String name, String fullname, int width) {
		super(name, fullname, width);

		super.setValue(1);
	}

	/**
	 * Sets a new value of MicroIP.
	 *
	 * @param value New value for MicroIP. If value is zero, MicroIP incremented
	 */
	@Override
	public void setValue(int value) {
		super.setValue((value & mask) == 0 ? this.value + 1 : value);
	}
}
