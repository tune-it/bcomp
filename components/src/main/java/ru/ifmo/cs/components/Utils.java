/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Utils {
	private final static char[] digits = {
		'0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
	};

	private final static String[] flags = { "0", "1" };

	public static String toBinaryFlag(long value) {
		return flags[(int)value];
	}

	public static int getBinaryWidth(int width) {
		return width + ((width - 1) >> 2);
	}

	public static String toBinary(long value, int width) {
		int chars = getBinaryWidth(width);
		char[] buf = new char[chars];
		int pos = chars;

		for (int i = 0; i < width; i++) {
			if ((i != 0) && (i & 3) == 0)
				buf[--pos] = ' ';
			buf[--pos] = digits[(int)(value & 1)];
			value >>= 1;
		}

		return new String(buf);
	}

	public static int getBitNo(int pos, int width, int charWidth) {
		pos -= charWidth >> 1;

		if (pos < 0)
			return -1;

		pos = width - (pos / charWidth);

		if (pos % 5 == 0)
			return -1;

		return pos - (pos / 5) - 1;
	}

	public static int getHexWidth(int width) {
		return (width + 3) >> 2;
	}

	public static String toHex(long value, long width) {
		int chars = getHexWidth((int)width);
		char[] buf = new char[chars];

		while (chars > 0) {
			buf[--chars] = digits[(int)(value & 0xf)];
			value >>= 4;
		}

		return new String(buf);
	}

	public static boolean isNumeric(String s, int radix) {
		if (s == null || s.length() == 0)
			return false;

		int i = 0;

		if (s.charAt(0) == '-') {
			i++;

			if (s.length() == 1)
				return false;
		}

		for (; i < s.length(); i++)
			if (Character.digit(s.charAt(i), radix) < 0)
				return false;

		return true;
	}

	public static boolean isHexNumeric(String s) {
		return isNumeric(s, 16);
	}
}
