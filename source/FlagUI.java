import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import Machine.Flag;

/*-----------------------------------------------------------------------------
		Флаг. Отображает флаг в виде круга. Белый цвет - 
						значение равно 0.
-----------------------------------------------------------------------------*/	

public class FlagUI 
{
	FlagUI(Flag flag, int x, int y)
	{
		this.flag = flag;
		
		leftX = x;
		leftY = y;
		
		radius = 35;
	}
	
	public void draw(Graphics g)
	{
		Graphics2D rs = (Graphics2D) g;
		
		if (flag.getValue() == 1)
		{
			rs.setColor(Color.RED);
			rs.fillOval(leftX, leftY, radius, radius);
		}
		else
		{
			rs.setColor(Color.WHITE);
			rs.fillOval(leftX, leftY, radius, radius);
		}
		
		rs.setColor(Color.BLACK);
		rs.drawOval(leftX, leftY, radius, radius);
	}
	
	public void setFlag()
	{
		flag.setFlag();
	}
	
	private Flag		 flag;
	private int			leftX; 
	private	int			leftY;
	private int			radius;
}
