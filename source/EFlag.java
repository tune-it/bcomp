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

	public void GetData(boolean[] bits)
	{
		flag = bits[0];
	}

	public boolean[] SendData() {
		boolean[] data = new boolean[1];
		data[0] = flag;
		return data;
	}

	public int Width()
	{
		return 1;
	}
	
	private boolean flag;
}
