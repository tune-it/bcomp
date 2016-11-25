/*
 * $Id: BCompApp.java 319 2012-09-29 17:28:34Z MATPOCKuH $
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
import ru.ifmo.cs.bcomp.Assembler;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.MicroProgram;
import ru.ifmo.cs.bcomp.SignalListener;
import ru.ifmo.cs.bcomp.ui.io.Keyboard;
import ru.ifmo.cs.bcomp.ui.io.Numpad;
import ru.ifmo.cs.bcomp.ui.io.SevenSegmentDisplay;
import ru.ifmo.cs.bcomp.ui.io.TextPrinter;
import ru.ifmo.cs.bcomp.ui.io.Ticker;
import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.Register;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class Hardcore {
	private static final Color LED_OFF = new Color(128, 128, 128);
	private static final Color LED_ON = new Color(0, 160, 0);
	private static final int BIT_RADIUS = 16;
	private static final Font LABEL_FONT = new Font("Courier New", Font.BOLD, 24);
	private static final Font HINTS_FONT = new Font("Courier New", Font.BOLD, 14);
	private final long[] delayPeriods = { 0, 1, 5, 10, 25, 50, 100, 1000 };
	private volatile int currentDelay = 3;

	private final BasicComp bcomp;
	private final CPU cpu;
	private final IOCtrl[] ioctrls;
	private EnumMap<CPU.Reg, RegisterView> regs = new EnumMap<CPU.Reg, RegisterView>(CPU.Reg.class);
	private final SignalListener[] listeners;

	private TextPrinter textPrinter = null;
	private Ticker ticker = null;
	private SevenSegmentDisplay ssd = null;
	private Keyboard kbd = null;
	private Numpad numpad = null;
	private GUI pairgui = null;

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

		private RegisterView(Register reg) {
			super(new FlowLayout(FlowLayout.CENTER, 0, 0));

			JLabel label = new JLabel((this.reg = reg).fullname);
			label.setFont(LABEL_FONT);
			add(label);

			bits = new BitView[reg.width];

			for (int i = reg.width - 1; i >= 0; i--)
				add(bits[i] = new BitView(reg, i));
		}

		public void setValue(int value) {
			for (int i = 0; i < reg.width; bits[i++].repaint());
		}

		private void invertBit(int startbit) {
			reg.invertBit(startbit);
			bits[startbit].repaint();
		}
	}

	public Hardcore(MicroProgram mp) throws Exception {
		String code = null;
		FileInputStream fin = null;

		this.bcomp = new BasicComp(mp);
		this.cpu = bcomp.getCPU();
		this.ioctrls = bcomp.getIOCtrls();

		try {
			code = System.getProperty("code", null);
		} catch (Exception e) { }

		if (code != null) {
			File file = new File(code);
			try {
				fin = new FileInputStream(file);
				byte content[] = new byte[(int)file.length()];
				fin.read(content);
				code = new String(content, Charset.forName("UTF-8"));
				Assembler asm = new Assembler(cpu.getInstructionSet());
				asm.compileProgram(code);
				asm.loadProgram(cpu);
			} finally {
				if (fin != null)
					fin.close();
			}
		}

		for (CPU.Reg reg : CPU.Reg.values())
			regs.put(reg, new RegisterView(cpu.getRegister(reg)));

		listeners = new SignalListener[] {
			new SignalListener(regs.get(CPU.Reg.STATE),
				ControlSignal.BUF_TO_STATE_C,
				ControlSignal.CLEAR_STATE_C,
				ControlSignal.SET_STATE_C,
				ControlSignal.HALT,
				ControlSignal.BUF_TO_STATE_N,
				ControlSignal.BUF_TO_STATE_Z,
				ControlSignal.DISABLE_INTERRUPTS,
				ControlSignal.ENABLE_INTERRUPTS,
				ControlSignal.IO0_TSF,
				ControlSignal.IO1_TSF,
				ControlSignal.IO2_TSF,
				ControlSignal.IO3_TSF,
				ControlSignal.IO4_TSF,
				ControlSignal.IO5_TSF,
				ControlSignal.IO6_TSF,
				ControlSignal.IO7_TSF,
				ControlSignal.IO8_TSF,
				ControlSignal.IO9_TSF,
				ControlSignal.SET_RUN_STATE,
				ControlSignal.SET_PROGRAM,
				ControlSignal.SET_REQUEST_INTERRUPT),
			new SignalListener(regs.get(CPU.Reg.ADDR), ControlSignal.BUF_TO_ADDR),
			new SignalListener(regs.get(CPU.Reg.DATA), ControlSignal.BUF_TO_DATA, ControlSignal.MEMORY_READ),
			new SignalListener(regs.get(CPU.Reg.INSTR), ControlSignal.BUF_TO_INSTR),
			new SignalListener(regs.get(CPU.Reg.IP), ControlSignal.BUF_TO_IP),
			new SignalListener(regs.get(CPU.Reg.ACCUM),
				ControlSignal.BUF_TO_ACCUM,
				ControlSignal.IO2_IN,
				ControlSignal.IO3_IN,
				ControlSignal.IO7_IN,
				ControlSignal.IO8_IN,
				ControlSignal.IO9_IN
			),
			new SignalListener(regs.get(CPU.Reg.BUF),
				ControlSignal.ALU_AND,
				ControlSignal.SHIFT_RIGHT,
				ControlSignal.SHIFT_LEFT
			)
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

		JPanel panel = new JPanel(new GridLayout(9, 1));
		panel.add(regs.get(CPU.Reg.ADDR));
		panel.add(regs.get(CPU.Reg.DATA));
		panel.add(regs.get(CPU.Reg.INSTR));
		panel.add(regs.get(CPU.Reg.IP));
		panel.add(regs.get(CPU.Reg.BUF));
		panel.add(regs.get(CPU.Reg.ACCUM));
		panel.add(regs.get(CPU.Reg.STATE));
		panel.add(regs.get(CPU.Reg.KEY));
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
					regs.get(CPU.Reg.KEY).invertBit(0);
					break;

				case KeyEvent.VK_1:
					regs.get(CPU.Reg.KEY).invertBit(1);
					break;

				case KeyEvent.VK_2:
					regs.get(CPU.Reg.KEY).invertBit(2);
					break;

				case KeyEvent.VK_3:
					regs.get(CPU.Reg.KEY).invertBit(3);
					break;

				case KeyEvent.VK_4:
					regs.get(CPU.Reg.KEY).invertBit(4);
					break;

				case KeyEvent.VK_5:
					regs.get(CPU.Reg.KEY).invertBit(5);
					break;

				case KeyEvent.VK_6:
					regs.get(CPU.Reg.KEY).invertBit(6);
					break;

				case KeyEvent.VK_7:
					regs.get(CPU.Reg.KEY).invertBit(7);
					break;

				case KeyEvent.VK_8:
					regs.get(CPU.Reg.KEY).invertBit(8);
					break;

				case KeyEvent.VK_9:
					regs.get(CPU.Reg.KEY).invertBit(9);
					break;

				case KeyEvent.VK_A:
					regs.get(CPU.Reg.KEY).invertBit(10);
					break;

				case KeyEvent.VK_B:
					regs.get(CPU.Reg.KEY).invertBit(11);
					break;

				case KeyEvent.VK_C:
					regs.get(CPU.Reg.KEY).invertBit(12);
					break;

				case KeyEvent.VK_D:
					regs.get(CPU.Reg.KEY).invertBit(13);
					break;

				case KeyEvent.VK_E:
					regs.get(CPU.Reg.KEY).invertBit(14);
					break;

				case KeyEvent.VK_F:
					regs.get(CPU.Reg.KEY).invertBit(15);
					break;

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
		//frame.setResizable(false);
		frame.setVisible(true);
		frame.requestFocus();
	}
}
