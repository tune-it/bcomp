/**
 * Канал для пересылки типа ввод/вывод
 * Пересылает только в младшие разряды регистра-приемника, не обнуляя старшие.
 * @version $Id$
 */

package Machine;

public class IOChannel implements IChannel
{
	public IOChannel(IRegister destination, IRegister source)
	{
		this.destination = destination;
		this.source = source;
		connection = false;
	}
	
	public void close()
	{
		connection = false;
	}

	public void open()
	{
		if (destination.width() > source.width())
		{
			int mask = ((int) Math.pow(2, destination.width()) - 1) - ((int) Math.pow(2, source.width()) - 1);
			destination.setValue((destination.getValue() & mask) | (source.getValue()));
			connection = true;
		}
		else
		{
			destination.setValue(source.getValue());
			connection = true;
		}
	}
	
	public boolean getConnect()
	{
		return connection;
	}
	
	private IRegister destination;    // Регистр-приемник
	private IRegister source;         // Регистр-источник
	private boolean   connection;     // "Открытость" канала
}
