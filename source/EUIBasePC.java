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
	
	ERegisterFactory regfact = new ERegisterFactory(11,16,11,16,16,16,17,16,13);
	EUIBasePCFabric ololo = new EUIBasePCFabric(regfact);
	EUIRegister[] regs =  ololo.CreateBinRegisters();
		
	EUIChannel[] chns =  ololo.CreateChannels();
	for (int i=0; i<chns.length; i++)
	{
		if (chns[i].GetConnect() == false)
		chns[i].Draw(g2);
	}
	for (int i=0; i<chns.length; i++)
	{
		if (chns[i].GetConnect())
		chns[i].Draw(g2);
	}
	
	for (int i=0; i<regs.length; i++)
	{
		regs[i].DrawBin(g2);
	}
	
	EUIAlu alu = ololo.CreateBinAlu();
	alu.Draw(g2);
	
	EUIMemory mem = ololo.CreateMemory();
	mem.Draw(g2);
	mem.LoadMem(g2);
}
}
