/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui;

import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import static ru.ifmo.cs.bcomp.MCDecoder.getFormattedMC;
import ru.ifmo.cs.bcomp.MicroCode;
import static ru.ifmo.cs.bcomp.RunningCycle.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MicroCodeDecoder {
	private final CPU cpu;
	private final MicroCode mc;

	public MicroCodeDecoder(BasicComp bcomp) {
		cpu = bcomp.getCPU();
		mc = cpu.getMicroCodeSource();
		cpu.stopCPU();
	}

	public void decode() throws Exception {
		int infetch = mc.findLabel(INFETCH.name());
		int reserved = mc.findLabel(RESERVED.name());

		for (int addr = infetch; addr < reserved; System.out.println(getFormattedMC(cpu, addr++)));
	}
}
