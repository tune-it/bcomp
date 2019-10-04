/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataCheckZero extends DataCtrl {
	private final DataSource input;
	private final int inputmask;

	public DataCheckZero(DataSource input, int width, int ctrlbit, DataSource ... ctrls) {
		super(1, ctrlbit, ctrls);

		this.input = input;
		this.inputmask = getMask(width);
	}

	@Override
	public void setValue(int ctrl) {
		if (isOpen(ctrl))
			super.setValue((input.getValue() & inputmask) == 0 ? 1 : 0);
	}
}
