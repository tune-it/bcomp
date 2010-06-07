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
	public EUIMicroPC (EUIBasePCFactory factory)
	{
		registers = factory.CreateMicroRegisters();
		channels = factory.CreateMicroChannels();
		memory = factory.CreateСlassicMemory();
		micro_memory = factory.CreateMicroMemory();
		alu = factory.CreateMicroAlu();
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
		
		alu.Draw(g2);				//Отрисовка Алу
		memory.Draw(g2);			//Отрисовка Памяти
		memory.LoadMem(g2);
		micro_memory.Draw(g2);		//Отрисовка Памяти МК
		micro_memory.LoadMem(g2);
		
		//Отрисовка регистров
		for (int i=0; i<registers.length; i++)
		{
			registers[i].Draw(g2);
		}
	
		//Отрисовка названий флагов в регистре состояния
		g2.setFont(new Font("Courier New", Font.BOLD, 21));
		g2.drawString("UXAKPWFIEONZC", 207, 403);
		
		//Отрисовка дешифратора МК
		Rectangle2D rect = new Rectangle2D.Double(463, 185, 200, 40);	
		g2.setPaint(new Color(187,249,166));
		g2.fill(rect);
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(1.0f));
		g2.draw(rect);
		g2.drawString("Дешифратор МК", 478, 210);
		
		g2.drawString("Устройство управления", 430, 20); 		//Установка заголовка
		
		//Отрисовка управляющих каналов
		g2.drawString("У0", 450, 274);
		g2.drawString("У28", 450, 339);
		g2.setColor(Color.GRAY);
		g2.drawString("...", 504, 250);
		
		//Отрисовка разделительной линии
		g2.setStroke(new BasicStroke(4.0f));
		g2.drawLine(423, 1, 423, 432);
		
	}
	
	private EUIRegister[]				registers;					//Массив регистров
	private EUIChannel[] 				channels;					//Массив каналов
	private	EUIMemory					memory;						//Память
	private EUIMemory					micro_memory;				//Память МК
	private	EUIAlu						alu;						//АЛУ
}
