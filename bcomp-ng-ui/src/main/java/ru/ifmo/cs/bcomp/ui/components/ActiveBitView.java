/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import javax.swing.JLabel;
import ru.ifmo.cs.components.Utils;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.CELL_HEIGHT;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.COLOR_INPUT_TITLE;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ActiveBitView extends BCompComponent {
	private final JLabel value = addValueLabel();

	public ActiveBitView(int x, int y) {
		super("",0, COLOR_INPUT_TITLE);

		setBounds(x, y, getValueWidth(2, true));
		value.setBounds(1, 1, width - 2, CELL_HEIGHT);
	}
	protected void setBounds(int x, int y, int wight){
		setBounds(x,y,this.width=wight,height);
	}

	public void setValue(int value) {
		this.value.setText(Utils.toHex(value, 1));
	}
}
