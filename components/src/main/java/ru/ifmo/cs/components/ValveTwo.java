/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ValveTwo extends Control {
	private long ctrlbit2;

	public ValveTwo(long ctrlbit1, long ctrlbit2, DataDestination ... dsts) {
		super(1, 0, ctrlbit1, dsts);

		this.ctrlbit2 = ctrlbit2;
	}

	@Override
	public synchronized void setValue(long value) {
		if (isOpen(value))
			super.setValue((value >> ctrlbit2) & 1L);
	}
}
