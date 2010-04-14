/*-----------------------------------------------------------------------------
  Флаг (однобитовый регистр)
-----------------------------------------------------------------------------*/
public class EFlag extends ERegister
{
	EFlag()
	{
		super.register_width = 1;
		super.data = new boolean[1];
	}
	
	public void ClearFlag()
	{
		super.data[0]=false;
	}
	
	public void SetFlag()
	{
		super.data[0]=true;
	}
//		
//	int       register_width; // Разрядность 
//	boolean[] data;           // Массив "битов"
}
