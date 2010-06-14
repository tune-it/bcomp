import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

/*-----------------------------------------------------------------------------
			Компонент "Работа с ВУ". Вызывает методы отрисовки
						необходимых элементов.
-----------------------------------------------------------------------------*/	

public class EUIOutputPC extends JComponent
{
	public EUIOutputPC (EUIBasePCFactory factory, EUIInputRegister input_register, JCheckBox movement_check)
	{
		registers = factory.CreateOutputRegisters();
		memory = factory.CreateСlassicMemory();
		
		this.input_register = input_register;
		this.movement_check = movement_check;
	}
	
	public void paintComponent(Graphics g) 
	{
		Graphics2D rs = (Graphics2D) g;
		
		//Отрисовка Регистров
		for (int i=0; i<registers.length; i++)
			registers[i].Draw(rs);
		
		memory.Draw(rs);			//Отрисовка Памяти
		
		//Отрисовка клавишного регистра
		input_register.Draw(rs);
		input_register.DrawPointer(rs);
		
		//Создания чекбокса проверки сдвига
		add(movement_check);
		
		//Отрисовка заголовка
		rs.setFont(new Font("Courier New", Font.BOLD, 25));
		rs.drawString("Регистры", 197, 25);
		rs.drawString("процессора", 183, 50);
	}
	
	private EUIRegister[]				registers;					//Массив регистров
	private EUIInputRegister			input_register;				//Клавишный регистр
	private	EUIMemory					memory;						//Память
	private JCheckBox					movement_check;				//Чекбокс установки сдвига указателя

	}
