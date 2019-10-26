/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Memory extends BasicComponent implements DataSource, DataDestination {
	private final long memory[];
	private final Register ar;
	private volatile long lastaccessed;

	public Memory(long width, Register ar) {
		super(width);

		memory = new long[1 << ((this.ar = ar).width)];

		for (int i = 0; i < memory.length; memory[i++] = 0L);
	}

	public synchronized long getValue(long addr) {
		return memory[(int)addr];
	}

	@Override
	public synchronized long getValue() {
		return getValue(lastaccessed = ar.getValue());
	}

	public synchronized void setValue(long addr, long value) {
		memory[(int)addr] = value & mask;
	}

	@Override
	public synchronized void setValue(long value) {
		setValue(lastaccessed = ar.getValue(), value);
	}

	public long getAddrWidth() {
		return ar.width;
	}

	public long getLastAccessedAddress() {
		return lastaccessed;
	}
}
