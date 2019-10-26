/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public enum State {
	/**
	 * Flag 0:
	 * Carry flag
	 */
	C,
	/**
	 * Flag 1:
	 * Overflow flag
	 */
	V,
	/**
	 * Flag 2:
	 * Zero flag
	 */
	Z,
	/**
	 * Flag 3:
	 * Negative flag
	 */
	N,
	/**
	 * Flag 4:
	 * IO flag???
	 */
	F,
	/**
	 * Flag 5:
	 * Always zero
	 */
	PS0,
	/**
	 * Flag 6:
	 * Enable Interrupts
	 */
	EI,
	/**
	 * Flag 7:
	 * Interrupt
	 */
	INTR,
	/**
	 * Flag 8:
	 * IOREADY
	 */
	IOREADY,
	/**
	 * Flag 9:
	 * Run state
	 */
	RUN,
	/**
	 * Flag 10:
	 * Program is running
	 */
	PROG,
}
