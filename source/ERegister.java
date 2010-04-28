/*-----------------------------------------------------------------------------
  Регистр (битовая ячейка)
-----------------------------------------------------------------------------*/
public class ERegister implements IRegister
{
	ERegister()  // Регистр "по умолчанию" - 16 разрядов
	{
		register_width = 16;
		data = 0;
	}
	
	ERegister(int width)
	{
		register_width = width;
		data = 0;
	}

	public int SendData()
	{
		return data;
	}
	
	public void GetData(int input)
	{
		data = input;
		ControlWidth();
	}
	
	public int Width()
	{
		return register_width;
	}
	
	private void ControlWidth()
	{
		int mask = (int)Math.pow(2, register_width);
		mask--;
		data = data & mask;
	}
	
	int	register_width; // Разрядность 
	int	data;           // Массив "битов"
}
