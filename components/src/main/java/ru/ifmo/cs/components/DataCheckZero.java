/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataCheckZero extends Control {
	private final DataSource input;
	private final long inputmask;

	public DataCheckZero(DataSource input, long width, long ctrlbit, DataDestination ... dsts) {
		super(1, 0, ctrlbit, dsts);

		this.input = input;
		this.inputmask = calculateMask(width);
	}

	@Override
	public void setValue(long value) {
		if (isOpen(value))
			super.setValue((input.getValue() & inputmask) == 0 ? 1 : 0);
	}
}
