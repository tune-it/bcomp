/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ValveValue extends Control implements DataSource {
	private volatile long value = 0;

	public ValveValue(long ctrlbit) {
		super(1L, 0L, ctrlbit);
	}

	@Override
	public synchronized void setValue(long value) {
		this.value = isOpen(value) ? 1 : 0;
	}

	@Override
	public synchronized long getValue() {
		return value;
	}
}
