import java.awt.Graphics;
import java.awt.Graphics2D;


import Machine.IRegister;

public class EUIInputRegister extends EUIRegister
{
	public EUIInputRegister(IRegister reg, double x, double y, double height, int messX, int messY, String text)
	{
		super(reg, x, y, height, messX, messY, text);	
			
		pointer_position = 0;
	}
	
	public void DrawPointer(Graphics g)
	{
		Graphics2D rs = (Graphics2D) g;
		
		int[] mass1 = {super.GetDataX()+GetPointerPosition(), super.GetDataX()+GetPointerPosition()+5, super.GetDataX()+GetPointerPosition()+10};
		int[] mass2 = {super.GetDataY()+15, super.GetDataY()+6, super.GetDataY()+15};
		rs.fillPolygon(mass1, mass2, 3);
		rs.drawPolygon(mass1, mass2, 3);
	}
	
	public int GetPointerPosition()
	{
		return pointer_position;
	}
	
	public void SetPointerPosition(int x)
	{
		pointer_position = x;
	}
	
	
	private int length;
	private int pointer_position;

}
