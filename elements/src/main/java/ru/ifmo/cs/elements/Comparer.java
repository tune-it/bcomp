/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Comparer extends DataHandler {
	private DataSource input;
	private int startbit;

	public Comparer(DataSource input, int startbit, DataSource ... ctrls) {
		super(1, ctrls);

		this.input = input;
		this.startbit = startbit;
	}

	@Override
	public void setValue(int ctrl) {
		super.setValue(((input.getValue() >> startbit) & 1) == ctrl ? 1 : 0);
	}
}
