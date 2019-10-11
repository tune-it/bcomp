/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ValveOnce extends DataCtrl {
	private final DataSource input;

	public ValveOnce(DataSource input, int ctrlbit, DataSource ... ctrls) {
		super(input.getWidth(), ctrlbit, ctrls);

		this.input = input;
	}

	public ValveOnce(DataSource input, DataSource ... ctrls) {
		this(input, 0, ctrls);
	}

	@Override
	public void setValue(int ctrl) {
		if (isOpen(ctrl))
			super.setValue(input.getValue());
	}

	@Override
	public int getValue() {
		int value = this.value;

		if (value != 0)
			super.resetValue();

		return value;
	}
}
