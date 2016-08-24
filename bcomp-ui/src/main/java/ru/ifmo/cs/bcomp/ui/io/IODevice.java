/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.io;

import java.awt.Component;
import javax.swing.JFrame;
import ru.ifmo.cs.bcomp.IOCtrl;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public abstract class IODevice {
	protected final IOCtrl ioctrl;
	protected final String title;
	private JFrame frame = null;

	public IODevice(final IOCtrl ioctrl, final String title) {
		this.ioctrl = ioctrl;
		this.title = title;
	}

	protected abstract Component getContent();

	public void activate() {
		if (frame == null) {
			frame = new JFrame(title);
			frame.add(getContent());
			frame.pack();
			// !!! Recheck this
			// frame.setResizable(false);
		}

		frame.setVisible(true);
		frame.requestFocus();
	}
}
