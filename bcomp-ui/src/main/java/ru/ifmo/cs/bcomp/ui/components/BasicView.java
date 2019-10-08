/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.SignalListener;
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
				new RegisterProperties(CPU.Reg.BUF,REG_C_X_BV+1,REG_DATA_Y_BV,false),
				new RegisterProperties(CPU.Reg.ADDR, MEM_X-REG_11_WIDTH/5, REG_ADDR_Y_BV, false),
				new RegisterProperties(CPU.Reg.DATA, REG_DATA_X_BV, REG_DATA_Y_BV, false),
				new RegisterProperties(CPU.Reg.IP, REG_DATA_X_BV+REG_16_WIDTH-REG_11_WIDTH, REG_IP_Y_BV, false),
				new RegisterProperties(CPU.Reg.INSTR, REG_DATA_X_BV, REG_INSTR_Y_BV, false),
				new RegisterProperties(CPU.Reg.ACCUM, REG_ACCUM_X_BV, REG_IP_Y_BV, false),
				new RegisterProperties(CPU.Reg.STATE, REG_C_X_BV, REG_IP_Y_BV, false),
				new RegisterProperties(CPU.Reg.SP, REG_DATA_X_BV+REG_16_WIDTH-REG_11_WIDTH,REG_KEY_Y,false )
			},
			new BusView[] {
					new BusView(new int[][] {//из бр
							{BUS_LEFT_X-9,  BUS_FROM_DATA_Y},
							{BUS_LEFT_X,  BUS_FROM_DATA_Y},
							{BUS_LEFT_X, BUS_LEFT_INPUT_DOWN},
					}),// TODO: 08.10.2019 сделать сигнал для бр
					new BusView(new int[][] {//в бр
							{FROM_ALU_X, FROM_ALU_Y},
							{FROM_ALU_X, FROM_ALU_Y1},
							{REG_C_X_BV-15, FROM_ALU_Y1},
							{REG_C_X_BV-15, BUS_FROM_DATA_Y},
							{REG_C_X_BV-ARROW, BUS_FROM_DATA_Y},
					}),// TODO: 08.10.2019 сделать сигнал для бр
				new BusView(new int[][] {
					{BUS_FROM_INSTR_X+20,BUS_FROM_DATA_Y},//из рд
					{BUS_RIGHT_X1, BUS_FROM_DATA_Y},
					{BUS_RIGHT_X1, BUS_LEFT_INPUT_DOWN},
				}, ControlSignal.DATA_TO_ALU),
				new BusView(new int[][] {
						{BUS_FROM_INSTR_X+20,BUS_FROM_INSTR_Y},//из рк
						{BUS_RIGHT_X1, BUS_FROM_INSTR_Y},
						{BUS_RIGHT_X1, BUS_LEFT_INPUT_DOWN},
				}, ControlSignal.INSTR_TO_ALU),
					new BusView(new int[][] {
							{REG_DATA_X_BV+REG_16_WIDTH-REG_11_WIDTH+20,BUS_FROM_INSTR_Y-44},//из sp
							{BUS_RIGHT_X1, BUS_FROM_IP_Y},
							{BUS_RIGHT_X1, BUS_LEFT_INPUT_DOWN},
					}), // TODO: 08.10.2019 cделать сигнал для sp
				new BusView(new int[][] {		//из ск
						{REG_DATA_X_BV+REG_16_WIDTH-REG_11_WIDTH+20,BUS_FROM_IP_Y},//из ск
						{BUS_RIGHT_X1, BUS_FROM_IP_Y},
						{BUS_RIGHT_X1, BUS_LEFT_INPUT_DOWN},
				}, ControlSignal.IP_TO_ALU),
				new BusView(new int[][] {//из а
						{BUS_LEFT_X-8,  BUS_FROM_IP_Y},
						{BUS_LEFT_X,  BUS_FROM_IP_Y},
						{BUS_LEFT_X, BUS_LEFT_INPUT_DOWN},
				}, ControlSignal.ACCUM_TO_ALU),
				new BusView(new int[][] {//из кр
						{BUS_LEFT_X-8,  BUS_FROM_INSTR_Y-44},
						{BUS_LEFT_X,  BUS_FROM_INSTR_Y-44},
						{BUS_LEFT_X, BUS_LEFT_INPUT_DOWN},
				}, ControlSignal.KEY_TO_ALU),
				new BusView(new int[][] { //в ра
					{MEM_X+REG_9_WIDTH/2, MEM_Y+17*CELL_HEIGHT+5},
					{MEM_X+REG_9_WIDTH/2, REG_ADDR_Y_BV-ARROW+1},

				}, ControlSignal.BUF_TO_ADDR),
				new BusView(new int[][] {//в рд
						{FROM_ALU_X, FROM_ALU_Y},
						{FROM_ALU_X, FROM_ALU_Y1},

						{BUS_TO_INSTR_X, FROM_ALU_Y1},
						{BUS_TO_INSTR_X, BUS_TO_DATA_Y},
						{BUS_TO_INSTR_X-10, BUS_TO_DATA_Y}
				}, ControlSignal.BUF_TO_DATA),
				new BusView(new int[][] {//в рк
					{FROM_ALU_X, FROM_ALU_Y},
					{FROM_ALU_X, FROM_ALU_Y1},
					{BUS_TO_INSTR_X, FROM_ALU_Y1},
					{BUS_TO_INSTR_X, BUS_FROM_INSTR_Y},
					{BUS_TO_INSTR_X-10, BUS_FROM_INSTR_Y}
				}, ControlSignal.BUF_TO_INSTR),
					new BusView(new int[][]{//в sp
							{FROM_ALU_X, FROM_ALU_Y},
							{FROM_ALU_X, FROM_ALU_Y1},
							{BUS_TO_INSTR_X, FROM_ALU_Y1},
							{BUS_TO_INSTR_X, BUS_FROM_INSTR_Y-ELEMENT_DELIM-REG_HEIGHT},
							{BUS_TO_INSTR_X-10, BUS_FROM_INSTR_Y-ELEMENT_DELIM-REG_HEIGHT}
					}),// TODO: 08.10.2019 cделать сигнал для sp
				new BusView(new int[][] {//в ск
						{FROM_ALU_X, FROM_ALU_Y},
						{FROM_ALU_X, FROM_ALU_Y1},
						{BUS_TO_INSTR_X, FROM_ALU_Y1},
						{BUS_TO_INSTR_X, BUS_FROM_IP_Y},
						{BUS_TO_INSTR_X-10, BUS_FROM_IP_Y}
				}, ControlSignal.BUF_TO_IP),
				new BusView(new int[][] {//в а
					{FROM_ALU_X, FROM_ALU_Y},
					{FROM_ALU_X, FROM_ALU_Y1},
						{REG_C_X_BV-15, FROM_ALU_Y1},
						{REG_C_X_BV-15, REG_ACCUM_X_BV+48},
						{REG_C_X_BV-ARROW, REG_ACCUM_X_BV+48},
				}, ControlSignal.BUF_TO_ACCUM),
				new BusView(new int[][] {//из ра
					{BUS_ADDR_X1, FROM_ALU_Y1},
					{BUS_ADDR_X1, REG_ADDR_Y_BV-15}
				}, ControlSignal.MEMORY_READ, ControlSignal.MEMORY_WRITE),
				new BusView(new int[][] {//в рд
						 {REG_DATA_X_BV+REG_16_WIDTH-REG_11_WIDTH+320, MEM_Y},
						{REG_DATA_X_BV+REG_16_WIDTH-REG_11_WIDTH+320, MEM_Y-30},
						{REG_DATA_X_BV+REG_16_WIDTH-REG_11_WIDTH, REG_DATA_Y_BV-30},
						{REG_DATA_X_BV+REG_16_WIDTH-REG_11_WIDTH, REG_DATA_Y_BV-15},

				}, ControlSignal.MEMORY_READ),
				new BusView(new int[][] {//из рд
					{REG_DATA_X_BV+REG_16_WIDTH-REG_11_WIDTH+20, REG_DATA_Y_BV-5},
					{REG_DATA_X_BV+REG_16_WIDTH-REG_11_WIDTH+20, REG_DATA_Y_BV-15},
					{REG_DATA_X_BV+REG_16_WIDTH-REG_11_WIDTH+300, REG_DATA_Y_BV-15},
					{REG_DATA_X_BV+REG_16_WIDTH-REG_11_WIDTH+300, REG_DATA_Y_BV-10},

				}, ControlSignal.MEMORY_WRITE),
				new BusView(new int[][] {
					{BUS_INSTR_TO_CU_X+70, BUS_FROM_INSTR_Y},
					{BUS_INSTR_TO_CU_X+70, BUS_INSTR_TO_CU_Y-80}
				})
			}
		);

		cpu = gui.getCPU();

		setSignalListeners(new SignalListener[] { });

		add(new ALUView(REG_C_X_BV+240, ALU_Y, ALU_WIDTH, ALU_HEIGHT));

		cycleview = new RunningCycleView(cpu, REG_INSTR_X_BV+95, CYCLEVIEW_Y-80);
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
