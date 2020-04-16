/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;
import ru.ifmo.cs.components.Utils;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author dima
 */
public class BCompComponent extends BorderedComponent {
	protected JLabel title;

	 BCompComponent(String title, int ncells, Color color) {
		super(3 + CELL_HEIGHT * (ncells + 1));

		this.title = addLabel(title, FONT_COURIER_BOLD_18, color);
	}

	public BCompComponent(String title, Color colorTitleBG) {
		this(title, 1, colorTitleBG);
	}

	public BCompComponent(String title, int ncells) {
		this(title, ncells, COLOR_TITLE);
	}

	private final JLabel addValueLabel(String value, Color color) {
		return addLabel(value, FONT_COURIER_BOLD_21, color);
	}

	protected final JLabel addValueLabel(Color color) {
		return addValueLabel("", color);
	}

	protected  JLabel addValueLabel(String value) {
		return addValueLabel(value, COLOR_VALUE);
	}

	protected final JLabel addValueLabel() {
		return addValueLabel("", COLOR_VALUE);
	}

	@Override
	protected void setBounds(int x, int y, int width) {
		super.setBounds(x, y, width);
		title.setBounds(1, 1, width - 2, CELL_HEIGHT);
	}

	protected int getValueY(int n) {
		return 2 + CELL_HEIGHT * (n + 1);
	}



	private int getPixelWidth(int chars) {
		return 2 + FONT_COURIER_BOLD_21_WIDTH * (1 + chars);
	}

	protected int getValueWidth(int width) {
		return getPixelWidth(Utils.getHexWidth(width));
	}

	protected int getValueWidth(int width, boolean hex) {
		return hex ? getValueWidth(width) : getPixelWidth(Utils.getBinaryWidth(width));
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

	}
}
