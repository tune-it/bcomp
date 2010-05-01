import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JApplet;

/*-----------------------------------------------------------------------------
				Отрисовка обычного регистра
-----------------------------------------------------------------------------*/

public class EUIRegister extends JApplet implements IUIBaseObject
{
	public EUIRegister(IRegister reg, double x, double y, double height, String text)
	{
		this.reg = reg;
		length = reg.Width();
		leftX = x;
		leftY = y;
		width = 16*reg.Width();
		this.height = height;
		messX = (int)x + 20;
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
		rs.drawString (str, (int)leftX+20, (int)leftY+45);
	}
	
	private String Convert(IRegister reg)	//Преобразование в строку
	{
		int a = reg.SendData();
		String str = ""; 
		boolean flag =true;
		int lng = 0;
		
		while (flag)
		{
			str = " " + str;
			for (int i = 0; i < 4; i++)
			{
				str = a%2 + str;
				a = a / 2;
				if (a < 2 & i == 3)
				{
					flag = false;
					str = " " + str;
					str = a + str;
					break;
				}
				lng++;
			}
		}
		
		for (int i = (15 - lng); i != 0; i--)
		{
			if ((i-1)%4 == 0)
			{
				str = " " + str;
			}
			str = "0" + str;
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


