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
public class And extends BasicComponent implements DataSource {
	private final DataSource input1;
	private final long startbit1;
	private final DataSource input2;
	private final long startbit2;

	public And(DataSource input1, long startbit1, DataSource input2, long startbit2) {
		super(1);

		this.input1 = input1;
		this.startbit1 = startbit1;
		this.input2 = input2;
		this.startbit2 = startbit2;
	}

	@Override
	public long getValue() {
		return ((input1.getValue() >> startbit1) & (input2.getValue() >> startbit2)) & mask;
	}
}
