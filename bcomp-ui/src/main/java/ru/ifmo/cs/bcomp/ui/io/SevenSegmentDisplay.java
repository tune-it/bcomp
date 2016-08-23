/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui.io;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ru.ifmo.cs.bcomp.IOCtrl;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class SevenSegmentDisplay extends OutputDevice {
	private static final Color LED_OFF = new Color(224, 224, 224);
	private static final Color LED_ON = new Color(0, 160, 0);
	private static final int COUNT = 8;
	private static final int SEGMENT_LENGTH = 16;
	private static final int SEGMENT_WIDTH = 2;
	private static final Dimension dims = new Dimension(
		SEGMENT_LENGTH + 4 * SEGMENT_WIDTH,
		2 * SEGMENT_LENGTH + 5 * SEGMENT_WIDTH);

	private final SSD ssd[] = new SSD[COUNT];

	private class SSD extends JComponent {
		private int value = -1;

		public SSD() {
			setMinimumSize(dims);
			setMaximumSize(dims);
			setPreferredSize(dims);
			setSize(dims);
		}

		private int pos(int length, int width) {
			return length * SEGMENT_LENGTH + width * SEGMENT_WIDTH;
		}

		@Override
		public void paintComponent(Graphics g) {
			// Верхняя
			switch(value) {
			case 1:
			case 4:
			case -1:
				g.setColor(LED_OFF);
				break;

			default:
				g.setColor(LED_ON);
			}
			g.fillRect(pos(0, 2), pos(0, 1), SEGMENT_LENGTH, SEGMENT_WIDTH);

			// Средняя
			switch(value) {
			case 0:
			case 1:
			case 7:
			case -1:
				g.setColor(LED_OFF);
				break;

			default:
				g.setColor(LED_ON);
			}
			g.fillRect(pos(0, 2), pos(1, 2), SEGMENT_LENGTH, SEGMENT_WIDTH);

			// Нижняя
			switch(value) {
			case 1:
			case 4:
			case 7:
			case -1:
				g.setColor(LED_OFF);
				break;

			default:
				g.setColor(LED_ON);
			}
			g.fillRect(pos(0, 2), pos(2, 3), SEGMENT_LENGTH, SEGMENT_WIDTH);

			// Левая верхняя
			switch(value) {
			case 1:
			case 2:
			case 3:
			case 7:
			case -1:
				g.setColor(LED_OFF);
				break;

			default:
				g.setColor(LED_ON);
			}
			g.fillRect(pos(0, 1), pos(0, 2), SEGMENT_WIDTH, SEGMENT_LENGTH);

			// Левая нижняя
			switch(value) {
			case 1:
			case 3:
			case 4:
			case 5:
			case 7:
			case 9:
			case -1:
				g.setColor(LED_OFF);
				break;

			default:
				g.setColor(LED_ON);
			}
			g.fillRect(pos(0, 1), pos(1, 3), SEGMENT_WIDTH, SEGMENT_LENGTH);

			// Правая верхняя
			switch(value) {
			case 5:
			case 6:
			case -1:
				g.setColor(LED_OFF);
				break;

			default:
				g.setColor(LED_ON);
			}
			g.fillRect(pos(1, 2), pos(0, 2), SEGMENT_WIDTH, SEGMENT_LENGTH);

			// Правая нижняя
			switch(value) {
			case 2:
			case -1:
				g.setColor(LED_OFF);
				break;

			default:
				g.setColor(LED_ON);
			}
			g.fillRect(pos(1, 2), pos(1, 3), SEGMENT_WIDTH, SEGMENT_LENGTH);
		}
			
	}

	public SevenSegmentDisplay(IOCtrl ioctrl) {
		super(ioctrl, "Семисегментный индикатор");
	}	

	protected Component getContent() {
		JPanel content = new JPanel(new BorderLayout());

		JPanel center = new JPanel(new FlowLayout());
		for (int i = COUNT; i > 0; center.add(ssd[--i] = new SSD()));
		content.add(BorderLayout.CENTER, center);

		JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
		north.add(getSleepSlider());
		north.add(getPowerChkBox());
		content.add(BorderLayout.NORTH, north);

		return content;
	}

	protected void actionPerformed(int value) {
		int pos = (value >> 4) & 0xf;
		value &= 0xf;
		ssd[pos].value = value > 9 ? -1 : value;

		ssd[pos].repaint();
	}
}
