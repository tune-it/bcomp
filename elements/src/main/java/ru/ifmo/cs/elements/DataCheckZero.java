/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataCheckZero extends DataCtrl {
	private DataSource input;
	private int inputmask;

	public DataCheckZero(String name, DataSource input, int width, int ctrlbit, DataSource ... ctrls) {
		super(name, 1, ctrlbit, ctrls);

		this.input = input;
		this.inputmask = getMask(width);
	}

	@Override
	public void setValue(int ctrl) {
		if (isOpen(ctrl))
			super.setValue((input.getValue() & inputmask) == 0 ? 1 : 0);
	}
}
