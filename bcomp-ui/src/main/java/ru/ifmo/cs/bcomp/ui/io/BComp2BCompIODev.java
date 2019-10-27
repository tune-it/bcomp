/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui.io;

import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.components.DataDestination;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BComp2BCompIODev {
//	public  BComp2BCompIODev(final IOCtrl ioctrl1, final IOCtrl ioctrl2) {
//		ioctrl2.setFlag();
//
//		ioctrl1.addDestination(IOCtrl.ControlSignal.OUT, new DataDestination() {
//			public void setValue(int value) {
//				try {
//					ioctrl2.setData(value);
//				} catch (Exception e) { }
//			}
//		});
//
//		ioctrl2.addDestination(IOCtrl.ControlSignal.OUT, new DataDestination() {
//			public void setValue(int value) {
//				try {
//					ioctrl1.setData(value);
//				} catch (Exception e) { }
//			}
//		});
//
//		ioctrl1.addDestination(IOCtrl.ControlSignal.SETFLAG, new DataDestination() {
//			public void setValue(int value) {
//				if (value == 0)
//					ioctrl2.setFlag();
//			}
//		});
//
//		ioctrl2.addDestination(IOCtrl.ControlSignal.SETFLAG, new DataDestination() {
//			public void setValue(int value) {
//				if (value == 0)
//					ioctrl1.setFlag();
//			}
//		});
//	}
}
