package Machine;

/*-----------------------------------------------------------------------------
	Базовая ЭВМ (управление работой)
-----------------------------------------------------------------------------*/
public class EMachine
{
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
		
		// Устройства ввода/вывода
		dev = new DeviceFactory(reg_factory);
		
		// Устройство управления
		man_dev = new EManagerDevice(reg_factory, channels, alu, flags, dev);
	}
	
	public void Start()
	{
		reg_factory.MicroInstructionPointer().GetData(0xA8);
		Continue();
	}
	
	public void Continue()
	{
		Runnable r = new MachineRunnable(reg_factory, flags, man_dev, ctrl);
		t = new Thread(r);
		t.start();		
	}
	
	public void Adress()
	{
		if (ctrl.MicroWork())
		{
			reg_factory.MicroInstructionPointer().GetData(reg_factory.InputRegister().SendData());
			ctrl.Repaint();
		}
		else
		{
			reg_factory.MicroInstructionPointer().GetData(0x99);
			Continue();
		}
		
	}
	
	public void Read()
	{
		reg_factory.MicroInstructionPointer().GetData(0x9C);
		Continue();
	}
	
	public void Record()
	{
		if (ctrl.MicroWork())
		{
			micro_mem.GetData(reg_factory.InputRegister().SendData());
			reg_factory.MicroInstructionPointer().GetData(reg_factory.MicroInstructionPointer().SendData()+1);
			ctrl.Repaint();
		}
		else
		{
			reg_factory.MicroInstructionPointer().GetData(0xA1);
			Continue();
		}
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
		ctrl.Repaint();
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
	
	public DeviceFactory GetDeviceFactory()
	{
		return dev;
	}
	
	private ERegisterFactory	reg_factory;
	private EMemory				memory;
	private EMicrocommandMemory	micro_mem;
	private EFlagFactory		flags;
	private EChannelFactory		channels;
	private EALU				alu;
	private EManagerDevice		man_dev;
	private DeviceFactory dev;
	
	Thread t;
	
	private EControlView ctrl;


}

class MachineRunnable implements Runnable
{
	public MachineRunnable(ERegisterFactory reg_factory, EFlagFactory flags, EManagerDevice man_dev, EControlView ctrl)
	{
		this.ctrl = ctrl;
		this.flags = flags;
		this.man_dev = man_dev;
		this.reg_factory = reg_factory;
	}
	
	public void run()
	{
		if (ctrl.GetTact())	{
			// Выполнение по тактам
			man_dev.TimeStep();
			ctrl.Repaint();
		} else {
			// Выполнение по командам
			do {
				man_dev.TimeStep();
				ctrl.Repaint();
				try {
					Thread.sleep(10L);
				} catch (Exception e) {
					// Do nothing
				}
			} while (reg_factory.MicroCommandRegister().SendData() != 0x4008);
		}
	}
	
	private ERegisterFactory	reg_factory;
	private EFlagFactory		flags;
	private EManagerDevice		man_dev;
	private EControlView		ctrl;
}
