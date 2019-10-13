/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ValveCtrlInput extends Valve {
	private final DataSource ctrl;

	public ValveCtrlInput(DataSource input, long width, long startbit, DataSource ctrl, int ctrlbit) {
		super(input, width, startbit, ctrlbit);

		this.ctrl = ctrl;
	}

	@Override
	public synchronized void setValue(long value) {
		super.setValue(ctrl.getValue());
	}
}
