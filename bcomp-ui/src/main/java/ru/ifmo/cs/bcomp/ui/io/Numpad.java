/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui.io;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import ru.ifmo.cs.bcomp.IOCtrl;

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
				public void actionPerformed(ActionEvent e) {
					buttonPressed(value);
				}
			});
		}
	}

	public Numpad(final IOCtrl ioctrl) {
		super(ioctrl, "Цифровая клавиатура");
	}

	@Override
	protected Component getContent() {
		JPanel content = new JPanel(new GridLayout(4, 4, 0, 0));
		content.add(new NumButton("7", 0x7));
		content.add(new NumButton("8", 0x8));
		content.add(new NumButton("9", 0x9));
		content.add(new NumButton("/", 0xc));
		content.add(new NumButton("4", 0x4));
		content.add(new NumButton("5", 0x5));
		content.add(new NumButton("6", 0x6));
		content.add(new NumButton("*", 0xd));
		content.add(new NumButton("1", 0x1));
		content.add(new NumButton("2", 0x2));
		content.add(new NumButton("3", 0x3));
		content.add(new NumButton("-", 0xa));
		content.add(new NumButton("0", 0x0));
		content.add(new NumButton(".", 0xe));
		content.add(new NumButton("=", 0xf));
		content.add(new NumButton("+", 0xb));

		return content;
	}

	private void buttonPressed(int value) {
		try {
			ioctrl.setData(value);
			ioctrl.setFlag();
		} catch (Exception e) { }
	}
}
