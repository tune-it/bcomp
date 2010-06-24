import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import Machine.*;

/*-----------------------------------------------------------------------------
		Отрисовка канала и стрелки
-----------------------------------------------------------------------------*/

public class ChannelUI
{

	public ChannelUI (EChannel channel, int[][] points)
	{
		this.channel = channel;
		this.points = points; 
		arrow = true;
	}
	
	public void draw(Graphics g)				//Отрисовка канала 
	{
		Graphics2D rs = (Graphics2D) g;
		
		if (channel.GetConnect())
			rs.setPaint(Color.RED);
		else
			rs.setPaint(Color.GRAY);
		
		//Отрисовка линии канала
		rs.setStroke(new BasicStroke(8.0f));
		for (int j = 0; j < points.length; j += 2)
			rs.drawLine(points[j][0], points[j][1], points[j+1][0], points[j+1][1]);
	
		if (arrow)			
		{	
			//Смещение координат стрелки от линии
			int shiftX = 9;	
			int shiftY = 9;
			
			//Массивы для отрисовки стрелок
			
			//Вверх
			int[] x1 = {points[points.length - 1][0] - shiftX, points[points.length - 1][0], points[points.length - 1][0] + shiftX};
			int[] y1 = {points[points.length - 1][1], points[points.length - 1][1] - shiftY, points[points.length - 1][1]};
			
			//Вправо
			int[] x2 = {points[points.length - 1][0], points[points.length - 1][0] + shiftX, points[points.length - 1][0]};
			int[] y2 = {points[points.length - 1][1] + shiftY - 1, points[points.length - 1][1], points[points.length - 1][1] - shiftY};
			
			//Вниз
			int[] x3 = {points[points.length - 1][0] - shiftX, points[points.length - 1][0], points[points.length - 1][0] + shiftX};
			int[] y3 = {points[points.length - 1][1], points[points.length - 1][1] + shiftY, points[points.length - 1][1]};
			
			//Влево
			int[] x4 = {points[points.length - 1][0], points[points.length - 1][0] - shiftX, points[points.length - 1][0]};
			int[] y4 = {points[points.length - 1][1] + shiftY - 1, points[points.length - 1][1], points[points.length - 1][1] - shiftY};
			
			//Выбор направления стрелки
			
			//Вверх
			if (points[points.length - 2][0] == points[points.length - 1][0] && points[points.length - 2][1]> points[points.length - 1][1] )
				rs.fillPolygon(x1, y1, 3);	
			
			//Вправо
			if (points[points.length - 2][1] == points[points.length - 1][1] && points[points.length - 2][0]< points[points.length - 1][0] )
				rs.fillPolygon(x2, y2, 3);
			 
			//Вниз
			if (points[points.length - 2][0] == points[points.length - 1][0] && points[points.length - 2][1]< points[points.length - 1][1] )
				rs.fillPolygon(x3, y3, 3);
			
			//Влево
			if (points[points.length - 2][1] == points[points.length - 1][1] && points[points.length - 2][0]> points[points.length - 1][0] )
				rs.fillPolygon(x4, y4, 3);
		}
	}
	
	public void enableArrow()					//Добавлять стрелку
	{
		arrow = true;
	}
	
	public void disableArrow()					//Убирать стрелку
	{
		arrow = false;
	}
	
	public boolean isConnect()					//"Открытость" канала
	{
		return channel.GetConnect();
	}
	
	private boolean		arrow;		//Наличие стрелки
	private EChannel	channel;	//Канал
	private int[][]		points;		//Координаты для отрисовки канала
}
