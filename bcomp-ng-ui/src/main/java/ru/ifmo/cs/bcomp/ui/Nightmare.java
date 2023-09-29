/*
 * $Id$
 */
package ru.ifmo.cs.bcomp.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import static ru.ifmo.cs.bcomp.ControlSignal.*;
import ru.ifmo.cs.bcomp.Reg;
import static ru.ifmo.cs.bcomp.Reg.*;
import static ru.ifmo.cs.bcomp.State.*;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.IOCtrlAdv;
import ru.ifmo.cs.bcomp.IOCtrlBasic;
import ru.ifmo.cs.bcomp.SignalListener;
import ru.ifmo.cs.bcomp.ui.io.Keyboard;
import ru.ifmo.cs.bcomp.ui.io.Numpad;
import ru.ifmo.cs.bcomp.ui.io.SevenSegmentDisplay;
import ru.ifmo.cs.bcomp.ui.io.TextPrinter;
import ru.ifmo.cs.bcomp.ui.io.Ticker;
import ru.ifmo.cs.components.DataDestination;
import ru.ifmo.cs.components.Register;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Nightmare {
	private static final Color LED_OFF = new Color(128, 128, 128);
	private static final Color LED_ON = new Color(0, 160, 0);
	private static final int BIT_RADIUS = 16;
	private static final Font LABEL_FONT = new Font("Courier New", Font.BOLD, 24);
	private static final Font HINTS_FONT = new Font("Courier New", Font.BOLD, 14);
	private final ResourceBundle res = ResourceBundle.getBundle("ru.ifmo.cs.bcomp.ui.components.loc", Locale.getDefault());
	private final long[] delayPeriods = { 0, 1, 5, 10, 25, 50, 100, 1000 };
	private volatile int currentDelay = 3;

	private final BasicComp bcomp;
	private final CPU cpu;
	private final IOCtrl[] ioctrls;
	private EnumMap<Reg, RegisterView> regs = new EnumMap<Reg, RegisterView>(Reg.class);
	private final SignalListener[] listeners;

	private BasicIOView io1 = null;
	private BasicIOView io2 = null;
	private BasicIOView io3 = null;
	private AdvIOView io4 = null;
	private TextPrinter textPrinter = null;
	private Ticker ticker = null;
	private SevenSegmentDisplay ssd = null;
	private Keyboard kbd = null;
	private Numpad numpad = null;
//	private GUI pairgui = null;

	private class BitView extends JComponent {
		private final Register reg;
		private final int startbit;

		protected BitView(Register reg, int startbit) {
			this.reg = reg;
			this.startbit = startbit;

			Dimension DIMS = new Dimension(2 * BIT_RADIUS + 4, 2 * BIT_RADIUS + 4);
			setMinimumSize(DIMS);
			setMaximumSize(DIMS);
			setPreferredSize(DIMS);
			setSize(DIMS);
		}

		@Override
		public void paintComponent(Graphics g) {
			g.setColor(reg.getValue(startbit) == 1 ? LED_ON : LED_OFF);
			g.fillOval(2, 2, 2 * BIT_RADIUS, 2 * BIT_RADIUS);
		}
	}

	private class RegisterView extends JPanel implements DataDestination {
		private final JLabel label;
		private final Register reg;
		private final BitView[] bits;

		private RegisterView(String name, Register reg) {
			super(new FlowLayout(FlowLayout.RIGHT, 0, 0));

			this.reg = reg;
			label = new JLabel(name);
			label.setFont(LABEL_FONT);
			add(label);

			bits = new BitView[(int)reg.width];

			for (long i = reg.width - 1; i >= 0; i--)
				add(bits[(int)i] = new BitView(reg, (int)i));
		}

		@Override
		public void setValue(long value) {
			for (int i = 0; i < reg.width; bits[i++].repaint());
		}

		private void invertBit(int startbit) {
			reg.invertBit(startbit);
			bits[startbit].repaint();
		}
	}

	private class BasicIOView {
		private final IOCtrlBasic ioctrl;
		private final JFrame frame;
		private final RegisterView data;
		private final RegisterView flag;
		private final RegisterView irq;

		private BasicIOView(IOCtrl ioctrls[], int number) {
			ioctrl = (IOCtrlBasic)ioctrls[number];
			data = new RegisterView("DR", ioctrl.getRegisters()[0]);
			flag = new RegisterView("SR", ioctrl.getRegisters()[1]);
			irq = new RegisterView("IRQ", ioctrl.getRegisters()[2]);

			ioctrl.addDestination(0, data);
			ioctrl.addDestination(1, flag);
			ioctrl.addDestination(2, irq);

			JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
			panel.add(irq);
			panel.add(flag);
			panel.add(data);

			frame = new JFrame(res.getString("cdev") + number);
			frame.add(panel);
			frame.pack();

			frame.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_F:
					case KeyEvent.VK_R:
					case KeyEvent.VK_F1:
					case KeyEvent.VK_F2:
					case KeyEvent.VK_F3:
						ioctrl.setReady();
						break;

					case KeyEvent.VK_0:
						invertBit(0);
						break;

					case KeyEvent.VK_1:
						invertBit(1);
						break;

					case KeyEvent.VK_2:
						invertBit(2);
						break;

					case KeyEvent.VK_3:
						invertBit(3);
						break;

					case KeyEvent.VK_4:
						invertBit(4);
						break;

					case KeyEvent.VK_5:
						invertBit(5);
						break;

					case KeyEvent.VK_6:
						invertBit(6);
						break;

					case KeyEvent.VK_7:
						invertBit(7);
						break;
					}
				}
			});
		}

		private void invertBit(int startbit) {
			data.invertBit(startbit);
		}

		private void activate() {
			frame.setVisible(true);
			frame.requestFocus();
		}
	}

	private class AdvIOView {
		private final String REGNAMES[] = {"DR#0", "DR#1", "State", "Mgmt"};
		private final IOCtrlAdv ioctrl;
		private final JFrame frame;
		private final RegisterView regview[];
		private final int COUNT;
		private volatile int active = 0;

		private AdvIOView(IOCtrl ioctrls[], int number) {
			ioctrl = (IOCtrlAdv)ioctrls[number];

			Register registers[] = ioctrl.getRegisters();
			regview = new RegisterView[COUNT = registers.length];

			JPanel panel = new JPanel(new GridLayout(COUNT, 1));

			for (int i = 0; i < COUNT; i++) {
				regview[i] = new RegisterView(REGNAMES[i], registers[i]);
				regview[i].label.setForeground(i == active ? LED_ON : LED_OFF);
				panel.add(regview[i]);
				ioctrl.addDestination(i, regview[i]);
			}

			frame = new JFrame(res.getString("cdev") + number);
			frame.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
			frame.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
			frame.add(panel);
			frame.pack();

			frame.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_TAB:
						regview[active].label.setForeground(LED_OFF);
						regview[active = (active + (e.isShiftDown() ? COUNT - 1 : 1)) % COUNT].label.setForeground(LED_ON);
						break;

					case KeyEvent.VK_0:
						invertBit(0);
						break;

					case KeyEvent.VK_1:
						invertBit(1);
						break;

					case KeyEvent.VK_2:
						invertBit(2);
						break;

					case KeyEvent.VK_3:
						invertBit(3);
						break;

					case KeyEvent.VK_4:
						invertBit(4);
						break;

					case KeyEvent.VK_5:
						invertBit(5);
						break;

					case KeyEvent.VK_6:
						invertBit(6);
						break;

					case KeyEvent.VK_7:
						invertBit(7);
						break;
					}
				}
			});
		}

		private void invertBit(int startbit) {
			regview[active].invertBit(startbit);
			ioctrl.updateStateIRQ();
		}

		private void activate() {
			frame.setVisible(true);
			frame.requestFocus();
		}
	}

	public Nightmare(BasicComp bcomp) {
		this.bcomp = bcomp;
		this.cpu = bcomp.getCPU();
		this.ioctrls = bcomp.getIOCtrls();

		for (Reg reg : Reg.values())
			if ((reg != MP) && (reg != MR))
				regs.put(reg, new RegisterView(reg.toString(), cpu.getRegister(reg)));

		listeners = new SignalListener[] {
			new SignalListener(regs.get(DR), WRDR, LOAD),
			new SignalListener(regs.get(CR), WRCR, INTS),
			new SignalListener(regs.get(IP), WRIP),
			new SignalListener(regs.get(SP), WRSP),
			new SignalListener(regs.get(AC), WRAC, IO),
			new SignalListener(regs.get(BR), WRBR),
			new SignalListener(regs.get(PS), WRPS, SETC, SETV, STNZ, SET_EI, HALT, SET_PROGRAM, SET_REQUEST_INTERRUPT),
			new SignalListener(regs.get(AR), WRAR),
		};

		bcomp.addDestination(listeners);
		cpu.setTickFinishListener(new Runnable() {
			@Override
			public void run() {
				if (delayPeriods[currentDelay] != 0)
					try {
						Thread.sleep(delayPeriods[currentDelay]);
					} catch (InterruptedException e) { }
			}
		});

		JFrame frame = new JFrame(res.getString("basename"));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new GridLayout(10, 1));
		panel.add(regs.get(DR));
		panel.add(regs.get(CR));
		panel.add(regs.get(IP));
		panel.add(regs.get(SP));
		panel.add(regs.get(AR));
		panel.add(regs.get(AC));
		panel.add(regs.get(BR));
		panel.add(regs.get(PS));
		panel.add(regs.get(IR));

		String buttons = res.getString("setip") + " "
				+ res.getString("write") + " "
				+ res.getString("read") + " "
				+ res.getString("start") + " "
				+ res.getString("continue") + " "
				+ res.getString("runstop") + " "
				+ res.getString("tick");


		JLabel hints = new JLabel(buttons);
		hints.setFont(HINTS_FONT);
		panel.add(hints);
		frame.add(panel);

		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown()) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_Q:
						System.exit(0);
						break;

					case KeyEvent.VK_1:
					case KeyEvent.VK_F1:
						if (io1 == null)
							io1 = new BasicIOView(ioctrls, 1);
						io1.activate();
						break;

					case KeyEvent.VK_2:
					case KeyEvent.VK_F2:
						if (io2 == null)
							io2 = new BasicIOView(ioctrls, 2);
						io2.activate();
						break;

					case KeyEvent.VK_3:
					case KeyEvent.VK_F3:
						if (io3 == null)
							io3 = new BasicIOView(ioctrls, 3);
						io3.activate();
						break;

					case KeyEvent.VK_4:
					case KeyEvent.VK_F4:
						if (io4 == null)
							io4 = new AdvIOView(ioctrls, 4);
						io4.activate();
						break;

					case KeyEvent.VK_5:
					case KeyEvent.VK_F5:
						if (textPrinter == null)
							textPrinter = new TextPrinter(ioctrls[5]);
						textPrinter.activate();
						break;

					case KeyEvent.VK_6:
					case KeyEvent.VK_F6:
						if (ticker == null)
							ticker = new Ticker(ioctrls[6]);
						ticker.activate();
						break;

					case KeyEvent.VK_7:
					case KeyEvent.VK_F7:
						if (ssd == null)
							ssd = new SevenSegmentDisplay(ioctrls[7]);
						ssd.activate();
						break;

					case KeyEvent.VK_8:
					case KeyEvent.VK_F8:
						if (kbd == null)
							kbd = new Keyboard(ioctrls[8]);
						kbd.activate();
						break;

					case KeyEvent.VK_9:
					case KeyEvent.VK_F9:
						if (numpad == null)
							numpad = new Numpad(ioctrls[9]);
						numpad.activate();
						break;
					}
					return;
				}

				if (e.isShiftDown()) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_F9:
						cpu.invertClockState();
					}

					return;
				}

				switch (e.getKeyCode()) {
				case KeyEvent.VK_0:
					regs.get(IR).invertBit(0);
					break;

				case KeyEvent.VK_1:
					regs.get(IR).invertBit(1);
					break;

				case KeyEvent.VK_2:
					regs.get(IR).invertBit(2);
					break;

				case KeyEvent.VK_3:
					regs.get(IR).invertBit(3);
					break;

				case KeyEvent.VK_4:
					regs.get(IR).invertBit(4);
					break;

				case KeyEvent.VK_5:
					regs.get(IR).invertBit(5);
					break;

				case KeyEvent.VK_6:
					regs.get(IR).invertBit(6);
					break;

				case KeyEvent.VK_7:
					regs.get(IR).invertBit(7);
					break;

				case KeyEvent.VK_8:
					regs.get(IR).invertBit(8);
					break;

				case KeyEvent.VK_9:
					regs.get(IR).invertBit(9);
					break;

				case KeyEvent.VK_A:
					regs.get(IR).invertBit(10);
					break;

				case KeyEvent.VK_B:
					regs.get(IR).invertBit(11);
					break;

				case KeyEvent.VK_C:
					regs.get(IR).invertBit(12);
					break;

				case KeyEvent.VK_D:
					regs.get(IR).invertBit(13);
					break;

				case KeyEvent.VK_E:
					regs.get(IR).invertBit(14);
					break;

				case KeyEvent.VK_F:
					regs.get(IR).invertBit(15);
					break;
/*
				case KeyEvent.VK_F1:
					ioctrls[1].setFlag();
					break;

				case KeyEvent.VK_F2:
					ioctrls[2].setFlag();
					break;

				case KeyEvent.VK_F3:
					ioctrls[3].setFlag();
					break;
*/
				case KeyEvent.VK_F4:
					cpu.startSetAddr();
					break;

				case KeyEvent.VK_F5:
					cpu.startWrite();
					break;

				case KeyEvent.VK_F6:
					cpu.startRead();
					break;

				case KeyEvent.VK_F7:
					cpu.startStart();
					break;

				case KeyEvent.VK_F8:
					cpu.startContinue();
					break;

				case KeyEvent.VK_F9:
					cpu.invertRunState();
					regs.get(PS).bits[W.ordinal()].repaint();
					break;

				case KeyEvent.VK_F11:
					currentDelay = (currentDelay > 0 ? currentDelay : delayPeriods.length) - 1;
					break;

				case KeyEvent.VK_F12:
					currentDelay = currentDelay < delayPeriods.length - 1 ? currentDelay + 1 : 0;
				}
			}
		});
		frame.pack();
		frame.setVisible(true);
		frame.requestFocus();
	}
}
