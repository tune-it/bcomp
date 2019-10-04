/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Register extends DataStorage {
	public Register(int width, DataSource ... inputs) {
		super(width, inputs);
	}
	
	public int getValue(int startbit) {
		return (value >> startbit) & 1;
	}

	public void setValue(int value, int startbit, int width) {
		int valuemask = getMask(width);

		setValue((this.value & (~(valuemask << startbit))) | ((value & valuemask) << startbit));
	}

	public void setValue(int value, int startbit) {
		setValue((this.value & (~(1 << startbit))) | ((value & 1) << startbit));
	}

	public void invertBit(int startbit) {
		int bitpos = 1 << startbit;

		value = (value & ~bitpos) | (~(value & bitpos) & bitpos);
	}
}
