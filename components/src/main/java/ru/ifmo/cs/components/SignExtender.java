/*
 * $Id$
 */

package ru.ifmo.cs.components;

import static ru.ifmo.cs.components.BasicComponent.calculateMask;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class SignExtender extends Control {
	private final DataSource input;
	private final long iwidth;
	private final long imask;
	private final long sign;

	public SignExtender(DataSource input, long width, long ctrlbit, DataDestination ... dsts) {
		super(width << 1, 0, ctrlbit, dsts);

		this.input = input;
		imask = calculateMask(iwidth = width);
		sign = 1 << (width - 1);
	}

	@Override
	public synchronized void setValue(long value) {
		if (isOpen(value)) {
			value = input.getValue();
			super.setValue((value & imask) + ((value & sign) == sign ? (~0 << iwidth) : 0));
		}
	}
}
