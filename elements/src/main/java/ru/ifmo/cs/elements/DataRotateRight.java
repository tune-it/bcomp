/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataRotateRight extends DataCtrl {
	private DataSource input;
	private DataSource c;

	public DataRotateRight(String name, DataSource input, DataSource c, int ctrlbit, DataSource ... ctrls) {
		super(name, input.getWidth() + 1, ctrlbit, ctrls);

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
