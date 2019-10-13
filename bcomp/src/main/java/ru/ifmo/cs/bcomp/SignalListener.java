/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.components.DataDestination;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class SignalListener {
	public final DataDestination dest;
	public final CS[] signals;

	public SignalListener(DataDestination dest, CS ... signals) {
		this.dest = dest;
		this.signals = signals;
	}
}
