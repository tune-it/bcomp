package Machine;

/**
 * Внешнее устройство ввода/вывода
 * @author Ponomarev
 */
public class InternalDevice
{
	/**
	 * 
	 * @param regFactory - фабрика регистров
	 * @param type - тип внешнего устройства (0 - вывода, 1 - ввода, другое - ввода/вывода)
	 */
	public InternalDevice(RegisterFactory regFactory, int type)
	{
		data_reg = new Register(8);
		state_flag = new Flag();
		this.type=type;
		
		switch (type)
		{
		case 0:
			// Output
			outputChannel = new IOChannel(data_reg, regFactory.getAccumulator());
			break;
		case 1:
			// Input
			inputChannel = new IOChannel(regFactory.getAccumulator(), data_reg);
			break;
		default:
			//Input/Output
			inputChannel = new IOChannel(regFactory.getAccumulator(), data_reg);
			outputChannel = new IOChannel(data_reg, regFactory.getAccumulator());
			break;
		}
		
		order_channel =			new Channel(null, null);
		state_flag_channel =	new Channel(null, null);
		address_channel =		new Channel(null, null);
		interruptChannel =		new Channel(null, null);
	}
	
	public InternalDevice(RegisterFactory regFactory)
	{
		data_reg = new Register(8);
		state_flag = new Flag();

		//Input/Output
		inputChannel = new IOChannel(regFactory.getAccumulator(), data_reg);
		outputChannel = new IOChannel(data_reg, regFactory.getAccumulator());

		order_channel =			new Channel(null, null);
		state_flag_channel =	new Channel(null, null);
		address_channel =		new Channel(null, null);
		interruptChannel =		new Channel(null, null);
	}
	
	/**
	 * Получить регистр состояния
	 * @return регистр состояния внешнего усройства
	 */
	public Flag getStateFlag()
	{
		return state_flag;
	}
	
	/**
	 * Получить регистр данных внешнего усройства
	 * @return регистр данных ВУ
	 */
	public Register getDataRegister()
	{
		return data_reg;
	}
	
	/**
	 * Получить канал ввода
	 * @return канал ввода
	 */
	public IChannel getInputChannel()
	{
		return inputChannel;
	}
	
	/**
	 * Получить канал вывода
	 * @return канал вывода
	 */
	public IChannel getOutputChannel()
	{
		return outputChannel;
	}
	
	/**
	 * 
	 * @return
	 */
	public IChannel	getIORequestChannel()
	{
		return order_channel;
	}
	
	/**
	 * 
	 * @return
	 */
	public IChannel	getAddressChannel()
	{
		return address_channel;
	}
	
	/**
	 * 
	 * @return
	 */
	public IChannel	getStateFlagChannel()
	{
		return state_flag_channel;
	}
	public IChannel	getInterruptionRequestChannel()
	{
		return interruptChannel;
	}
	
//	private Channel	order_channel;		
//	private Channel	address_channel;		
//	private Channel	state_flag_channel;	
//	private Channel	intrpt_channel;		
//	
//	private IOChannel	data_channel;		
//	private Register	data_reg;			
//	private Flag		state_flag;			
	
	
	
	private Register data_reg;			// Регистр данных
	private Flag state_flag;			// флаг ВУ
	private int type;					// Тип внешнего устройства
	
	private IOChannel inputChannel;		// Шина ввода
	private IOChannel outputChannel; 	// Шина вывода
	
	private Channel order_channel;		// Приказ на ввод/вывод
	private Channel state_flag_channel;	// Состояние флагов ВУ
	private Channel address_channel;	// Адрес ВУ
	private Channel interruptChannel;	// Запрос прерывания
}
