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
public class CtrlBus extends Bus implements DataDestination {
	private final ArrayList<DataDestination> dsts = new ArrayList<DataDestination>();

	public CtrlBus(long width) {
		super(width);
	}

	public synchronized void addDestination(DataDestination ... dsts) {
		this.dsts.addAll(Arrays.asList(dsts));
	}

	private void callDestinations() {
		for (DataDestination dst : dsts)
			dst.setValue(this.value);
	}

	@Override
	public synchronized void setValue(long value) {
		super.setValue(value);
		callDestinations();
	}

	@Override
	public synchronized void setValue(long value, long mask, long startbit) {
		super.setValue(value, mask, startbit);
		callDestinations();
	}
}
