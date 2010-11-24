package Machine;

/**
* АЛУ (Арифметическо-логическое устройство)
* @author Ponomarev
*/
public class ALU
{
	public ALU(RegisterFactory reg_factory, FlagFactory flag_factory)
	{
		this.left_input = reg_factory.getLeftALUInput();
		this.right_input = reg_factory.getRightALUInput();                 // Exception?
		this.buffer_register = reg_factory.getBufferRegister();
		this.accumulator = reg_factory.getAccumulator();
		this.flag_factory = flag_factory;
		left_reverse = false;                           // 
		right_reverse = false;
		// Обработка исключений - разрядность входов ~ разрядности БР
	}
	
	/**
	 * Включить правый инвертор
	 */
	public void setRightReverse()
	{
		right_reverse = true;
	}
	
	/**
	 * Включить левый инвертор
	 */
	public void setLeftReverse()
	{
		left_reverse = true;
	}
	
	/**
	 * Включить инкрементор
	 */
	public void setIncrement()
	{
		incrementor = true;
	}
	
	/**
	 * Сброс инкрементора и инверторов
	 */
	public void resetALU()
	{
		left_reverse	= false;
		right_reverse	= false;
		incrementor		= false;
	}
	
	/**
	 * Инвертор.
	 * Инвертирует reg при условии, что установлен бит инверсии flag 
	 * @param reg - инвертируемый регистр
	 * @param flaп - бит инверсии
	 */
	private void reverse(IRegister reg, boolean flag)  // 
	{
		if (flag) reg.setValue(~reg.getValue());
	}
	
	/**
	 * Логическое умножение правого и левого входов АЛУ
	 */
	public void and() // 
	{
		reverse(left_input,  left_reverse);
		reverse(right_input, right_reverse);
		
		buffer_register.setValue(left_input.getValue() & right_input.getValue());
	}
	/**
	 * Cложение правого и левого входов АЛУ
	 */
	public void add()
	{
		reverse(left_input,  left_reverse);
		reverse(right_input, right_reverse);
		
		buffer_register.setValue(left_input.getValue() + right_input.getValue());
		
		if (incrementor) buffer_register.setValue(buffer_register.getValue() + 1);
	}
	/**
	 *  Циклический сдвиг влево
	 */
	public void rol()
	{
		int bits = accumulator.getValue();
		if (flag_factory.getC().getValue() != 0)
		{
			bits = bits | ((int) StrictMath.pow(2, accumulator.width()));
		}
		
		int upperbit = bits & ((int) Math.pow(2, accumulator.width()));
		bits = bits<<1;
		
		if (upperbit != 0)
		{
			bits = bits | 1;
		}
		
		buffer_register.setValue(bits);
	}
	/**
	 *  Циклический сдвиг право
	 */
	public void ror()
	{
		int bits = accumulator.getValue();
		if (flag_factory.getC().getValue() != 0)
		{
			bits = bits | ((int) Math.pow(2, accumulator.width()));
		}
		
		int lowerbit = bits & 1;
		bits = bits>>1;
		
		if (lowerbit != 0)
		{
			bits = bits | ((int) Math.pow(2, accumulator.width()));
		}
		
		buffer_register.setValue(bits);
	}
	
	/**
	 * Перенос старшего бита БР в С (установка С, если присутствует перенос)
	 */
	public void setCIfExist()
	{
		if ( ( buffer_register.getValue() &(int) Math.pow(2, buffer_register.width())>>1 ) != 0)
		{
			flag_factory.getC().setFlag();
			flag_factory.refreshStateCounter();
		}
		else
		{
			flag_factory.getC().clearFlag();
			flag_factory.refreshStateCounter();
		}
	}
	/**
	 * Очистить С 
	 */
	public void clearC()
	{
		flag_factory.getC().clearFlag();
		flag_factory.refreshStateCounter();
	}
	/**
	 * Установить С
	 */
	public void setC()
	{
		flag_factory.getC().setFlag();
		flag_factory.refreshStateCounter();
	}
	/**
	 * Установить Z, если присутствует
	 */
	public void setZ()
	{
		if (buffer_register.getValue() == 0)
		{
			flag_factory.getZ().setFlag();
			flag_factory.refreshStateCounter();
		}
		else
		{
			flag_factory.getZ().clearFlag();
			flag_factory.refreshStateCounter();
		}
	}
	/**
	 * Установить N, если присутствует
	 */
	public void setN()
	{
		if ( ( buffer_register.getValue() &(int) Math.pow(2, buffer_register.width())>>2 ) != 0)
		{
			flag_factory.getN().setFlag();
			flag_factory.refreshStateCounter();
		}
		else
		{
			flag_factory.getN().clearFlag();
			flag_factory.refreshStateCounter();
		}
	}
	
	private Register		left_input;			// Левый вход АЛУ (А, РС, КР)
	private Register		right_input;		// Правый вход АЛУ (РД, РК, СК)
	private Register		buffer_register;	// Буферный регистр
	private Register		accumulator;		// Аккумулятор
	private boolean			left_reverse;		// Вкл/Откл левый инвертор
	private boolean			right_reverse;		// Вкл/Откл правый инвертор
	private boolean			incrementor;		// Вкл/Откл инкрементор
	private FlagFactory		flag_factory;		// Фабрика флагов
}