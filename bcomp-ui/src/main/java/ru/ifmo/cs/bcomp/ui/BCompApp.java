/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui;

import ru.ifmo.cs.bcomp.MicroProgram;
import ru.ifmo.cs.bcomp.MicroPrograms;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BCompApp {
	public static void main(String[] args) throws Exception {
		String mpname;
		String app;

		try {
			mpname = System.getProperty("mp", MicroPrograms.DEFAULT_MICROPROGRAM);
			app = System.getProperty("mode", "gui");
		} catch (Exception e) {
			mpname = MicroPrograms.DEFAULT_MICROPROGRAM;
			app = "gui";
		}

		MicroProgram mp = MicroPrograms.getMicroProgram(mpname);
		if (mp == null) {
			System.err.println("Invalid microprogram selected");
			System.exit(1);
		}

		if (app.equals("gui")) {
			GUI gui = new GUI(mp);
			gui.gui();
			return;
		}

		if (app.equals("cli")) {
			CLI cli = new CLI(mp);
			cli.cli();
			return;
		}

		if (app.equals("decoder")) {
			MPDecoder mpdecoder = new MPDecoder(mp);
			mpdecoder.decode();
			return;
		}

		if (app.equals("hardcore")) {
			Hardcore hardcore = new Hardcore(mp);
			return;
		}

		System.err.println("Invalid mode selected");
	}
}
