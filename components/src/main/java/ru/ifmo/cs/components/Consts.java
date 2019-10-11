/*
 * $Id$
 */

package ru.ifmo.cs.components;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Consts {
	public static final DataSource[] consts = new DataSource[2];

	static {
		for (int i = 0; i < consts.length; i++)
			consts[i] = new DataConst(i, 1);
	};
}
