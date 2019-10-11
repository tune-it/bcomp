/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataInverter extends DataCtrl {
	private final DataSource input;

	public DataInverter(DataSource input, DataSource ... ctrls) {
		super(input.getWidth(), ctrls);

		this.input = input;
	}

	@Override
	public void setValue(int ctrl) {
		super.setValue(isOpen(ctrl) ? ~input.getValue() : input.getValue());
	}
}
