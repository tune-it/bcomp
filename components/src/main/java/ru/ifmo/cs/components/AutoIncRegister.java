/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class AutoIncRegister extends Register {
	public AutoIncRegister(long width) {
		super(width);
	}

	@Override
	public synchronized void setValue(long value) {
		super.setValue(value == 0 ? this.value + 1 : value);
	}

}
