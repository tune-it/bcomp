/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.ui.Utils;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.CELL_HEIGHT;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.COLOR_INPUT_TITLE;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ActiveBitView extends BCompComponent {
	private final JLabel value = addValueLabel();

	public ActiveBitView(int x, int y) {
		super("Бит", COLOR_INPUT_TITLE);

		setBounds(x, y, getValueWidth(8, true));
		value.setBounds(1, getValueY(), width - 2, CELL_HEIGHT);
	}

	public void setValue(int value) {
		this.value.setText(Utils.toHex(value, 1));
	}
}
