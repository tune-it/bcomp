/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public abstract class DataPart extends BasicComponent implements DataSource, DataDestination {
	protected long value = 0;

	public DataPart(long width) {
		super(width);
	}
	
	@Override
	public synchronized long getValue() {
		return value;
	}

	public abstract void setValue(long value, long mask, long startbit);
}
