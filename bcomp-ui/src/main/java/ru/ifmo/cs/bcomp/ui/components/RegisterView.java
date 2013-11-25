/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.ui.Utils;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.CELL_HEIGHT;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.COLOR_TITLE;
import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.DataWidth;
import ru.ifmo.cs.elements.Register;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class RegisterView extends BCompComponent implements DataDestination {
	private int formatWidth;
	private int valuemask;
	private boolean hex;

	private final Register reg;
	protected final JLabel value = addValueLabel();

	public RegisterView(Register reg, Color colorTitleBG) {
		super("", colorTitleBG);

		this.reg = reg;
	}

	public RegisterView(Register reg) {
		this(reg, COLOR_TITLE);
	}

	protected void setProperties(int x, int y, boolean hex, int regWidth) {
		this.hex = hex;
		this.formatWidth = regWidth;
		this.valuemask = DataWidth.getMask(regWidth);

		setBounds(x, y, getValueWidth(regWidth, hex));
		setTitle(hex ? reg.name : reg.fullname);
		setValue();

		value.setBounds(1, getValueY(), width - 2, CELL_HEIGHT);
	}

	public void setProperties(int x, int y, boolean hex) {
		setProperties(x, y, hex, reg.getWidth());
	}

	protected int getRegWidth() {
		return reg.getWidth();
	}

	protected void setValue(String val) {
		value.setText(val);
	}

	public void setValue() {
		setValue(hex ?
			Utils.toHex(reg.getValue() & valuemask, formatWidth) :
			Utils.toBinary(reg.getValue() & valuemask, formatWidth));
	}

	@Override
	public void setValue(int value) {
		setValue();
	}
}
