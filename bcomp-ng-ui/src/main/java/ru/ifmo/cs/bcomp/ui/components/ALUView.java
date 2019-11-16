/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JLabel;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ALUView extends JComponent {
	private int xpoints[];
	private int ypoints[];

	public ALUView(int x, int y, int width, int height) {
		int half = width / 2;
		int offset = height / 3;
		int soffset = offset / 3;

		xpoints = new int[] {
			0, half - soffset, half, half + soffset, width - 1, width - 1 - offset, offset
		};
		ypoints = new int[] {
			0, 0, offset, 0, 0, height - 1, height - 1
		};

		JLabel title = new JLabel("ALU", JLabel.CENTER);
		title.setFont(FONT_COURIER_BOLD_45);
		title.setBounds(offset, offset, width - 2 * offset, height - offset);
		add(title);

		setBounds(x, y, width, height);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(COLOR_TITLE);
		g.fillPolygon(xpoints, ypoints, xpoints.length);
		g.setColor(COLOR_TEXT);
		g.drawPolygon(xpoints, ypoints, xpoints.length);

	}
}
