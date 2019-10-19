/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataConst extends BasicComponent implements DataSource {
	private final long value;

	public DataConst(long value, long width) {
		super(width);

		this.value = value & mask;
	}

	@Override
	public long getValue() {
		return value;
	}
}
