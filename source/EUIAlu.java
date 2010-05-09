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

public class EUIAlu
{
	public EUIAlu(int x, int y, String text)
	{
		leftX = x;
		leftY = y;
		this.text = text;
		messX = x + 85;
		messY = y + 70;
	}
	
	public EUIAlu(int x, int y, int messX, int messY, String text)
	{
		leftX = x;
		leftY = y;
		this.text = text;
		this.messX = messX;
		this.messY = messY;
	}
	
	public void Draw(Graphics g)	//Отрисовка АЛУ без БР
	{
		Graphics2D rs = (Graphics2D) g;
		
		//Массивы координат для отрисовки (рядом с кординатами левой верхней точки указаны смещения от нее)
		int[] x = {leftX, leftX+60, leftX+90, leftX+140, leftX+170, leftX+230, leftX+180, leftX+50, leftX};
		int[] y = {leftY, leftY, leftY+30, leftY+30, leftY, leftY, leftY+90, leftY+90, leftY};
		
		rs.setPaint(new Color(187,249,166));
		rs.fillPolygon(x, y, 9);
		rs.setStroke(new BasicStroke(1.0f));
		rs.setPaint(Color.BLACK);
		rs.drawPolygon(x, y, 9);
		
		rs.setFont(new Font("Courier New", Font.BOLD, 35));
		rs.drawString(text, messX, messY);
	}
	
	private int			leftX;	//Координата X левого верхнего угла фигуры
	private int			leftY;	//Координата Y левого верхнего угла фигуры
	private int 		messX;	//Координата X положения текста
	private int			messY;	//Координата Y положения текста
	private String		text;	//Текст
}
