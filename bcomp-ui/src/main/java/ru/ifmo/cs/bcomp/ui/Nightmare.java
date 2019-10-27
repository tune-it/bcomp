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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.EnumMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
//import ru.ifmo.cs.bcomp.Assembler;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import static ru.ifmo.cs.bcomp.ControlSignal.*;
import ru.ifmo.cs.bcomp.Reg;
import static ru.ifmo.cs.bcomp.Reg.*;
import static ru.ifmo.cs.bcomp.State.*;
//import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.SignalListener;
//import ru.ifmo.cs.bcomp.ui.io.Keyboard;
//import ru.ifmo.cs.bcomp.ui.io.Numpad;
//import ru.ifmo.cs.bcomp.ui.io.SevenSegmentDisplay;
//import ru.ifmo.cs.bcomp.ui.io.TextPrinter;
//import ru.ifmo.cs.bcomp.ui.io.Ticker;
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
	private final long[] delayPeriods = { 0, 1, 5, 10, 25, 50, 100, 1000 };
	private volatile int currentDelay = 3;

	private final BasicComp bcomp;
	private final CPU cpu;
	//private final IOCtrl[] ioctrls;
	private EnumMap<Reg, RegisterView> regs = new EnumMap<Reg, RegisterView>(Reg.class);
	private final SignalListener[] listeners;

	/*
	private BasicIOView io1 = null;
	private BasicIOView io2 = null;
	private BasicIOView io3 = null;
	private TextPrinter textPrinter = null;
	private Ticker ticker = null;
	private SevenSegmentDisplay ssd = null;
	private Keyboard kbd = null;
	private Numpad numpad = null;
	private GUI pairgui = null;
	*/

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
		private final Register reg;
		private final BitView[] bits;

		private RegisterView(Reg r, Register reg) {
			super(new FlowLayout(FlowLayout.RIGHT, 0, 0));

			this.reg = reg;
			JLabel label = new JLabel(r.toString());
			label.setFont(LABEL_FONT);
			add(label);

			bits = new BitView[(int)reg.width];

			for (long i = reg.width - 1; i >= 0; i--)
				add(bits[(int)i] = new BitView(reg, (int)i));
		}

		public void setValue(long value) {
			for (int i = 0; i < reg.width; bits[i++].repaint());
		}

		private void invertBit(int startbit) {
			reg.invertBit(startbit);
			bits[startbit].repaint();
		}
	}
/*
	private class BasicIOView {
		private final IOCtrl ioctrl;
		private final JFrame frame;
		private final RegisterView data;
		private final RegisterView flag;

		private BasicIOView(IOCtrl ioctrls[], int number) {
			this.ioctrl = ioctrls[number];
			data = new RegisterView(ioctrl.getRegData());
			flag = new RegisterView(ioctrl.getRegFlag());

			ioctrl.addDestination(IOCtrl.ControlSignal.SETFLAG, flag);
			if (ioctrl.getDirection() != IOCtrl.Direction.IN)
				ioctrl.addDestination(IOCtrl.ControlSignal.OUT, data);

			JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
			panel.add(flag);
			panel.add(data);

			frame = new JFrame("Контроллер ВУ" + number);
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
						ioctrl.setFlag();
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

		// XXX: Говнокод
		private void invertBit(int startbit) {
			if (ioctrl.getDirection() != IOCtrl.Direction.OUT)
				data.invertBit(startbit);
		}

		private void activate() {
			frame.setVisible(true);
			frame.requestFocus();
		}
	}
*/

	public Nightmare() throws Exception {
		this.bcomp = new BasicComp();
		this.cpu = bcomp.getCPU();
		//this.ioctrls = bcomp.getIOCtrls();

		/*
		try {
			String code = System.getProperty("code", null);
			File file = new File(code);
			FileInputStream fin = null;

			try {
				fin = new FileInputStream(file);
				byte content[] = new byte[(int)file.length()];
				fin.read(content);
				code = new String(content, Charset.forName("UTF-8"));
				// XXX: Handle exceptions from assembler
				Assembler asm = new Assembler(cpu.getInstructionSet());
				asm.compileProgram(code);
				asm.loadProgram(cpu);
			} finally {
				if (fin != null)
					fin.close();
			}
		} catch (Exception e) { }
		*/

		for (Reg reg : Reg.values())
			if ((reg != MP) && (reg != MR))
				regs.put(reg, new RegisterView(reg, cpu.getRegister(reg)));

		listeners = new SignalListener[] {
			new SignalListener(regs.get(DR), WRDR, LOAD),
			new SignalListener(regs.get(CR), WRCR),
			new SignalListener(regs.get(IP), WRIP),
			new SignalListener(regs.get(SP), WRSP),
			new SignalListener(regs.get(AC), WRAC),
			new SignalListener(regs.get(BR), WRBR),
			new SignalListener(regs.get(PS), SETC, SETV, STNZ, DINT, EINT, HALT),
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

		JFrame frame = new JFrame("БЭВМ");
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
		JLabel hints = new JLabel(
			"F4 Ввод адреса F5 Запись F6 Чтение F7 Пуск F8 Продолжение F9 Работа/Останов Shift-F9 Такт");
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
/*
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
						if (textPrinter == null)
							textPrinter = new TextPrinter(ioctrls[4]);
						textPrinter.activate();
						break;

					case KeyEvent.VK_5:
					case KeyEvent.VK_F5:
						if (ticker == null)
							ticker = new Ticker(ioctrls[5]);
						ticker.activate();
						break;

					case KeyEvent.VK_6:
					case KeyEvent.VK_F6:
						if (ssd == null)
							ssd = new SevenSegmentDisplay(ioctrls[6]);
						ssd.activate();
						break;

					case KeyEvent.VK_7:
					case KeyEvent.VK_F7:
						if (kbd == null)
							kbd = new Keyboard(ioctrls[7]);
						kbd.activate();
						break;

					case KeyEvent.VK_8:
					case KeyEvent.VK_F8:
						if (numpad == null)
							numpad = new Numpad(ioctrls[8]);
						numpad.activate();
						break;
*/
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
					regs.get(PS).bits[RUN.ordinal()].repaint();
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
