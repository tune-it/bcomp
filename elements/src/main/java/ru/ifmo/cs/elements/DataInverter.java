/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataInverter extends DataCtrl {
	private DataSource input;

	public DataInverter(String name, DataSource input, DataSource ... ctrls) {
		super(name, input.getWidth(), ctrls);

		this.input = input;
	}

	@Override
	public void setValue(int ctrl) {
		super.setValue(isOpen(ctrl) ? ~input.getValue() : input.getValue());
	}
}
