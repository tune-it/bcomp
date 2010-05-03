/*-----------------------------------------------------------------------------
  Память (программ/микропрограмм).
-----------------------------------------------------------------------------*/
class EMemory implements IRegister
{
	public EMemory(int memory_width, ERegisterFactory factory)
	{
		this.adress_register = factory.GetAdressRegister();
		this.memory_length = (int) Math.pow(2, factory.GetAdressRegister().Width());
		memory = new ERegister[memory_length];
		for (int i = 0; i < memory_length; i++)
		{
			memory[i] = new ERegister(memory_width);
		}
	}
	public int SendData()
	{
		return memory[MakeAdress()].SendData();
	}
	
	public void GetData(int bits)
	{
		memory[MakeAdress()].GetData(bits);
	}
	
	public int Width()
	{
		return memory_width;
	}
	
	private int MakeAdress()
	{
		return adress_register.SendData();
	}
	
	public int[] GetMemory()
	{
		int[] mem = new int[memory_length];
		for (int i = 0; i < memory_length; i++)
		{
			mem[i] = memory[i].SendData();
		}
		return mem;
	}
	
	private int			memory_length;   // Длина памяти
	private int			memory_width;    // Разрядность памяти
	private ERegister[]	memory;          // 
	private ERegister	adress_register; // Регистр адреса
}