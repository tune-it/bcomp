/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class IODevTimer {
	private final IOCtrl ctrl;
	private Thread timer;
	private volatile boolean running = true;

	public IODevTimer(IOCtrl ctrl) {
		this.ctrl = ctrl;
	}

	public void start(String name) {
		timer = new Thread(new Runnable() {
			public void run() {
				int countdown = 0;
				int value;

				while (running) {
					try {
						Thread.sleep(100);
					} catch (Exception ex) { }

					value = ctrl.getData();

					if (countdown != 0)
						if (countdown <= value) {
							if ((--countdown) == 0)
								ctrl.setFlag();
							else
								continue;
						}

					countdown = value;
				}
			}
		}, name);

		timer.start();
	}

	public void done() {
		running = false;

		try {
			timer.join();
		} catch (Exception ex) {
			System.out.println("Can't join thread: " + ex.getMessage());
		}
	}
}
