import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Formatter;
import Machine.*;

/*-----------------------------------------------------------------------------
				Отрисовка обычного регистра. По умолчанию
				отображает содержимое в двоичном виде.
-----------------------------------------------------------------------------*/

public class EUIRegister
{
	public EUIRegister(IRegister reg, double x, double y, String text)
	{
		this.reg = reg;
		length = reg.Width();
		
		style = false;
		content = ConvertToBin(reg);
		
		leftX = x;
		leftY = y;
		
		width = 17 * reg.Width() - 6;
		height = 50;
		
		dataX = (int)leftX+10;
		dataY = (int)leftY+42;
		
		messX = (int)x + 10;
		messY = (int)y + 18;	
		this.text = text;	
	}

	public EUIRegister(IRegister reg, double x, double y, double height, String text)
	{
		this.reg = reg;
		length = reg.Width();
		
		style = false;
		content = ConvertToBin(reg);
		
		leftX = x;
		leftY = y;
		
		width = 17 * reg.Width() - 6;
		this.height = height;
		
		dataX = (int)leftX+10;
		dataY = (int)leftY+42;
		
		messX = (int)x + 10;
		messY = (int)y + 18;
		this.text = text;		
	}
	
	public EUIRegister(IRegister reg, double x, double y, int messX, int messY, String text)
	{
		this.reg = reg;
		length = reg.Width();
		
		style = false;
		content = ConvertToBin(reg);	
		
		leftX = x;
		leftY = y;
		
		width = 17 * reg.Width() - 6;
		height = 50;
		
		dataX = (int)leftX+10;
		dataY = (int)leftY+42;
		
		this.messX = messX;
		this.messY = messY;
		this.text = text;	
	}
	
	public EUIRegister(IRegister reg, double x, double y, double height, int messX, int messY, String text)
	{
		this.reg = reg;
		length = reg.Width();
		
		style = false;
		content = ConvertToBin(reg);
		
		leftX = x;
		leftY = y;
		
		width = 17 * reg.Width() - 6;
		this.height = height;
		
		dataX = (int)leftX+10;
		dataY = (int)leftY+42;
		
		this.messX = messX;
		this.messY = messY;
		this.text = text;	
	}
		
	public double GetX()									//Получение координаты X левого верхнего угла
	{
		return leftX;
	}
	
	public double GetY()									//Получение координаты Y левого верхнего угла
	{
		return leftY;
	}
	
	public double GetHeight()								//Получение высоты регистра
	{
		return height;
	}
	
	public void SetWidth(int x) 							//Установка длины регистра
	{
		width = x;
	}
	
	public void SetDataPosition(int dataX, int dataY)		//Установка положение содержимого регистра
	{
		this.dataX = dataX;
		this.dataY = dataY;
	}
	
	public int GetDataX()									//Получение координаты Х положения текста
	{
		return dataX;
	}
	
	public int GetDataY()									//Получение координаты У положения текста
	{
		return dataY;
	}
	
	public void SetStyle(boolean x)							//Установка вида содержимого (16ый/2ый true/false)
	{
		style = x;
		
		if (style == false)
		{
			content = ConvertToBin(reg);
			width = 17 * reg.Width() - 6;
		}
		else
		{
			content = ConvertToHex(reg);
			width = 100;
		}
	}
	
	public String GetContent()								//Получение содержимого регистра
	{
		String str = "";
		
		if(content.length() == 19)
			str = content.substring(0,4) + content.substring(5,9) + content.substring(10,14) + content.substring (15);
		if(content.length() == 9)
			str = content.substring(0,4) + content.substring(5,9);
		
		return str;
	}

	public void Draw(Graphics g)							//Отрисовка регистра
	{
		Graphics2D rs = (Graphics2D) g;

		//Обновление содержимого
		if (style == false)
			content = ConvertToBin(reg);
		else
			content = ConvertToHex(reg);
		
		//Отрисовка прямоугольника и контура
		Rectangle2D rect = new Rectangle2D.Double(leftX, leftY, width, height);	
		rs.setPaint(new Color(187,249,166));
		rs.fill(rect);
		rs.setPaint(Color.BLACK);
		rs.setStroke(new BasicStroke(1.0f));
		rs.draw(rect);
		
		//Отрисовка названия
		rs.setFont(new Font("Courier New", Font.BOLD, 20));
		rs.drawString(text, messX, messY);
			
		//Отрисовка фона для содержимого
		rs.setPaint(new Color(231,236,119)); 
		rs.fillRect((int)leftX + 5, (int)leftY + 24, (int)width - 10, 23);
		
		//Отрисовка содержимого	
		rs.setPaint(Color.BLACK);
		rs.setFont(new Font("Courier New", Font.BOLD, 21));
		rs.drawString (content, dataX, dataY);
	}
	
	private String ConvertToBin(IRegister reg)				//Преобразование в двоичный вид
	{
		int pish = reg.SendData();
		String str = "";
		boolean flag = true;
		
		while (flag)								
		{
			str = pish % 2 + str;
			pish = pish / 2;
			if (pish == 0 || pish == 1)
			{
				flag = false;
			}
		}
		str = pish % 2 + str;
			
		int x = str.length();	
		if (str.length() < length)					//
		{											//
			for (int i = 0; i < length - x; i++) 	//Добавление значащих нулей в начало строки
			str = "0" + str;						//
		}											//
		
		if (str.length() > length)					//
		{											//
			for (int i = 0; i < x - length; i++)	//Удаление лишних нулей в начале строки
				str = str.substring(1);				//											
		}											//
		
		//Добавление пробелов между тетрадами для регистров различной разрядности	
		if (length == 16)
		{
			str = str.substring(0,4) + " " + str.substring(4,8) + " " + str.substring(8,12) + " " + str.substring(12,16);
		}
		if (length == 11)
		{
			str = str.substring(0,3) + " " + str.substring(3,7) + " " + str.substring(7,11);
		}
		if (length == 8)
		{
			str = str.substring(0,4) + " " + str.substring(4,8);
		}
		
		return str;
	}
	
	private String ConvertToHex(IRegister reg)				//Преобразование в шестнадцатеричный вид
	{
		Formatter fmt = new Formatter();
		fmt.format("%x", reg.SendData());
		String str = fmt.toString();
		
		int y = str.length();
		if (y <= length / 4 )									//
		{														//
			for (int i = 0; i < (double)length / 4 - y; i++) 	//Добавление значащих нулей в начало строки
				str = "0" + str;								//
		}														//
		return str.toUpperCase();
	}
	
	private IRegister 	reg;		//Регистр
	private String		content;	//Содержимое регистра
	private int      	length;		//Разрядность регистра
	private boolean		style;		//Вид содержимого (16ый/2ый true/false)
	private double		leftX; 		//Координата X левой верхней точки
	private double 		leftY;		//Координата Y левой верхней точки
	private double 		width; 		//Длина регистра
	private double 		height;		//Высота
	private int			dataX;		//Координата X положения содержимого регистра
	private int			dataY;		//Координата Y положения содержимого регистра		
	private int 		messX;		//Координата X положения текста	
	private int       	messY;		//Координата Y положения текста
	private	String    	text;		//Название регистра
}


