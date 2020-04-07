/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
//import ru.ifmo.cs.bcomp.StateReg;
import ru.ifmo.cs.components.Utils;

import ru.ifmo.cs.components.Register;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class StateRegisterView{
//	private final int formattedWidth;
//	private MouseMotionAdapter listener = new MouseMotionAdapter() {
//		private String tooltip = null;
//
//		@Override
//		public void mouseMoved(MouseEvent e) {
//			int bitno = Utils.getBitNo(e.getX(), formattedWidth, FONT_COURIER_BOLD_25_WIDTH);
//
//			if (bitno < 0) {
//				value.setToolTipText(tooltip = null);
//				return;
//			}
//
//			String newtooltip = StateReg.FULLNAME[bitno];
//			if (newtooltip != tooltip)
//				value.setToolTipText(tooltip = newtooltip);
//		}
//	};
//
//	public StateRegisterView(Register reg) {
//		super(reg);
//
//		formattedWidth = Utils.getBinaryWidth(reg.getWidth());
//	}
//
//	@Override
//	public void setProperties(int x, int y, boolean fullView) {
//		super.setProperties(x, y, !fullView, fullView ? getRegWidth() : 1);
//
//		if (fullView) {
//			value.addMouseMotionListener(listener);
//		} else {
//			value.removeMouseMotionListener(listener);
//			value.setToolTipText(null);
//		}
//	}
}
