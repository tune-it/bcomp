/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ValveOnce extends DataCtrl {
	private DataSource input;

	public ValveOnce(String name, DataSource input, int ctrlbit, DataSource ... ctrls) {
		super(name, input.getWidth(), ctrlbit, ctrls);

		this.input = input;
	}

	public ValveOnce(String name, DataSource input, DataSource ... ctrls) {
		this(name, input, 0, ctrls);
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
