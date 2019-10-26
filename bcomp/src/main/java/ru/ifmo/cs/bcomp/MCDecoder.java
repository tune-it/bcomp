/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import java.util.ArrayList;
import static ru.ifmo.cs.bcomp.ControlSignal.*;
import static ru.ifmo.cs.bcomp.State.*;
import static ru.ifmo.cs.bcomp.Utils.cs;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MCDecoder {
	private static ControlSignal[] signals = ControlSignal.values();
	private static ControlSignal[] LEFT = {RDAC, RDBR, RDPS, RDIR};
	private static ControlSignal[] RIGHT = {RDDR, RDCR, RDIP, RDSP};

	public final static String[] decodeMC(CPU cpu, long addr) {
		String[] res = new String[2];
		ArrayList<ControlSignal> cs = new ArrayList<ControlSignal>();
		long cmd = cpu.getMicroCode().getValue(addr);

		res[0] = cpu.getMicroCodeSource().getLabel((int)addr);

		for (int i = 0; i < 16; i++)
			if ((cmd & (1L << i)) != 0)
				cs.add(signals[i]);

		if ((cmd & (1L << TYPE.ordinal())) == 0)
			for (int i = 16; i < TYPE.ordinal(); i++)
				if ((cmd & (1L << i)) != 0)
					cs.add(signals[i]);

		res[1] = getSwOutput(cs);

		return res;
	}

	private static String getInput(ArrayList<ControlSignal> cs, ControlSignal[] rdsignals) {
		String regs = null;
		boolean addpar = false;

		for (ControlSignal c : rdsignals)
			if (cs.contains(c)) {
				String name = c.name().substring(2);

				if (regs == null)
					regs = name;
				else {
					regs += " | " + name;
					addpar = true;
				}
			}

		return regs == null ? null : ((addpar ? "(" : "") + regs + (addpar ? ")" : ""));
	}

	private static String getComplement(ArrayList<ControlSignal> cs, ControlSignal[] rdsignals, ControlSignal complement) {
		String input = getInput(cs, rdsignals);

		if (cs.contains(complement)) {
			if (input == null)
				return "~0";
			else
				return "~" + input;
		}

		if (input == null)
			return null;

		return input;
	}

	private  static String getAluOutput(ArrayList<ControlSignal> cs) {
		String left = getComplement(cs, LEFT, COML);
		String right = getComplement(cs, RIGHT, COMR);
			
		if (cs.contains(SORA)) {
			return (left == null ? "0" : left) + " & " + (right == null ? "0" : right);
		}

		boolean pls1 = cs.contains(PLS1);

		if (left == null) {
			if (pls1)
				return right == null ? "1" : right + " + 1";
			else
				return right == null ? "0" : right;
		}

		return left + (right == null ? "" : " + " + right) + (pls1 ? " + 1" : "");
	}

	private static String getSwOutput(ArrayList<ControlSignal> cs) {
		String alu = getAluOutput(cs);

		if (cs.contains(HTOH)) {
			if (cs.contains(LTOL))
				return alu;
			else
				return "HTOH(" + alu + ")";
		}

		if (cs.contains(LTOL))
			return "LTOL(" + alu + ")";

		if (cs.contains(LTOH)) {
			if (cs.contains(HTOL))
				return "SWAB(" + alu + ")";
			else
				return "LTOH(" + alu + ")";
		}

		if (cs.contains(HTOL))
			return "HTOL(" + alu + ")";

		if (cs.contains(SEXT))
			return "SEXT(" + alu + ")";


		if (cs.contains(SHLT)) {
			if (cs.contains(SHL0))
				return "SHLT+SHL0(" + alu + ")";
			else
				return "SHLT(" + alu + ")";
		}

		if (cs.contains(SHRT)) {
			if (cs.contains(SHRF))
				return "SHRT+SHRF(" + alu + ")";
			else
				return "SHRT(" + alu + ")";
		}

		return null;
	}
}
