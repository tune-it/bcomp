/*
 * Канал.
 * Осуществляет пересылку данных регистр-регистр, память-регистр и регистр-память.
 * @author Ponomarev
 * @version $Id$
 */

package Machine;

public class Channel implements IChannel 
{
	public Channel(IRegister destination, IRegister source)
	{
		this.destination = destination;
		this.source = source;
		connection = false;
	}
	
	/**
	 * Открывает канал.
	 * Происходит пересылка source -> destination
	 */
	public void open()
	{
		if ((source != null) && (destination != null))
		{
			destination.setValue(source.getValue());
		}
		connection = true;
	}
	
	/**
	 * Закрыть канал.
	 */
	public void close()
	{
		connection = false;
	}
	
	/**
	 * Получить статус канала.
	 * true - открыт.
	 * false - закрыт.
	 */
	public boolean getConnect()
	{
		return connection;
	}

	
	private IRegister destination;    // Регистр-приемник
	private IRegister source;         // Регистр-источник
	private boolean   connection;     // "Открытость" канала
}
