package Machine;

/**
 * Устройство вывода.
 * @author Ponomarev
 */
public class OutputDevice implements InternalDevice
{
	public OutputDevice(RegisterFactory reg_factory)
	{
		data_reg = new Register(8);
		state_flag = new Flag();
		data_channel = new IOChannel(data_reg, reg_factory.getAccumulator());
		
		order_channel =			new Channel(null, null);
		state_flag_channel =	new Channel(null, null);
		adress_channel =		new Channel(null, null);
		intrpt_channel =		new Channel(null, null);
	}

	public Flag getStateFlag()
	{
		return state_flag;
	}

	public Register getDataRegister()
	{
		return data_reg;
	}

	public IChannel getDataChannel()
	{
		return data_channel;
	}
	public IChannel	getIORequestChannel()
	{
		return order_channel;
	}
	
	public IChannel	getAdressChannel()
	{
		return adress_channel;
	}
	
	public IChannel	getStateFlagChannel()
	{
		return state_flag_channel;
	}
	
	public IChannel	getInterruptionRequestChannel()
	{
		return intrpt_channel;
	}
	
	private Channel	order_channel;		// Приказ на ввод/вывод
	private Channel	adress_channel;		// Адрес ВУ
	private Channel	state_flag_channel;	// Состояние флагов ВУ
	private Channel	intrpt_channel;		// Запрос прерывания
	
	private IOChannel	data_channel;		// Шина ввода/вывода
	private Register	data_reg;			// Регистра данных
	private Flag		state_flag;			// флаг ВУ
}
