import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import Machine.*;

import javax.swing.JComponent;


public class EUIBasePC extends JComponent
{
	public EUIBasePC (EUIBasePCFactory factory)
	{
		registers = factory.CreateClassicRegisters();
		channels = factory.CreateClassicChannels();
		memory = factory.CreateСlassicMemory();
		manager_device = factory.CreateManagerDevice();
		alu = factory.CreateClassicAlu();
	}
	
	public void paintComponent(Graphics g) 
	{	
		Graphics2D g2 = (Graphics2D) g;
	
		//Отрисовка каналов (сначала открытые)
		for (int i=0; i<channels.length; i++)
		{
			if (channels[i].GetConnect() == false)
				channels[i].Draw(g2);
		}
		for (int i=0; i<channels.length; i++)
		{
			if (channels[i].GetConnect())
				channels[i].Draw(g2);
		}
		
		//Отрисовка канала в устройство управления
		g2.setColor(Color.GRAY);
		g2.fillRect(713, 160, 40, 20);
		int[] mass1 = {703, 733, 763};
		int[] mass2 = {180, 210, 180};
		g2.fillPolygon(mass1, mass2, 3);
		
		//Отрисовка регистров
		for (int i=0; i<registers.length; i++)
		{
			registers[i].Draw(g2);
		}
		
		alu.Draw(g2);				//Отрисовка АЛУ
		memory.Draw(g2);			//Отрисовка Памяти
		memory.LoadMem(g2);
		manager_device.Draw(g2);	//Отрисовка Устройства Управления
	}
	
	private EUIRegister[]				registers;					//Массив регистров
	private EUIChannel[] 				channels;					//Массив каналов
	private	EUIMemory					memory;						//Память
	private EUIManagerDevice			manager_device;				//Устройство Управления
	private	EUIAlu						alu;						//АЛУ
}
