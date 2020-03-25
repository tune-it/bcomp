/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ValveAnd extends Control {
	private final DataSource input1;
	private final long startbit1;
	private final DataSource input2;

	public ValveAnd(DataSource input1, long startbit1, DataSource input2, DataDestination ... dsts) {
		super(1, 0, 0, dsts);

		this.input1 = input1;
		this.startbit1 = startbit1;
		this.input2 = input2;
	}

	@Override
	public synchronized void setValue(long value) {
		super.setValue((input1.getValue() >> startbit1) & input2.getValue());
	}
}
