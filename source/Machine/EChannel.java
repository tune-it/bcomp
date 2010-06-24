package Machine;

/*-----------------------------------------------------------------------------
    Канал.
    Осуществляет пересылку данных регистр-регистр, память-регистр и регистр-память.
-----------------------------------------------------------------------------*/
public class EChannel implements IChannel 
{
	public EChannel(IRegister destination, IRegister source)
	{
		this.destination = destination;
		this.source = source;
		connection = false;
	}
	
	public void Open()
	{
		if ((source != null) && (destination != null))
		{
			destination.GetData(source.SendData());
			connection = true;
		}
	}
	
	public void Close()
	{
		connection = false;
	}
	
	public boolean GetConnect()
	{
		return connection;
	}

	
	private IRegister destination;    // Регистр-приемник
	private IRegister source;         // Регистр-источник
	private boolean   connection;     // "Открытость" канала
}
