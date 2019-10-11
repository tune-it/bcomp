/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class PseudoRegister extends DataInputs implements DataDestination {
	private final Register reg;
	private final int startbit;

	public PseudoRegister(Register reg, int startbit, int width, DataSource ... inputs) {
		super(width, inputs);

		this.reg = reg;
		this.startbit = startbit;
	}

	public PseudoRegister(Register reg, int startbit, DataSource ... inputs) {
		this(reg, startbit, 1, inputs);
	}

	@Override
	public void setValue(int value) {
		reg.setValue(value, startbit, width);
	}
}
