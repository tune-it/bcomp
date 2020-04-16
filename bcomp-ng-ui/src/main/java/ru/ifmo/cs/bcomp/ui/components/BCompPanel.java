/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

import ru.ifmo.cs.bcomp.ControlSignal;

import ru.ifmo.cs.bcomp.SignalListener;

import javax.swing.*;

import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.COLOR_ACTIVE;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.COLOR_BUS;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public abstract class BCompPanel extends ActivateblePanel {
	protected final ComponentManager cmanager;
	private final RegisterProperties[] regProps;
	protected Map<BusNames, BusView> busesMap;

	private SignalListener[] listeners;
	protected RegPanel regPanel;


	public BCompPanel(ComponentManager cmanager, RegisterProperties[] regProps, Map<BusNames, BusView> baseMap) {
		setLayout(new BorderLayout());
		this.cmanager = cmanager;
		this.regProps = regProps;
		this.busesMap = baseMap;
		regPanel = new RegPanel();
	}
	protected class RegPanel extends JComponent {
		RegPanel() {
			setLayout(new GridBagLayout());
		}
	}

	protected void setSignalListeners(SignalListener[] listeners) {
		this.listeners = listeners;
	}

	protected SignalListener[] getSignalListeners() {
		return listeners;
	}

	private void drawBuses(Graphics g) {
		ArrayList<BusView> openbuses = new ArrayList<BusView>();
		ArrayList<ControlSignal> signals = cmanager.getActiveSignals();

		for (BusView bus : busesMap.values()) {
			for (ControlSignal signal : bus.getSignals())
				if (signals.contains(signal))
					openbuses.add(bus);

			bus.draw(g, COLOR_BUS);
		}

		for (BusView bus : openbuses)
			bus.draw(g, COLOR_ACTIVE);
	}

	private void drawOpenBuses(Color color) {
		Graphics g = getGraphics();
		ArrayList<ControlSignal> signals = cmanager.getActiveSignals();

		for (BusView bus : busesMap.values())
			for (ControlSignal signal : bus.getSignals())
				if (signals.contains(signal))
					bus.draw(g, color);
	}

	public void stepStart() {
		drawOpenBuses(COLOR_BUS);
	}

	public void stepFinish() {
		drawOpenBuses(COLOR_ACTIVE);
	}

	@Override
	public void panelActivate() {

		cmanager.panelActivate(this);

		for (RegisterProperties prop : regProps) {
			RegisterView reg = cmanager.getRegisterView(prop.reg);
			reg.setProperties(prop.x, prop.y, prop.hex,prop.isLeft);
			reg.setPreferredSize(reg.getSize());
			reg.setTitle(prop.reg.toString());
			regPanel.add(reg, prop.constraints);
		}
	}

	@Override
	public void panelDeactivate() {
		cmanager.panelDeactivate();
	}

	@Override
	public void paintComponent(Graphics g) {
		drawBuses(g);
	}
}
