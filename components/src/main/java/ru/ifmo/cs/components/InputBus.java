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
	private final long startbit;

	public InputBus(long width, long startbit, DataSource ... inputs) {
		super(width);

		this.startbit = startbit;
		addInput(inputs);
	}

	public InputBus(long width, DataSource ... inputs) {
		this(width, 0, inputs);
	}

	public final synchronized void addInput(DataSource ... inputs) {
		this.inputs.addAll(Arrays.asList(inputs));
	}

	@Override
	public long getValue() {
		long value = 0;

		for (DataSource input : inputs)
			value |= input.getValue() >> startbit;

		return value & mask;
	}

}
