/*-----------------------------------------------------------------------------
  Память (программ/микропрограмм).
  
-----------------------------------------------------------------------------*/
class EMemory implements IRegister
{
	private EMemory(int memory_length, int memory_width,IRegister adress_register)
	{
		this.adress_register = adress_register;
		this.memory_length = memory_length;
		memory = new ERegister[memory_length];
		for (int i = 0; i < memory_length; i++)
		{
			memory[i] = new ERegister(memory_width);
		}
	}
	public boolean[] SendData()
	{
		return null;
	}
	public void GetData(boolean[] bits)
	{
		//memory[]
	}
	
	private int         memory_length;   // Длина памяти
	private int         memory_width;    // Разрядность памяти
	private IRegister[] memory;          // 
	private IRegister   adress_register; // Регистр адреса
}