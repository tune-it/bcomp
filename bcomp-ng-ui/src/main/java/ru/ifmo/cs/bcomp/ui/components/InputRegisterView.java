/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.event.*;

import ru.ifmo.cs.bcomp.Reg;
import ru.ifmo.cs.components.Utils;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;
import ru.ifmo.cs.components.Register;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class InputRegisterView extends RegisterView {
	private final ComponentManager cmanager;
	private final Register reg;
	private final ActiveBitView activeBitView;
	private boolean active = false;
	private int regWidth;
	private int bitno;
	private int formattedWidth;

	public InputRegisterView(ComponentManager cmgr, Register reg) {
		super(reg, COLOR_TITLE);

		this.cmanager = cmgr;
		this.reg = reg;
		activeBitView = cmanager.getActiveBit();

		bitno = (regWidth =(int) reg.width) - 1;
		formattedWidth = Utils.getBinaryWidth(regWidth);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!value.isFocusOwner())
					reqFocus();
			}
		});

		value.setFocusable(true);
		value.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				active = true;
				setActiveBit(bitno);
			}

			@Override
			public void focusLost(FocusEvent e) {
				active = false;
				setValue();
			}
		});

		value.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
					case KeyEvent.VK_BACK_SPACE:
						moveLeft();
						break;

					case KeyEvent.VK_RIGHT:
						moveRight();
						break;

					case KeyEvent.VK_UP:
						invertBit();
						break;

					case KeyEvent.VK_0:
					case KeyEvent.VK_NUMPAD0:
						setBit(0);
						break;

					case KeyEvent.VK_1:
					case KeyEvent.VK_NUMPAD1:
						setBit(1);
						break;

					default:
						cmanager.keyPressed(e);
				}
			}
		});

		value.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!value.isFocusOwner())
					reqFocus();

				int bitno = Utils.getBitNo(e.getX(), e.getX() > value.getWidth() / 2 ? formattedWidth - 1 : formattedWidth, FONT_COURIER_BOLD_21_WIDTH);

				if (bitno < 0)
					return;

				setActiveBit(bitno);

				if (e.getClickCount() > 1)
					invertBit();
			}
		});
	}

	private void setActiveBit(int bitno) {
		activeBitView.setValue(this.bitno = bitno);
		setValue();
	}

	private void moveLeft() {
		setActiveBit((bitno + 1) % regWidth);
	}

	private void moveRight() {
		setActiveBit((bitno == 0 ? regWidth : bitno) - 1);
	}

	private void invertBit() {
		reg.invertBit(bitno);
		setValue();
	}

	private void setBit(int value) {
		reg.setValue(value,1,bitno);
		moveRight();
	}

	@Override
	public void setValue() {
		if (active) {
			StringBuilder str = new StringBuilder(HTML +
				Utils.toBinary((int)reg.getValue(), regWidth) + HTML_END);

			int pos = 6 + formattedWidth - Utils.getBinaryWidth(bitno + 1);
			str.insert(pos + 1, COLOR_END);
			str.insert(pos, COLOR_ACTIVE_BIT);
			setValue(str.toString());
		} else
			super.setValue(HTML + Utils.toBinary(reg.getValue(), regWidth) + HTML_END);
	}

	public void reqFocus() {
		try {
			value.requestFocus();
		} catch (Exception e) { }
		
		value.requestFocusInWindow();
	}

	public void setActive() {
		reqFocus();
		active = true;
		setActiveBit(bitno);
	}
}
