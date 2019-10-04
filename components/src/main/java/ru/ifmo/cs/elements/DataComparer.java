/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataComparer extends DataHandler {
	private final DataSource input;
	private final int cmp2;

	public DataComparer(DataSource input, int cmp2, DataSource ... ctrls) {
		super(1, ctrls);

		this.input = input;
		this.cmp2 = cmp2;
	}

	@Override
	public void setValue(int ctrl) {
		if (input.getValue() == cmp2)
			super.setValue(1);
	}
}
