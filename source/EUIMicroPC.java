import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import Machine.*;
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
		EMemory Mem = new EMemory(regfact);
		EMicrocommandMemory Micromem = new EMicrocommandMemory(regfact);
		EChannelFactory channfact = new EChannelFactory(regfact, Mem, Micromem);
		EUIBasePCFabric ololo = new EUIBasePCFabric(regfact, flagfact, channfact);
		EUIRegister[] regs =  ololo.CreateMicroRegisters();
		EUIChannel[] channels = ololo.CreateMicroChannels();
		
		ERegister input = new ERegister();
		final EUIInputRegister inputreg = new EUIInputRegister(input, 1, 460, 60, 33, 478, "Клавишный Регистр");
		inputreg.Draw(g2);
		inputreg.DrawPointer(g2);
		
		for (int i=0; i<channels.length; i++)
		{
			channels[i].Draw(g2);
		}
		
		EUIAlu alu = ololo.CreateMicroAlu();
		alu.Draw(g2);
		
		for (int i=0; i<regs.length; i++)
		{
			regs[i].Draw(g2);
		}
	
		g2.setFont(new Font("Courier New", Font.BOLD, 21));
		g2.drawString("UXAKPWFIEONZC", 207, 403);
		
		Rectangle2D rect = new Rectangle2D.Double(463, 185, 200, 40);	
		g2.setPaint(new Color(187,249,166));
		g2.fill(rect);
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(1.0f));
		g2.draw(rect);
		g2.drawString("Дешифратор МК", 478, 210);
		g2.drawString("Устройство управления", 430, 20);
		g2.drawString("У0", 450, 274);
		g2.drawString("У28", 450, 339);
		g2.setColor(Color.GRAY);
		g2.drawString("...", 504, 250);
		
		g2.setStroke(new BasicStroke(4.0f));
		g2.drawLine(423, 1, 423, 432);
		
		EUIMemory mem = ololo.CreateСlassicMemory();
		int x[] = {32023, 1, 1234, 242, 1024, 65535, 1234, 6542, 1234, 5678, 1234, 3242, 18, 1234, 52425, 1234,1234, 5678, 1234, 3242, 1311, 1245, 1234, 9765, 1234, 5678, 1234, 3242, 1245, 1234, 9765, 1234};
		mem.SetMemory(x);
		mem.Draw(g2);
		mem.LoadMem(g2);
		
		EUIMemory micromem = ololo.CreateMicroMemory();
		micromem.SetAdressWidth(8);
		micromem.SetSeparator(53);
		micromem.Draw(g2);
		micromem.LoadMem(g2);
	}
}
