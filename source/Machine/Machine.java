package Machine;

/**
* Базовая ЭВМ (управление работой)
* @author Ponomarev
*/
public class Machine
{
	public Machine(ControlView ctrl)
	{
		this.ctrl=ctrl;
		
		// Фабрика регистров
		reg_factory = new RegisterFactory();
		
		// Основная память
		memory = new Memory(reg_factory);

		// Память микрокомманд
		micro_mem = new MicrocommandMemory(reg_factory);
		
		// Фабрика флагов
		flags = new FlagFactory(reg_factory);
		
		// Фабрика Каналов
		channels = new ChannelFactory(reg_factory, memory, micro_mem);
		
		// АЛУ
		alu = new ALU(reg_factory, flags);
		
		// Устройства ввода/вывода
		dev = new DeviceFactory(reg_factory);
		
		// Устройство управления
		man_dev = new ManagerDevice(reg_factory, channels, alu, flags, dev);
		
		r = new MachineRunnable(reg_factory, man_dev, ctrl);
		t = new Thread();
	}
	
	/**
	 * "Пуск"
	 */
	public void start()
	{
		reg_factory.getMicroInstructionPointer().setValue(0xA8);
		ссontinue();
	}
	
	/**
	 * "Продолжение"
	 */
	public void ссontinue()
	{
		while(t.isAlive())t.interrupt();
		t = new Thread(r);
		t.start();				
	}
	
	/**
	 * "Ввод адреса"
	 */
	public void adress()
	{
		if (ctrl.microWork())
		{
			reg_factory.getMicroInstructionPointer().setValue(reg_factory.getInputRegister().getValue());
			ctrl.repaint();
		}
		else
		{
			reg_factory.getMicroInstructionPointer().setValue(0x99);
			ссontinue();
		}
	}
	
	/**
	 * "Чтение"
	 */
	public void read()
	{
		reg_factory.getMicroInstructionPointer().setValue(0x9C);
		ссontinue();
	}
	
	/**
	 * "Запись"
	 */
	public void record()
	{
		if (ctrl.microWork())
		{
			micro_mem.setValue(reg_factory.getInputRegister().getValue());
			reg_factory.getMicroInstructionPointer().setValue(reg_factory.getMicroInstructionPointer().getValue()+1);
			ctrl.repaint();
		}
		else
		{
			reg_factory.getMicroInstructionPointer().setValue(0xA1);
			ссontinue();
		}
	}
	
	/**
	 * "Работа останов"
	 */
	public void workStop()
	{
		if (flags.getStateOfTumbler().getValue() == 0)
		{
			flags.getStateOfTumbler().setValue(1);
		}
		else
		{
			flags.getStateOfTumbler().setValue(0);
		}
		flags.refreshStateCounter();
		ctrl.repaint();
	}
	
	/**
	 * Получить фабрику регистров
	 * @return Возвращает фабрику регистров
	 */
	public RegisterFactory getRegFactory()
	{
		return reg_factory;
	}
	
	/**
	 * Получить основную память
	 * @return Возвращает основную память
	 */
	public Memory getMemory()
	{
		return memory;
	}
	
	/**
	 * Получить память микрокоманд
	 * @return Возвращает память микрокоманд
	 */
	public MicrocommandMemory getMicroMem()
	{
		return micro_mem;
	}
	
	/**
	 * Получить фабрику флагов
	 * @return Возвращает фабрику флагов
	 */
	public FlagFactory getFlagFactory()
	{
		return flags;
	}
	
	/**
	 * Получить фабрику каналов
	 * @return Возвращает фабрику каналов
	 */
	public ChannelFactory getChannelFactory()
	{
		return channels;
	}
	
	/**
	 * Получить АЛУ
	 * @return Возвращает АЛУ
	 */
	public ALU getALU()
	{
		return alu;
	}
	
	/**
	 * Получить устройство управление
	 * @return Возвращает Возвращает УУ
	 */
	public ManagerDevice getManagerDevice()
	{
		return man_dev;
	}
	
	/**
	 * Получить фабрику усройств
	 * @return Возвращает фабрику усройств
	 */
	public DeviceFactory getDeviceFactory()
	{
		return dev;
	}
	
	private RegisterFactory		reg_factory;
	private Memory				memory;
	private MicrocommandMemory	micro_mem;
	private FlagFactory			flags;
	private ChannelFactory		channels;
	private ALU					alu;
	private ManagerDevice		man_dev;
	private DeviceFactory 		dev;
	
	Runnable r;
	Thread t;
	
	private ControlView ctrl;


}

class MachineRunnable implements Runnable
{
	public MachineRunnable(RegisterFactory reg_factory, ManagerDevice man_dev, ControlView ctrl)
	{
		this.ctrl = ctrl;
		this.man_dev = man_dev;
		this.reg_factory = reg_factory;
	}
	
	public void run()
	{
		if (ctrl.getTact())
		{
			// Выполнение по тактам
			man_dev.timeStep();
			ctrl.repaint();
		}
		else
		{
			// Выполнение по командам
			do
			{
				man_dev.timeStep();
				ctrl.repaint();
				try
				{
					Thread.sleep(10L);
				}
				catch (Exception e)
				{
					// Do nothing
				}
			} while (!Thread.currentThread().isInterrupted() && reg_factory.getMicroCommandRegister().getValue() != 0x4008);
		}
	}
	
	private RegisterFactory	reg_factory;
	private ManagerDevice		man_dev;
	private ControlView		ctrl;
}
