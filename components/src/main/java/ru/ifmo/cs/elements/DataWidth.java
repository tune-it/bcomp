/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataWidth {
	public final int width;
	public final int mask;

	public DataWidth(int width) {
		this.width = width;
		this.mask = getMask(width);
	}

	public static int getMask(int width) {
		return (1 << width) - 1;
	}

	public int getWidth() {
		return width;
	}
}
