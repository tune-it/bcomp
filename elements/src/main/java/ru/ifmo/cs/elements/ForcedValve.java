/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ForcedValve extends DataCtrl {
	private DataSource input;

	public ForcedValve(DataSource input, int width, DataSource ... ctrls) {
		super(width, 0, ctrls);

		this.input = input;
	}

	@Override
	public void setValue(int ctrl) {
		super.setValue(isOpen(ctrl) ? input.getValue() : 0);
	}
}
