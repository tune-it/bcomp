/**
 * Интерфейс канала
 * @author Ponomarev
 * @version $Id$
 */

package Machine;

public interface IChannel
{
	/**
	 * Открыть канал
	 */
	void open();
	
	/**
	 * Закрыть канал
	 */
	void close();
	
	/**
	 * Состояние канала
	 * @return Возвращает состояние канала (true|false = open|closed)
	 */
	public boolean getConnect();	// 
}
