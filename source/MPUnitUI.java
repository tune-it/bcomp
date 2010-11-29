import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

/*-----------------------------------------------------------------------------
				Компонент "Работа с МПУ". Вызывает методы отрисовки
							необходимых элементов.
-----------------------------------------------------------------------------*/	

public class MPUnitUI extends JComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MPUnitUI (ObjectFactoryUI factory, InputRegisterUI input_register, JButton[] dbuttons, JCheckBox tact, JCheckBox memory_check, JButton work)
	{
		registers = factory.createMPURegisters();
		channels = factory.createMPUChannels();
		memory = factory.createСlassicMemory();
		micro_memory = factory.createMCMemory();
		alu = factory.createMPUAlu();
		
		this.key_register = input_register;

                this.dbuttons = dbuttons;
		this.tact = tact;	
		this.memory_check = memory_check;
		this.work = work;

	}

	public void paintComponent(Graphics g) 
	{
		Graphics2D rs = (Graphics2D) g;
		
		//Отрисовка каналов (сначала открытые)
		for (int i=0; i<channels.length; i++)
			if (channels[i].isConnect() == false)
				channels[i].draw(rs);
		for (int i=0; i<channels.length; i++)
			if (channels[i].isConnect())
				channels[i].draw(rs);
		
		alu.draw(rs);				//Отрисовка Алу
		memory.draw(rs);			//Отрисовка Памяти
		
		//Отрисовка Памяти МК
		micro_memory.setSeparator(50);
		micro_memory.draw(rs);		
		
		//Отрисовка регистров
		for (int i=0; i<registers.length; i++)
			registers[i].draw(rs);
		
		//Отрисовка клавишного регистра
		key_register.draw(rs);
		key_register.drawPointer(rs);

                //Добавление дублирующих кнопок
                for (int i = 0; i < dbuttons.length; i++)
                    add(dbuttons[i]);

		//Добавление чекбоксов и рамок
		add(tact);						//"Такт"
		rs.drawRect(329, 465, 101, 50);
		add(memory_check);				//"Работа с памятью МК"
		rs.drawRect(437, 465, 276, 50);		
		add(work);                      	//Работа/Останов.
				
		//Отрисовка номера бита
		rs.setFont(new Font("Courier New", Font.BOLD, 32));
		rs.setPaint(new Color(231,236,119)); 
		rs.fillRect(274, 465, 48, 50);
		rs.setColor(Color.BLACK);
		rs.drawRect(274, 465, 48, 50);
		if(15 - key_register.getPointerPosition() >= 10)
			rs.drawString("" + (15 - key_register.getPointerPosition()), 278, 500);
		else
			rs.drawString("" + (15 - key_register.getPointerPosition()), 287, 500);
		
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
		
		rs.setStroke(new BasicStroke(1.0f));
	}
	
	private RegisterUI[]				registers;					//Массив регистров
	private InputRegisterUI				key_register;				//Клавишный регистр
	private ChannelUI[] 				channels;					//Массив каналов
	private	MemoryUI				memory;						//Память
	private MemoryUI				micro_memory;				//Память МК
	private	AluUI					alu;						//АЛУ
        private JButton[]                               dbuttons;                               //Дублирующие кнопки
	private JCheckBox				tact;						//Чекбокс для режима "Такт"
	private JCheckBox				memory_check;				//Чекбокс "Работа с памятью МК"
	private JButton					work;						//Кнопка "работа/остановка"

}
