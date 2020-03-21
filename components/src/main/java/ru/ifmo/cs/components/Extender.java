/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Extender extends Control {
	private final DataSource input;
	private final long inputstartbit;

	public Extender(DataSource input, long width, long startbit, long ctrlbit, DataDestination ... dsts) {
		super(width, 0, ctrlbit, dsts);

		this.input = input;
		this.inputstartbit = startbit;
	}

	@Override
	public synchronized void setValue(long value) {
		if (isOpen(value))
			super.setValue(((input.getValue() >> inputstartbit) & 1L) == 1L ? mask : 0);
	}
}
