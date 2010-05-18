import java.awt.Font;
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
		
		
		ERegisterFactory regfact = new ERegisterFactory();
		EFlagFactory flagfact = new EFlagFactory(regfact);
		EUIBasePCFabric ololo = new EUIBasePCFabric(regfact, flagfact);
		EUIRegister[] regs =  ololo.CreateMicroRegisters();
		EUIChannel[] channels = ololo.CreateMicroChannels();
		
		for (int i=0; i<channels.length; i++)
		{
			channels[i].Draw(g2);
		}
		
		EUIAlu alu = ololo.CreateHexAlu();
		alu.Draw(g2);
		
		for (int i=0; i<regs.length; i++)
		{
			regs[i].Draw(g2);
		}
	
		g2.setFont(new Font("Courier New", Font.BOLD, 21));
		g2.drawString("UXAKPWFIEONZC", 217, 403);
		
		EUIMemory mem = ololo.CreateMemory();
		int x[] = {32023, 1, 1234, 242, 1024, 65535, 1234, 6542, 1234, 5678, 1234, 3242, 18, 1234, 52425, 1234,1234, 5678, 1234, 3242, 1311, 1245, 1234, 9765, 1234, 5678, 1234, 3242, 1245, 1234, 9765, 1234};
		mem.SetMemory(x);
		mem.Draw(g2);
		mem.LoadMem(g2);
		
		EUIMemory micromem = ololo.CreateMicroMemory();
		int x1[] = {123, 3442, 32000, 125, 1, 23, 22345, 9564, 4321, 1234, 0, 11187, 2, 1415, 1354, 1863};
		micromem.SetAdressWidth(8);
		micromem.SetMemory(x1);
		micromem.SetSeparator(53);
		micromem.Draw(g2);
		micromem.LoadMem(g2);
	}
}
