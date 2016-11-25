/*
 * $Id$
 */

package ru.ifmo.cs.elements;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DataWidth {
	public final String name;
	public final int width;
	public final int mask;

	public DataWidth(String name, int width) {
		this.name = name;
		this.width = width;
		this.mask = getMask(width);
	}

	public DataWidth(int width) {
		this(null, width);
	}

	public static int getMask(int width) {
		return (1 << width) - 1;
	}

	public int getWidth() {
		return width;
	}
}
