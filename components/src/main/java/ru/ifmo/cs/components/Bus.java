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
	}

	public synchronized void setValue(long value, long mask, long startbit) {
		this.value |= ((value & mask) << startbit) & this.mask;
	}

	public synchronized void resetValue() {
		value = 0L;
	}
}
