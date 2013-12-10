/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Graphics;
import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.Utils;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;
import ru.ifmo.cs.elements.Memory;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MemoryView extends BCompComponent {
	private Memory mem;
	private int addrBitWidth;
	private int valueBitWidth;
	private int lineX;
	private int lastPage = 0;
	// Components
	private JLabel[] addrs = new JLabel[16];
	private JLabel[] values = new JLabel[16];

	public MemoryView(Memory mem, int x, int y) {
		super(mem.name, 16);
		this.mem = mem;

		addrBitWidth = mem.getAddrWidth();
		int addrWidth = FONT_COURIER_BOLD_25_WIDTH * (1 + Utils.getHexWidth(addrBitWidth));
		valueBitWidth = mem.getWidth();
		int valueWidth = FONT_COURIER_BOLD_25_WIDTH * (1 + Utils.getHexWidth(valueBitWidth));
		lineX = 1 + addrWidth;

		setBounds(x, y, 3 + addrWidth + valueWidth);

		for (int i = 0; i < 16; i++) {
			addrs[i] = addValueLabel(COLOR_TITLE);
			addrs[i].setBounds(1, getValueY(i), addrWidth, CELL_HEIGHT);

			values[i] = addValueLabel(COLOR_VALUE);
			values[i].setBounds(lineX + 1, getValueY(i), valueWidth, CELL_HEIGHT);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawLine(lineX, CELL_HEIGHT + 2, lineX, height - 2);
	}

	private void updateValue(int offset) {
		values[offset].setText(Utils.toHex(mem.getValue(lastPage + offset), valueBitWidth));
	}

	public void updateMemory() {
		for (int i = 0; i < 16; i++) {
			addrs[i].setText(Utils.toHex(lastPage + i, addrBitWidth));
			updateValue(i);
		}
	}

	private int getPage(int addr) {
		return addr & (~0xf);
	}

	private int getPage() {
		return getPage(mem.getAddrValue());
	}

	public void updateLastAddr() {
		lastPage = getPage();
	}

	public void eventRead() {
		int addr = getPage();

		if (addr != lastPage) {
			lastPage = addr;
			updateMemory();
		}
	}

	public void eventWrite() {
		int addr = mem.getAddrValue();
		int page = getPage(addr);

		if (page != lastPage) {
			lastPage = page;
			updateMemory();
		} else
			updateValue(addr - page);
	}
}
