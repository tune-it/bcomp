/*-----------------------------------------------------------------------------
  Память (программ/микропрограмм).
-----------------------------------------------------------------------------*/
class EMemory implements IRegister
{
	public EMemory(int memory_width, IRegister adress_register)
	{
		this.adress_register = adress_register;
		this.memory_length = (int) Math.pow(2, adress_register.Width());
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
	private IRegister[]	memory;          // 
	private IRegister	adress_register; // Регистр адреса
}