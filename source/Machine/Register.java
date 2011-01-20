package Machine;

/*-----------------------------------------------------------------------------
  Регистр (битовая ячейка)
-----------------------------------------------------------------------------*/
public class Register implements IRegister
{
	Register()  // Регистр "по умолчанию" - 16 разрядов
	{
		this(16);
	}

	Register(int width)
	{
		register_width = width;
		mask = (1 << register_width) - 1;
		data = 0;
	}

	public int getValue()
	{
		return data;
	}

	public void setValue(int input)
	{
		data = input & mask;
	}

	public void setValue(IRegister register)
	{
		setValue(register.getValue());
	}
	
	public int width()
	{
		return register_width;
	}

	private int				register_width;	// Разрядность 
	private int				mask;			// Маска для проверки разрядности
	private volatile int	data;			// Массив "битов"
}
