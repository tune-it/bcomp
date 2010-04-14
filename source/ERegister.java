import java.util.Arrays;

/*-----------------------------------------------------------------------------
  Регистр (битовая ячейка)
-----------------------------------------------------------------------------*/
public class ERegister implements IRegister
{
	ERegister()  // Регистр "по умолчанию" - 16 разрядов
	{
		register_width = 16;
		data = new boolean[16];
	}
	
	ERegister(int width)
	{
		register_width = width;
		data = new boolean[width];
	}

	public boolean[] SendData()
	{
		return data;
	}
	
	public void GetData(boolean[] bits)
	{
		data = Arrays.copyOf(bits, register_width);
	}
	
	public int Width()
	{
		return register_width;
	}
	
	int       register_width; // Разрядность 
	boolean[] data;           // Массив "битов"
}
