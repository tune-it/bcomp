/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Xor extends Control {
	private final DataSource input;
	private final long inputwidth;
	private final long inputstartbit;

	public Xor(DataSource input, long width, long startbit, long ctrlbit, DataDestination ... dsts) {
		super(1, 0, ctrlbit, dsts);

		this.input = input;
		this.inputwidth = width;
		this.inputstartbit = startbit;
	}

	@Override
	public void setValue(long value) {
		if (isOpen(value)) {
			long src = input.getValue() >> inputstartbit;
			long res = src & 1;

			for (long i = 1; i < inputwidth; i++) {
				src >>= 1;
				res ^= src & 1;
			}

			super.setValue(res);
		}
	}
}
