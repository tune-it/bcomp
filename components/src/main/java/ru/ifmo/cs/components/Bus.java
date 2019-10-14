/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Bus extends DataPart  {
	public Bus(long width) {
		super(width);
	}

	@Override
	public synchronized void setValue(long value) {
		this.value |= value & mask;
		System.out.println("New value for " + this + " is " + Long.toHexString(this.value));
	}

	public synchronized void setValue(long value, long startbit, long mask) {
		this.value |= ((value & mask) << startbit) & this.mask;
	}

	public synchronized void resetValue() {
		value = 0L;
	}
}
