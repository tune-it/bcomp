/*-----------------------------------------------------------------------------
  АЛУ (Арифметическо-логическое устройство)
-----------------------------------------------------------------------------*/
public class EALU
{
	EALU(ERegisterFactory reg_factory, EFlagFactory flag_factory)
	{
		this.left_input = reg_factory.LeftALUInput();
		this.right_input = reg_factory.LeftALUInput();                 // Exception?
		this.buffer_register = reg_factory.BufferRegister();
		this.accumulator = reg_factory.Accumulator();
		left_reverse = false;                           // 
		right_reverse = false;
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
		if (flag_factory.GetC().SendData() != 0)
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
		if (flag_factory.GetC().SendData() != 0)
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
			flag_factory.GetC().SetFlag();
		}
	}
	
	public void ClearC() // Очистить С 
	{
		flag_factory.GetC().ClearFlag();
		flag_factory.RefreshStateCounter();
	}
	
	public void SetC() // Установить С
	{
		flag_factory.GetC().SetFlag();
		flag_factory.RefreshStateCounter();
	}
	
	public void SetZ() // Установить Z, если присутствует
	{
		if (buffer_register.SendData() == 0)
		{
			flag_factory.GetZ().SetFlag();
			flag_factory.RefreshStateCounter();
		}
	}
	
	public void SetN() // Установить N, если присутствует
	{
		if ( ( buffer_register.SendData() &(int) Math.pow(2, buffer_register.Width())>>2 ) != 0)
		{
			flag_factory.GetN().SetFlag();
			flag_factory.RefreshStateCounter();
		}
	}
	
	private ERegister		left_input;			// Левый вход АЛУ (А, РС, КР)
	private ERegister		right_input;		// Правый вход АЛУ (РД, РК, СК)
	private ERegister		buffer_register;	// Буферный регистр
	private ERegister		accumulator;		// Аккумулятор
	private boolean			left_reverse;		// Вкл/Откл левый инвертор
	private boolean			right_reverse;		// Вкл/Откл правый инвертор
	private boolean			incrementor;		// Вкл/Откл инкрементор
	private EFlagFactory	flag_factory;		// Фабрика флагов
}