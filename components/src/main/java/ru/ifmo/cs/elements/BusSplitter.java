/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BusSplitter extends DataWidth implements DataSource {
	private final DataSource input;
	private final int startbit;

	public BusSplitter(DataSource input, int startbit, int width) {
		super(width);

		this.input = input;
		this.startbit = startbit;
	}

	@Override
	public int getValue() {
		return (input.getValue() >> startbit) & mask;
	}
}
