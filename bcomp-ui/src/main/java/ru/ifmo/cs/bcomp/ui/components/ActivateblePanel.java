/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import javax.swing.JComponent;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public abstract class ActivateblePanel extends JComponent {
	public abstract void panelActivate();
	public abstract void panelDeactivate();
	public abstract String getPanelName();
	public abstract void redrawArrows();
}
