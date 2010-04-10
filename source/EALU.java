/*-----------------------------------------------------------------------------
  АЛУ (Арифметическо-логическое устройство)
-----------------------------------------------------------------------------*/
public class EALU
{
	private IRegister  left_input;
	private IRegister right_input;
	private ERegister buffer_register;
	
	private EALU(IRegister li, IRegister ri, int n)
	{
		left_input = li;
		right_input = ri;
		buffer_register = new ERegister(n);
	}
}
