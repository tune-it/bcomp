/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataAnd extends Control {
	private final DataSource left;
	private final DataSource right;

	public DataAnd(DataSource left, DataSource right, long width, long ctrlbit, DataDestination ... dsts) {
		super(width, 0, ctrlbit, dsts);

		this.left = left;
		this.right = right;
	}

	@Override
	public synchronized void setValue(long value) {
		if (isOpen(value))
			super.setValue(left.getValue() & right.getValue() & mask);
	}
}
