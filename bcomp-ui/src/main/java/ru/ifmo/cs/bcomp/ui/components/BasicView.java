/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.ui.GUI;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BasicView extends BCompPanel {
	private final CPU cpu;
	private final RunningCycleView cycleview;

	public BasicView(GUI gui) {
		super(gui.getComponentManager(),
			new RegisterProperties[] { 
				new RegisterProperties(CPU.Reg.ADDR, REG_ACCUM_X_BV, REG_ADDR_Y_BV, false),
				new RegisterProperties(CPU.Reg.DATA, REG_ACCUM_X_BV, REG_DATA_Y_BV, false),
				new RegisterProperties(CPU.Reg.IP, REG_IP_X_BV, REG_IP_Y_BV, false),
				new RegisterProperties(CPU.Reg.INSTR, REG_INSTR_X_BV, REG_ADDR_Y_BV, false),
				new RegisterProperties(CPU.Reg.ACCUM, REG_ACCUM_X_BV, REG_ACCUM_Y_BV, false),
				new RegisterProperties(CPU.Reg.STATE, REG_C_X_BV, REG_ACCUM_Y_BV, false)
			},
			new BusView[] {
				new BusView(new int[][] {
					{BUS_RIGHT_X1, BUS_FROM_DATA_Y},
					{BUS_RIGHT_X1, BUS_LEFT_INPUT_UP},
					{BUS_RIGHT_X, BUS_LEFT_INPUT_UP},
					{BUS_RIGHT_X, BUS_LEFT_INPUT_DOWN}
				}, ControlSignal.DATA_TO_ALU),
				new BusView(new int[][] {
					{BUS_FROM_INSTR_X, BUS_FROM_INSTR_Y},
					{BUS_FROM_INSTR_X, BUS_LEFT_INPUT_UP},
					{BUS_RIGHT_X, BUS_LEFT_INPUT_UP},
					{BUS_RIGHT_X, BUS_LEFT_INPUT_DOWN}
				}, ControlSignal.INSTR_TO_ALU),
				new BusView(new int[][] {
					{BUS_FROM_IP_X, BUS_FROM_IP_Y},
					{BUS_RIGHT_X1, BUS_FROM_IP_Y},
					{BUS_RIGHT_X1, BUS_LEFT_INPUT_UP},
					{BUS_RIGHT_X, BUS_LEFT_INPUT_UP},
					{BUS_RIGHT_X, BUS_LEFT_INPUT_DOWN}
				}, ControlSignal.IP_TO_ALU),
				new BusView(new int[][] {
					{BUS_FROM_ACCUM_X, BUS_FROM_ACCUM_Y},
					{BUS_LEFT_INPUT_X1, BUS_FROM_ACCUM_Y},
					{BUS_LEFT_INPUT_X1, BUS_LEFT_INPUT_UP},
					{BUS_LEFT_INPUT_X, BUS_LEFT_INPUT_UP},
					{BUS_LEFT_INPUT_X, BUS_LEFT_INPUT_DOWN}
				}, ControlSignal.ACCUM_TO_ALU),
				new BusView(new int[][] {
					{BUS_LEFT_INPUT_X1, BUS_KEY_ALU},
					{BUS_LEFT_INPUT_X1, BUS_LEFT_INPUT_UP},
					{BUS_LEFT_INPUT_X, BUS_LEFT_INPUT_UP},
					{BUS_LEFT_INPUT_X, BUS_LEFT_INPUT_DOWN}
				}, ControlSignal.KEY_TO_ALU),
				new BusView(new int[][] {
					{FROM_ALU_X, FROM_ALU_Y},
					{FROM_ALU_X, FROM_ALU_Y1},
					{BUS_RIGHT_TO_X, FROM_ALU_Y1},
					{BUS_RIGHT_TO_X, BUS_TO_ADDR_Y},
					{BUS_TO_ADDR_X, BUS_TO_ADDR_Y}
				}, ControlSignal.BUF_TO_ADDR),
				new BusView(new int[][] {
					{FROM_ALU_X, FROM_ALU_Y},
					{FROM_ALU_X, FROM_ALU_Y1},
					{BUS_RIGHT_TO_X, FROM_ALU_Y1},
					{BUS_RIGHT_TO_X, BUS_TO_DATA_Y},
					{BUS_TO_DATA_X, BUS_TO_DATA_Y}
				}, ControlSignal.BUF_TO_DATA),
				new BusView(new int[][] {
					{FROM_ALU_X, FROM_ALU_Y},
					{FROM_ALU_X, FROM_ALU_Y1},
					{BUS_RIGHT_TO_X, FROM_ALU_Y1},
					{BUS_RIGHT_TO_X, BUS_TO_ADDR_Y},
					{BUS_TO_INSTR_X, BUS_TO_ADDR_Y}
				}, ControlSignal.BUF_TO_INSTR),
				new BusView(new int[][] {
					{FROM_ALU_X, FROM_ALU_Y},
					{FROM_ALU_X, FROM_ALU_Y1},
					{BUS_RIGHT_TO_X, FROM_ALU_Y1},
					{BUS_RIGHT_TO_X, BUS_FROM_IP_Y},
					{BUS_TO_DATA_X, BUS_FROM_IP_Y}
				}, ControlSignal.BUF_TO_IP),
				new BusView(new int[][] {
					{FROM_ALU_X, FROM_ALU_Y},
					{FROM_ALU_X, TO_ACCUM_Y}
				}, ControlSignal.BUF_TO_ACCUM),
				new BusView(new int[][] {
					{BUS_ADDR_X1, BUS_TO_ADDR_Y},
					{BUS_ADDR_X2, BUS_TO_ADDR_Y}
				}, ControlSignal.MEMORY_READ, ControlSignal.MEMORY_WRITE),
				new BusView(new int[][] {
					{BUS_READ_X2, BUS_READ_Y},
					{BUS_READ_X1, BUS_READ_Y}
				}, ControlSignal.MEMORY_READ),
				new BusView(new int[][] {
					{BUS_ADDR_X1, BUS_WRITE_Y},
					{BUS_ADDR_X2, BUS_WRITE_Y}
				}, ControlSignal.MEMORY_WRITE),
				new BusView(new int[][] {
					{BUS_INSTR_TO_CU_X, BUS_FROM_INSTR_Y},
					{BUS_INSTR_TO_CU_X, BUS_INSTR_TO_CU_Y}
				})
			}
		);

		cpu = gui.getCPU();

		setSignalListeners(new SignalListener[] { });

		add(new ALUView(REG_C_X_BV, ALU_Y, ALU_WIDTH, ALU_HEIGHT));

		cycleview = new RunningCycleView(cpu, REG_INSTR_X_BV, CYCLEVIEW_Y);
		add(cycleview);
	}

	@Override
	public void panelActivate() {
		cycleview.update();
		super.panelActivate();
	}

	@Override
	public String getPanelName() {
		return "Базовая ЭВМ";
	}

	@Override
	public void stepFinish() {
		super.stepFinish();
		cycleview.update();
	}
}
