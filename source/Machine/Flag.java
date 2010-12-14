package Machine;

/**
* Флаг (однобитовый регистр)
* @author Ponomarev
*/
public class Flag implements IRegister
{
	Flag()
	{
		flag = false;
	}
	
	public void clearFlag()
	{
		flag = false;
	}
	
	public void setFlag()
	{
		flag = true;
	}

	public void setValue(int bits)
	{
		flag = bits != 0;
	}

	public void setValue(boolean value)
	{
		flag = value;
	}

	public int getValue()
	{
		return flag ? 1 : 0;
	}

	public int width()
	{
		return 1;
	}
	
	private boolean flag;
}
