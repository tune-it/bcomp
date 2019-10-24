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
	public final ControlSignal[] signals;

	public SignalListener(DataDestination dest, ControlSignal ... signals) {
		this.dest = dest;
		this.signals = signals;
	}
}
