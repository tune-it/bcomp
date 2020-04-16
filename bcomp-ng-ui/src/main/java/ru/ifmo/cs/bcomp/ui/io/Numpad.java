/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui.io;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.components.DataDestination;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Numpad extends IODevice {
	private class NumButton extends JButton {
		private final int value;

		public NumButton(final String title, final int value) {
			super(title);

			this.value = value;

			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					buttonPressed(value);
				}
			});
		}
	}

	private final NumButton[] buttons = new NumButton[16];

	public Numpad(final IOCtrl ioctrl) {
		super(ioctrl, "numpad");
	}

	@Override
	protected Component getContent() {
		JPanel content = new JPanel(new GridLayout(4, 4, 0, 0));
		content.add(buttons[0] = new NumButton("7", 0x7));
		content.add(buttons[1] = new NumButton("8", 0x8));
		content.add(buttons[2] = new NumButton("9", 0x9));
		content.add(buttons[3] = new NumButton("/", 0xc));
		content.add(buttons[4] = new NumButton("4", 0x4));
		content.add(buttons[5] = new NumButton("5", 0x5));
		content.add(buttons[6] = new NumButton("6", 0x6));
		content.add(buttons[7] = new NumButton("*", 0xd));
		content.add(buttons[8] = new NumButton("1", 0x1));
		content.add(buttons[9] = new NumButton("2", 0x2));
		content.add(buttons[10] = new NumButton("3", 0x3));
		content.add(buttons[11] = new NumButton("-", 0xa));
		content.add(buttons[12] = new NumButton("0", 0x0));
		content.add(buttons[13] = new NumButton(".", 0xe));
		content.add(buttons[14] = new NumButton("=", 0xf));
		content.add(buttons[15] = new NumButton("+", 0xb));

		ioctrl.addDestination(ioctrl.getRegisters()[1], new DataDestination() {
			@Override
			public void setValue(long value) {
				for (NumButton button : buttons)
					button.setForeground(value == 0 ? Color.black : Color.red);
			}
		});

		return content;
	}

	private void buttonPressed(int value) {
		try {
			ioctrl.setData(value);
			ioctrl.setReady();
		} catch (Exception e) { }
	}
}
