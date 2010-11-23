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
		flag = false;
	}
	
	public void SetFlag()
	{
		flag = true;
	}

	public void GetData(int bits)
	{
		flag = bits != 0;
	}

	public int SendData()
	{
		return flag ? 1 : 0;
	}

	public int Width()
	{
		return 1;
	}
	
	private boolean flag;
}
