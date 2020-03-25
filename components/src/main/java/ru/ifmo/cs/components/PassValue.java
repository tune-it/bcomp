/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class PassValue extends Control {
	public PassValue(long width, long startbit, DataDestination ... dsts) {
		super(width, startbit, 0, dsts);
	}

	@Override
	public synchronized void setValue(long value) {
		super.setValue(value);
	}
}
