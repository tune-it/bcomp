import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

/*-----------------------------------------------------------------------------
			Компонент "Работа с ВУ". Вызывает методы отрисовки
						необходимых элементов.
-----------------------------------------------------------------------------*/	

public class IOUnitUI extends JComponent
{
	public IOUnitUI (ObjectFactoryUI factory, InputRegisterUI[] input_registers, JCheckBox movement_check, JCheckBox tact, JButton work, JRadioButton[] register_check)
	{
		registers = factory.createIORegisters();
		memory = factory.createСlassicMemory();
				
		this.input_registers = input_registers;
		key_register = input_registers[0];
		
		this.movement_check = movement_check;
		this.tact = tact;
		this.work = work;
		this.register_check = register_check;
		
	}
	
	public void paintComponent(Graphics g) 
	{
		Graphics2D rs = (Graphics2D) g;
		
		//Отрисовка Регистров
		for (int i=0; i<registers.length; i++)
			registers[i].draw(rs);
		
		memory.draw(rs);			//Отрисовка Памяти
		
		//Отрисовка устройств вывода
		for (int i=0; i<input_registers.length; i++)
		{
			input_registers[i].draw(rs);
			input_registers[i].drawPointer(rs);
		}

		//Добавление чекбоксов и рамок
		add(movement_check);			//Проверка сдвига
		rs.drawRect(1, 436, 301, 20);
		add(tact);						//"Такт"
		rs.drawRect(329, 465, 101, 50);
		
		//Добавление панели выбора регистра и отрисовка рамки
		for (int i=0; i<register_check.length; i++)
			add(register_check[i]);
		rs.drawRect(437, 439, 175, 76);
		
		work.setLocation(619, 465);
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
	}
	
	private RegisterUI[]				registers;					//Массив регистров
	private InputRegisterUI				key_register;				//Клавишный регистр
	private InputRegisterUI[]			input_registers;			//Устройства вывода + клавишный регистр
	private	MemoryUI					memory;						//Память
	private JCheckBox					movement_check;				//Чекбокс установки сдвига указателя
	private JCheckBox					tact;						//Чекбокс режима "Такт"
	private JButton						work;						//Кнопка "Работа/Остановка"
	private JRadioButton[]				register_check;				//Кнопки выбора активного регистра
}
