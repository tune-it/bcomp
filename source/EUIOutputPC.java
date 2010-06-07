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
	public EUIOutputPC (EUIBasePCFactory factory)
	{
		registers = factory.CreateOutputRegisters();
		memory = factory.CreateСlassicMemory();
	}
	
	public void paintComponent(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D) g;
	
		//Отрисовка Регистров
		for (int i=0; i<registers.length; i++)
		{
			registers[i].Draw(g2);
		}
		
		memory.Draw(g2);			//Отрисовка Памяти
		memory.LoadMem(g2);
		
		//Отрисовка заголовка
		g2.setFont(new Font("Courier New", Font.BOLD, 25));
		g2.drawString("Регистры", 197, 25);
		g2.drawString("процессора", 183, 50);
	}
	
	private EUIRegister[]				registers;					//Массив регистров
	private	EUIMemory					memory;						//Память
	}
