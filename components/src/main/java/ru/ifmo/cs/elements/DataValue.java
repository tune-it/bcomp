/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataValue extends DataInputs implements DataSource {
	protected volatile int value = 0;

	public DataValue(int width, DataSource ... inputs) {
		super(width, inputs);
	}

	@Override
	public int getValue() {
		//System.out.println("Read: " + this.getClass() + ": " + Integer.toString(this.value, 16));
		return value;
	}
}
