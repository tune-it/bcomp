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
		input_register = factory.CreateInputRegister();
		channels = factory.CreateClassicChannels();
		memory = factory.CreateСlassicMemory();
		manager_device = factory.CreateManagerDevice();
		alu = factory.CreateClassicAlu();
	}
	
	public void paintComponent(Graphics g) 
	{	
		Graphics2D rs = (Graphics2D) g;
	
		//Отрисовка каналов (сначала открытые)
		for (int i=0; i<channels.length; i++)
		{
			if (channels[i].GetConnect() == false)
				channels[i].Draw(rs);
		}
		for (int i=0; i<channels.length; i++)
		{
			if (channels[i].GetConnect())
				channels[i].Draw(rs);
		}
		
		//Отрисовка канала в устройство управления
		rs.setColor(Color.GRAY);
		rs.fillRect(713, 160, 40, 20);
		int[] mass1 = {703, 733, 763};		//Координаты x
		int[] mass2 = {180, 210, 180};		//Координаты у
		rs.fillPolygon(mass1, mass2, 3);
		
		//Отрисовка регистров
		for (int i=0; i<registers.length; i++)
		{
			registers[i].Draw(rs);
		}
		
		input_register.Draw(rs);
		input_register.DrawPointer(rs);
		
		alu.Draw(rs);				//Отрисовка АЛУ
		memory.Draw(rs);			//Отрисовка Памяти
		manager_device.Draw(rs);	//Отрисовка Устройства Управления
		
		String str = "" + input_register.GetPointerPosition() + " " + input_register.GetContent() + " ";
		rs.drawString(str, 300, 500);
	}
	
	public void SetPointerPosition(int x)
	{
		input_register.SetPointerPosition(x);
	}
	
	public int GetPointerPosition()
	{
		return input_register.GetPointerPosition();
	}
	
	public void SetBit()
	{
		input_register.SetBit();
	}
	
	public void SetBit(boolean bit)
	{
		input_register.SetBit(bit);
	}
	
	private EUIRegister[]				registers;					//Массив регистров
	private EUIInputRegister			input_register;				//Клавишный регистр
	private EUIChannel[] 				channels;					//Массив каналов
	private	EUIMemory					memory;						//Память
	private EUIManagerDevice			manager_device;				//Устройство Управления
	private	EUIAlu						alu;						//АЛУ
}
