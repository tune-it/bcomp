import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Formatter;

/*-----------------------------------------------------------------------------
				Отрисовка обычного регистра
-----------------------------------------------------------------------------*/

public class EUIRegister 
{
	public EUIRegister(IRegister reg, double x, double y, String text)
	{
		this.reg = reg;
		length = reg.Width();
		
		leftX = x;
		leftY = y;
		
		widthBin = 17 * reg.Width() - 6;
		widthHex = 100;
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
		
		leftX = x;
		leftY = y;
		
		widthBin = 17 * reg.Width() - 6;
		widthHex = 100;
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
		
		leftX = x;
		leftY = y;
		
		widthBin = 17 * reg.Width() - 6;
		widthHex = 100;
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
		
		leftX = x;
		leftY = y;
		
		widthBin = 17 * reg.Width() - 6;
		widthHex = 100;
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
	
	public void SetWidth(int width) 						//Установка длины регистра
	{
		widthBin = width;
		widthHex = width;
	}
	
	public void SetDataPosition(int dataX, int dataY)		//Установка положение содержимого регистра
	{
		this.dataX = dataX;
		this.dataY = dataY;
	}
	
	public int GetDataX()
	{
		return dataX;
	}
	
	public int GetdDataY()
	{
		return dataY;
	}

	public void DrawBin(Graphics g)							//Отрисовка регистра с содержимым в двоичном виде
	{
		Graphics2D rs = (Graphics2D) g;
		
		Rectangle2D rect = new Rectangle2D.Double(leftX, leftY, widthBin, height);
		rs.setPaint(new Color(187,249,166));
		rs.fill(rect);
		rs.setPaint(Color.BLACK);
		rs.setStroke(new BasicStroke(1.0f));
		rs.draw(rect);
		
		rs.setFont(new Font("Courier New", Font.BOLD, 20));
		rs.drawString(text, messX, messY);
	
		String str = ConvertToBin(reg);
		rs.setPaint(new Color(231,236,119)); 
		rs.fillRect((int)leftX+5, (int)leftY+24, (int)widthBin-10, 23);
		rs.setPaint(Color.BLACK);
		rs.setFont(new Font("Courier New", Font.BOLD, 21));
		rs.drawString (str, dataX, dataY);
	}
	
	public void DrawHex(Graphics g)							//Отрисовка регистра с содержимым в шестнадцатеричном виде
	{
		Graphics2D rs = (Graphics2D) g;
		
		Rectangle2D rect = new Rectangle2D.Double(leftX, leftY, widthHex, height);
		rs.setPaint(new Color(187,249,166));
		rs.fill(rect);
		rs.setPaint(Color.BLACK);
		rs.setStroke(new BasicStroke(1.0f));
		rs.draw(rect);
		
		rs.setFont(new Font("Courier New", Font.BOLD, 20));
		rs.drawString(text, messX, messY);
	
		String str = ConvertToHex(reg);
		rs.setPaint(new Color(231,236,119));
		rs.fillRect((int)leftX+5, (int)leftY+24, (int)widthHex-10, 23);
		
		rs.setPaint(Color.BLACK);
		rs.setFont(new Font("Courier New", Font.BOLD, 21));
		rs.drawString (str, dataX, dataY);
	}
	
	private String ConvertToBin(IRegister reg)				//Преобразование в двоичный вид
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
		if (str.length() < length)					//
		{											//
			for (int i = 0; i < length-x; i++) 		//Добавление значащих нулей в начало строки
			str = "0" + str;						//
		}											//
		
		if (str.length() > length)					//
		{											//
			for (int i = 0; i < x-length; i++)		//Удаление лишних нулей в начале строки
				str = str.substring(1);				//											
		}											//
			
		if (length == 16)
		{
			str = str.substring(0,4) + " " + str.substring(4,8) + " " + str.substring(8,12) + " " + str.substring(12,16);
		}
		if (length == 11)
		{
			str = str.substring(0,3) + " " + str.substring(3,7) + " " + str.substring(7,11);
		}
		
		return str;
	}
	
	private String ConvertToHex(IRegister reg)				//Преобразование в шестнадцатеричный вид
	{
		Formatter fmt = new Formatter();
		fmt.format("%x", reg.SendData());
		String str = fmt.toString();
		
		int y = str.length();
		if (y < length / 4)										//
		{														//
			for (int i = 0; i < (double)length / 4 - y; i++) 	//Добавление значащих нулей в начало строки
				str = "0" + str;								//
		}														//
		return str.toUpperCase();
	}
	
	private IRegister 	reg;		//Регистр
	private int      	length;		//Разрядность регистра
	private double		leftX; 		//Координата X левой верхней точки
	private double 		leftY;		//Координата Y левой верхней точки
	private double 		widthBin; 	//Длина регистра с двоичным содержимым
	private double 		widthHex; 	//Длина регистра с шестнадцатеричным содержимым
	private double 		height;		//Высота
	private int			dataX;		//Координата X положения содержимого регистра
	private int			dataY;		//Координата Y положения содержимого регистра		
	private int 		messX;		//Координата X положения текста	
	private int       	messY;		//Координата Y положения текста
	private	String    	text;		//Название регистра
	
}


