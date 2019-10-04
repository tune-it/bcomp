/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataRotateRight extends DataCtrl {
	private final DataSource input;
	private final DataSource c;

	public DataRotateRight(DataSource input, DataSource c, int ctrlbit, DataSource ... ctrls) {
		super(input.getWidth() + 1, ctrlbit, ctrls);

		this.input = input;
		this.c = c;
	}

	@Override
	public void setValue(int ctrl) {
		if (isOpen(ctrl)) {
			int i = input.getValue();
			super.setValue(((i & 1) << 16) | ((c.getValue() & 1) << 15) | (i >> 1));
		}
	}
}
