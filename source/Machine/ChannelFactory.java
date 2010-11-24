package Machine;

/**
* Фабрика регистров
* @author Ponomarev
*/
public class ChannelFactory 
{
	public ChannelFactory(RegisterFactory factory, Memory memory, MicrocommandMemory microcommand_memory)
	{
		this.channels = new Channel[15];
		
		this.channels[0] = new Channel(factory.getRightALUInput(), factory.getDataRegister());
		this.channels[1] = new Channel(factory.getRightALUInput(), factory.getCommandRegister());
		this.channels[2] = new Channel(factory.getRightALUInput(), factory.getInstructionPointer());
		
		this.channels[3] = new Channel(factory.getLeftALUInput(), factory.getAccumulator());
		this.channels[4] = new Channel(factory.getLeftALUInput(), factory.getStateCounter());
		this.channels[5] = new Channel(factory.getLeftALUInput(), factory.getInputRegister());
		
		this.channels[6]  = new Channel(factory.getAddressRegister(),		factory.getBufferRegister());
		this.channels[7]  = new Channel(factory.getDataRegister(),		factory.getBufferRegister());
		this.channels[8]  = new Channel(factory.getCommandRegister(),		factory.getBufferRegister());
		this.channels[9]  = new Channel(factory.getInstructionPointer(),	factory.getBufferRegister());
		this.channels[10] = new Channel(factory.getAccumulator(),			factory.getBufferRegister());
		
		this.channels[11] = new Channel(factory.getDataRegister(), memory);
		this.channels[12] = new Channel(memory, factory.getDataRegister());
		
		this.channels[13] = new Channel(factory.getMicroCommandRegister(), microcommand_memory);
		
		this.channels[14] = new Channel(factory.getAddressRegister(), factory.getAddressRegister());
	}
	/**
	 * Канал РД -> Правый вход АЛУ
	 * @return 
	 */
	public Channel FromDR()
	{
		return channels[0];
	}
	
	/**
	 * Канал РК -> Правый вход АЛУ
	 * @return 
	 */
	public Channel FromCR()
	{
		return channels[1];
	}
	
	/**
	 * Канал СК -> Правый вход АЛУ
	 * @return
	 */
	public Channel FromIP()
	{
		return channels[2];
	}
	
	/*-----------------------------------------------------------------------------
	-----------------------------------------------------------------------------*/
	
	/**
	 * Канал А  -> Левый вход АЛУ
	 * @return
	 */
	public Channel FromAcc()
	{
		return channels[3];
	}
	
	/**
	 * Канал РС -> Левый вход АЛУ
	 * @return
	 */
	public Channel FromSC()
	{
		return channels[4];
	}
	
	/**
	 * Канал КР -> Левый вход АЛУ
	 * @return
	 */
	public Channel FromIR()
	{
		return channels[5];
	}
	
	/*-----------------------------------------------------------------------------
	-----------------------------------------------------------------------------*/
	
	/**
	 * Канал БР -> РА
	 * @return
	 */
	public Channel ToAR()
	{
		return channels[6];
	}
	
	/**
	 * Канал БР -> РД
	 * @return
	 */
	public Channel ToDR()
	{
		return channels[7];
	}
	
	/**
	 * Канал БР -> РК
	 * @return
	 */
	public Channel ToCR()
	{
		return channels[8];
	}
	
	/**
	 * Канал БР -> СК
	 * @return
	 */
	public Channel ToIP()
	{
		return channels[9];
	}
	
	/**
	 * Канал БР -> А
	 * @return
	 */
	public Channel ToAcc()
	{
		return channels[10];
	}
	
	
	/*-----------------------------------------------------------------------------
	-----------------------------------------------------------------------------*/
	
	/**
	 * Канал Память -> РД
	 * @return
	 */
	public Channel ReadFromMem()
	{
		return channels[11];
	}
	
	/**
	 * Канал РД -> Память
	 * @return
	 */
	public Channel WriteToMem()
	{
		return channels[12];
	}
	
	/**
	 * Канал Память МК -> РМК
	 * @return
	 */
	public Channel MicroCommandToRMC()
	{
		return channels[13];
	}
	
	/*-----------------------------------------------------------------------------
	-----------------------------------------------------------------------------*/
	
	/**
	 * Канал РА -> Память
	 * @return
	 */
	public Channel AdressRegToMem()
	{
		return channels[14];
	}
	
	/**
	 * Закрыть все каналы.
	 */
	public void CloseAllChannels()
	{
		for (int i = 0; i < channels.length; i++)
		{
			channels[i].close();
		}
	}
	
	/*-----------------------------------------------------------------------------
	-----------------------------------------------------------------------------*/
	// 0  - РД -> Правый вход АЛУ
	// 1  - РК -> Правый вход АЛУ
	// 2  - СК -> Правый вход АЛУ
	
	// 3  - А  -> Левый вход АЛУ
	// 4  - РС -> Левый вход АЛУ
	// 5  - КР -> Левый вход АЛУ
	
	// 6  - БР -> РА
	// 7  - БР -> РД
	// 8  - БР -> РК
	// 9  - БР -> СК
	// 10 - БР -> А
	
	// 11 - Память -> РД
	// 12 - РД -> Память
	
	// 13 - Память МК -> РМК
	
	// 14 - РА -> Память
	
	Channel[] channels;
}
