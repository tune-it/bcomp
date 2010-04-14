/*-----------------------------------------------------------------------------
  Память (программ/микропрограмм).
-----------------------------------------------------------------------------*/
class EMemory implements IRegister
{
	public EMemory(int memory_length, int memory_width,IRegister adress_register)
	{
		this.adress_register = adress_register;
		this.memory_length = memory_length;
		memory = new ERegister[memory_length];
		for (int i = 0; i < memory_length; i++)
		{
			memory[i] = new ERegister(memory_width);
		}
		// Обработка исключения - разрядность РА ~ кол-ву ячеек памяти
	}
	public boolean[] SendData()
	{
		return memory[MakeAdress()].SendData();
	}
	
	public void GetData(boolean[] bits)
	{
		memory[MakeAdress()].GetData(bits);
	}
	
	public int Width()
	{
		return memory_width;
	}
	
//	public boolean[] GetAdress()
//	{
//		return null;
//	}
	
	private int MakeAdress()
	{
		int adress=0;
		boolean[] bits = adress_register.SendData();
		
		for (int i=0; i < adress_register.Width(); i++)
		{
			if( bits[i] )
			{
				adress+=Math.pow(2, i);
			}
		}
		return adress;
	}
	
	private int         memory_length;   // Длина памяти
	private int         memory_width;    // Разрядность памяти
	private IRegister[] memory;          // 
	private IRegister   adress_register; // Регистр адреса
}