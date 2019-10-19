/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class PartWriter extends BasicComponent implements DataDestination {
	private final DataPart dst;
	private final long startbit;

	public PartWriter(DataPart dst, long width, long startbit) {
		super(width);

		this.dst = dst;
		this.startbit = startbit;
	}

	@Override
	public synchronized void setValue(long value) {
		dst.setValue(value, mask, startbit);
	}
}
