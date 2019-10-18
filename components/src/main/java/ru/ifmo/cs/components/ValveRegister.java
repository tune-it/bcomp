/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ValveRegister extends Control implements DataSource {
	private final DataSource input;
	private volatile long value = 0;

	public ValveRegister(DataSource input, long width, long startbit, long ctrlbit, DataDestination ... dsts) {
		super(width, startbit, ctrlbit, dsts);

		this.input = input;
	}

	@Override
	public synchronized void setValue(long value) {
		if (isOpen(value)) {
			this.value = ((value = input.getValue()) >> startbit) & mask;
			super.setValue(value);
		}
	}

	@Override
	public synchronized long getValue() {
		return value;
	}
}
