import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import Machine.EChannelFactory;
import Machine.EFlagFactory;
import Machine.EMemory;
import Machine.EMicrocommandMemory;
import Machine.ERegisterFactory;

public class EUIOutputPC extends JComponent
{
	public EUIOutputPC (EUIBasePCFactory factory, EUIInputRegister input_register)
	{
		registers = factory.CreateOutputRegisters();
		this.input_register = input_register;		
		memory = factory.CreateСlassicMemory();
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
		
		//Отрисовка заголовка
		rs.setFont(new Font("Courier New", Font.BOLD, 25));
		rs.drawString("Регистры", 197, 25);
		rs.drawString("процессора", 183, 50);
	}
	
	private EUIRegister[]				registers;					//Массив регистров
	private EUIInputRegister			input_register;				//Клавишный регистр
	private	EUIMemory					memory;						//Память
	}
