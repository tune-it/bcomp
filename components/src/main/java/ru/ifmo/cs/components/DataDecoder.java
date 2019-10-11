/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataDecoder extends DataHandler {
	private final int startbit;
	private final int inputmask;

	public DataDecoder(DataStorage input, int startbit, int width) {
		super(1 << width, input);

		this.startbit = startbit;
		this.inputmask = getMask(width);
	}

	@Override
	public void setValue(int value) {
		super.setValue(1 << ((value >> startbit) & inputmask));
	}
}
