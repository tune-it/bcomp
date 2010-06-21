import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

/*-----------------------------------------------------------------------------
	Компонент "Работа с МПУ". Вызывает методы отрисовки
	необходимых элементов.
-----------------------------------------------------------------------------*/	

public class EUIMicroPC extends JComponent
{
	public EUIMicroPC (EUIBasePCFactory factory, EUIInputRegister input_register, JCheckBox movement_check, JCheckBox tact, JCheckBox memory_check, JLabel work)
	{
		registers = factory.CreateMicroRegisters();
		channels = factory.CreateMicroChannels();
		memory = factory.CreateСlassicMemory();
		micro_memory = factory.CreateMicroMemory();
		alu = factory.CreateMicroAlu();
		
		this.key_register = input_register;
		
		this.movement_check = movement_check ;
		this.tact = tact;	
		this.memory_check = memory_check;
		this.work = work;

	}

	public void paintComponent(Graphics g) 
	{
		Graphics2D rs = (Graphics2D) g;
		
		//Отрисовка каналов (сначала открытые)
		for (int i=0; i<channels.length; i++)
			if (channels[i].GetConnect() == false)
				channels[i].Draw(rs);
		for (int i=0; i<channels.length; i++)
			if (channels[i].GetConnect())
				channels[i].Draw(rs);
		
		alu.Draw(rs);				//Отрисовка Алу
		memory.Draw(rs);			//Отрисовка Памяти
		
		//Отрисовка Памяти МК
		micro_memory.SetSeparator(50);
		micro_memory.Draw(rs);		
		
		//Отрисовка регистров
		for (int i=0; i<registers.length; i++)
			registers[i].Draw(rs);
		
		//Отрисовка клавишного регистра
		key_register.Draw(rs);
		key_register.DrawPointer(rs);
		
		//Добавление чекбоксов и рамок
		add(movement_check);		//Проверка сдвига
		add(tact);					//"Такт"
		add(memory_check);			//"Работа с памятью МК"
		rs.drawRect(1, 436, 301, 20);
		rs.drawRect(329, 465, 101, 50);
		rs.drawRect(437, 465, 296, 50);		
		
		work.setLocation(740, 466);
		add(work);
		
		//Отрисовка номера бита
		rs.setFont(new Font("Courier New", Font.BOLD, 32));
		rs.setPaint(new Color(231,236,119)); 
		rs.fillRect(274, 465, 48, 50);
		rs.setColor(Color.BLACK);
		rs.drawRect(274, 465, 48, 50);
		if(15 - key_register.GetPointerPosition() >= 10)
			rs.drawString("" + (15 - key_register.GetPointerPosition()), 278, 500);
		else
			rs.drawString("" + (15 - key_register.GetPointerPosition()), 287, 500);
		
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
	private EUIInputRegister			key_register;				//Клавишный регистр
	private EUIChannel[] 				channels;					//Массив каналов
	private	EUIMemory					memory;						//Память
	private EUIMemory					micro_memory;				//Память МК
	private	EUIAlu						alu;						//АЛУ
	private JCheckBox					movement_check;				//Чекбокс установки сдвига указателя
	private JCheckBox					tact;						//Чекбокс для режима "Такт"
	private JCheckBox					memory_check;				//Чекбокс "Работа с памятью МК"
	private JLabel						work;						//Кнопка "работа/остановка"

}
