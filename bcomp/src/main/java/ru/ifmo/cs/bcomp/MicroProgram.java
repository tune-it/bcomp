/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MicroProgram {
	public final String microprogramName;
	public final Instruction[] instructionSet;
	public final String[][] microprogram;

	public MicroProgram(String microprogramName, Instruction[] instructionSet,	String[][] microprogram) {
		this.microprogramName = microprogramName;
		this.instructionSet = instructionSet;
		this.microprogram = microprogram;
	}

	private boolean checkBit(int cmd, int bit) {
		return ((cmd >> bit) & 1) == 1;
	}

	private int getBits(int cmd, int startbit, int width) {
		return (cmd >> startbit) & ((1 << width) - 1);
	}

	// XXX: Получать имена регистров из их свойств (?)
	private String getRegister(int cmd) {
		switch (getBits(cmd, 12, 2)) {
			case 0:
				return "РС";

			case 1:
				return "РД";

			case 2:
				return "РК";

			case 3:
				return "А";
		}

		return null;
	}

	private String getLeftInput(int cmd) {
		switch (getBits(cmd, 12, 2)) {
			case 0:
				return "0";

			case 1:
				return "А";

			case 2:
				return "РС";

			case 3:
				return "КлР";
		}

		return null;
	}

	private String getRightInput(int cmd) {
		switch (getBits(cmd, 8, 2)) {
			case 0:
				return "0";

			case 1:
				return "РД";

			case 2:
				return "РК";

			case 3:
				return "СК";
		}

		return null;
	}

	private String getInvert(String reg, int inv) {
		return inv == 1 ? "COM(" + reg + ")" : reg;
	}

	private String getOperation(int cmd, String left, String right) {
		switch (getBits(cmd, 4, 2)) {
			case 0:
				return left + " + " + right;

			case 1:
				return left + " + " + right + " + 1";

			case 2:
				return left + " & " + right;
		}

		return null;
	}

	private String getRotate(int cmd) {
		switch (getBits(cmd, 2, 2)) {
			case 0:
				return getOperation(cmd,
					getInvert(getLeftInput(cmd), getBits(cmd, 6, 1)),
					getInvert(getRightInput(cmd), getBits(cmd, 7, 1)));

			case 1:
				return "RAR(А)";

			case 2:
				return "RAL(А)";
		}

		return null;
	}

	private String getMemory(int cmd) {
		switch (getBits(cmd, 0, 2)) {
			case 0:
				return "";

			case 1:
				return ", ОП(РА) ==> РД";

			case 2:
				return ", РД ==> ОП(РА)";
		}

		return null;
	}

	private String getOutput(int cmd) {
		switch (getBits(cmd, 0, 3)) {
			case 0:
				return "";

			case 1:
				return "БР ==> РА";

			case 2:
				return "БР ==> РД";

			case 3:
				return "БР ==> РК";

			case 4:
				return "БР ==> СК";

			case 5:
				return "БР ==> А";
		}

		return null;
	}

	private String getC(int cmd) {
		switch (getBits(cmd, 6, 2)) {
			case 0:
				return "";

			case 1:
				return ", С";

			case 2:
				return "0 ==> C";

			case 3:
				return "1 ==> C";
		}

		return null;
	}

	public String decodeCmd(int cmd) {
		if (checkBit(cmd, 15)) {
			int addr = getBits(cmd, 0, 8);
			return "IF " + getRegister(cmd) + "(" + getBits(cmd, 8, 4) + ") = " +
				getBits(cmd, 14, 1) + " THEN " +
				(addr >= microprogram.length ? "" : microprogram[addr][0]) + 
				"(" + Utils.toHex(addr, 8) + ")";
		} else if (checkBit(cmd, 14))
			return
				(checkBit(cmd, 11) ? "Разрешить прерывания " : "") +
				(checkBit(cmd, 10) ? "Запретить прерывания " : "") +
				(checkBit(cmd, 8) ? "Ввод/вывод" : "") +
				(checkBit(cmd, 3) ? "Останов машины" : "") +
				getOutput(cmd) + getC(cmd) +
				(checkBit(cmd, 5) ? ", N" : "") +
				(checkBit(cmd, 4) ? ", Z" : "");
		else
			return getRotate(cmd) + " ==> БР" + getMemory(cmd);
	}
}
