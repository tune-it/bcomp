/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.portlet;

import java.util.Random;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BCompOpts {
	private static final int aopts[] = {2, 4, 8, 16, 32, 64, 128};
	private static final int bopts[] = {
		3, 5, 7, 9, 15, 17, 31, 33, 63, 65, 127, 129 ,6 ,12 ,24 ,48 ,96 ,10 , 18, 34,
		66, 130, 20, 36, 68, 40, 74, 80
	};
	private static final int minvalue = 33;
	private static final int maxvalue = 32767;
	private static final int nvalues = 3;

	public final int a;
	public final boolean anegative;
	public final int b;
	public final boolean bnegative;
	public final int[] x;
	public final int[] y;
	public final String formula;

	public BCompOpts(String username) {
		Random r = new Random(Integer.parseInt(username.substring(1)));

		r.nextBoolean();
		a = aopts[r.nextInt(aopts.length)];
		anegative = r.nextBoolean();
		b = bopts[r.nextInt(bopts.length)];
		bnegative = r.nextBoolean();

		formula = "R = " +
			(anegative ? " -" : "" ) + "X / " + a +
			(bnegative ? " - " : " + ") + b + " * Y";

		x = new int[nvalues];
		y = new int[nvalues];
		int maxy = maxvalue / b / 2;

		for (int i = 0; i < nvalues; i++) {
			x[i] = minvalue + r.nextInt(maxvalue - minvalue);
			y[i] = (r.nextBoolean() ? -1 : 1 ) * (minvalue + r.nextInt(maxy));
		}
	}

	public static void main(String[] args) {
		for (String arg : args) {
			BCompOpts opts = new BCompOpts(arg);

			System.out.println(opts.formula);

			for (int i = 0; i < opts.x.length; i++) {
				int x = opts.x[i];
				int y = opts.y[i];
				int r = (opts.anegative ? -1 : 1) * x / opts.a +
					(opts.bnegative ? -1 : 1) * y * opts.b;

				System.out.println("\t" +
					"X = " + String.format("%04X", x & 0xffff) +
					" Y = " + String.format("%04X", y & 0xffff) +
					" R = " + String.format("%04X", r & 0xffff));
			}
		}
	}
}
