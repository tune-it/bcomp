/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.io;

import java.awt.*;
import javax.swing.JFrame;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.components.Messages;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public abstract class IODevice {
	protected final IOCtrl ioctrl;
	protected final String title;
	private JFrame frame = null;


	public IODevice(final IOCtrl ioctrl, final String titleKey) {
		this.ioctrl = ioctrl;
		this.title = Messages.get(titleKey);
	}

	protected abstract Component getContent();

	public void activate() {
		if (frame == null) {
			frame = new JFrame(title);
			frame.add(getContent());
			frame.pack();
			frame.setMinimumSize(new Dimension(560,170));
		}

		frame.setVisible(true);
		frame.requestFocus();
	}

	public JFrame getFrame() {
		return frame;
	}
}
