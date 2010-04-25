import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import javax.swing.JApplet;

/*-----------------------------------------------------------------------------
				Отрисовка АЛУ 
-----------------------------------------------------------------------------*/

public class EUIAlu extends JApplet implements IUIBaseObject
{
	public EUIAlu(int x, int y, String text)
	{
		leftX = x;
		leftY = y;
		this.text = text;
		messX = x + 100;
		messY = y + 80;
	}
	
	public void Draw(Graphics g)	//Отрисовка АЛУ без БР
	{
		Graphics2D g2 = (Graphics2D) g;
		
		int[] x = {leftX, leftX+80, leftX+105, leftX+160, leftX+185, leftX+265, leftX+230, leftX+35, leftX};
		int[] y = {leftY, leftY, leftY+40, leftY+40, leftY, leftY, leftY+100, leftY+100, leftY};
		
		g2.setPaint(Color.GREEN);
		g2.fillPolygon(x, y, 9);
		g2.setPaint(Color.BLACK);
		g2.drawPolygon(x, y, 9);
		
		Font f = new Font("Courier New", Font.BOLD, 35);
		g2.setFont(f);
		g2.drawString(text, messX, messY);
	}
	
	private int			leftX;	//Координата X левого верхнего угла фигуры
	private int			leftY;	//Координата Y левого верхнего угла фигуры
	private int 		messX;	//Координата X положения текста
	private int			messY;	//Координата Y положения текста
	private String		text;	//Текст
}
