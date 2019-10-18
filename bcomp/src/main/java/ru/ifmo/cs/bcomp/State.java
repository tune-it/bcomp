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
	 * Carry flag
	 */
	C,
	/**
	 * Overflow flag
	 */
	V,
	/**
	 * Zero flag
	 */
	Z,
	/**
	 * Negative flag
	 */
	N,
	/**
	 * Always zero
	 */
	PS0,
	/**
	 * Enable Interrupts
	 */
	EI,
	/**
	 * Interrupt
	 */
	INTR,
	/**
	 * IOREADY
	 */
	IOREADY,
	/**
	 * Run state
	 */
	RUN,
	/**
	 * Program is running
	 */
	PROG,
	/**
	 * Use not assigned bit as zero for micro commands
	 */
	LAST_BIT,
}
