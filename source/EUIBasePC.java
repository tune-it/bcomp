import java.awt.Graphics;
import java.awt.Graphics2D;
import Machine.*;
import javax.swing.JComponent;


public class EUIBasePC extends JComponent
{
public EUIBasePC ()
{
	
}

public void paintComponent(Graphics g) 
{
	Graphics2D g2 = (Graphics2D) g;
	
	ERegisterFactory regfact = new ERegisterFactory();
	EFlagFactory flagfact = new EFlagFactory(regfact);
	EMemory Mem = new EMemory(regfact);
	EMicrocommandMemory micromem = new EMicrocommandMemory(regfact);
	EChannelFactory channfact = new EChannelFactory(regfact, Mem, micromem);
	EUIBasePCFabric ololo = new EUIBasePCFabric(regfact, flagfact, channfact);
	EUIRegister[] regs =  ololo.CreateClassicRegisters();
	
	regfact.Accumulator().GetData(436);
	flagfact.GetC().SetFlag();
	
	EUIChannel[] chns =  ololo.CreateClassicChannels();
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
		regs[i].Draw(g2);
	}
	
	EUIAlu alu = ololo.CreateClassicAlu();
	alu.Draw(g2);
	
	EUIMemory mem = ololo.CreateСlassicMemory();
	int x[] = {32023, 1, 1234, 242, 1024, 65535, 1234, 6542, 1234, 5678, 1234, 3242, 18, 1234, 52425, 1234,1234, 5678, 1234, 3242, 1311, 1245, 1234, 9765, 1234, 5678, 1234, 3242, 1245, 1234, 9765, 1234};
	mem.SetMemory(x);
	mem.Draw(g2);
	mem.LoadMem(g2);
}
}
