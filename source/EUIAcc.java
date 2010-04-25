import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

/*-----------------------------------------------------------------------------
			Отрисовка регистра с флагом 
-----------------------------------------------------------------------------*/

public class EUIAcc extends EUIRegister implements IUIBaseObject
{
	public EUIAcc(ERegister reg, double x, double y, double height, String text)
	{
		super(reg, x, y, height, text);
		flag = null;
	}
	
	public EUIAcc(ERegister reg, double x, double y, String text, double height, EFlag flag)
	{
		super(reg, x, y, height, text);
		this.flag = flag;
	}
	
	public void LoadFlag(Graphics g)	//Отрисовка флага 
	{
		Graphics2D rs = (Graphics2D) g;
				
		boolean[] array = Arrays.copyOf(flag.SendData(), flag.Width());
		int[] a = new int[1];
		for (int i = 0; i < 1; i++)
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
				
		Rectangle2D rect = new Rectangle2D.Double(super.GetX()-30, super.GetY(), 20, super.GetHeight());
		rs.setPaint(Color.GREEN);
		rs.fill(rect);
		rs.setPaint(Color.BLACK);
		rs.draw(rect);
			
		Font f = new Font("Courier New", Font.BOLD, 20);
		rs.setFont(f);
		rs.drawString("C", (int)super.GetX()-20, (int)super.GetY()+20);
				
		String str = "" + a[0];
				 
		rs.drawString(str, (int)super.GetX()-20, (int)super.GetY()+45);			
	}
	
	private EFlag 		flag;	//Флаг
}
