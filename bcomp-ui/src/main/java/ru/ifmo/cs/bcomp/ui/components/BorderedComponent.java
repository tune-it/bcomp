/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JLabel;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.COLOR_TEXT;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BorderedComponent extends JComponent {
	protected int width;
	protected final int height;

	protected BorderedComponent(int height) {
		this.height = height;
	}

	protected final JLabel addLabel(String value, Font font, Color color) {
		JLabel label = new JLabel(value, JLabel.CENTER);
		label.setFont(font);
		label.setBackground(color);
		label.setOpaque(true);
		add(label);
		return label;
	}

	protected void setBounds(int x, int y, int width) {
		setBounds(x, y, this.width = width, height);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(COLOR_TEXT);
		g.drawRect(0, 0, width - 1, height - 1);
	}
}
