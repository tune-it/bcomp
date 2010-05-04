import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/*-----------------------------------------------------------------------------
		Отрисовка канала и стрелки
-----------------------------------------------------------------------------*/

public class EUIChannel implements IUIBaseObject
{

public EUIChannel (EChannel channel, int[][] points)
	{
		this.channel = channel;
		this.points = points; 
		arrow = false;
	}
	
	public void Draw(Graphics g)				//Отрисовка канала 
	{
		Graphics2D rs = (Graphics2D) g;
		
		if (channel.GetConnect())
		{
			rs.setPaint(Color.RED);
		}
		else
		{
			rs.setPaint(Color.GRAY);
		}
			
		rs.setStroke(new BasicStroke(8.0f));
		for (int j = 0; j<points.length; j+=2)
		{
			rs.drawLine(points[j][0], points[j][1], points[j+1][0], points[j+1][1]);
		}
	}
	public void DrawArrow(Graphics g)			//Отрисовка содержимого
		{
			Graphics2D  rs = (Graphics2D) g;
			
			if (channel.GetConnect())
			{
				rs.setPaint(Color.RED);
			}
			else
			{
				rs.setPaint(Color.GRAY);
			}
			
			//Выбор направления стрелки
			rs.setStroke(new BasicStroke(3.0f));
			
			if (points[points.length - 2][0] == points[points.length - 1][0] && points[points.length - 2][1]> points[points.length - 1][1] ) //Cтрелка вверх
			{	
				rs.drawLine(points[points.length - 1][0]-7, points[points.length - 1][1]+4, points[points.length - 1][0]-1, points[points.length - 1][1]-7);
				rs.drawLine(points[points.length - 1][0]+7, points[points.length - 1][1]+4, points[points.length - 1][0], points[points.length - 1][1]-7);
			}	
			
			if (points[points.length - 2][1] == points[points.length - 1][1] && points[points.length - 2][0]< points[points.length - 1][0] ) //Cтрелка вправо
			{
				rs.drawLine(points[points.length - 1][0]-4, points[points.length - 1][1]-7, points[points.length - 1][0]+6, points[points.length - 1][1]-1);
				rs.drawLine(points[points.length - 1][0]-4, points[points.length - 1][1]+7, points[points.length - 1][0]+6, points[points.length - 1][1]);
			}
			 
			if (points[points.length - 2][0] == points[points.length - 1][0] && points[points.length - 2][1]< points[points.length - 1][1] ) //Cтрелка вниз
			{
				rs.drawLine(points[points.length - 1][0]-7, points[points.length - 1][1]-5, points[points.length - 1][0]-1, points[points.length - 1][1]+7);
				rs.drawLine(points[points.length - 1][0]+7, points[points.length - 1][1]-5, points[points.length - 1][0], points[points.length - 1][1]+7);
			}	
			
			if (points[points.length - 2][1] == points[points.length - 1][1] && points[points.length - 2][0]> points[points.length - 1][0] ) //Cтрелка влево
			{
				rs.drawLine(points[points.length - 1][0]+4, points[points.length - 1][1]+7, points[points.length - 1][0]-7, points[points.length - 1][1]-1);
				rs.drawLine(points[points.length - 1][0]+4, points[points.length - 1][1]-7, points[points.length - 1][0]-7, points[points.length - 1][1]);
			}
		}
	
	
	public void EnableArrow()
	{
		arrow = true;
	}
	
	public void DisableArrow()
	{
		arrow = false;
	}
	
	private boolean		arrow;		//Наличие стрелки
	private EChannel	channel;	//Канал
	private int[][]		points;		//Координаты для отрисовки канала
}
