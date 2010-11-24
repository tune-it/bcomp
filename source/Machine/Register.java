package Machine;

/*-----------------------------------------------------------------------------
  Регистр (битовая ячейка)
-----------------------------------------------------------------------------*/
public class Register implements IRegister
{
	Register()  // Регистр "по умолчанию" - 16 разрядов
	{
		register_width = 16;
		data = 0;
	}
	
	Register(int width)
	{
		register_width = width;
		data = 0;
	}

	public int getValue()
	{
		return data;
	}
	
	public void setValue(int input)
	{
		data = input;
		controlWidth();
	}
	
	public int width()
	{
		return register_width;
	}
	
	private void controlWidth()
	{
		int mask = (int)Math.pow(2, register_width);
		mask--;
		data = data & mask;
	}
	
	private int	register_width; // Разрядность 
	private int	data;           // Массив "битов"
}
