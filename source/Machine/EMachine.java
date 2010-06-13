package Machine;

/*-----------------------------------------------------------------------------
	Базовая ЭВМ (управление работой)
-----------------------------------------------------------------------------*/
public class EMachine {
	public EMachine(EControlView ctrl)
	{
		this.ctrl=ctrl;
		
		// Фабрика регистров
		reg_factory = new ERegisterFactory();
		
		// Основная память
		memory = new EMemory(reg_factory);

		// Память микрокомманд
		micro_mem = new EMicrocommandMemory(reg_factory);
		
		// Фабрика флагов
		flags = new EFlagFactory(reg_factory);
		
		// Фабрика Каналов
		channels = new EChannelFactory(reg_factory, memory, micro_mem);
		
		// АЛУ
		alu = new EALU(reg_factory, flags);
		
		// Устройство управления
		man_dev = new EManagerDevice(reg_factory, channels, alu, flags);
	}
	
	public void Start()
	{
		reg_factory.MicroInstructionPointer().GetData(0x9C);
		Continue();
	}
	
	public void Continue()
	{
		if (ctrl.GetTact())
		{
			// Выполнение по тактам
			
			while (flags.GetStateOfTumbler().SendData() != 0)
			do
			{
				man_dev.TimeStep();
				ctrl.Repaint();
			}
			while (flags.GetStateOfTumbler().SendData() != 0);
		}
		else
		{
			// Выполнение по командам
			do
			{
				do
				{
					man_dev.TimeStep();
					ctrl.Repaint();
				}
				while (reg_factory.MicroInstructionPointer().SendData() != 1);
			}
			while (flags.GetStateOfTumbler().SendData() != 0);
		}		
	}
	
	public void Adress()
	{
		reg_factory.MicroInstructionPointer().GetData(0x99);
		Continue();
	}
	
	public void Record()
	{
		reg_factory.MicroInstructionPointer().GetData(0xA1);
		Continue();
	}
	
	public void StopWork()
	{
		if (flags.GetStateOfTumbler().SendData() == 0)
		{
			flags.GetStateOfTumbler().GetData(1);
		}
		else
		{
			flags.GetStateOfTumbler().GetData(0);
		}
		flags.RefreshStateCounter();
	}
	
	public ERegisterFactory GetRegFac()
	{
		return reg_factory;
	}
	
	public EMemory GetMemory()
	{
		return memory;
	}
	
	public EMicrocommandMemory GetMicroMem()
	{
		return micro_mem;
	}
	
	public EFlagFactory GetFlagFac()
	{
		return flags;
	}
	
	public EChannelFactory GetChannelFac()
	{
		return channels;
	}
	
	public EALU GetALU()
	{
		return alu;
	}
	
	public EManagerDevice GetManagerDev()
	{
		return man_dev;
	}
	
	private ERegisterFactory	reg_factory;
	private EMemory				memory;
	private EMicrocommandMemory	micro_mem;
	private EFlagFactory		flags;
	private EChannelFactory		channels;
	private EALU				alu;
	private EManagerDevice		man_dev;
	
	private EControlView ctrl;
}
