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
//	private int xTop[];
//	private int yTop[];
//	private int xLeft[];
//	private int xRight[];
//	private int yLeft[];
//	private int yRight[];
//	private int xBot[];
//	private int yBot[];
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
//		xpoints = new int[] {
//				soffset*2,
//				offset*3/2,
//				offset*5/3,
//				half,
//				width-1-soffset*5,
//				width-1-offset*3/2,
//				width - 1-soffset*2,//верх
//				width-1 - offset,
//				width-1 - soffset*5,
//				width-1 -15*soffset/2,
//				width-1 - soffset*11/2,
//				width-1 - soffset*11/2,
//				width-1-15*soffset/2,
//				width-1-8*soffset,
//				8*soffset,//право
//				15*soffset/2,
//				soffset*11/2,
//				11*soffset/2,
//				15*soffset/2,
//				soffset*5,
//				offset//лево
//		};
//		ypoints = new int[] {
//				0,soffset/3, 7*soffset/3, soffset*4, soffset*7/3,soffset/3, 0,//верх
//				height - 1-5*soffset,//право
//				height-1-soffset*4,
//				height-1-soffset*4,
//				height-1-3*soffset,
//				height-1-soffset,
//				height-1,
//				height-1-3*soffset/2,
//				height-1-3*soffset/2,//лево
//				height-1,
//				height-1-soffset,
//				height-1-3*soffset,
//				height - 1-soffset*4,
//				height - 1-soffset*4,
//				height-1-soffset*5
//							};
//
//		xTop=new int[]{
//			xpoints[1]+soffset/3,
//				xpoints[2]+soffset/5,
//				xpoints[3],
//				xpoints[4]-soffset/5,
//				xpoints[5]-soffset/3,
//				soffset*13, soffset*10, soffset*8, soffset*5
//						};
//		yTop=new int[]{
//				ypoints[1], ypoints[2]-soffset/5, ypoints[3]-soffset/5, ypoints[4]-soffset/5,ypoints[5],
//				soffset/3,0,0,soffset/3
//			};
//		xLeft=new int[]{
//				offset+soffset/3,
//				5*soffset-soffset/5,
//				5*soffset-soffset/5,
//				11*soffset/2-soffset/5,
//				11*soffset/2-soffset/5,
//				offset+soffset/3
//		};
//		yLeft=yRight=new int[]{
//				height-1-soffset*5+soffset/2,
//				height - 1-soffset*7/2-soffset/5,
//				height - 1-soffset*7/2+soffset/2,
//				height-1-3*soffset,
//				height-1-soffset-soffset/10,
//				height-1-soffset-soffset-soffset/3
//		};
//		xRight=new int[]{
//				width-1-(offset+soffset/3),
//				width-1-(5*soffset-soffset/5),
//				width-1-(5*soffset-soffset/5),
//				width-1-(11*soffset/2-soffset/5),
//				width-1-(11*soffset/2-soffset/5),
//				width-1-(offset+soffset/3)
//		};
//
//		yBot=new int[]{
//				height-1-3*soffset/2+2,height-1-3*soffset/2+2,
//				height-1,height-1
//		};
//		xBot=new int[]{
//				8*soffset+2,
//				width-1-8*soffset-2,
//				width-1-15*soffset/2-2,
//				15*soffset/2+2,
//
//		};

		JLabel title = new JLabel("АЛУ", JLabel.CENTER);
		title.setFont(FONT_COURIER_BOLD_45);
		title.setBounds(offset, offset, width - 2*offset, height - 4*soffset);
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
