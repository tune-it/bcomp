/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Not extends Control {
	public Not(long ctrlbit, DataDestination ... dsts) {
		super(1L, 0L, ctrlbit, dsts);
	}

	@Override
	public synchronized void setValue(long value) {
		super.setValue(isOpen(value) ? 0 : 1);
	}
}
