/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Comparer extends Control {
	private final DataSource input1;
	private final DataSource input2;

	public Comparer(DataSource input1, DataSource input2, long ctrlbit, DataDestination ... dsts) {
		super(1, 0, ctrlbit, dsts);

		this.input1 = input1;
		this.input2 = input2;
	}

	@Override
	public void setValue(long value) {
		if (isOpen(value))
			super.setValue(input1.getValue() == input2.getValue() ? 1 : 0);
	}
}
