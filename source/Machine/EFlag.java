package Machine;

/*-----------------------------------------------------------------------------
  Флаг (однобитовый регистр)
-----------------------------------------------------------------------------*/
public class EFlag implements IRegister
{
	EFlag()
	{
		flag = false;
	}
	
	public void ClearFlag()
	{
		flag=false;
	}
	
	public void SetFlag()
	{
		flag=true;
	}

	public void GetData(int bits)
	{
		if (bits != 0)
		{
			flag = true;
		}
		else
		{
			flag = false;
		}
	}

	public int SendData()
	{
		if(flag)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

	public int Width()
	{
		return 1;
	}
	
	private boolean flag;
}
