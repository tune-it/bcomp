/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui.io;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import ru.ifmo.cs.bcomp.IOCtrl;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.FONT_COURIER_PLAIN_12;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Keyboard extends IODevice {
	private enum Lang {EN, RU};

	static final Dimension DIMS = new Dimension(45, 30);
	private static final String KEYS[][][] = new String[][][] {
		{ // First row
			{"1", "!", "1", "!"},
			{"2", "@", "2", "\""},
			{"3", "#", "3", "#"},
			{"4", "$", "4", "*"},
			{"5", "%", "5", ":"},
			{"6", "^", "6", ","},
			{"7", "&", "7", "."},
			{"8", "*", "8", ";"},
			{"9", "(", "9", "("},
			{"0", ")", "0", ")"},
			{"-", "_", "-", "_"},
			{"=", "+", "=", "+"},
			{"\\", "|", "\\", "|"},
		}, { // Second row
			{"q", "Q", "й", "Й"},
			{"w", "W", "ц", "Ц"},
			{"e", "E", "у", "У"},
			{"r", "R", "к", "К"},
			{"t", "T", "е", "Е"},
			{"y", "Y", "н", "Н"},
			{"u", "U", "г", "Г"},
			{"i", "I", "ш", "Ш"},
			{"o", "O", "щ", "Щ"},
			{"p", "P", "з", "З"},
			{"[", "{", "х", "Х"},
			{"]", "}", "ъ", "Ъ"},
		}, { // Third row
			{"a", "A", "ф", "Ф"},
			{"s", "S", "ы", "Ы"},
			{"d", "D", "в", "В"},
			{"f", "F", "а", "А"},
			{"g", "G", "п", "П"},
			{"h", "H", "р", "Р"},
			{"j", "J", "о", "О"},
			{"k", "K", "л", "Л"},
			{"l", "L", "д", "Д"},
			{";", ":", "ж", "Ж"},
			{"'", "\"", "э", "Э"},
		}, { // Fourth row
			{"z", "Z", "я", "Я"},
			{"x", "X", "ч", "Ч"},
			{"c", "C", "с", "С"},
			{"v", "V", "м", "М"},
			{"b", "B", "и", "И"},
			{"n", "N", "т", "Т"},
			{"m", "M", "ь", "Ь"},
			{",", "<", "б", "Б"},
			{".", ">", "ю", "Ю"},
			{"/", "?", "ё", "Ё"},
		}
	};

	private final ArrayList<Key> keys = new ArrayList<Key>();
	private String charset;

	private class SizedButton extends JButton {
		public SizedButton(final String title) {
			super(title);
		}

		public final void buttonSetSize(Dimension d) {
			setFont(FONT_COURIER_PLAIN_12);
			setSize(d);
			setMinimumSize(d);
			setMaximumSize(d);
			setPreferredSize(d);
		}
	}

	private class Key extends SizedButton {
		private final String values[];
		private String active;
		private Lang lang = Lang.EN;
		private boolean caps = false;

		public Key(String values[]) {
			super(values[0]);

			this.values = values;
			this.active = values[0];

			buttonSetSize(DIMS);

			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					buttonPressed(active);
				}
			});
		}

		private void setActiveLayout() {
			switch (lang) {
			case EN:
				active = caps ? values[1] : values[0];
				break;

			case RU:
				active = caps ? values[3] : values[2];
				break;
			}

			setText(active);
		}
	}

	public Keyboard(final IOCtrl ioctrl) {
		super(ioctrl, "Клавиатура");
	}

	@Override
	protected Component getContent() {
		JPanel content = new JPanel(new GridLayout(5, 1, 0, 0));

		for (String[][] row : KEYS) {
			JPanel jrow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			for (String[] key : row) {
				Key k = new Key(key);

				jrow.add(k);
				keys.add(k);
			}
			content.add(jrow);
		}

		JPanel jrow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		jrow.add(new FlagIndicator(ioctrl, 30));

		SizedButton caps = new SizedButton("Caps Lock");
		caps.buttonSetSize(new Dimension(120, 30));
		caps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Key key : keys) {
					key.caps = !key.caps;
					key.setActiveLayout();
				}
			}
		});
		jrow.add(caps);
		SizedButton space = new SizedButton(" ");
		space.buttonSetSize(new Dimension(180, 30));
		space.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					buttonPressed(" ");
				}
			});
		jrow.add(space);
		SizedButton latrus = new SizedButton("Lat/Рус");
		latrus.buttonSetSize(new Dimension(120, 30));
		latrus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Key key : keys) {
					key.lang = key.lang == Lang.EN ? Lang.RU : Lang.EN;
					key.setActiveLayout();
				}
			}
		});
		jrow.add(latrus);
		JComboBox charsetbox = new JComboBox(new String[] {charset = "KOI8-R", "ISO8859-5", "CP866", "CP1251"});
		charsetbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox source = (JComboBox) e.getSource();
				charset = (String)source.getSelectedItem();
			}
		});
		jrow.add(charsetbox);
		content.add(jrow);

		return content;
	}

	private void buttonPressed(String s) {
		try {
			int c = ((int)s.getBytes(charset)[0]) & 0xff;
			ioctrl.setData(c);
			ioctrl.setFlag();
		} catch (Exception e) { }
	}
}
