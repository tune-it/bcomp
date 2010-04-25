import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JApplet;

/*-----------------------------------------------------------------------------
		Отрисовка канала и стрелки
-----------------------------------------------------------------------------*/

public class EUIChanell extends JApplet implements IUIBaseObject
{
	public EUIChanell (EChanell chanell, int[] points)
	{
		this.chanell = chanell;
		this.points = points; 
	}
	
	public void Draw(Graphics g)				//Отрисовка канала
	{
		Graphics2D g2 = (Graphics2D) g;
		
		if (chanell.GetConnect())
		{
			g2.setPaint(Color.RED);
		}
		else
		{
			g2.setPaint(Color.GRAY);
		}
			
		g2.setStroke(new BasicStroke(8.0f));
		for (int i = 0; i < points.length; i+=4)
		{
			g2.drawLine(points[i], points[i+1], points[i+2], points[i+3]);
		}
	}
	
	public void DrawArrow(Graphics g)			//Отрисовка стрелки
	{
		Graphics2D g2 = (Graphics2D) g;
		
		if (chanell.GetConnect())
		{
			g2.setPaint(Color.RED);
		}
		else
		{
			g2.setPaint(Color.GRAY);
		}
		
		for (int i = 0; i < points.length; i+=4)
		{
			g2.drawLine(points[i], points[i+1], points[i+2], points[i+3]);
		}
		
		//Выбор направления стрелки
		g2.setStroke(new BasicStroke(3.0f));
		if (points[points.length - 4] == points[points.length - 2] && points[points.length - 3]> points[points.length - 1] ) //Cтрелка вверх
		{
			g2.drawLine(points[points.length - 2]-7, points[points.length - 1]+4, points[points.length - 2]-1, points[points.length - 1]-7);
			g2.drawLine(points[points.length - 2]+7, points[points.length - 1]+4, points[points.length - 2], points[points.length - 1]-7);
		}	
		
		if (points[points.length - 3] == points[points.length - 1] && points[points.length - 4]< points[points.length - 2] ) //Cтрелка вправо
		{
			g2.drawLine(points[points.length - 2]-4, points[points.length - 1]-7, points[points.length - 2]+6, points[points.length - 1]-1);
			g2.drawLine(points[points.length - 2]-4, points[points.length - 1]+7, points[points.length - 2]+6, points[points.length - 1]);
		}
		 
		if (points[points.length - 4] == points[points.length - 2] && points[points.length - 3]< points[points.length - 1] ) //Cтрелка вниз
		{
			g2.drawLine(points[points.length - 2]-7, points[points.length - 1]-5, points[points.length - 2]-1, points[points.length - 1]+7);
			g2.drawLine(points[points.length - 2]+7, points[points.length - 1]-5, points[points.length - 2], points[points.length - 1]+7);
		}	
		
		if (points[points.length - 3] == points[points.length - 1] && points[points.length - 4]> points[points.length - 2] ) //Cтрелка влево
		{
			g2.drawLine(points[points.length - 2]+4, points[points.length - 1]+7, points[points.length - 2]-7, points[points.length - 1]-1);
			g2.drawLine(points[points.length - 2]+4, points[points.length - 1]-7, points[points.length - 2]-7, points[points.length - 1]);
		}
	}

	private EChanell	chanell;	//Канал
	private int[]		points;		//Координаты для отрисовки канала
}
