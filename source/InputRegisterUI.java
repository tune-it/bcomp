/**
 * Отрисовка обычного регистра. Изменяет значение
 * клавишного регистра машинной части программы.
 * @version $Id$
 */

import java.awt.Graphics;
import java.awt.Graphics2D;
import Machine.Register;

public class InputRegisterUI extends RegisterUI
{
	public InputRegisterUI(Register reg, double x, double y, double height, int messX, int messY, String text)
	{
		super(reg, x, y, height, messX, messY, text);

		register = reg;
		pointer_position = 0;
		active = false;
	}

	public void drawPointer(Graphics g)				//Отрисовка указателя
	{
		if(active)
		{
			Graphics2D rs = (Graphics2D) g;

			int	position = getPointerPosition();	//Текущая позиция
			int shift = 0;							//Смещение от первого символа содержимого регистра

			//Условия для перемещения указателя через пробелы
			if (position >= 0 && position < 4)
				shift = position * 13;
			if (position >= 4 && position < 8)
				shift = position * 13 + 13;
			if (position >= 8 && position < 12)
				shift = position * 13 + 13 * 2;
			if (position >= 12 && position < 16)
				shift = position * 13 + 13 * 3;

			//Отрисовка указателя
			int[] mass1 = {super.getDataX() + shift, super.getDataX() + shift + 5, super.getDataX() + shift + 6, super.getDataX() + shift + 11};
			int[] mass2 = {super.getDataY() + 15, super.getDataY() + 6, super.getDataY() + 6, super.getDataY() + 15};
			rs.fillPolygon(mass1, mass2, 4);
			rs.drawPolygon(mass1, mass2, 4);
		}
	}

	public int getPointerPosition()							//Получение позиции указателя
	{
		return pointer_position;
	}

	public void setPointerPosition(int position)			//Установка позиции указателя
	{
		if(active) {
			int cLen = super.getContent().length();

			pointer_position = position < 0 ? cLen - 1 : position >= cLen ? 0 : position;
		}
	}

	public void setActive(boolean x)						//Активировать регистр (выводить указатель)
	{
		active = x;
	}

	public boolean isActive()								//Передача текущего состояния активности
	{
		return active;
	}

	public void setBit()									//Инверсия бита
	{
		if(active)
			register.invertBit(register.width() - pointer_position - 1);
	}

	public void setBit(boolean bit)					//Установка бита
	{
		if(active)
			register.setBit(bit ? 1 : 0, register.width() - pointer_position - 1);
	}

	private Register	register;				//Регистр
	private int			pointer_position;		//Позиция указателя
	private boolean		active;					//Активность регистра
}
