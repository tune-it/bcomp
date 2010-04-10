/*-----------------------------------------------------------------------------
  Регистр - набор битовых ячеек
-----------------------------------------------------------------------------*/
public class ERegister implements IRegister
{
	int       register_length;
	boolean[] data; 
	
	ERegister(int n)
	{
		register_length = n;
		data = new boolean[n];
	}

	public boolean[] SendData()
	{
		return data;
	}
	public void GetData(boolean[] bits)
	{
		
	}
}
