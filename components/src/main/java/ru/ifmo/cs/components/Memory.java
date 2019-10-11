/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Memory extends DataWidth implements DataSource, DataDestination {
	private int memory[];
	private DataSource addr;
	private int size;

	public Memory(String name, int width, DataSource addr) {
		super(width);

		memory = new int[size = 1 << (this.addr = addr).getWidth()];
	}

	public int getValue(int addr) {
		return memory[addr];
	}

	@Override
	public int getValue() {
		return getValue(addr.getValue());
	}

	public void setValue(int addr, int value) {
		memory[addr] = value & mask;
	}

	@Override
	public void setValue(int value) {
		setValue(addr.getValue(), value);
	}

	public int getSize() {
		return size;
	}

	public int getAddrValue() {
		return addr.getValue();
	}

	public int getAddrWidth() {
		return addr.getWidth();
	}
}
