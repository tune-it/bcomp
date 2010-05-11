import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;


public class EUIMicroPC extends JComponent
{
	public EUIMicroPC ()
	{
		
	}

	public void paintComponent(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D) g;
		
		
		ERegisterFactory regfact = new ERegisterFactory(11,16,11,16,16,16,17,16,13);
		EUIBasePCFabric ololo = new EUIBasePCFabric(regfact);
		EUIRegister[] regs =  ololo.CreateHexRegisters();
		
		EUIAlu alu = ololo.CreateHexAlu();
		alu.Draw(g2);
		
		for (int i=0; i<regs.length; i++)
		{
			regs[i].DrawHex(g2);
		}
		
		
		EUIMemory mem = ololo.CreateMemory();
		mem.Draw(g2);
		mem.LoadMem(g2);
	}
}
