/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Inverter extends DataHandler {
	private int startbit;

	public Inverter(int startbit, DataSource ... inputs) {
		super(1, inputs);

		this.startbit = startbit;
	}

	public Inverter(DataSource ... inputs) {
		this(0, inputs);
	}

	@Override
	public void setValue(int value) {
		super.setValue(~(value >> startbit));
	}
}
