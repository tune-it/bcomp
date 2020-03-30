/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Comparer extends Control {
	private final DataSource input;

	public Comparer(DataSource input, DataDestination ... dsts) {
		super(1, 0, 0, dsts);

		this.input = input;
	}

	@Override
	public void setValue(long value) {
			super.setValue(value == input.getValue() ? 1 : 0);
	}
}
