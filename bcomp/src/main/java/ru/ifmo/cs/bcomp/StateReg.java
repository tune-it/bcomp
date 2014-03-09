/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.PseudoRegister;
import ru.ifmo.cs.elements.Register;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class StateReg extends PseudoRegister {
	public static final int FLAG_C = 0;
	public static final int FLAG_Z = 1;
	public static final int FLAG_N = 2;
	public static final int FLAG_0 = 3;
	public static final int FLAG_EI = 4;
	public static final int FLAG_INTR = 5;
	public static final int FLAG_READY = 6;
	public static final int FLAG_RUN = 7;
	public static final int FLAG_PROG = 8;
	public static final int WIDTH = FLAG_PROG + 1;
	public static String[] FULLNAME = {
		"Перенос (C)",
		"Нуль (Z)",
		"Знак (N)",
		"0",
		"Разрешение прерывания",
		"Запрос прерывания",
		"Флаг ВУ",
		"Работа/останов",
		"Программа"
	};

	public static String[] NAME = {
		"C",
		"Z",
		"N",
		"0",
		"EI",
		"INT",
		"READY",
		"RUN",
		"PROG"
	};

	public StateReg(Register reg, int startbit, DataSource ... inputs) {
		super(FULLNAME[startbit], reg, startbit, inputs);
	}

	public static int getFlag(String s) throws Exception {
		for (int i = 0; i < NAME.length; i++)
			if (NAME[i].equals(s))
				return i;

		throw new Exception("Unknown flag " + s);
	}
}
