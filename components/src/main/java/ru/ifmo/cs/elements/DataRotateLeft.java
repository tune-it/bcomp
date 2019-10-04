/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataRotateLeft extends DataCtrl {
	private final DataSource input;
	private final DataSource c;

	public DataRotateLeft(DataSource input, DataSource c, int ctrlbit, DataSource ... ctrls) {
		super(input.getWidth() + 1, ctrlbit, ctrls);

		this.input = input;
		this.c = c;
	}

	@Override
	public void setValue(int ctrl) {
		if (isOpen(ctrl))
			super.setValue((input.getValue() << 1) | (c.getValue() & 1));
	}
}
