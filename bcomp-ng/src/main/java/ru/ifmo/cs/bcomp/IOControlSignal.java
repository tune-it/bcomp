/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public enum IOControlSignal {
	/**
	 * IO Control signal 0:
	 * Input
	 */
	IN,
	/**
	 * IO Control signal 1:
	 * Output
	 */
	OUT,
	/**
	 * Control signal 2:
	 * Disable interrupts
	 */
	DI,
	/**
	 * Control signal 3:
	 * Enable interrupts
	 */
	EI,
	/**
	 * Control signal 4:
	 * Reserved
	 * @hidden
	 */
	RESERVED4,
	/**
	 * Control signal 5:
	 * Reserved
	 * @hidden
	 */
	RESERVED5,
	/**
	 * Control signal 6:
	 * IRQ response
	 */
	IRQ,
	/**
	 * Control signal 7:
	 * Ready
	 */
	RDY,
}
