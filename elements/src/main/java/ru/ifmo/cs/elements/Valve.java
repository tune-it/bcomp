/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Valve extends DataCtrl {
	private final DataSource input;
	private final int startbit;

	public Valve(String name, DataSource input, int startbit, int width, int ctrlbit, DataSource ... ctrls) {
		super(name, width, ctrlbit, ctrls);

		this.input = input;
		this.startbit = startbit;
	}

	public Valve(String name, DataSource input, int ctrlbit, DataSource ... ctrls) {
		this(name, input, 0, input.getWidth(), ctrlbit, ctrls);
	}

	public Valve(DataSource input, int startbit, int width, int ctrlbit, DataSource ... ctrls) {
		this(null, input, startbit, width, ctrlbit, ctrls);
	}

	public Valve(DataSource input, int ctrlbit, DataSource ... ctrls) {
		this(input, 0, input.getWidth(), ctrlbit, ctrls);
	}

	public Valve(DataSource input, DataSource ... ctrls) {
		this(input, 0, ctrls);
	}

	@Override
	public void setValue(int ctrl) {
		if (isOpen(ctrl))
			super.setValue(input.getValue() >> startbit);
	}
}
