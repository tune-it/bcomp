import java.awt.Color;
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
	public EUIOutputPC (EUIBasePCFactory factory, EUIInputRegister input_register)
	{
		registers = factory.CreateOutputRegisters();
		memory = factory.CreateСlassicMemory();
		
		this.input_register = input_register;
		movement_check = factory.CreateMovementCheckBox();
		tact = factory.CreateTactCheckBox();

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
		
		//Добавление чекбоксов и рамок
		add(movement_check);		//Проверка сдвига
		add(tact);					//"Такт"
		rs.drawRect(1, 436, 301, 20);
		rs.drawRect(329, 465, 101, 50);
		
		//Отрисовка номера бита
		rs.setFont(new Font("Courier New", Font.BOLD, 32));
		rs.setPaint(new Color(231,236,119)); 
		rs.fillRect(274, 465, 48, 50);
		rs.setColor(Color.BLACK);
		rs.drawRect(274, 465, 48, 50);
		if(15 - input_register.GetPointerPosition() >= 10)
			rs.drawString("" + (15 - input_register.GetPointerPosition()), 278, 500);
		else
			rs.drawString("" + (15 - input_register.GetPointerPosition()), 287, 500);
		
		//Отрисовка заголовка
		rs.setFont(new Font("Courier New", Font.BOLD, 25));
		rs.drawString("Регистры", 197, 25);
		rs.drawString("процессора", 183, 50);
	}
	
	private EUIRegister[]				registers;					//Массив регистров
	private EUIInputRegister			input_register;				//Клавишный регистр
	private	EUIMemory					memory;						//Память
	private JCheckBox					movement_check;				//Чекбокс установки сдвига указателя
	private JCheckBox					tact;						//Чекбокс режима "Такт"
}
