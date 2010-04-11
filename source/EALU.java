/*-----------------------------------------------------------------------------
  АЛУ (Арифметическо-логическое устройство)
-----------------------------------------------------------------------------*/
public class EALU implements IRegister
{
	EALU(IRegister left_input, IRegister right_input, int buffer_length)
	{
		this.left_input = left_input;
		this.right_input = right_input;                 // Exception?
		buffer_register = new ERegister(buffer_length);
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
	
	public void And() // Логическое умножение
	{
		
	}
	
	public void Addition() // Логическое сложение
	{
		
	}
	
	public void GetData(boolean[] bits) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean[] SendData() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private IRegister left_input;      // Левый вход АЛУ (А, РС, РК)
	private IRegister right_input;     // Правый вход АЛУ (РД, РК, СК)
	private IRegister buffer_register; // Буферный регистр
	private boolean   left_reverse;     // Вкл/Откл левый инвертор
	private boolean   right_reverse;     // Вкл/Откл левый инвертор
}
