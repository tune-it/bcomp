/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Not extends Control {
	public Not(long ctrlbit) {
		super(1L, 0L, ctrlbit);
	}

	@Override
	public synchronized void setValue(long value) {
		super.setValue(~(value >> ctrlbit));
	}
}
