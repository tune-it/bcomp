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

	public void setBit(int value, int bitno)
	{
		data = (data & (~(1 << bitno))) | (value << bitno);
	}

	public void invertBit(int bitno)
	{
		int bitpos = 1 << bitno;
		data = (data & ~bitpos) | (~(data & bitpos) & bitpos);
	}

	public int getBit(int bitno)
	{
		return (data >> bitno) & 1;
	}

	public boolean checkBit(int bitno)
	{
		return getBit(bitno) == 1;
	}

	public int getBits(int startbit, int bitcount)
	{
		return (data >> startbit) & ((1 << bitcount) - 1);
	}

	public int width()
	{
		return register_width;
	}

	private int				register_width;	// Разрядность 
	private int				mask;			// Маска для проверки разрядности
	private volatile int	data;			// Массив "битов"
}
