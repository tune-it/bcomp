import java.awt.BasicStroke;
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

public class EUIAlu implements IUIBaseObject
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
		Graphics2D rs = (Graphics2D) g;
		
		int[] x = {leftX, leftX+80, leftX+105, leftX+160, leftX+185, leftX+265, leftX+230, leftX+35, leftX};
		int[] y = {leftY, leftY, leftY+40, leftY+40, leftY, leftY, leftY+100, leftY+100, leftY};
		
		rs.setPaint(new Color(187,249,166));
		rs.fillPolygon(x, y, 9);
		rs.setStroke(new BasicStroke(1.0f));
		rs.setPaint(Color.BLACK);
		rs.drawPolygon(x, y, 9);
		
		Font f = new Font("Courier New", Font.BOLD, 35);
		rs.setFont(f);
		rs.drawString(text, messX, messY);
	}
	
	private int			leftX;	//Координата X левого верхнего угла фигуры
	private int			leftY;	//Координата Y левого верхнего угла фигуры
	private int 		messX;	//Координата X положения текста
	private int			messY;	//Координата Y положения текста
	private String		text;	//Текст
}
