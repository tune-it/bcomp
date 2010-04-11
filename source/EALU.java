/*-----------------------------------------------------------------------------
  АЛУ (Арифметическо-логическое устройство)
-----------------------------------------------------------------------------*/
public class EALU
{
	private EALU(IRegister left_input, IRegister right_input, int buffer_length)
	{
		this.left_input = left_input;
		this.right_input = right_input;
		buffer_register = new ERegister(buffer_length);
	}
	
	private IRegister left_input;      // Левый вход АЛУ (А, РС, РК)
	private IRegister right_input;     // Правый вход АЛУ (РД, РК, СК)
	private ERegister buffer_register; // Буферный регистр
}
