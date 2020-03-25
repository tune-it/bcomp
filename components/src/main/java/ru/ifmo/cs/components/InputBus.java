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
public class InputBus extends BasicComponent implements DataSource {
	private final ArrayList<DataSource> inputs = new ArrayList<DataSource>();

	public InputBus(long width) {
		super(width);
	}

	public synchronized void addInput(DataSource ... inputs) {
		this.inputs.addAll(Arrays.asList(inputs));
	}

	@Override
	public long getValue() {
		long value = 0;

		for (DataSource input : inputs)
			value |= input.getValue();

		return value & mask;
	}

}
