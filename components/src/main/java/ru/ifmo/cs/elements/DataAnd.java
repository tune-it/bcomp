/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataAnd extends DataHandler {
	private final DataSource input1;
	private final int startbit1;
	private final DataSource input2;

	public DataAnd(DataSource input1, int startbit1, DataSource input2, DataSource ... ctrls) {
		super(1, ctrls);

		this.input1 = input1;
		this.startbit1 = startbit1;
		this.input2 = input2;
	}

	@Override
	public void setValue(int ctrl) {
		if (((input1.getValue() >> startbit1) & 1) == 1)
			super.setValue(input2.getValue());
		else
			super.setValue(0);
	}
}
