/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui;

import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.MCDecoder;
import ru.ifmo.cs.bcomp.MicroCode;
import static ru.ifmo.cs.bcomp.RunningCycle.*;
import static ru.ifmo.cs.bcomp.Utils.toHex;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MicroCodeDecoder {
	private final CPU cpu;
	private final MicroCode mc;

	public MicroCodeDecoder() throws Exception {
		cpu = (new BasicComp().getCPU());
		mc = cpu.getMicroCodeSource();
		cpu.stopCPU();
	}

	public void decode() throws Exception {
		int infetch = mc.findLabel(INFETCH.name());
		int reserved = mc.findLabel(RESERVED.name());

		for (int addr = infetch; addr < reserved; addr++) {
			String[] decoded = MCDecoder.decodeMC(cpu, addr);

			System.out.println(
				toHex(addr, 8) + " " +
				decoded[1] + "\t" + 
				(decoded[0] == null ? "\t\t" : decoded[0] + (decoded[0].length() > 7 ? "\t" : "\t\t")) +
				decoded[2]);
		}
	}
}
