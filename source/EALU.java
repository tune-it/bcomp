/*-----------------------------------------------------------------------------
  АЛУ (Арифметическо-логическое устройство)
-----------------------------------------------------------------------------*/
public class EALU
{
	EALU(ERegister left_input, ERegister right_input, ERegister buffer_register, ERegister accumulator, EFlag c, EFlag n, EFlag z)
	{
		this.left_input = left_input;
		this.right_input = right_input;                 // Exception?
		this.buffer_register = buffer_register;
		this.accumulator = accumulator;
		left_reverse = false;                           // 
		right_reverse = false;
		this.c=c;
		this.n=n;
		this.z=z;
		// Обработка исключений - разрядность входов ~ разрядности БР
	}
	
	public void SetRightReverse() // Включить правый инвертор
	{
		right_reverse = true;
	}
	
	public void SetLeftReverse() // Включить левый инвертор
	{
		left_reverse = true;
	}
	
	public void SetIncrement() // Включить инкрементор
	{
		incrementor = true;
	}
	
	private void Reverse(IRegister reg, boolean flag)  // Инвертор
	{
		if (flag) reg.GetData(~reg.SendData());
	}
	
	public void AND() // Логическое умножение
	{
		Reverse(left_input,  left_reverse);
		Reverse(right_input, right_reverse);
		
		buffer_register.GetData(left_input.SendData() & right_input.SendData());
	}
	
	public void ADD() // Cложение
	{
		Reverse(left_input,  left_reverse);
		Reverse(right_input, right_reverse);
		
		buffer_register.GetData(left_input.SendData() + right_input.SendData());
		
		if (incrementor) buffer_register.GetData(buffer_register.SendData() + 1);
	}
	
	public void ROL()
	{
		int bits = accumulator.SendData();
		if (c.SendData() != 0)
		{
			bits = bits | ((int) Math.pow(2, accumulator.Width()));
		}
		
		int upperbit = bits & ((int) Math.pow(2, accumulator.Width()));
		bits = bits<<1;
		
		if (upperbit != 0)
		{
			bits = bits | 1;
		}
		
		buffer_register.GetData(bits);
	}
	
	public void ROR()
	{
		int bits = accumulator.SendData();
		if (c.SendData() != 0)
		{
			bits = bits | ((int) Math.pow(2, accumulator.Width()));
		}
		
		int lowerbit = bits & 1;
		bits = bits>>1;
		
		if (lowerbit != 0)
		{
			bits = bits | ((int) Math.pow(2, accumulator.Width()));
		}
		
		buffer_register.GetData(bits);
	}
	
	public void SetCIfExist() // Перенос старшего бита БР в С (установка С, если присутствует перенос) 
	{
		if ( ( buffer_register.SendData() &(int) Math.pow(2, buffer_register.Width())>>1 ) != 0)
		{
			c.SetFlag();
		}
	}
	
	public void ClearC() // Очистить С 
	{
		c.ClearFlag();
	}
	
	public void SetC() // Установить С
	{
		c.SetFlag();
	}
	
	public void SetZ() // Установить Z, если присутствует
	{
		if (buffer_register.SendData() == 0)
		{
			z.SetFlag();
		}
	}
	
	public void SetN() // Установить N, если присутствует
	{
		if ( ( buffer_register.SendData() &(int) Math.pow(2, buffer_register.Width())>>2 ) != 0)
		{
			n.SetFlag();
		}
	}
	
	private ERegister	left_input;			// Левый вход АЛУ (А, РС, КР)
	private ERegister	right_input;		// Правый вход АЛУ (РД, РК, СК)
	private ERegister	buffer_register;	// Буферный регистр
	private ERegister	accumulator;		// Аккумулятор
	private boolean		left_reverse;		// Вкл/Откл левый инвертор
	private boolean		right_reverse;		// Вкл/Откл правый инвертор
	private boolean		incrementor;		// Вкл/Откл инкрементор
	private EFlag		c;					// Флаг C
	private EFlag		n;					// Флаг N 
	private EFlag		z;					// Флаг Z
}