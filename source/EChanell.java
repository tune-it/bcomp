/*-----------------------------------------------------------------------------
    Канал.
    Осуществляет пересылку данных регистр-регистр, память-регистр и регистр-память.
-----------------------------------------------------------------------------*/
public class EChanell implements IChanell 
{
	EChanell(IRegister destination, IRegister source)
	{
		this.destination = destination;
		this.source = source;
		connection = false;
	}
	
	public void Open()
	{
		destination.GetData(source.SendData());
		connection = true;
	}
	
	public void Close()
	{
		connection = false;
	}
	
	private IRegister destination;    // Регистр-приемник
	private IRegister source;         // Регистр-источник
//	private boolean   visible;        // "Видимость" канала 
	private boolean   connection;     // "Открытость" канала
}
