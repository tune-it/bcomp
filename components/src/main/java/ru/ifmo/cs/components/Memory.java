/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Memory extends Component implements DataSource, DataDestination {
	private final long memory[];
	private final Register ar;

	public Memory(long width, Register ar) {
		super(width);

		memory = new long[1 << ((this.ar = ar).width)];

		for (int i = 0; i < memory.length; memory[i++] = 0L);
	}

	public synchronized long getValue(int addr) {
		return memory[addr];
	}

	@Override
	public synchronized long getValue() {
		return getValue((int)ar.getValue());
	}

	public synchronized void setValue(int addr, long value) {
		memory[addr] = value & mask;
	}

	@Override
	public synchronized void setValue(long value) {
		setValue((int)ar.getValue(), value);
	}
}
