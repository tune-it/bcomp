/*
 * $Id$
 */

package ru.ifmo.cs.components;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Control extends BasicComponent implements DataDestination {
	private final ArrayList<DataDestination> dsts = new ArrayList<DataDestination>();
	protected final long startbit;
	protected final long ctrlbit;

	public Control(long width, long startbit, long ctrlbit, DataDestination ... dsts) {
		super(width);
		this.startbit = startbit;
		this.ctrlbit = ctrlbit;

		this.dsts.addAll(Arrays.asList(dsts));
	}

	public synchronized void addDestination(DataDestination ... dsts) {
		this.dsts.addAll(Arrays.asList(dsts));
	}

	public synchronized void removeDestination(DataDestination dst) {
		int index = dsts.indexOf(dst);

		if (index >= 0)
			dsts.remove(index);
	}

	protected boolean isOpen(long value) {
		return ((value >> ctrlbit) & 1L) == 1L;
	}

	@Override
	public synchronized void setValue(long value) {
		value = (value >> startbit) & mask;

		for (DataDestination dst : dsts)
			dst.setValue(value);
	}
}
