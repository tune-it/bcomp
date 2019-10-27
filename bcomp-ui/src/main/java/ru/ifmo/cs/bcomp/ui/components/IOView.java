/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.CPU;

import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.SignalListener;
import ru.ifmo.cs.bcomp.ui.GUI;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;
import ru.ifmo.cs.bcomp.ui.io.BComp2BCompIODev;
import ru.ifmo.cs.bcomp.ui.io.Keyboard;
import ru.ifmo.cs.bcomp.ui.io.Numpad;
import ru.ifmo.cs.bcomp.ui.io.SevenSegmentDisplay;
import ru.ifmo.cs.bcomp.ui.io.TextPrinter;
import ru.ifmo.cs.bcomp.ui.io.Ticker;


/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class IOView  {
//	private class FlagButtonListener implements ActionListener {
//		private final IOCtrl ioctrl;
//
//		public FlagButtonListener(IOCtrl ioctrl) {
//			this.ioctrl = ioctrl;
//		}
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			ioctrl.setFlag();
//		}
//	}
//
//	private class FlagListener implements DataDestination {
//		private final JButton flag;
//
//		public FlagListener(JButton flag) {
//			this.flag = flag;
//		}
//
//		@Override
//		public void setValue(int value) {
//			flag.setForeground(value == 1 ? COLOR_ACTIVE : COLOR_TEXT);
//		}
//	}
//
//	private final IOCtrl[] ioctrls;
//	private TextPrinter textPrinter = null;
//	private Ticker ticker = null;
//	private SevenSegmentDisplay ssd = null;
//	private Keyboard kbd = null;
//	private Numpad numpad = null;
//	private GUI pairgui = null;
//	private final RegisterView[] ioregs = new RegisterView[3];
//	private final JButton[] flags = {
//		new JButton("F1 ВУ1"),
//		new JButton("F2 ВУ2"),
//		new JButton("F3 ВУ3")
//	};
//	private final BusView[] intrBuses = {
//		new BusView(new int[][] {
//			{IO1_CENTER, BUS_INTR_Y1},
//			{IO1_CENTER, BUS_INTR_Y},
//			{BUS_INTR_LEFT_X, BUS_INTR_Y}
//		}),
//		new BusView(new int[][] {
//			{IO2_CENTER, BUS_INTR_Y1},
//			{IO2_CENTER, BUS_INTR_Y},
//			{BUS_INTR_LEFT_X, BUS_INTR_Y}
//		}),
//		new BusView(new int[][] {
//			{IO3_CENTER, BUS_INTR_Y1},
//			{IO3_CENTER, BUS_INTR_Y},
//			{BUS_INTR_LEFT_X, BUS_INTR_Y}
//		})
//	};
//
//	public IOView(final GUI gui, final GUI _pairgui) {
//		super(gui.getComponentManager(),
//			new RegisterProperties[] {
//				new RegisterProperties(CPU.Reg.ADDR, CU_X_IO, REG_ADDR_Y_IO, true),
//				new RegisterProperties(CPU.Reg.IP, CU_X_IO, REG_IP_Y_IO, true),
//				new RegisterProperties(CPU.Reg.DATA, CU_X_IO, REG_DATA_Y_IO, true),
//				new RegisterProperties(CPU.Reg.INSTR, CU_X_IO, REG_INSTR_Y_IO, true),
//				new RegisterProperties(CPU.Reg.ACCUM, REG_ACC_X_IO, REG_ACCUM_Y_IO, true),
//				new RegisterProperties(CPU.Reg.STATE, CU_X_IO, REG_ACCUM_Y_IO, false)
//			},
//			new BusView[] {
//				new BusView(new int[][] {
//					{IO1_CENTER, BUS_TSF_Y2},
//					{IO1_CENTER, BUS_TSF_Y},
//					{BUS_TSF_X, BUS_TSF_Y},
//					{BUS_TSF_X, BUS_TSF_Y1}
//				}, ControlSignal.IO1_TSF),
//				new BusView(new int[][] {
//					{IO2_CENTER, BUS_TSF_Y2},
//					{IO2_CENTER, BUS_TSF_Y},
//					{BUS_TSF_X, BUS_TSF_Y},
//					{BUS_TSF_X, BUS_TSF_Y1}
//				}, ControlSignal.IO2_TSF),
//				new BusView(new int[][] {
//					{IO3_CENTER, BUS_TSF_Y2},
//					{IO3_CENTER, BUS_TSF_Y},
//					{BUS_TSF_X, BUS_TSF_Y},
//					{BUS_TSF_X, BUS_TSF_Y1}
//				}, ControlSignal.IO3_TSF),
//				new BusView(new int[][] {
//					{IO1_CENTER, BUS_IO_ADDR_Y},
//					{IO1_CENTER, BUS_IO_ADDR_Y1}
//				}, ControlSignal.INPUT_OUTPUT),
//				new BusView(new int[][] {
//					{IO2_CENTER, BUS_IO_ADDR_Y},
//					{IO2_CENTER, BUS_IO_ADDR_Y1}
//				}, ControlSignal.INPUT_OUTPUT),
//				new BusView(new int[][] {
//					{BUS_IO_ADDR_X, BUS_IO_ADDR_Y2},
//					{BUS_TSF_X, BUS_IO_ADDR_Y2},
//					{BUS_TSF_X, BUS_IO_ADDR_Y},
//					{IO3_CENTER, BUS_IO_ADDR_Y},
//					{IO3_CENTER, BUS_IO_ADDR_Y1}
//				}, ControlSignal.INPUT_OUTPUT),
//				new BusView(new int[][] {
//					{IO1_CENTER, BUS_IO_REQ_Y},
//					{IO1_CENTER, BUS_IO_REQ_Y1}
//				}, ControlSignal.INPUT_OUTPUT),
//				new BusView(new int[][] {
//					{IO2_CENTER, BUS_IO_REQ_Y},
//					{IO2_CENTER, BUS_IO_REQ_Y1}
//				}, ControlSignal.INPUT_OUTPUT),
//				new BusView(new int[][] {
//					{BUS_IO_ADDR_X, BUS_IO_REQ_Y},
//					{IO3_CENTER, BUS_IO_REQ_Y},
//					{IO3_CENTER, BUS_IO_REQ_Y1}
//				}, ControlSignal.INPUT_OUTPUT),
//				new BusView(new int[][] {
//					{IO2_CENTER, BUS_IN_Y2},
//					{IO2_CENTER, BUS_IN_Y},
//					{BUS_TSF_X, BUS_IN_Y},
//					{BUS_TSF_X, BUS_IN_Y1},
//					{BUS_IN_X, BUS_IN_Y1}
//				}, ControlSignal.IO2_IN),
//				new BusView(new int[][] {
//					{IO3_CENTER, BUS_IN_Y2},
//					{IO3_CENTER, BUS_IN_Y},
//					{BUS_TSF_X, BUS_IN_Y},
//					{BUS_TSF_X, BUS_IN_Y1},
//					{BUS_IN_X, BUS_IN_Y1}
//				}, ControlSignal.IO3_IN),
//				new BusView(new int[][] {
//					{BUS_OUT_X, BUS_OUT_Y},
//					{IO1_CENTER, BUS_OUT_Y},
//					{IO1_CENTER, BUS_OUT_Y2}
//				}, ControlSignal.IO1_OUT),
//				new BusView(new int[][] {
//					{BUS_OUT_X, BUS_OUT_Y},
//					{IO3_CENTER, BUS_OUT_Y},
//					{IO3_CENTER, BUS_OUT_Y2}
//				}, ControlSignal.IO3_OUT)
//			}
//		);
//
//		pairgui = _pairgui;
//		ioctrls = gui.getIOCtrls();
//
//		JButton button = new JButton("ВУ4");
//		button.setFont(FONT_COURIER_PLAIN_12);
//		button.setBounds(IO1_CENTER, REG_KEY_Y, FLAG_WIDTH, CELL_HEIGHT);
//		button.setFocusable(false);
//		button.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (textPrinter == null)
//					textPrinter = new TextPrinter(ioctrls[4]);
//				textPrinter.activate();
//			}
//		});
//		add(button);
//
//		button = new JButton("ВУ5");
//		button.setFont(FONT_COURIER_PLAIN_12);
//		button.setBounds(IO2_CENTER, REG_KEY_Y, FLAG_WIDTH, CELL_HEIGHT);
//		button.setFocusable(false);
//		button.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (ticker == null)
//					ticker = new Ticker(ioctrls[5]);
//				ticker.activate();
//			}
//		});
//		add(button);
//
//		button = new JButton("ВУ6");
//		button.setFont(FONT_COURIER_PLAIN_12);
//		button.setBounds(IO3_CENTER - 30, REG_KEY_Y, FLAG_WIDTH, CELL_HEIGHT);
//		button.setFocusable(false);
//		button.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (ssd == null)
//					ssd = new SevenSegmentDisplay(ioctrls[6]);
//				ssd.activate();
//			}
//		});
//		add(button);
//
//		button = new JButton("ВУ7");
//		button.setFont(FONT_COURIER_PLAIN_12);
//		button.setBounds(IO1_CENTER, REG_KEY_Y + 30, FLAG_WIDTH, CELL_HEIGHT);
//		button.setFocusable(false);
//		button.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (kbd == null)
//					kbd = new Keyboard(ioctrls[7]);
//				kbd.activate();
//			}
//		});
//		add(button);
//
//		button = new JButton("ВУ8");
//		button.setFont(FONT_COURIER_PLAIN_12);
//		button.setBounds(IO2_CENTER, REG_KEY_Y + 30, FLAG_WIDTH, CELL_HEIGHT);
//		button.setFocusable(false);
//		button.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (numpad == null)
//					numpad = new Numpad(ioctrls[8]);
//				numpad.activate();
//			}
//		});
//		add(button);
//
//		button = new JButton("ВУ9");
//		button.setFont(FONT_COURIER_PLAIN_12);
//		button.setBounds(IO3_CENTER - 30, REG_KEY_Y + 30, FLAG_WIDTH, CELL_HEIGHT);
//		button.setFocusable(false);
//		button.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (pairgui == null) {
//					try {
//						pairgui = new GUI(gui);
//						pairgui.gui();
//						BComp2BCompIODev b2b = new BComp2BCompIODev(
//							gui.getBasicComp().getIOCtrls()[9],
//							pairgui.getBasicComp().getIOCtrls()[9]);
//					} catch (Exception ex) { }
//				}
//				gui.requestFocus();
//			}
//		});
//		add(button);
//
//		for (int i = 0; i < ioregs.length; i++) {
//			int x = IO_X + i * IO_DELIM;
//
//			ioregs[i] = i == 0 ?
//				new RegisterView(ioctrls[i + 1].getRegData()) :
//				(new InputRegisterView(cmanager, ioctrls[i + 1].getRegData()));
//			ioregs[i].setProperties(x, IO_DATA_Y, false);
//			add(ioregs[i]);
//
//			flags[i].setFont(FONT_COURIER_PLAIN_12);
//			flags[i].setBounds(x + FLAG_OFFSET, FLAG_Y, FLAG_WIDTH, CELL_HEIGHT);
//			flags[i].setFocusable(false);
//			add(flags[i]);
//			flags[i].addActionListener(new FlagButtonListener(ioctrls[i + 1]));
//			ioctrls[i + 1].addDestination(IOCtrl.ControlSignal.SETFLAG, new FlagListener(flags[i]));
//
//			add(new BCompLabel(x, DECODER_Y, REG_8_WIDTH, "Дешифратор", "адреса и", "приказов"));
//		}
//
//		add(new BCompLabel(CU_X_IO, CU_Y_IO, REG_8_WIDTH, "Устройство", "управления"));
//
//		addLabel("Запрос прерывания", LABEL_INTR_Y);
//		addLabel("Состояние флага ВУ", LABEL_TSF_Y);
//		addLabel("Адрес ВУ", LABEL_ADDR_Y);
//		addLabel("Приказ на ввод/вывод", LABEL_REQ_Y);
//		addLabel("Шина ввода", LABEL_IN_Y);
//		addLabel("Шина вывода", LABEL_OUT_Y);
//
//		DataDestination intrListener = new DataDestination() {
//			@Override
//			public void setValue(int value) {
//				drawIntrBuses(getGraphics());
//			}
//		};
//
//		setSignalListeners(new SignalListener[] {
//			new SignalListener(ioregs[0], ControlSignal.IO1_OUT),
//			new SignalListener(ioregs[2], ControlSignal.IO3_OUT),
//			new SignalListener(intrListener, ControlSignal.IO1_SETFLAG),
//			new SignalListener(intrListener, ControlSignal.IO2_SETFLAG),
//			new SignalListener(intrListener, ControlSignal.IO3_SETFLAG)
//		});
//	}
//
//	private void addLabel(String text, int y) {
//		JLabel l = new JLabel(text, JLabel.CENTER);
//		l.setFont(FONT_COURIER_BOLD_18);
//		l.setBounds(IO1_CENTER, y, IO3_CENTER - IO1_CENTER, ELEMENT_DELIM);
//		add(l);
//	}
//
//	private void drawIntrBuses(Graphics g) {
//		for (int i = 0; i < 3; i++)
//			if (ioctrls[i + 1].getFlag() == 0)
//				intrBuses[i].draw(g, COLOR_BUS);
//
//		for (int i = 0; i < 3; i++)
//			if (ioctrls[i + 1].getFlag() == 1)
//				intrBuses[i].draw(g, COLOR_ACTIVE);
//	}
//
//	@Override
//	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		drawIntrBuses(g);
//	}
//
//	@Override
//	public String getPanelName() {
//		return "Работа с ВУ";
//	}
//
//	@Override
//	public void stepFinish() {
//		super.stepFinish();
//
//		for (ControlSignal signal : cmanager.getActiveSignals())
//			switch (signal) {
//				case IO1_OUT:
//					ioregs[0].setValue();
//					break;
//
//				case IO3_OUT:
//					ioregs[2].setValue();
//					break;
//			}
//	}
}
