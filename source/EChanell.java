/////////////////////////////////////////////////////////////////////////////////////////////
// Соединение регистр-регистр или регистр-память
/////////////////////////////////////////////////////////////////////////////////////////////
abstract class EChanellRegToReg implements IChanell 
{
	IRegister destination;  // Назначение
	IRegister source;       // Источник
	boolean visible;        // "Видимость" канала 
	boolean sending;        // Пересылка
	
	EChanellRegToReg(IRegister dst, IRegister src)
	{
		destination = dst;
		source = src;
	}
	
	public void Send()
	{
		
	}
	
	public void Close()
	{
		
	}
	
}
