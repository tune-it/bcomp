/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import java.util.ArrayList;
import static ru.ifmo.cs.components.Utils.toHex;
import static ru.ifmo.cs.bcomp.ControlSignal.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MCDecoder {
	private static ControlSignal[] signals = ControlSignal.values();
	private static ControlSignal[] LEFT = {RDAC, RDBR, RDPS, RDIR};
	private static ControlSignal[] RIGHT = {RDDR, RDCR, RDIP, RDSP};

	public final static String[] decodeMC(CPU cpu, long addr) {
		MicroCode mc = cpu.getMicroCodeSource();
		String[] res = new String[3];
		ArrayList<ControlSignal> cs = new ArrayList<ControlSignal>();
		long cmd = cpu.getMicroCode().getValue(addr);

		res[0] = mc.getLabel((int)addr);
		res[1] = toHex(cmd, 40);

		for (int i = 0; i < 16; i++)
			if ((cmd & (1L << i)) != 0)
				cs.add(signals[i]);

		if ((cmd & (1L << TYPE.ordinal())) == 0) {
			for (int i = 16; i < TYPE.ordinal(); i++)
				if ((cmd & (1L << i)) != 0)
					cs.add(signals[i]);

			res[2] = decodeOMC(cs);
		} else
			res[2] = decodeCMC(mc, cs, (cmd >> 16) & 0xff, (cmd >> 24) & 0xff, (cmd >> 32) & 1);

		return res;
	}

	public static String getFormattedMC(CPU cpu, long addr) {
		String[] decoded = MCDecoder.decodeMC(cpu, addr);

		return 
			toHex(addr, 8) + " " +
			decoded[1] + "\t" + 
			(decoded[0] == null ? "\t\t" : decoded[0] + (decoded[0].length() > 7 ? "\t" : "\t\t")) +
			(decoded[2] == null ? "No operations" : decoded[2]);
	}

	private static String decodeCMC(MicroCode mc, ArrayList<ControlSignal> cs, long checkbit, long addr, long expected) {
		String label = mc.getLabel((int)addr);
		String aluOutput = getAluOutput(cs);
		String bit = null;
		String to = (label == null ? "" : label + " @ ") + toHex(addr, 8);
		int i;

		for (i = 0; i < 8; i++, checkbit >>= 1)
			if ((checkbit & 1) == 1)
				break;

		if (cs.contains(HTOL))
			i += 8;

		if (aluOutput.equals("PS")) {
			if (i == State.PS0.ordinal())
				return "GOTO " + to;

			for (State state : State.values())
				if (i == state.ordinal())
					bit = state.name();
		} else
			bit = "" + i;

		return "if " + aluOutput + "(" + bit + ") = " + expected + " then GOTO " + to;
	}

	private static String decodeOMC(ArrayList<ControlSignal> cs) {
		ArrayList<String> operations = new ArrayList<String>();
		String writelist = getWriteList(cs);
		String result = null;

		if (writelist != null) {
			String swOutput = getSwOutput(cs);
			operations.add((swOutput == null ? "0" : swOutput) + " → " + writelist);
		}

		if (cs.contains(LOAD))
			operations.add("MEM(AR) → DR");

		if (cs.contains(STOR))
			operations.add("DR → MEM(AR)");

		if (cs.contains(IO))
			operations.add("IO");

		if (cs.contains(INTS))
			operations.add("INTS");

		if (cs.contains(HALT))
			operations.add("Halt");

		for (String op : operations)
			result = result == null ? op : result + "; " + op;

		return result;
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

		if (cs.contains(LTOL)) {
			if (cs.contains(SEXT))
				return "extend sign " + alu + "(0..7)";
			else
				return "LTOL(" + alu + ")";
		}

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
				return "ROL(" + alu + ")";
			else
				return "SHL(" + alu + ")";
		}

		if (cs.contains(SHRT)) {
			if (cs.contains(SHRF))
				return "ROR(" + alu + ")";
			else
				return "ASR(" + alu + ")";
		}

		return null;
	}

	private static String getWriteList(ArrayList<ControlSignal> cs) {
		ArrayList<String> dsts = new ArrayList<String>();
		String result = null;

		for (int i = WRDR.ordinal(); i <= WRAR.ordinal(); i++)
			if (cs.contains(signals[i]))
				dsts.add(signals[i].name().substring(2));

		if (cs.contains(STNZ)) {
			dsts.add("N");
			dsts.add("Z");
		}

		if (cs.contains(SETV))
			dsts.add("V");

		if (cs.contains(SETC))
			dsts.add("C");

		for (String dst : dsts)
			result = result == null ? dst : (result + ", " + dst);

		return result;
	}
}
