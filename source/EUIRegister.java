import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import javax.swing.JApplet;

/*-----------------------------------------------------------------------------
				Отрисовка обычного регистра
-----------------------------------------------------------------------------*/

public class EUIRegister extends JApplet implements IUIBaseObject
{
	public EUIRegister(ERegister reg, double x, double y, double height, String text)
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
	
	public double GetX()				//Получение координаты X левого верхнего угла
	{
		return leftX;
	}
	
	public double GetY()				//Получение координаты Y левого верхнего угла
	{
		return leftY;
	}
	
	public double GetHeight()			//Получение высоты регистра
	{
		return height;
	}
	
	public void Draw(Graphics g)		//Отрисовка прямоугольника и текста
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
	
	public void LoadReg(Graphics g)		//Отрисовка содержимого
	{
		Graphics2D rs = (Graphics2D) g;
		
		boolean[] array = Arrays.copyOf(reg.SendData(), reg.Width());
		int[] a = new int[length];
		for (int i = 0; i < length; i++)
		{
			if(array[i])
			{
				a[i]=1;
			}
			else
			{
				a[i]=0;
			}
		}		
		
		String str = "";
		for (int i=length; i>0; i--)
		{
			str = "" + str + a[i];
		}
		
		if (length == 16)
		rs.drawString((str.substring(0,4) + " " + str.substring(4,8) + " " + str.substring(8,12) + " " + str.substring(12,16)), (int)leftX+10, (int)leftY+45);
		else
		rs.drawString((str.substring(0,4) + " " + str.substring(4,8) + " " + str.substring(8,12)), (int)leftX+10, (int)leftY+45);
	}
	
	private double		leftX; 	//Координата X левой верхней точки
	private double 		leftY;	//Координата Y левой верхней точки
	private double 		width; 	//Длина 
	private double 		height;	//Высота
	private int      	length;	//Разрадность регистра
	private int 		messX;	//Координата X положения текста
	private int       	messY;	//Координата Y положения текста
	private String    	text;	//Название регистра
	private ERegister 	reg;	//Регистр	
}


