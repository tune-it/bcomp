import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;

/*-----------------------------------------------------------------------------
			Компонент "Работа с ВУ". Вызывает методы отрисовки
						необходимых элементов.
-----------------------------------------------------------------------------*/	

public class IOUnitUI extends JComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public IOUnitUI (ObjectFactoryUI factory, InputRegisterUI[] input_registers, JButton[] dbuttons, JButton[] flag_buttons, FlagUI[] flags, JCheckBox tact, JButton work, JRadioButton[] register_check)
	{
		registers = factory.createIORegisters();
		memory = factory.createСlassicMemory();
		channels = factory.createIOChannels();
		this.flags = flags;		
		
		this.input_registers = input_registers;
		key_register = input_registers[0];

                this.dbuttons = dbuttons;
                this.flag_buttons = flag_buttons;
		this.tact = tact;
		this.work = work;
		this.register_check = register_check;
	}
	
	public void paintComponent(Graphics g) 
	{
		Graphics2D rs = (Graphics2D) g;
		
		//Отрисовка каналов
		for (int i=0; i<channels.length; i++)
			if (channels[i].isConnect() == false)
				channels[i].draw(rs);
		for (int i=0; i<channels.length; i++)
			if (channels[i].isConnect())
				channels[i].draw(rs);
		
		//Отрисовка флагов 
		for (int i=0; i<flags.length; i++)
			flags[i].draw(rs);
			
		//Отрисовка Регистров
		for (int i=0; i<registers.length; i++)
			registers[i].draw(rs);
		
		memory.draw(rs);			//Отрисовка Памяти
		
		//Отрисовка устройств вывода
		for (int i = 0; i < input_registers.length; i++)
		{
			input_registers[i].draw(rs);
			input_registers[i].drawPointer(rs);
		}

                //Добавление дублирующих кнопок
                for (int i = 0; i < dbuttons.length; i++)
                    add(dbuttons[i]);

                //Добавление кнопок работы с ВУ
                for (int i = 0; i < flag_buttons.length; i++)
                    add(flag_buttons[i]);

		//Добавление чекбоксов и рамок
		add(tact);						//"Такт"
		rs.drawRect(329, 465, 101, 50);
		
		//Добавление панели выбора регистра и отрисовка рамки
		for (int i = 0; i < register_check.length; i++)
			add(register_check[i]);
		rs.drawRect(437, 439, 175, 76);
		
		//Установка позиции кнопки "работа/остановка" и ее отрисовка
		add(work);
		
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
		
		//Отрисовка заголовка
		rs.setFont(new Font("Courier New", Font.BOLD, 25));
		rs.drawString("Регистры", 197, 25);
		rs.drawString("процессора", 183, 50);
		
		//Отрисовка разделительной линии
		rs.setColor(Color.GRAY);
		rs.setStroke(new BasicStroke(4.0f));
		rs.drawLine(360, 1, 360, 432);
		
		rs.setStroke(new BasicStroke(1.0f));

		//Отрисовка Дешифраторов
		int width = 35;	 		//Ширина дешифратора
		int length = 135;		//Длина дешифратора
		String mess = "Дешифратор";
		rs.setColor(new Color(187,249,166));
		rs.fillRect(400, 50, length, width);
		rs.fillRect(550, 50, length, width);
		rs.fillRect(700, 50, length, width);
		
		rs.setColor(Color.BLACK);
		rs.drawRect(400, 50, length, width);
		rs.drawRect(550, 50, length, width);
		rs.drawRect(700, 50, length, width);
		
		rs.setFont(new Font("Courier New", Font.BOLD, 20));
		rs.drawString(mess, 408, 73);
		rs.drawString(mess, 558, 73);
		rs.drawString(mess, 708, 73);
		
		//Отрисовка назначений каналов
		rs.setFont(new Font("Courier New", Font.BOLD, 20));
		rs.drawString("Приказ на ввод/вывод", 365, 15);
		rs.drawString("Адрес ВУ", 365, 129);
		rs.drawString("Запрос прерывания", 365, 150);
		rs.drawString("Состояние флагов ВУ", 365, 256);
		rs.drawString("Шина ввода", 365, 278);
		rs.drawString("Шина вывода", 365, 413);
	}
	
	private RegisterUI[]				registers;					//Массив регистров
	private ChannelUI[]				channels;					//Массив каналов
	private InputRegisterUI				key_register;				//Клавишный регистр
	private InputRegisterUI[]			input_registers;			//Устройства вывода + клавишный регистр
	private FlagUI[]				flags;						//Флаги
	private	MemoryUI				memory;						//Память
        private JButton[]                               dbuttons;                   //Дублирующие кнопки
        private JButton[]                               flag_buttons;               //Кнопки работы с ВУ
	private JCheckBox				tact;						//Чекбокс режима "Такт"
	private JButton                                 work;						//Кнопка "Работа/Остановка"
	private JRadioButton[]				register_check;				//Кнопки выбора активного регистра
}
