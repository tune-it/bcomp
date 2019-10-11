/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataCtrl extends DataHandler {
	private int ctrlbit;

	public DataCtrl(int width, int ctrlbit, DataSource ... ctrls) {
		super(width, ctrls);

		this.ctrlbit = ctrlbit;
	}

	public DataCtrl(int width, DataSource ... ctrls) {
		this(width, 0, ctrls);
	}

	public final boolean isOpen(int ctrl) {
		return ((ctrl >> ctrlbit) & 1) == 1;
	}
}
