/*-----------------------------------------------------------------------------
  АЛУ (Арифметическо-логическое устройство)
-----------------------------------------------------------------------------*/
public class EALU
{
	EALU(IRegister left_input, IRegister right_input, IRegister bufer_register, int buffer_length)
	{
		this.left_input = left_input;
		this.right_input = right_input;                 // Exception?
		this.buffer_register = bufer_register;
		left_reverse = false;                            // 
		right_reverse = false;
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
	
	public boolean[] Reverse(IRegister reg)  // Инвертор
	{
		
		return null;
	}
	
	public void And() // Логическое умножение
	{
		
	}
	
	public void Addition() // Логическое сложение
	{
		
	}
	
	public void SetCIfExist() // Перенос 17-го бита БР в С (установка С, если присутствует перенос) 
	{
		
	}
	
	public void ClearC() // Очистить С 
	{
		
	}
	
	public void SetC() // Установить С
	{
		
	}
	
	public void SetN() // Установить N, если присутствует
	{
		
	}
	
	public void SetZ() // Установить Z, если присутствует
	{
		
	}
	
	private IRegister left_input;      // Левый вход АЛУ (А, РС, РК)
	private IRegister right_input;     // Правый вход АЛУ (РД, РК, СК)
	private IRegister buffer_register; // Буферный регистр
	private boolean   left_reverse;    // Вкл/Откл левый инвертор
	private boolean   right_reverse;   // Вкл/Откл правый инвертор
	private boolean   incrementor;     // Вкл/Откл инкрементор
	private IRegister C;
	private IRegister N;
	private IRegister Z;
}
