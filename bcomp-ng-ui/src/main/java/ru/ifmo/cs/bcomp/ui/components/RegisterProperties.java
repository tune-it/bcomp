/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;


import ru.ifmo.cs.bcomp.Reg;

import java.awt.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class RegisterProperties {
	public final Reg reg;
	public final int x;
	public final int y;
	public final boolean hex;
	public final GridBagConstraints constraints;
	public final boolean isLeft;

	public RegisterProperties(Reg reg, int x, int y,  boolean hex,boolean isLeft,GridBagConstraints constraints) {
		this.reg = reg;
		this.x = x;
		this.y = y;
		this.hex = hex;
		this.constraints=constraints;
		this.isLeft=isLeft;
	}
}
