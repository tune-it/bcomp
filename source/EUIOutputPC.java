import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import Machine.EChannelFactory;
import Machine.EFlagFactory;
import Machine.EMemory;
import Machine.EMicrocommandMemory;
import Machine.ERegisterFactory;

public class EUIOutputPC extends JComponent
{
	public EUIOutputPC ()
	{
		
	}
	
	public void paintComponent(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D) g;
		
		ERegisterFactory regfact = new ERegisterFactory();
		EFlagFactory flagfact = new EFlagFactory(regfact);
		EMemory Mem = new EMemory(regfact);
		EMicrocommandMemory Micromem = new EMicrocommandMemory(regfact);
		EChannelFactory channfact = new EChannelFactory(regfact, Mem, Micromem);
		EUIBasePCFabric ololo = new EUIBasePCFabric(regfact, flagfact, channfact);
		EUIRegister[] regs =  ololo.CreateOutputRegisters();
	
		for (int i=0; i<regs.length; i++)
		{
			regs[i].Draw(g2);
		}
		
		EUIMemory mem = ololo.CreateСlassicMemory();
		int x[] = {32023, 1, 1234, 242, 1024, 65535, 1234, 6542, 1234, 5678, 1234, 3242, 18, 1234, 52425, 1234,1234, 5678, 1234, 3242, 1311, 1245, 1234, 9765, 1234, 5678, 1234, 3242, 1245, 1234, 9765, 1234};
		mem.SetMemory(x);
		mem.Draw(g2);
		mem.LoadMem(g2);
		
		g2.setFont(new Font("Courier New", Font.BOLD, 25));
		g2.drawString("Регистры", 197, 25);
		g2.drawString("процессора", 183, 50);
	}
}
