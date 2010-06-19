package Machine;

/*-----------------------------------------------------------------------------
	Канал для пересылки типа ввод/вывод
	Пересылает только в младшие разряды регистра-приемника, не обнуляя старшие.
-----------------------------------------------------------------------------*/
public class EIOChannel implements IChannel
{
	public EIOChannel(IRegister destination, IRegister source)
	{
		this.destination = destination;
		this.source = source;
		connection = false;
	}
	
	public void Close()
	{
		connection = false;
	}

	public void Open()
	{
		if (destination.Width() > source.Width())
		{
			int mask = ((int) Math.pow(2, destination.Width()) - 1) - ((int) Math.pow(2, source.Width()) - 1);
			destination.GetData((destination.SendData() & mask) | (source.SendData()));
			connection = true;
		}
		else
		{
			destination.GetData(source.SendData());
			connection = true;
		}
	}
	
	public boolean GetConnect()
	{
		return connection;
	}
	
	private IRegister destination;    // Регистр-приемник
	private IRegister source;         // Регистр-источник
	private boolean   connection;     // "Открытость" канала
}
