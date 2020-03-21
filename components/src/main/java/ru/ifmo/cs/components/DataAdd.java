/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataAdd extends Control {
	private final DataSource left;
	private final DataSource right;
	private final DataSource carry;
	private final long vmask;

	public DataAdd(DataSource left, DataSource right, DataSource carry, long width, long ctrlbit, DataDestination ... dsts) {
		super(width + 2, 0, ctrlbit, dsts);

		this.left = left;
		this.right = right;
		this.carry = carry;
		vmask = calculateMask(width - 1);
	}

	@Override
	public synchronized void setValue(long value) {
		if (isOpen(value)) {
			long l = left.getValue();
			long r = right.getValue();
			long c = carry.getValue();
			super.setValue(((l + r + c) + ((((l & vmask) + (r & vmask) + c ) << 2) & (1 << (width - 1)))) & mask);
		}
	}
}
