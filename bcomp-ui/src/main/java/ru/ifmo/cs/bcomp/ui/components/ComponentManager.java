/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.EnumMap;
import javax.swing.*;

import ru.ifmo.cs.bcomp.*;
import ru.ifmo.cs.bcomp.ui.GUI;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;


import ru.ifmo.cs.components.DataDestination;
import ru.ifmo.cs.components.Register;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class ComponentManager {
	private class SignalHandler implements DataDestination {
		private final ControlSignal signal;

		public SignalHandler(ControlSignal signal) {
			this.signal = signal;
		}

		public void setValue(long value) {
			openBuses.add(signal);
		}
	}

	private class ButtonProperties {
		 final String[] texts;
		public final ActionListener listener;

		public ButtonProperties(String[] texts, ActionListener listener) {

			this.texts = texts;
			this.listener = listener;
		}
	}
	private JRadioButton rbRanStop;
	private JRadioButton rbTact;
	private class ButtonsPanel extends JComponent {
		public ButtonsPanel() {
			setBounds(0, BUTTONS_Y, PANE_WIDTH, BUTTONS_HEIGHT);
			setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints() {{
				anchor = GridBagConstraints.WEST;
				fill=GridBagConstraints.HORIZONTAL;
				gridx = 0;
				gridy = 0;
				weightx = 1;
				insets=new Insets(1,1,1,1);
			}};
			buttons = new JButton[buttonProperties.length];

			for (int i = 0; i < buttons.length-2; i++) {
				buttons[i] = new JButton(buttonProperties[i].texts[0]);
				buttons[i].setForeground(buttonColors[0]);
				buttons[i].setFont(FONT_COURIER_PLAIN_12);
				buttons[i].setFocusable(false);
				buttons[i].addActionListener(buttonProperties[i].listener);
				buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
				constraints.gridwidth=i==0?2:1;
				if (i>0) constraints.gridy=1;
				if (i==2) constraints.gridx=0;
				add(buttons[i], constraints);
				if (i==2) constraints.gridx+=3; else   constraints.gridx++;

			}
			constraints.gridy=0;
			constraints.gridx=3;
			constraints.fill=GridBagConstraints.NONE;
			constraints.anchor=GridBagConstraints.CENTER;
			rbRanStop=new JRadioButton(buttonProperties[5].texts[0]);
			rbRanStop.setFont(FONT_COURIER_PLAIN_12);
			rbRanStop.setBackground(new Color(200, 221, 242));
			rbRanStop.setBorderPainted(false);
			rbRanStop.addActionListener(buttonProperties[5].listener);
			rbRanStop.setCursor(new Cursor(Cursor.HAND_CURSOR));
			rbRanStop.setFocusPainted(false);
			add(rbRanStop,constraints);
			constraints.gridx++;

			rbTact=new JRadioButton((buttonProperties[6].texts[0]));
			rbTact.setFont(FONT_COURIER_PLAIN_12);
			rbTact.setBackground(new Color(200, 221, 242));
			rbTact.setBorderPainted(false);
			rbTact.addActionListener(buttonProperties[6].listener);
			rbTact.setCursor(new Cursor(Cursor.HAND_CURSOR));
			rbTact.setFocusPainted(false);

			add(rbTact,constraints);
		}
	}

	private Color[] buttonColors = new Color[] { COLOR_TEXT, COLOR_ACTIVE };
	private ButtonProperties[] buttonProperties = {
		new ButtonProperties(new String[] { "F4 Ввод адреса" }, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				cmdEnterAddr();
			}
		}),
		new ButtonProperties( new String[] { "F5 Запись" }, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				cmdWrite();
			}
		}),
		new ButtonProperties( new String[] { "F6 Чтение" }, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				cmdRead();
			}
		}),
		new ButtonProperties( new String[] { "F7 Пуск" }, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				cmdStart();
			}
		}),
		new ButtonProperties( new String[] { "F8 Продолжение" }, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				cmdContinue();
			}
		}),
		new ButtonProperties( new String[] { "F9 Останов", "F9  Работа" }, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdInvertRunState();
			}
		}),
		new ButtonProperties(new String[] { "Shift+F9 Такт" }, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				cmdInvertClockState();
			}
		})
	};

	private final KeyAdapter keyListener = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_F4:
					cmdEnterAddr();
					break;

				case KeyEvent.VK_F5:
					cmdWrite();
					break;

				case KeyEvent.VK_F6:
					cmdRead();
					break;

				case KeyEvent.VK_F7:
					cmdStart();
					break;

				case KeyEvent.VK_F8:
					cmdContinue();
					break;

				case KeyEvent.VK_F9:
					if (e.isShiftDown())
						cmdInvertClockState();
					else
						cmdInvertRunState();
					break;

				case KeyEvent.VK_F10:
					System.exit(0);
					break;

				case KeyEvent.VK_F11:
					cmdPrevDelay();
					break;

				case KeyEvent.VK_F12:
					cmdNextDelay();
					break;

				case KeyEvent.VK_Q:
					if (e.isControlDown())
						System.exit(0);
					break;
			}
		}
	};

	private static final int BUTTON_RUN = 5;
	private static final int BUTTON_CLOCK = 6;
	private JButton[] buttons;
	private ButtonsPanel buttonsPanel = new ButtonsPanel();

	private final GUI gui;
	private final BasicComp bcomp;
	private final CPU cpu;
	private final IOCtrl[] ioctrls;
	private final MemoryView mem;
	private FlagView[] flagViews=new FlagView[4];
	private EnumMap<Reg, RegisterView> regs = new EnumMap<Reg, RegisterView>(Reg.class);
	private RegisterView input2=new RegisterView(new Register(16));
	private ActiveBitView activeBit = new ActiveBitView(ACTIVE_BIT_X, REG_KEY_Y);
	private volatile BCompPanel activePanel;
	private final long[] delayPeriods = { 0, 1, 5, 10, 25, 50, 100, 1000 };
	private volatile int currentDelay = 3;
	private volatile int savedDelay;
	private final Object lockActivePanel = new Object();

	private volatile boolean cuswitch = false;
	private final SignalListener[] listeners;
	private ArrayList<ControlSignal> openBuses = new ArrayList<ControlSignal>();
	private static final ControlSignal[] busSignals = {
		ControlSignal.RDDR,
		ControlSignal.RDCR,
		ControlSignal.RDIP,
		ControlSignal.RDAC,
		ControlSignal.RDPS,
		ControlSignal.RDIR,
		ControlSignal.WRAR,
		ControlSignal.WRDR,
		ControlSignal.WRCR,
		ControlSignal.WRIP,
		ControlSignal.WRAC,
		ControlSignal.LOAD,
		ControlSignal.STOR,
		ControlSignal.WRPS,
		ControlSignal.IO,
	};

	public ComponentManager(GUI gui) {
		this.gui = gui;
		bcomp = gui.getBasicComp();
		cpu = gui.getCPU();
		ioctrls = gui.getIOCtrls();

		cpu.setTickStartListener(new Runnable() {
			@Override
			public void run() {
				synchronized (lockActivePanel) {
					if (activePanel != null)
						activePanel.stepStart();
				}

				openBuses.clear();
			}
		});

		cpu.setTickFinishListener(new Runnable() {
			@Override
			public void run() {
				synchronized (lockActivePanel) {
					if (activePanel != null)
						activePanel.stepFinish();
				}

				if (delayPeriods[currentDelay] != 0)
					try {
						Thread.sleep(delayPeriods[currentDelay]);
					} catch (InterruptedException e) {
					}
			}
		});

		for (ControlSignal cs : busSignals)
			cpu.addDestination(cs, new SignalHandler(cs));

		for(int i=0;i<4;i++) {
			flagViews[i]=new FlagView(0,0,25,25);
			flagViews[i].setPreferredSize(flagViews[i].getSize());
		}
		flagViews[0].setTitle("N");flagViews[1].setTitle("Z");flagViews[2].setTitle("V");flagViews[3].setTitle("C");

		for (Reg reg : Reg.values()) {
			switch (reg) {
				case IR:
					InputRegisterView regKey = new InputRegisterView(this, cpu.getRegister(reg));
					regs.put(reg, regKey);
					regKey.setProperties(REG_KEY_X, REG_KEY_Y, false, false);
					break;


				default:
					regs.put(reg, new RegisterView(cpu.getRegister(reg)));
			}
		}



		listeners = new SignalListener[] {
			new SignalListener(regs.get(Reg.AR), ControlSignal.WRAR),
			new SignalListener(regs.get(Reg.DR), ControlSignal.WRDR, ControlSignal.LOAD),
			new SignalListener(regs.get(Reg.CR), ControlSignal.WRCR),
			new SignalListener(regs.get(Reg.IP), ControlSignal.WRIP),
			new SignalListener(regs.get(Reg.AC), ControlSignal.WRAC),
			new SignalListener(regs.get(Reg.PS),ControlSignal.RDPS,ControlSignal.WRPS,ControlSignal.WRAC),

		};



		mem = new MemoryView(cpu.getMemory(), MEM_X, MEM_Y);


		cpu.addDestination(ControlSignal.LOAD, new DataDestination() {

			public void setValue(long value) {
				if (activePanel != null)
					mem.eventRead();
				else
					mem.updateLastAddr();
			}
		});



		cpu.addDestination(ControlSignal.SETC, new DataDestination() {
			@Override
			public void setValue(long value) {
				flagViews[3].setActive(regs.get(Reg.PS).getReg().getValue(0)==1);
			}
		});

		cpu.addDestination(ControlSignal.SETV, new DataDestination() {
			@Override
			public void setValue(long value) {
				flagViews[2].setActive(regs.get(Reg.PS).getReg().getValue(1)==1);
			}
		});
		cpu.addDestination(ControlSignal.STNZ, new DataDestination() {
			@Override
			public void setValue(long value) {
				flagViews[1].setActive(regs.get(Reg.PS).getReg().getValue(2)==1);
			}
		});
		cpu.addDestination(ControlSignal.STNZ, new DataDestination() {
			@Override
			public void setValue(long value) {
				flagViews[0].setActive(regs.get(Reg.PS).getReg().getValue(3)==1);
			}
		});



		cpu.addDestination(ControlSignal.STOR, new DataDestination() {

			public void setValue(long value) {
				if (activePanel != null)
					mem.eventWrite();
				else
					mem.updateLastAddr();
			}
		});







	}

	public void panelActivate(BCompPanel component) {
		synchronized (lockActivePanel) {
			activePanel = component;
			bcomp.addDestination(listeners);
		}
		buttonsPanel.setPreferredSize(buttonsPanel.getSize());


		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridheight=2;
		constraints.insets=new Insets(0,0,0,30);

		input2.setProperties(0,0,false,true);
		input2.setTitle("IR");
		input2.setPreferredSize(input2.getSize());

		regs.get(Reg.IR).setPreferredSize(regs.get(Reg.IR).getSize());
		regs.get(Reg.IR).setMinimumSize(regs.get(Reg.IR).getSize());
		buttonsPanel.add(regs.get(Reg.IR), constraints);


		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(0, REG_16_WIDTH+26, 0, 20);//помещаем бит рядом с кл регистром
		activeBit.setPreferredSize(activeBit.getSize());
		activeBit.setMinimumSize(activeBit.getSize());
		buttonsPanel.add(activeBit, constraints);
		mem.setPreferredSize(mem.getSize());

		component.add(buttonsPanel, BorderLayout.SOUTH);



		mem.updateMemory();
		cuswitch = false;

		switchFocus();
	}

	public void panelDeactivate() {

	}

	public void keyPressed(KeyEvent e) {
		keyListener.keyPressed(e);
	}

	public void switchFocus() {
		((InputRegisterView)regs.get(Reg.IR)).setActive();
	}

	public RegisterView getRegisterView(Reg reg) {
		return regs.get(reg);
	}

	public FlagView getFlagView(int i) {
		return flagViews[i];
	}

	public void cmdContinue() {
		cpu.startContinue();
	}

	public void cmdEnterAddr() {

			cpu.startSetAddr();
	}

	public void cmdWrite() {

			cpu.startWrite();
	}

	public void cmdRead() {

			cpu.startRead();
	}

	public void cmdStart() {
		cpu.startStart();
	}

	public void cmdInvertRunState() {
		cpu.invertRunState();
		long state=	cpu.getProgramState(State.RUN);
		rbRanStop.setSelected(state==1);
		rbRanStop.setText(buttonProperties[BUTTON_RUN].texts[(int)state]);
	}

	public void cmdInvertClockState() {
		boolean state = cpu.invertClockState();
		rbTact.setSelected(!state);
	}


	public void cmdNextDelay() {
		currentDelay = currentDelay < delayPeriods.length - 1 ? currentDelay + 1 : 0;
	}

	public void cmdPrevDelay() {
		currentDelay = (currentDelay > 0 ? currentDelay : delayPeriods.length) - 1;
	}

	public void saveDelay() {
		savedDelay = currentDelay;
		currentDelay = 0;
	}

	public void restoreDelay() {
		currentDelay = savedDelay;
	}

//	public void cmdAbout() {
//		JOptionPane.showMessageDialog(gui,
//			"Эмулятор Базовой ЭВМ. Версия r" + GUI.class.getPackage().getImplementationVersion() +
//			"\n\nЗагружена " + gui.getMicroProgramName() + " микропрограмма",
//			"О программе", JOptionPane.INFORMATION_MESSAGE);
//	}





	public ActiveBitView getActiveBit() {
		return activeBit;
	}

	public KeyListener getKeyListener() {
		return keyListener;
	}

	public ArrayList<ControlSignal> getActiveSignals() {
		return openBuses;
	}

	public void clearActiveSignals() {
		openBuses.clear();
	}
	public MemoryView getMem(){
		return mem;
	}
	public RegisterView getInput2(){
		return input2;
	}
}
