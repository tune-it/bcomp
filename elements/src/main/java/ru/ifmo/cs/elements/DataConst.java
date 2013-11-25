/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataConst extends DataValue {
	public DataConst(int value, int width) {
		super(Integer.toHexString(value), width);

		this.value = value & mask;
	}
}
