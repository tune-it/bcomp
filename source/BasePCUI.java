import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

/*-----------------------------------------------------------------------------
				Компонент "Базовая ЭВМ". Вызывает методы отрисовки
							необходимых элементов.
-----------------------------------------------------------------------------*/	

public class BasePCUI extends JComponent
{
	public BasePCUI (ObjectFactoryUI factory, InputRegisterUI input_register, JCheckBox movement_check, JCheckBox tact, JButton work)
	{
		registers = factory.createClassicRegisters();
		channels = factory.createClassicChannels();
		memory = factory.createСlassicMemory();
		manager_device = factory.createManagerDevice();
		alu = factory.createClassicAlu();
		
		this.movement_check = movement_check;
		this.tact = tact;
		this.work = work;
		
		this.key_register = input_register;
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
		
		//Отрисовка канала в устройство управления
		rs.setColor(Color.GRAY);
		rs.fillRect(713, 160, 40, 20);
		int[] mass1 = {703, 733, 763};		//Координаты x
		int[] mass2 = {180, 210, 180};		//Координаты у
		rs.fillPolygon(mass1, mass2, 3);
		
		//Отрисовка регистров
		for (int i=0; i<registers.length; i++)
			registers[i].draw(rs);
		
		//Отрисовка клавишного регистра
		key_register.draw(rs);
		key_register.drawPointer(rs);
		
		//Отрисовка номера бита
		rs.setFont(new Font("Courier New", Font.BOLD, 32));
		rs.setPaint(new Color(231,236,119)); 
		rs.fillRect(274, 465, 48, 50);
		rs.setColor(Color.BLACK);
		rs.drawRect(274, 465, 48, 50);
		int bit_number = 15 - key_register.getPointerPosition();
		if(bit_number >= 10)
			rs.drawString("" + bit_number, 278, 500);
		else
			rs.drawString("" + bit_number, 287, 500);

		alu.draw(rs);				//Отрисовка АЛУ
		memory.draw(rs);			//Отрисовка Памяти
		manager_device.draw(rs);	//Отрисовка Устройства Управления
		
		//Добавление чекбоксов и рамок
		add(movement_check);		//Проверка сдвига
		rs.drawRect(1, 436, 301, 20);
		add(tact);					//"Такт"
		rs.drawRect(329, 465, 101, 50);
		
		work.setLocation(437, 465);
		add(work);
	}
	
	private RegisterUI[]				registers;					//Массив регистров
	private InputRegisterUI				key_register;				//Клавишный регистр
	private ChannelUI[] 				channels;					//Массив каналов
	private	MemoryUI					memory;						//Память
	private ManagerDeviceUI				manager_device;				//Устройство Управления
	private	AluUI						alu;						//АЛУ
	private JCheckBox					movement_check;				//Чекбокс установки сдвига указателя
	private JCheckBox					tact;						//Чекбокс режима "Такт"
	private JButton						work;						//Лэйбл "Работа/Остановка"
}
