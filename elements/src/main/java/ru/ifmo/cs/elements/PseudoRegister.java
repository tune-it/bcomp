/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class PseudoRegister extends DataInputs implements DataDestination {
	private Register reg;
	private int startbit;

	public PseudoRegister(String name, Register reg, int startbit, int width, DataSource ... inputs) {
		super(name, width, inputs);

		this.reg = reg;
		this.startbit = startbit;
	}

	public PseudoRegister(String name, Register reg, int startbit, DataSource ... inputs) {
		this(name, reg, startbit, 1, inputs);
	}

	public PseudoRegister(Register reg, int startbit, int width, DataSource ... inputs) {
		this(null, reg, startbit, width, inputs);
	}

	public PseudoRegister(Register reg, int startbit, DataSource ... inputs) {
		this(reg, startbit, 1, inputs);
	}

	@Override
	public void setValue(int value) {
		reg.setValue(value, startbit, width);
	}
}
