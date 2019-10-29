/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import ru.ifmo.cs.bcomp.*;

import ru.ifmo.cs.bcomp.ui.GUI;
import ru.ifmo.cs.components.DataDestination;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;

import static ru.ifmo.cs.bcomp.ControlSignal.*;
import static ru.ifmo.cs.bcomp.ui.components.DisplayStyles.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BasicView extends BCompPanel {
	private final CPU cpu;
	private final RunningCycleView cycleview;

	private final ALUView alu;
	private final CommutView commutView;
	public BasicView(GUI gui) {

		super(gui.getComponentManager(),
			new RegisterProperties[] {
				new RegisterProperties(Reg.AR, REG_ACCUM_X_BV, REG_ADDR_Y_BV, false,false,
						new GridBagConstraints() {{
							fill = GridBagConstraints.NONE;
							gridy = 5;
							gridx = 5;
							weightx = 0.5;
							weighty = 0.5;
							gridwidth = 1;
							anchor = GridBagConstraints.NORTH;
							insets = new Insets(0, 0, 0, 0);
						}}),
				new RegisterProperties(Reg.DR, REG_ACCUM_X_BV, REG_DATA_Y_BV, false, false,

						new GridBagConstraints() {{
							fill = GridBagConstraints.NONE;
							gridy = 0;
							gridx = 4;
							weightx = 0.5;
							weighty = 0.5;
							insets = new Insets(60, 40, 0, 0);
							// anchor = GridBagConstraints.WEST;

						}}

						),
				new RegisterProperties(Reg.IP, REG_IP_X_BV, REG_IP_Y_BV, false,false,
						new GridBagConstraints() {{
							fill = GridBagConstraints.NONE;
							gridy = 2;
							gridx = 4;
							weightx = 0.5;
							weighty = 0.5;
							insets = new Insets(0, 0, 0, 37);
							//  anchor = GridBagConstraints.WEST;

						}}
						),
				new RegisterProperties(Reg.CR, REG_INSTR_X_BV, REG_ADDR_Y_BV, false,false,
						new GridBagConstraints() {{
							fill = GridBagConstraints.NONE;
							gridy = 1;
							gridx = 4;
							weightx = 0.5;
							weighty = 0.5;
							//anchor = GridBagConstraints.WEST;
							insets = new Insets(0, 40, 0, 0);
						}}
						),
				new RegisterProperties(Reg.AC, REG_ACCUM_X_BV, REG_ACCUM_Y_BV, false,true,
						new GridBagConstraints() {{
							fill = GridBagConstraints.NONE;
							gridy = 0;
							gridx = 3;
							weighty = 0.5;
							weightx=0.5;
							// anchor=EAST;
							insets = new Insets(60, 23, 0, 60);
						}}),
					new RegisterProperties(Reg.SP, REG_ACCUM_X_BV, 0, false,false,
							new GridBagConstraints() {{
								fill = GridBagConstraints.NONE;
								gridy = 3;
								gridx = 4;
								weighty = 0.5;
								insets = new Insets(0, 0, 80, 37);
							}}),
					new RegisterProperties(Reg.BR,0,0,false,true,
							new GridBagConstraints(){{
						gridy=1;
						gridx=3;
						insets=new Insets(0,3,0,40);
					}}),
					new RegisterProperties(Reg.PS,0,0,false, true,
							new GridBagConstraints(){{
						gridy=2;
						gridx=3;
						insets=new Insets(0,3,0,40);
					}})
			},
				new HashMap<String, BusView>() {{
					put("BR_2_ALU", new BusView( RDBR));
					put("ALU_2_BR", new BusView(WRBR));
					put("PS_2_ALU", new BusView(RDPS));
					put("ALU_2_PS", new BusView( WRPS));
					put("COMM_2_ALL", new BusView( WRBR, WRAC,WRIP,WRCR,WRDR,WRAR,WRPS,WRSP));
					put("ALU_2_COMM", new BusView(WRBR, WRAC,WRIP,WRCR,WRDR,WRAR,WRPS,WRSP ));
					put("DR_2_ALU", new BusView( RDDR));
					put("CR_2_ALU", new BusView( RDCR));
					put("IP_2_ALU", new BusView(RDIP));
					put("SP_2_ALU", new BusView( RDSP));
					put("AC_2_ALU", new BusView( RDAC));
					put("IR_2_ALU", new BusView(RDIR));
					put("ALU_2_AR", new BusView( WRAR));
					put("ALU_2_DR", new BusView( WRDR));
					put("ALU_2_CR", new BusView( WRCR));
					put("ALU_2_IP", new BusView( WRIP));
					put("ALU_2_SP", new BusView(WRSP));
					put("ALU_2_AC", new BusView( WRAC));
					put("MEM_IO", new BusView( LOAD, STOR));
					put("MEM_R", new BusView( LOAD));
					put("MEM_W", new BusView(STOR));
					put("CU", new BusView());

				}}
		);
		add(regPanel,BorderLayout.CENTER);
		cpu = gui.getCPU();

		setSignalListeners(new SignalListener[] { });

		GridBagConstraints constraintsALU = new GridBagConstraints() {{
			gridx =3;
			gridy = 4;
			gridwidth=2;
			weightx=0.5;
			weighty=0.5;
			anchor = GridBagConstraints.NORTH;
			insets = new Insets(0, 30, 100, 0);
		}};
		alu=new ALUView(REG_C_X_BV, ALU_Y, ALU_WIDTH, ALU_HEIGHT);
		alu.setPreferredSize(alu.getSize());
		regPanel.add(alu,constraintsALU);

		GridBagConstraints constraintsComm = new GridBagConstraints() {{
			gridx =3;
			gridy = 4;
			gridwidth=2;
			weightx=0.5;
			weighty=0.5;

			anchor = GridBagConstraints.NORTH;
			insets = new Insets(ALU_HEIGHT+17, 30, 0, 0);
		}};
		commutView=new CommutView(0,0,150,30);
		commutView.setPreferredSize(commutView.getSize());
		regPanel.add(commutView,constraintsComm);

		GridBagConstraints constraintsCycle = new GridBagConstraints() {{
			gridx = 4;
			gridy = 3;

			gridheight=2;

			anchor = GridBagConstraints.CENTER;
			insets = new Insets(20, 90, 0, 0);
		}};


		cycleview = new RunningCycleView(cpu, REG_INSTR_X_BV, CYCLEVIEW_Y);
		cycleview.setPreferredSize(cycleview.getSize());
		regPanel.add(cycleview, constraintsCycle);

		GridBagConstraints constraintMem = new GridBagConstraints() {{
			gridx = 5;
			gridy = 0;
			gridheight=5;
			weighty=0;
			anchor = GridBagConstraints.CENTER;
			insets = new Insets(0, 0, 0, 0);
		}};

		regPanel.add(cmanager.getMem(),constraintMem);


		GridBagConstraints constraintsF  = new GridBagConstraints() {{
			fill = GridBagConstraints.NONE;
			gridy = 4;
			gridx = 3;
			gridwidth=2;
			weightx = 0.5;
			weighty = 0;
			anchor=NORTH;
			insets = new Insets(101+CELL_HEIGHT, 0, 0, 20);
		}};



		regPanel.add(cmanager.getFlagView(1),constraintsF);
		constraintsF.insets.right+=60;
		regPanel.add(cmanager.getFlagView(0),constraintsF);
		constraintsF.insets.right=0;constraintsF.insets.left=140;
		regPanel.add(cmanager.getFlagView(3),constraintsF);
		constraintsF.insets=new Insets(101+CELL_HEIGHT, 80, 0, 0);
		regPanel.add(cmanager.getFlagView(2),constraintsF);

		cpu.addDestination(SET_PROGRAM, new DataDestination() {
			@Override
			public void setValue(long value) {
				cycleview.updateProg(true);
			}
		});

		cpu.addDestination(HALT, new DataDestination() {
			@Override
			public void setValue(long value) {
				cycleview.updateProg(false);
			}
		});

		GridBagConstraints constraintsIN2=new GridBagConstraints(){{
			gridy=3;
			gridx=3;
			insets=new Insets(0,3,80,40);
		}};
		regPanel.add(cmanager.getInput2(),constraintsIN2);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				redrawArrows();
			}
		});
	redrawArrows();
	}



	@Override
	public void stepStart() {
		cycleview.update();
		super.stepStart();
	}

	@Override
	public String getPanelName() {
		return "Базовая ЭВМ";
	}

	@Override
	public void stepFinish() {
		super.stepFinish();
	}

	public void redrawArrows() {
		busesMap.keySet().forEach(key -> {
			BusView bus = busesMap.get(key);
			RegisterView data = cmanager.getRegisterView(Reg.DR);
			RegisterView addr = cmanager.getRegisterView(Reg.AR);
			RegisterView instr = cmanager.getRegisterView(Reg.CR);
			RegisterView accum = cmanager.getRegisterView(Reg.AC);
			RegisterView keyReg = cmanager.getRegisterView(Reg.IR);
			RegisterView ipReg = cmanager.getRegisterView(Reg.IP);
			RegisterView spReg = cmanager.getRegisterView(Reg.SP);
			RegisterView buf = cmanager.getRegisterView(Reg.BR);
			RegisterView rs = cmanager.getRegisterView(Reg.PS);
			int regPanelX  = regPanel.getX();
			switch (key) {
				case "DR_2_ALU":
					bus.calcBounds(new int[][]{
							{regPanelX+data.getX()-5 , data.getY() + REG_HEIGHT/2},
							{regPanelX+alu.getX()+ALU_WIDTH*5/8,data.getY() +REG_HEIGHT/2},
							{regPanelX+alu.getX()+ALU_WIDTH*5/8, alu.getY()-13},
					});
					break;
				case "CR_2_ALU":
					bus.calcBounds(new int[][]{
							{regPanelX+instr.getX() -5, instr.getY() + REG_HEIGHT/2},
							{regPanelX+alu.getX()+ALU_WIDTH*5/8,instr.getY() +REG_HEIGHT/2},
							{regPanelX+alu.getX()+ALU_WIDTH*5/8, alu.getY()-13},
					});
					break;
				case "IP_2_ALU":
					bus.calcBounds(new int[][]{
							{regPanelX+ipReg.getX()-5 , ipReg.getY() + REG_HEIGHT/2},
							{regPanelX+alu.getX()+ALU_WIDTH*5/8,ipReg.getY() +REG_HEIGHT/2},
							{regPanelX+alu.getX()+ALU_WIDTH*5/8, alu.getY()-13},
					});
					break;
				case "SP_2_ALU":
					bus.calcBounds(new int[][]{
							{regPanelX+spReg.getX()-5 , spReg.getY() + REG_HEIGHT/2},
							{regPanelX+alu.getX()+ALU_WIDTH*5/8,spReg.getY() +REG_HEIGHT/2},
							{regPanelX+alu.getX()+ALU_WIDTH*5/8, alu.getY()-13},
					});
					break;
				case "AC_2_ALU":
					bus.calcBounds(new int[][]{
							{regPanelX+accum.getX()+REG_16_WIDTH+4 , accum.getY() +REG_HEIGHT/2},
							{regPanelX+alu.getX()+ALU_WIDTH*3/8, accum.getY() +REG_HEIGHT/2},
							{regPanelX+alu.getX()+ ALU_WIDTH*3/8, alu.getY()-13},
					});
					break;
				case "BR_2_ALU":
					bus.calcBounds(new int[][]{
							{regPanelX+buf.getX()+REG_16_WIDTH+4 , buf.getY() +REG_HEIGHT/2},
							{regPanelX+alu.getX()+ALU_WIDTH*3/8, buf.getY() +REG_HEIGHT/2},
							{regPanelX+alu.getX()+ ALU_WIDTH*3/8, alu.getY()-13},
					});
					break;
				case "PS_2_ALU":
					bus.calcBounds(new int[][]{
							{regPanelX+rs.getX()+REG_16_WIDTH+4 , rs.getY() +REG_HEIGHT/2},
							{regPanelX+alu.getX()+ALU_WIDTH*3/8, rs.getY() +REG_HEIGHT/2},
							{regPanelX+alu.getX()+ ALU_WIDTH*3/8, alu.getY()-13},
					});
					break;


				case "IR_2_ALU":
					bus.calcBounds(new int[][]{
							{regPanelX+accum.getX()+REG_16_WIDTH+4, cmanager.getInput2().getY() +REG_HEIGHT/2},
							{regPanelX+alu.getX()+ALU_WIDTH*3/8, cmanager.getInput2().getY() +REG_HEIGHT/2},
							{regPanelX+alu.getX()+ ALU_WIDTH*3/8, alu.getY()-13},

					});
					break;
				case "ALU_2_AR":
					bus.calcBounds(new int[][]{

							{regPanelX+alu.getX()+ ALU_WIDTH / 2,commutView.getY()+commutView.getHeight()+4},
							{regPanelX+alu.getX()+ ALU_WIDTH / 2,addr.getY()+REG_HEIGHT/2},
							{addr.getX()-13,addr.getY()+REG_HEIGHT/2},

					});
					break;
				case "ALU_2_DR":
					bus.calcBounds(new int[][]{

							{regPanelX+alu.getX()+ ALU_WIDTH / 2,commutView.getY()+commutView.getHeight()+4},
							{regPanelX+alu.getX()+ ALU_WIDTH / 2,alu.getY() + ALU_HEIGHT + 90},
							{ regPanelX+data.getX()  + REG_16_WIDTH + 20,alu.getY() + ALU_HEIGHT + 90},
							{regPanelX+data.getX()  + REG_16_WIDTH + 20, data.getY() + REG_HEIGHT / 2},
							{regPanelX+data.getX()  + REG_16_WIDTH + 13, data.getY() + REG_HEIGHT / 2}
					});
					break;
				case "ALU_2_CR":
					bus.calcBounds(new int[][]{

							{regPanelX+alu.getX()+ ALU_WIDTH / 2,commutView.getY()+commutView.getHeight()+4},

							{regPanelX+alu.getX()+ ALU_WIDTH / 2,alu.getY() + ALU_HEIGHT + 90},

							{regPanelX+instr.getX()  + REG_16_WIDTH + 20 ,alu.getY() + ALU_HEIGHT + 90},

							{regPanelX+instr.getX()  + REG_16_WIDTH + 20, instr.getY() + REG_HEIGHT / 2},
							{regPanelX+instr.getX()  + REG_16_WIDTH + 13, instr.getY() + REG_HEIGHT / 2}
					});
					break;
				case "ALU_2_IP":
					bus.calcBounds(new int[][]{

							{regPanelX+alu.getX()+ ALU_WIDTH / 2,commutView.getY()+commutView.getHeight()+4},
							{regPanelX+alu.getX()+ ALU_WIDTH / 2,alu.getY() + ALU_HEIGHT + 90},
							{regPanelX+instr.getX() + REG_16_WIDTH + 20,alu.getY() + ALU_HEIGHT + 90},
							{regPanelX+instr.getX()  + REG_16_WIDTH + 20, ipReg.getY() + REG_HEIGHT / 2},
							{regPanelX+ipReg.getX()  + REG_11_WIDTH + 13, ipReg.getY() + REG_HEIGHT / 2}
					});
					break;
				case "ALU_2_SP":
					bus.calcBounds(new int[][]{
							//   {regPanelX+alu.getX() + ALU_WIDTH / 2, alu.getY() + ALU_HEIGHT + 4},
							// {regPanelX+alu.getX()+ ALU_WIDTH / 2,commutView.getY()},
							{regPanelX+alu.getX()+ ALU_WIDTH / 2,commutView.getY()+commutView.getHeight()+4},
							{regPanelX+alu.getX()+ ALU_WIDTH / 2,alu.getY() + ALU_HEIGHT + 90},
							{regPanelX+instr.getX() + REG_16_WIDTH + 20,alu.getY() + ALU_HEIGHT + 90},
							{regPanelX+instr.getX()  + REG_16_WIDTH + 20, spReg.getY() + REG_HEIGHT / 2},
							{regPanelX+spReg.getX()  + REG_11_WIDTH + 13, spReg.getY() + REG_HEIGHT / 2}
					});
					break;
				case "COMM_2_ALL":
					bus.calcBounds(new int[][]{
							{regPanelX+alu.getX()+ ALU_WIDTH / 2,commutView.getY()+commutView.getHeight()+4},
							{regPanelX+alu.getX()+ ALU_WIDTH / 2,alu.getY() + ALU_HEIGHT + 74},
					});
					break;
				case "ALU_2_AC":
					bus.calcBounds(new int[][]{

							{regPanelX+alu.getX()+ ALU_WIDTH / 2,commutView.getY()+commutView.getHeight()+4},
							{regPanelX+alu.getX()+ ALU_WIDTH / 2,alu.getY() + ALU_HEIGHT + 90},
							{regPanelX+accum.getX()-20,alu.getY() + ALU_HEIGHT + 90},

							{regPanelX+accum.getX()-20, accum.getY() + REG_HEIGHT / 2},
							{regPanelX+accum.getX()-13, accum.getY() + REG_HEIGHT / 2}
					});
					break;
				case "ALU_2_COMM" :
					bus.calcBounds(new int[][]{
							{regPanelX+alu.getX()+ALU_WIDTH/2,alu.getY()+ALU_HEIGHT+4},
							{regPanelX+alu.getX()+ALU_WIDTH/2,commutView.getY()-13}
					}); break;
				case "ALU_2_BR":
					bus.calcBounds(new int[][]{
							{regPanelX+alu.getX()+ ALU_WIDTH / 2,commutView.getY()+commutView.getHeight()+4},
							{regPanelX+alu.getX()+ ALU_WIDTH / 2,alu.getY() + ALU_HEIGHT + 90},
							{regPanelX+accum.getX()-20,alu.getY() + ALU_HEIGHT + 90},

							{regPanelX+accum.getX()-20, buf.getY() + REG_HEIGHT / 2},
							{regPanelX+accum.getX()-13, buf.getY() + REG_HEIGHT / 2}
					}); break;
				case "ALU_2_PS":
					bus.calcBounds(new int[][]{
							{regPanelX+alu.getX()+ ALU_WIDTH / 2,commutView.getY()+commutView.getHeight()+4},
							{regPanelX+alu.getX()+ ALU_WIDTH / 2,alu.getY() + ALU_HEIGHT + 90},
							{regPanelX+accum.getX()-20,alu.getY() + ALU_HEIGHT + 90},

							{regPanelX+accum.getX()-20, rs.getY() + REG_HEIGHT / 2},
							{regPanelX+accum.getX()-13, rs.getY() + REG_HEIGHT / 2}
					});
					break;
				case "MEM_IO":
					bus.calcBounds(new int[][]{
							{regPanelX+cmanager.getMem().getX() + MEM_WIDTH/4, addr.getY()-5 },
							{regPanelX+cmanager.getMem().getX()+MEM_WIDTH/4, cmanager.getMem().getY() + CELL_HEIGHT*17+15},
					});
					break;
				case "MEM_R":
					bus.calcBounds(cmanager.getMem().getY()<35?
							new int[][]{
									{regPanelX+cmanager.getMem().getX()-5, cmanager.getMem().getY()+10},
									{regPanelX+data.getX() +REG_16_WIDTH/2-10, cmanager.getMem().getY()+10},
									{regPanelX+data.getX()+REG_16_WIDTH/2-10, data.getY()-13 },
							}
							:new int[][]{
							{regPanelX+cmanager.getMem().getX()+MEM_WIDTH/2+10, cmanager.getMem().getY()-5},
							{regPanelX+cmanager.getMem().getX()+MEM_WIDTH/2+10, data.getY()-65},
							{regPanelX+data.getX()+REG_16_WIDTH/2 -10, data.getY()-65},
							{regPanelX+data.getX()+REG_16_WIDTH/2-10, data.getY()-13 }
					});
					break;
				case "MEM_W":
					bus.calcBounds(
							cmanager.getMem().getY()<35?
									new int[][]{{regPanelX+data.getX()+REG_16_WIDTH/2+10, data.getY()-5 },
											{regPanelX+data.getX() + REG_16_WIDTH/2+10, cmanager.getMem().getY()+25},
											{regPanelX+cmanager.getMem().getX()-13, cmanager.getMem().getY()+25},
									}
									:
									new int[][]{
											{regPanelX+data.getX()+REG_16_WIDTH/2+10, data.getY()-5 },
											{regPanelX+data.getX()+REG_16_WIDTH/2+10, data.getY()-50},
											{regPanelX+cmanager.getMem().getX()+MEM_WIDTH/2-10, data.getY()-50},
											{regPanelX+cmanager.getMem().getX()+MEM_WIDTH/2-10, cmanager.getMem().getY()-13},
									});
					break;
				case "CU":
					bus.calcBounds(new int[][]{
							{cycleview.getX() + MEM_WIDTH*3/2 , instr.getY() + REG_HEIGHT + 1},
							{cycleview.getX() + MEM_WIDTH *3/2, cycleview.getY() - 14}
					});
					break;
			}
		});
	}
}
