/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.io;

import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
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
	private ResourceBundle res = ResourceBundle.getBundle("ru.ifmo.cs.bcomp.ui.components.loc", Locale.getDefault());


	public IODevice(final IOCtrl ioctrl, final String title) {
		this.ioctrl = ioctrl;
		this.title = res.getString(title);
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

	public ResourceBundle getRes() {
		return res;
	}
}
