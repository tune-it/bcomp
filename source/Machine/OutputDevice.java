package Machine;

/*-----------------------------------------------------------------------------
	Устройство вывода.
-----------------------------------------------------------------------------*/
public class OutputDevice implements InternalDevice
{
	public OutputDevice(ERegisterFactory reg_factory)
	{
		data_reg = new ERegister(8);
		state_flag = new EFlag();
		channel = new EIOChannel(data_reg, reg_factory.Accumulator());
	}

	public EFlag getStateFlag()
	{
		return state_flag;
	}

	public ERegister getDataRegister()
	{
		return data_reg;
	}

	public IChannel getChannel()
	{
		return channel;
	}
	

	public void connect()
	{
		channel.Open();
	}

	private EIOChannel	channel;
	private ERegister	data_reg;
	private EFlag		state_flag;
}
