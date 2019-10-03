/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import java.util.Arrays;
import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.PseudoRegister;
import ru.ifmo.cs.elements.Register;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class StateReg extends PseudoRegister {
	public StateReg(Register reg, int startbit, DataSource ... inputs) {
		super(FULLNAME[startbit], reg, startbit, inputs);
	}

	public static boolean isFlag(String s) {
		return Arrays.asList(NAME).contains(s);
	}

	public static int getFlag(String s) throws Exception {
		for (int i = 0; i < NAME.length; i++)
			if (NAME[i].equals(s))
				return i;

		throw new Exception("Unknown flag " + s);
	}
}
