import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;


public class EUIRegister implements IUIBaseObject
{
	public EUIRegister(ERegister.Width(), double x, double y, String text)
	{
		lenght = ERegister.Width();
		leftX = x;
		leftY = y;
		width = 16*ERegister.Width;
		height = 50;
		messX = (int)x + 20;
		messY = (int)y + 20;
		this.text = text;
	}
	
	public void Draw(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		Rectangle2D rect = new Rectangle2D.Double(leftX, leftY, width, height);
		g2.setPaint(Color.GREEN);
		g2.fill(rect);
		g2.setPaint(Color.BLACK);
		g2.draw(rect);
		
		Font f = new Font("Courier New", Font.BOLD, 20);
		g2.setFont(f);
		g2.drawString(text, messX, messY);
	}
	
	public void LoadReg()
	{
		String str = "";
		int[] a = new int[lenght]; 
		//...
		for (int i=0; i<lenght; i++)
		{
			str = "" + str + a[i];
		}
		if (lenght = 16)
		g2.drawString((str.substring(0,4) + " " + str.substring(4,8) + " " + str.substring(8,12) + " " + str.substring(12,16)), (int)leftX+10, (int)leftY+45);
		else
		g2.drawString((str.substring(0,4) + " " + str.substring(4,8) + " " + str.substring(8,12)), (int)leftX+10, (int)leftY+45);
	}
	
	private double leftX;
	private double leftY;
	private double width;
	private double height;
	private int messX;
	private int messY;
	private int lenght;
	private String text;
}


