/**
 * Отрисовка обычного регистра. По умолчанию
 * отображает содержимое в двоичном виде.
 * @version $Id$
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Formatter;
import Machine.*;

public class RegisterUI
{
	public RegisterUI(IRegister reg, double x, double y, String text)
	{
		this.reg = reg;
		length = reg.width();
		
		style = false;
		content = convertToBin(reg);
		
		leftX = x;
		leftY = y;
		
		width = 17 * reg.width() - 6;
		height = 50;
		
		contentX = (int)leftX+10;
		contentY = (int)leftY+42;
		
		messX = (int)x + 10;
		messY = (int)y + 18;	
		this.text = text;	
	}

	public RegisterUI(IRegister reg, double x, double y, double height, String text)
	{
		this.reg = reg;
		length = reg.width();
		
		style = false;
		content = convertToBin(reg);
		
		leftX = x;
		leftY = y;
		
		width = 17 * reg.width() - 6;
		this.height = height;
		
		contentX = (int)leftX+10;
		contentY = (int)leftY+42;
		
		messX = (int)x + 10;
		messY = (int)y + 18;
		this.text = text;		
	}
	
	public RegisterUI(IRegister reg, double x, double y, int messX, int messY, String text)
	{
		this.reg = reg;
		length = reg.width();
		
		style = false;
		content = convertToBin(reg);	
		
		leftX = x;
		leftY = y;
		
		width = 17 * reg.width() - 6;
		height = 50;
		
		contentX = (int)leftX+10;
		contentY = (int)leftY+42;
		
		this.messX = messX;
		this.messY = messY;
		this.text = text;	
	}
	
	public RegisterUI(IRegister reg, double x, double y, double height, int messX, int messY, String text)
	{
		this.reg = reg;
		length = reg.width();
		
		style = false;
		content = convertToBin(reg);
		
		leftX = x;
		leftY = y;
		
		width = 17 * reg.width() - 6;
		this.height = height;
		
		contentX = (int)leftX+10;
		contentY = (int)leftY+42;
		
		this.messX = messX;
		this.messY = messY;
		this.text = text;	
	}
		
	public double getX()									//Получение координаты X левого верхнего угла
	{
		return leftX;
	}
	
	public double getY()									//Получение координаты Y левого верхнего угла
	{
		return leftY;
	}
	
	public double getHeight()								//Получение высоты регистра
	{
		return height;
	}
	
	public void setWidth(int x) 							//Установка длины регистра
	{
		width = x;
	}
	
	public void setContentPosition(int dataX, int dataY)		//Установка положение содержимого регистра
	{
		this.contentX = dataX;
		this.contentY = dataY;
	}
	
	public int getDataX()									//Получение координаты Х положения текста
	{
		return contentX;
	}
	
	public int getDataY()									//Получение координаты У положения текста
	{
		return contentY;
	}
	
	public void setStyle(boolean x)							//Установка вида содержимого (16ый/2ый true/false)
	{
		style = x;
		
		if (style == false)
		{
			content = convertToBin(reg);
			width = 17 * reg.width() - 6;
		}
		else
		{
			content = convertToHex(reg);
			width = 100;
		}
	}
	
	public String getContent()								//Получение содержимого регистра
	{
		String str = "";
		
		if(content.length() == 19)
			str = content.substring(0,4) + content.substring(5,9) + content.substring(10,14) + content.substring (15);
		if(content.length() == 9)
			str = content.substring(0,4) + content.substring(5,9);
		
		return str;
	}

	public void draw(Graphics g)							//Отрисовка регистра
	{
		Graphics2D rs = (Graphics2D) g;

		//Обновление содержимого
		if (style == false)
			content = convertToBin(reg);
		else
			content = convertToHex(reg);
		
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
		rs.drawString (content, contentX, contentY);
	}
	
	private String convertToBin(IRegister reg)				//Преобразование в двоичный вид
	{
		int pish = reg.getValue();
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
	
	private String convertToHex(IRegister reg)				//Преобразование в шестнадцатеричный вид
	{
		Formatter fmt = new Formatter();
		fmt.format("%x", reg.getValue());
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
	private int			contentX;		//Координата X положения содержимого регистра
	private int			contentY;		//Координата Y положения содержимого регистра		
	private int 		messX;		//Координата X положения текста	
	private int       	messY;		//Координата Y положения текста
	private	String    	text;		//Название регистра
}


