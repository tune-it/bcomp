/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BasicComponent {
	public final long width;
	public final long mask;

	public BasicComponent(long width) {
		this.width = width;
		this.mask = calculateMask(width);
	}

	public static long calculateMask(long width) {
		return (1L << width) - 1L;
	}
}
