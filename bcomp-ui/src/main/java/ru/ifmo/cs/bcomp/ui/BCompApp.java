/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui;



/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BCompApp {
	public static void main(String[] args) throws Exception {
		String mpname;
		String app;

		try {

			app = System.getProperty("mode", "gui");
		} catch (Exception e) {

			app = "gui";
		}



		if (app.equals("gui")) {
			GUI gui = new GUI();
			gui.gui();
			return;
		}

		if (app.equals("cli")) {
			CLI cli = new CLI();
			cli.cli();
			return;
		}

		if (app.equals("decoder")) {
			MicroCodeDecoder mpdecoder = new MicroCodeDecoder();
			mpdecoder.decode();
			return;
		}

		if (app.equals("nightmare")) {
			Nightmare nightmare = new Nightmare();
			return;
		}

		System.err.println("Invalid mode selected");
	}
}
