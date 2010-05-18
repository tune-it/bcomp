/*-----------------------------------------------------------------------------
  Фабрика регистров
-----------------------------------------------------------------------------*/
public class EChannelFactory 
{
	public EChannelFactory(ERegisterFactory factory, EMemory memory, EMicrocommandMemory microcommand_memory)
	{
		this.channels = new EChannel[14];
		
		this.channels[0] = new EChannel(factory.RightALUInput(), factory.DataRegister());
		this.channels[1] = new EChannel(factory.RightALUInput(), factory.CommandRegister());
		this.channels[2] = new EChannel(factory.RightALUInput(), factory.InstructionPointer());
		
		this.channels[3] = new EChannel(factory.RightALUInput(), factory.Accumulator());
		this.channels[4] = new EChannel(factory.RightALUInput(), factory.StateCounter());
		this.channels[5] = new EChannel(factory.RightALUInput(), factory.InputRegister());
		
		this.channels[6]  = new EChannel(factory.AdressRegister(),		factory.BufferRegister());
		this.channels[7]  = new EChannel(factory.DataRegister(),		factory.BufferRegister());
		this.channels[8]  = new EChannel(factory.CommandRegister(),		factory.BufferRegister());
		this.channels[9]  = new EChannel(factory.InstructionPointer(),	factory.BufferRegister());
		this.channels[10] = new EChannel(factory.Accumulator(),			factory.BufferRegister());
		
		this.channels[11] = new EChannel(factory.DataRegister(), memory);
		this.channels[12] = new EChannel(memory, factory.DataRegister());
		this.channels[13] = new EChannel(factory.MicroCommandRegister(), microcommand_memory);
	}
	
	public EChannel FromDR()
	{
		return channels[0];
	}
	
	public EChannel FromCR()
	{
		return channels[1];
	}
	
	public EChannel FromIP()
	{
		return channels[2];
	}
	
	/*-----------------------------------------------------------------------------
	-----------------------------------------------------------------------------*/
	
	public EChannel FromAcc()
	{
		return channels[3];
	}
	
	public EChannel FromSC()
	{
		return channels[4];
	}
	
	public EChannel FromIR()
	{
		return channels[5];
	}
	
	/*-----------------------------------------------------------------------------
	-----------------------------------------------------------------------------*/
	
	public EChannel ToAR()
	{
		return channels[6];
	}
	
	public EChannel ToDR()
	{
		return channels[7];
	}
	
	public EChannel ToCR()
	{
		return channels[8];
	}
	
	public EChannel ToIP()
	{
		return channels[9];
	}
	
	public EChannel ToAcc()
	{
		return channels[10];
	}
	
	/*-----------------------------------------------------------------------------
	-----------------------------------------------------------------------------*/
	
	public EChannel ReadFromMem()
	{
		return channels[11];
	}
	
	public EChannel WriteToMem()
	{
		return channels[12];
	}
	
	public EChannel MicroCommandToRMC()
	{
		return channels[13];
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
	
	EChannel[] channels;
}
