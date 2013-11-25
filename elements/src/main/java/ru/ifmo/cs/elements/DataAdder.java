/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataAdder extends DataCtrl {
	private DataSource left;
	private DataSource right;
	private DataSource c;

	public DataAdder(String name, DataSource left, DataSource right, DataSource c, DataSource ... ctrls) {
		super(name, left.getWidth() + 1, ctrls);

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
