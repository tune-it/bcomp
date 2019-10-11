/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataAdder extends DataCtrl {
	private final DataSource left;
	private final DataSource right;
	private final DataSource c;

	public DataAdder(DataSource left, DataSource right, DataSource c, DataSource ... ctrls) {
		super(left.getWidth() + 1, ctrls);

		this.left = left;
		this.right = right;
		this.c = c;
	}

	@Override
	public void setValue(int ctrl) {
		int c = this.c.getValue();

		super.setValue(isOpen(ctrl) ? left.getValue() & right.getValue() : left.getValue() + right.getValue() + c);
	}
}
