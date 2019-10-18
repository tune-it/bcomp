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
	private final long valuestartbit;

	public Comparer(DataSource input, long startbit, DataDestination ... dsts) {
		super(1, 0, 0, dsts);

		this.input = input;
		this.valuestartbit = startbit;
	}

	@Override
	public void setValue(long value) {
		System.out.println("Input = " + Long.toHexString((input.getValue() >> valuestartbit) & 1) + " Value = " + Long.toHexString(value));
	}
}
