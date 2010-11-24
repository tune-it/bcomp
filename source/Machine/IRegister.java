package Machine;

/**
 * Интерфейс регистра/памяти
 * @author Ponomarev
 */
public interface IRegister
{
	/**
	 * Выставить информацию на выход регистра.
	 * @return Возвращает значение данных из регистра
	 */
	int		getValue();
	
	/**
	 * Установить значение данных в регистре
	 * @param input - новое значение.
	 */
	void	setValue(int input);
	
	/**
	 * Разрядность регистра
	 * @return возвращает разрядность регистра
	 */
	int		width();
}
