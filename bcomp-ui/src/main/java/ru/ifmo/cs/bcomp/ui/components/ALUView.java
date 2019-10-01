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
	private int xpoints1[];
	private int ypoints1[];

	public ALUView(int x, int y, int width, int height) {

		int half = width / 2;
		int offset = height / 3;
		int soffset = offset / 3;
		int k=20;

//		xpoints = new int[] {
//			0, half - soffset, half, half + soffset, width - 1, width - 1 - offset, offset
//		};
//		ypoints = new int[] {
//			0, 0, offset, 0, 0, height - 1, height - 1
//		};
		xpoints = new int[] {
				k,25+k, offset+k, half, width-1-offset-k,width-1-25-k,width - 1-k,//верх
				width-1 - offset, width-1 - offset-k/4, width-1 - offset-k, width-1 - offset-5*k/4,width-1-2*offset-k,//право
				2*offset+k,offset+5*k/4,offset+k	,offset+k/4,offset//лево
		};
		ypoints = new int[] {
				0,3, 13+k/2, offset+k/2, 13+k/2,3, 0,//верх
				height - 1-50, height - 1-50+10,height-1-50+15,height-1-10,height-1,//право
		height-1	,height-1-10	,height - 1-50+15,height - 1-50+10, height-1-50//лево
		};

		xpoints1=new int[]{
			xpoints[1]+3,xpoints[2]+2, xpoints[3], xpoints[4]-2,xpoints[5]-3,
				//127,104,77,50
				soffset*13, soffset*10, soffset*8, soffset*5
		};
		ypoints1=new int[]{
				3, 13+k/2-2, offset+k/2-2, 13+k/2-2,3,
				3,0,0,3
			};




		JLabel title = new JLabel("АЛУ", JLabel.CENTER);
		title.setFont(FONT_COURIER_BOLD_45);
		title.setBounds(offset+3, offset-5, width - 2*offset, height - offset);
	//	add(title);

		setBounds(x, y, width, height);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(COLOR_TITLE);
		g.fillPolygon(xpoints, ypoints, xpoints.length);g.fillPolygon(xpoints1,ypoints1,xpoints1.length);
		g.setColor(COLOR_TEXT);
		g.drawPolygon(xpoints, ypoints, xpoints.length);g.drawPolygon(xpoints1,ypoints1,xpoints1.length);


	}
}
