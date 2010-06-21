import java.awt.Graphics;
import java.awt.Graphics2D;
import Machine.IRegister;

/*-----------------------------------------------------------------------------
		Отрисовка обычного регистра. Изменяет значение
		клавишного регистра машинной части программы.
-----------------------------------------------------------------------------*/

public class EUIInputRegister extends EUIRegister
{
	public EUIInputRegister(IRegister reg, double x, double y, double height, int messX, int messY, String text)
	{
		super(reg, x, y, height, messX, messY, text);	
			
		register = reg;
		pointer_position = 0;
		movement = false;
		active = false;
	}
	
	public void DrawPointer(Graphics g)				//Отрисовка указателя
	{
		if(active)
		{
			Graphics2D rs = (Graphics2D) g;
			
			//Условия для зацикливания установки позиций указателя
			if (super.GetContent().length() == 16)
			{	
				if (GetPointerPosition() == -1)
					SetPointerPosition(15);
				if (GetPointerPosition() == 16)
					SetPointerPosition(0);
			}
			if (super.GetContent().length() == 8)
			{	
				if (GetPointerPosition() == -1)
					SetPointerPosition(7);
				if (GetPointerPosition() == 8)
					SetPointerPosition(0);
			}
			
			int	position = GetPointerPosition();	//Текущая позиция
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
			int[] mass1 = {super.GetDataX() + shift, super.GetDataX() + shift + 5, super.GetDataX() + shift + 6, super.GetDataX() + shift + 11};
			int[] mass2 = {super.GetDataY() + 15, super.GetDataY() + 6, super.GetDataY() + 6, super.GetDataY() + 15};
			rs.fillPolygon(mass1, mass2, 4);
			rs.drawPolygon(mass1, mass2, 4);
		}
	}
	
	public int GetPointerPosition()							//Получение позиции указателя
	{
		return pointer_position;
	}
	
	public void SetPointerPosition(int position)			//Установка позиции указателя
	{
		pointer_position = position;
	}
	
	public void SetMovement(boolean x)						//Вкл/выкл смещение указателя после изменения бита
	{
		movement = x;	
	}
	
	public boolean isMovement()								//Передача текущего состояния смещяемости
	{
		return movement;
	}
	
	public void SetActive(boolean x)						//Активировать регистр (выводить указатель)
	{
		active = x;
	}
	
	public boolean isActive()								//Передача текущего состояния активности
	{
		return active;
	}
	
	public void SetBit()									//Инверсия бита
	{
		String content = super.GetContent();				//Содержимого регистра
		int posit = GetPointerPosition();					//Позиция указателя
				
		String bit = content.substring(posit, posit+1);		//Бит на который показывает указатель
		
		//Изменение бита на который показывает указатель
		if (bit.equals("0"))
			content = content.substring(0, posit) + "1" + content.substring (posit + 1);
		else
			content = content.substring(0, posit) + "0" + content.substring (posit + 1);
	
		register.GetData((int)ConvertToDec(content));
		
		if(movement)
			pointer_position ++;
	}
	
	public void SetBit(boolean bit)					//Установка бита
	{
		String content = super.GetContent();		//Содержимого регистра
		int posit = GetPointerPosition();			//Позиция указателя
		
		//Изменение бита на который показывает указатель
		if (bit)
			content = content.substring(0, posit) + "1" + content.substring (posit + 1);
		else
			content = content.substring(0, posit) + "0" + content.substring (posit + 1);
		
		register.GetData((int)ConvertToDec(content));
		
		if(movement)
			pointer_position ++;
	}
	
	private double ConvertToDec(String content)		//Преобразование в десятичную систему
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
