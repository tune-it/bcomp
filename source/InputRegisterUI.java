import java.awt.Graphics;
import java.awt.Graphics2D;
import Machine.IRegister;

/*-----------------------------------------------------------------------------
		Отрисовка обычного регистра. Изменяет значение
		клавишного регистра машинной части программы.
-----------------------------------------------------------------------------*/

public class InputRegisterUI extends RegisterUI
{
	public InputRegisterUI(IRegister reg, double x, double y, double height, int messX, int messY, String text)
	{
		super(reg, x, y, height, messX, messY, text);	
			
		register = reg;
		pointer_position = 0;
		movement = false;
		active = false;
	}
	
	public void drawPointer(Graphics g)				//Отрисовка указателя
	{
		if(active)
		{
			Graphics2D rs = (Graphics2D) g;
			
			//Условия для зацикливания установки позиций указателя
			if (super.getContent().length() == 16)
			{	
				if (getPointerPosition() == -1)
					setPointerPosition(15);
				if (getPointerPosition() == 16)
					setPointerPosition(0);
			}
			if (super.getContent().length() == 8)
			{	
				if (getPointerPosition() == -1)
					setPointerPosition(7);
				if (getPointerPosition() == 8)
					setPointerPosition(0);
			}
			
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
		pointer_position = position;
	}
	
	public void setMovement(boolean x)						//Вкл/выкл смещение указателя после изменения бита
	{
		movement = x;	
	}
	
	public boolean isMovement()								//Передача текущего состояния смещяемости
	{
		return movement;
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
		{
			String content = super.getContent();				//Содержимого регистра
			int posit = getPointerPosition();					//Позиция указателя
					
			String bit = content.substring(posit, posit+1);		//Бит на который показывает указатель
			
			//Изменение бита на который показывает указатель
			if (bit.equals("0"))
				content = content.substring(0, posit) + "1" + content.substring (posit + 1);
			else
				content = content.substring(0, posit) + "0" + content.substring (posit + 1);
		
			register.GetData((int)convertToDec(content));
			
			if(movement)
				pointer_position ++;
		}
	}
	
	public void setBit(boolean bit)					//Установка бита
	{
		if(active)
		{
			String content = super.getContent();		//Содержимого регистра
			int posit = getPointerPosition();			//Позиция указателя
			
			//Изменение бита на который показывает указатель
			if (bit)
				content = content.substring(0, posit) + "1" + content.substring (posit + 1);
			else
				content = content.substring(0, posit) + "0" + content.substring (posit + 1);
			
			register.GetData((int)convertToDec(content));
			
			if(movement)
				pointer_position ++;
		}
	}
	
	private double convertToDec(String content)		//Преобразование в десятичную систему
	{
		double data = 0;
		for (int i = 0; i < content.length(); i++ )
		{
			if(content.substring(i, i+1).equals("1"))
				data = data + Math.pow(2, content.length() - 1 - i);
		}
		
		return data;
	}
			
	private IRegister	register;				//Регистр
	private int			pointer_position;		//Позиция указателя
	private boolean		movement;				//Сдвигание указателя после изменения бита
	private boolean		active;					//Активность регистра
}
