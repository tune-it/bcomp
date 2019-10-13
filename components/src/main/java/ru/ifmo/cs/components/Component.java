/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Component {
	public final long width;
	public final long mask;

	public Component(long width) {
		this.width = width;
		this.mask = getMask(width);
	}

	public static long getMask(long width) {
		return (1L << width) - 1L;
	}
}
