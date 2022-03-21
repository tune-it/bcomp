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
	 * Always zero
	 */
	PS0,
	/**
	 * Flag 5:
	 * Enable Interrupts
	 */
	EI,
	/**
	 * Flag 6:
	 * Interrupt
	 */
	INT,
	/**
	 * Flag 7:
	 * Run state
	 */
	W,
	/**
	 * Flag 8:
	 * Program is running
	 */
	P,
}
