/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import javax.swing.JLabel;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BCompLabel extends BorderedComponent {
	public BCompLabel(int x, int y, int width, String ... text) {
		super(text.length * CELL_HEIGHT + 2);

		setBounds(x, y, width);

		for (int i = 0; i < text.length; i++) {
			JLabel title = addLabel(text[i], FONT_COURIER_BOLD_21, COLOR_TITLE);
			title.setBounds(1, 1 + i * CELL_HEIGHT, width - 2, CELL_HEIGHT);
		}
	}
}
