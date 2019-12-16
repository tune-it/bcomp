/*
 * $Id: BusView.java 501 2019-10-11 08:23:26Z MATPOCKuH@mini $
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.*;


import ru.ifmo.cs.bcomp.ControlSignal;

import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.BUS_WIDTH;
import static java.lang.Math.*;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.COLOR_BACKGROUND;


/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BusView {
	private final ControlSignal[] signals;
	private int xs[];
	private int ys[];
	private int widths[];
	private int heights[];
	private int[] arrowX = new int[3];
	private int[] arrowY = new int[3];

	private boolean isVisible = true;

	public BusView( ControlSignal ... signals) {
		this.signals = signals;

	}

	public void calcBounds(int[][] points) {
		int npoints = points.length - 1;
		int x1, x2 = 0, y1, y2 = 0, width = 0, height = 0;

		xs = new int[npoints];
		ys = new int[npoints];
		widths = new int[npoints];
		heights = new int[npoints];

		for (int i = 0; i < npoints; i++) {
			x1 = points[i][0];
			x2 = points[i + 1][0];
			y1 = points[i][1];
			y2 = points[i + 1][1];
			width = x1 - x2;

			if (width != 0) {
				xs[i] = (width < 0) ? x1 - BUS_WIDTH : x2 - BUS_WIDTH;
				widths[i] = abs(width) + 2 * BUS_WIDTH + 1;
				ys[i] = y1 - BUS_WIDTH;
				heights[i] = 2 * BUS_WIDTH + 1;
			} else {
				height = y1 - y2;

				xs[i] = x1 - BUS_WIDTH;
				widths[i] = 2 * BUS_WIDTH + 1;
				ys[i] = (height < 0) ? y1 - BUS_WIDTH : y2 - BUS_WIDTH;
				heights[i] = abs(height) + 2 * BUS_WIDTH + 1;
			}
		}

		if (width != 0) {
			arrowY[0] = y2;
			arrowY[1] = y2 - 2 * BUS_WIDTH;
			arrowY[2] = y2 + 2 * BUS_WIDTH;

			if (width > 0) {
				arrowX[0] = x2 - 3 * BUS_WIDTH - 1;
				arrowX[1] = arrowX[2] = x2 - BUS_WIDTH - 1;
			} else {
				arrowX[0] = x2 + 3 * BUS_WIDTH + 1;
				arrowX[1] = arrowX[2] = x2 + BUS_WIDTH + 1;
			}
		} else {
			arrowX[0] = x2;
			arrowX[1] = x2 - 2 * BUS_WIDTH;
			arrowX[2] = x2 + 2 * BUS_WIDTH;

			if (height > 0) {
				arrowY[0] = y2 - 3 * BUS_WIDTH - 1;
				arrowY[1] = arrowY[2] = y2 - BUS_WIDTH - 1;
			} else {
				arrowY[0] = y2 + 3 * BUS_WIDTH + 1;
				arrowY[1] = arrowY[2] = y2 + BUS_WIDTH + 1;
			}
		}
	}

	public void draw(Graphics g, Color color) {
		if (isVisible) {
			g.setColor(color);
		} else {
			g.setColor(COLOR_BACKGROUND);
		}
		g.drawPolygon(arrowX, arrowY, arrowX.length);
		g.fillPolygon(arrowX, arrowY, arrowX.length);

		for (int i = 0; i < xs.length; i++)
			g.fillRect(xs[i], ys[i], widths[i], heights[i]);
	}



	public ControlSignal[] getSignals() {
		return signals;
	}
}