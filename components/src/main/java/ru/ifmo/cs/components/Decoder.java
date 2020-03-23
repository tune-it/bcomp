/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Decoder extends Control {
	private final DataSource input;
	private final long inputstartbit;
	private final long vmask;

	public Decoder(DataSource input, long startbit, long width, long ctrlbit, DataDestination ... dsts) {
		super(1L << width, 0, ctrlbit, dsts);

		this.input = input;
		this.inputstartbit = startbit;
		this.vmask = calculateMask(width);
	}

	@Override
	public void setValue(long value) {
		if (isOpen(value))
			super.setValue(1 << ((input.getValue() >> inputstartbit) & vmask));
	}
}
