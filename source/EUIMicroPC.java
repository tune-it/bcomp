import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
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
		
		alu.Draw(rs);				//Отрисовка Алу
		memory.Draw(rs);			//Отрисовка Памяти
		micro_memory.SetSeparator(50);
		micro_memory.Draw(rs);		//Отрисовка Памяти МК
		
		//Отрисовка регистров
		for (int i=0; i<registers.length; i++)
		{
			registers[i].Draw(rs);
		}
	
		//Отрисовка названий флагов в регистре состояния
		rs.setFont(new Font("Courier New", Font.BOLD, 21));
		rs.drawString("UXAKPWFIEONZC", 207, 403);
		
		//Отрисовка дешифратора МК
		Rectangle2D rect = new Rectangle2D.Double(463, 185, 200, 40);	
		rs.setPaint(new Color(187,249,166));
		rs.fill(rect);
		rs.setPaint(Color.BLACK);
		rs.setStroke(new BasicStroke(1.0f));
		rs.draw(rect);
		rs.drawString("Дешифратор МК", 478, 210);
		
		rs.drawString("Устройство управления", 430, 20); 		//Установка заголовка
		
		//Отрисовка управляющих каналов
		rs.drawString("У0", 450, 274);
		rs.drawString("У28", 450, 339);
		rs.setColor(Color.GRAY);
		rs.drawString("...", 504, 250);
		
		//Отрисовка разделительной линии
		rs.setStroke(new BasicStroke(4.0f));
		rs.drawLine(423, 1, 423, 432);
		
	}
	
	private EUIRegister[]				registers;					//Массив регистров
	private EUIChannel[] 				channels;					//Массив каналов
	private	EUIMemory					memory;						//Память
	private EUIMemory					micro_memory;				//Память МК
	private	EUIAlu						alu;						//АЛУ
}