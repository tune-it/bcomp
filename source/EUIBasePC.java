import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;


public class EUIBasePC extends JComponent
{
public EUIBasePC ()
{
	
}

public void paintComponent(Graphics g) 
{
	Graphics2D g2 = (Graphics2D) g;
	
	EUIBasePCFabric ololo = new EUIBasePCFabric();
	
	EUIRegister[] regs =  ololo.CreateRegisters();
		
	EUIChannel[] chns =  ololo.CreateChannels();
	for (int i=0; i<chns.length; i++)
	{
		chns[i].Draw(g2);
	}
	
	for (int i=0; i<regs.length; i++)
	{
		regs[i].Draw(g2);
		regs[i].LoadReg(g2);
	}
	
	EUIAlu alu = ololo.CreateAlu();
	alu.Draw(g2);
	
	EUIMemory mem = ololo.CreateMemory();
	mem.Draw(g2);
	mem.LoadMem(g2);
}
}
