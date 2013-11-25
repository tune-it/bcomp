/*
 * $Id$
 */

package ru.ifmo.cs.elements;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Bus extends DataWidth implements DataSource {
	private ArrayList<DataSource> inputs = new ArrayList<DataSource>();

	public Bus(DataSource ... inputs) {
		super(getMaxWidth(inputs));
		this.inputs.addAll(Arrays.asList(inputs));
	}

	public Bus(int width) {
		super(width);
	}

	private static int getMaxWidth(DataSource ... inputs) {
		int width = 0;

		for (DataSource input : inputs)
			if (width < input.getWidth())
				width = input.getWidth();

		return width;
	}

	public void addInput(DataSource ... newinputs) {
		inputs.addAll(Arrays.asList(newinputs));
	}

	@Override
	public int getValue() {
		int value = 0;

		for (DataSource input : inputs)
			value |= input.getValue();

		return value & mask;
	}
}
