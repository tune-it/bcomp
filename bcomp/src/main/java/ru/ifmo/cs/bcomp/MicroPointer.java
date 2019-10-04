/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.components.Register;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MicroPointer extends Register {
	public MicroPointer(int width) {
		super(width);

		super.setValue(1);
	}

	/**
	 * Sets a new value of MicroIP.
	 *
	 * @param value New value for MicroIP. If value is zero, MicroIP increments
	 */
	@Override
	public void setValue(int value) {
		super.setValue((value & mask) == 0 ? this.value + 1 : value);
	}
}
