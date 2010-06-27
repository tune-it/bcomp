package Machine;

/*-----------------------------------------------------------------------------
	Устройство ввода.
-----------------------------------------------------------------------------*/
public class InputDevice implements InternalDevice
{
	public InputDevice(ERegisterFactory reg_factory)
	{
		data_reg = new ERegister(8);
		
		state_flag = new EFlag();
		
		data_channel = new EIOChannel(reg_factory.Accumulator(), data_reg);
		
		order_channel =			new EChannel(null, null);
		state_flag_channel =	new EChannel(null, null);
		adress_channel =		new EChannel(null, null);
		intrpt_channel =		new EChannel(null, null);
	}
	
	public InputDevice(int width, ERegisterFactory reg_factory)
	{
		data_reg = new ERegister(width);
		state_flag = new EFlag();
		data_channel = new EIOChannel(reg_factory.Accumulator(), data_reg);
	}

	public EFlag getStateFlag()
	{
		return state_flag;
	}

	public ERegister getDataRegister()
	{
		return data_reg;
	}

	public IChannel getDataChannel()
	{
		return data_channel;
	}
	
	// Приказ на ввод/вывод
	public IChannel	getIORequestChannel()
	{
		return order_channel;
	}
	
	// Адрес ВУ
	public IChannel	getAdressChannel()
	{
		return adress_channel;
	}
	
	// Состояние флагов ВУ
	public IChannel	getStateFlagChannel()
	{
		return state_flag_channel;
	}
	
	public IChannel	getInterruptionRequestChannel()
	{
		return intrpt_channel;
	}

	private EChannel	order_channel;		// Приказ на ввод/вывод
	private EChannel	adress_channel;		// Адрес ВУ
	private EChannel	state_flag_channel;	// Состояние флагов ВУ
	private EChannel	intrpt_channel;		// Запрос прерывания
	
	private EIOChannel	data_channel;		// Шина ввода/вывода
	private ERegister	data_reg;			// Регистра данных
	private EFlag		state_flag;			// флаг ВУ
}