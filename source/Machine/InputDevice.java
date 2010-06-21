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
		channel = new EIOChannel(reg_factory.Accumulator(), data_reg);
	}
	
	public InputDevice(int width, ERegisterFactory reg_factory)
	{
		data_reg = new ERegister(width);
		state_flag = new EFlag();
		channel = new EIOChannel(reg_factory.Accumulator(), data_reg);
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
	private ERegister 	data_reg;
	private EFlag		state_flag;
}