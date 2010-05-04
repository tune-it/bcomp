import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/*-----------------------------------------------------------------------------
				Отрисовка обычного регистра
-----------------------------------------------------------------------------*/

public class EUIRegister implements IUIBaseObject
{
	public EUIRegister(IRegister reg, double x, double y, double height, String text)
	{
		this.reg = reg;
		length = reg.Width();
		leftX = x;
		leftY = y;
		width = 16*reg.Width() -5;
		this.height = height;
		messX = (int)x + 10;
		messY = (int)y + 20;
		this.text = text;	
	}
	
	public double GetX()					//Получение координаты X левого верхнего угла
	{
		return leftX;
	}
	
	public double GetY()					//Получение координаты Y левого верхнего угла
	{
		return leftY;
	}
	
	public double GetHeight()				//Получение высоты регистра
	{
		return height;
	}
	
	public void SetWidth(int w) 			//Установка длины регистра
	{
		width = w;
	}

	public void Draw(Graphics g)			//Отрисовка прямоугольника и текста
	{
		Graphics2D rs = (Graphics2D) g;
		
		Rectangle2D rect = new Rectangle2D.Double(leftX, leftY, width, height);
		rs.setPaint(Color.GREEN);
		rs.fill(rect);
		rs.setPaint(Color.BLACK);
		rs.draw(rect);
		
		Font f = new Font("Courier New", Font.BOLD, 20);
		rs.setFont(f);
		rs.drawString(text, messX, messY);
	}
	
	public void LoadReg(Graphics g)			//Отрисовка содержимого
	{
		Graphics2D rs = (Graphics2D) g;
		String str = Convert(reg);
		
		Font f = new Font("Courier New", Font.BOLD, 20);
		rs.setFont(f);
		rs.drawString (str, (int)leftX+10, (int)leftY+45);
	}
	
	private String Convert(IRegister reg)	//Преобразование в строку
	{
		int pish = reg.SendData();
		String str = "";
		boolean flag = true;
		
		while (flag)
		{
			str = pish%2+str;
			pish = pish / 2;
			if (pish == 0 || pish == 1)
			{
				flag = false;
			}
		}
		str = pish%2+str;
			
		int x = str.length();	
		if (str.length() < length)
		{
			for (int i = 0; i < length-x; i++) 
			str = "0" + str;
		}
		
		if (str.length() > length)
		{
			for (int i = 0; i < x-length; i++)
			{
				str = str.substring(1);
			}
		}
			
			
		if (length == 16)
		{
			str = str.substring(0,4) + " " + str.substring(4,8) + " " + str.substring(8,12) + " " + str.substring(12,16);
		}
		if (length == 12)
		{
			str = str.substring(0,4) + " " + str.substring(4,8) + " " + str.substring(8,12);
		}
		return str;
	}
	
	private double		leftX; 	//Координата X левой верхней точки
	private double 		leftY;	//Координата Y левой верхней точки
	private double 		width; 	//Длина 
	private double 		height;	//Высота
	private int      	length;	//Разрадность регистра
	private int 		messX;	//Координата X положения текста
	private int       	messY;	//Координата Y положения текста
	private	String    	text;	//Название регистра
	private IRegister 	reg;	//Регистр	
}


