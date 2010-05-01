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
public EUIChanell (EChanell chanell, int[][] points)
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
		int i = 0;
		for (int j = 0; j < points.length; j+=4)
		{
			g2.drawLine(points[i][j], points[i+1][j], points[i][j+1], points[i+1][j+1]);
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
		
		
		g2.setStroke(new BasicStroke(8.0f));
		for (int j = 0; j<points.length; j+=2)
		{
			g2.drawLine(points[j][0], points[j][1], points[j+1][0], points[j+1][1]);
		}
		
		//Выбор направления стрелки
		g2.setStroke(new BasicStroke(3.0f));
		
		if (points[points.length - 1][0] == points[points.length - 1][0] && points[points.length - 2][1]> points[points.length - 1][0] ) //Cтрелка вверх
		{	
			g2.drawLine(points[points.length - 1][0]-7, points[points.length - 1][1]+4, points[points.length - 1][0]-1, points[points.length - 1][1]-7);
			g2.drawLine(points[points.length - 1][0]+7, points[points.length - 1][1]+4, points[points.length - 1][0], points[points.length - 1][1]-7);
		}	
		
		if (points[points.length - 2][1] == points[points.length - 1][1] && points[points.length - 2][0]< points[points.length - 1][0] ) //Cтрелка вправо
		{
			g2.drawLine(points[points.length - 1][0]-4, points[points.length - 1][1]-7, points[points.length - 1][0]+6, points[points.length - 1][1]-1);
			g2.drawLine(points[points.length - 1][0]-4, points[points.length - 1][1]+7, points[points.length - 1][0]+6, points[points.length - 1][1]);
		}
		 
		if (points[points.length - 2][0] == points[points.length - 1][0] && points[points.length - 2][1]< points[points.length - 1][1] ) //Cтрелка вниз
		{
			g2.drawLine(points[points.length - 1][0]-7, points[points.length - 1][1]-5, points[points.length - 1][0]-1, points[points.length - 1][1]+7);
			g2.drawLine(points[points.length - 1][0]+7, points[points.length - 1][1]-5, points[points.length - 1][0], points[points.length - 1][1]+7);
		}	
		
		if (points[points.length - 2][1] == points[points.length - 1][1] && points[points.length - 2][0]> points[points.length - 1][0] ) //Cтрелка влево
		{
			g2.drawLine(points[points.length - 1][0]+4, points[points.length - 1][1]+7, points[points.length - 1][0]-7, points[points.length - 1][1]-1);
			g2.drawLine(points[points.length - 1][0]+4, points[points.length - 1][1]-7, points[points.length - 1][0]-7, points[points.length - 1][1]);
		}
	}

	private EChanell	chanell;	//Канал
	private int[][]		points;		//Координаты для отрисовки канала
}
