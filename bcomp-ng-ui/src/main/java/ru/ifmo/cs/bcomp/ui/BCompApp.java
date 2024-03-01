/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.ProgramBinary;
import ru.ifmo.cs.bcomp.assembler.AsmNg;
import ru.ifmo.cs.bcomp.assembler.Program;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BCompApp {
	public static void main(String[] args) throws Exception {
		BasicComp bcomp = new BasicComp();
		String mpname;
		String app;

		try {
			app = System.getProperty("mode", "gui");
		} catch (Exception e) {
			app = "gui";
		}

		try {
			String code = System.getProperty("code", null);
			File file = new File(code);

			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				StringBuilder sb = new StringBuilder();
				while (reader.ready()) {
					sb.append(reader.readLine()).append("\n");
				}
				code = sb.toString();
				AsmNg asm = new AsmNg(code);
				Program pobj = asm.compile();
				if (asm.getErrors().isEmpty()) {
					ProgramBinary prog = new ProgramBinary(pobj.getBinaryFormat());
					bcomp.loadProgram(prog);
				} else {
					for (String err : asm.getErrors())
						System.out.println(err);
				}
			}
		} catch (Exception e) { }

		try {
			String debuglevel = System.getProperty("debuglevel", "0");
			bcomp.getCPU().setDebugLevel(Long.parseLong(debuglevel));
		} catch (Exception e) { }

		if (app.equals("decoder")) {
			MicroCodeDecoder mpdecoder = new MicroCodeDecoder(bcomp);
			mpdecoder.decode();
			return;
		}

		bcomp.startTimer();

		if (app.equals("gui")) {
			GUI gui = new GUI(bcomp);
			gui.gui();
			return;
		}

		if (app.equals("cli")) {
			CLI cli = new CLI(bcomp);
			cli.cli();
			return;
		}

		if (app.equals("dual")) {
			CLI cli = new CLI(bcomp);
			GUI gui = new GUI(bcomp);
			gui.gui();
			cli.cli();
			return;
		}

		if (app.equals("nightmare")) {
			Nightmare nightmare = new Nightmare(bcomp);
			return;
		}

		System.err.println("Invalid mode selected");
	}
}
