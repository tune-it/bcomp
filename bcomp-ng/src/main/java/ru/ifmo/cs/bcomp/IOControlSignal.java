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
	 * Disable interrupts
	 */
	DI,
	/**
	 * IO Control signal 1:
	 * Enable interrupts
	 */
	EI,
	/**
	 * IO Control signal 2:
	 * Input
	 */
	IN,
	/**
	 * IO Control signal 3:
	 * Output
	 */
	OUT,
	/**
	 * Control signal 4:
	 * Reserved
	 */
	RESERVED4,
	/**
	 * Control signal 5:
	 * Reserved
	 */
	RESERVED5,
	/**
	 * Control signal 6:
	 * IRQ response
	 */
	IRQ,
	/**
	 * Control signal 7:
	 * Ready response
	 */
	RDY,
}
