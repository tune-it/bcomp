
public class EUIAcc 
{
	public EUIAcc(double x, double y, String text)
	{
		leftX = x;
		leftY = y;
		width = 270;
		height = 50;
		messX = (int)x + 80;
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
		g2.drawString("C", leftX+5, messY);
	}
	
	public void LoadReg()
	{
		String str = "";
		int[] a = new int[16]; 
		//...
		for (int i=0; i<16; i++)
		{
			str = "" + str + a[i];
		}
		g2.drawString((str.substring(0,4) + " " + str.substring(4,8) + " " + str.substring(8,12) + " " + str.substring(12,16)), (int)leftX+30, (int)leftY+45);
	}
	
	public void LoadFlag()
	{
		//??
	}
	
	private double leftX;
	private double leftY;
	private double width;
	private double height;
	private int messX;
	private int messY;
	private String text;
}
