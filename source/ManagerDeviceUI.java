import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import Machine.EFlag;
import Machine.EFlagFactory;

/*-----------------------------------------------------------------------------
		Отрисовка устройства управления
-----------------------------------------------------------------------------*/

public class ManagerDeviceUI 
{
	public ManagerDeviceUI(EFlagFactory flag_factory, double x, double y)
	{
		this.flag_factory = flag_factory;
		
		leftX = x;
		leftY = y;
		
		width = 226;
		height = 218;
		
		banner_height = 50;
		
		messX = (int)leftX + 9;
		messY = (int)leftY + 18;
	}
	
	public void draw(Graphics g) 			//Отрисовка рамки и вызов методов отрисовки состояния режимов 
	{
		Graphics2D rs = (Graphics2D) g;
		
		//Отрисовка рамки
		Rectangle2D rect = new Rectangle2D.Double(leftX, leftY, width, height);	
		rs.setPaint(new Color(187,249,166));
		rs.fill(rect);
		rs.setPaint(Color.BLACK);
		rs.setStroke(new BasicStroke(1.0f));
		rs.draw(rect);
	
		//Отрисовка разделителя для заголовка
		rs.drawLine((int)leftX, (int)leftY + banner_height, (int)leftX + width, (int)leftY + banner_height);
	
		//Вывод заголовка
		rs.setFont(new Font("Courier New", Font.BOLD, 23));
		rs.drawString("Устройство", messX + 35, messY + 2);
		rs.drawString("управления", messX + 34, messY + 25);
		
		//Вызов методов отрисовки строк
		rs.setFont(new Font("Courier New", Font.BOLD, 17));
		drawMode(rs, flag_factory.GetInstructionFetch(), 1, "Выборка команды");
		drawMode(rs, flag_factory.GetAdressSelection(), 2, "Выборка aдреса");
		drawMode(rs, flag_factory.GetExecution(), 3, "Исполнение");
		drawMode(rs, flag_factory.GetInterruption(), 4, "Прерывание");
		drawMode(rs, flag_factory.GetInputOutput(), 5, "Ввод/вывод");
		drawMode(rs, flag_factory.GetStateOfExternalDevice(), 6, "Состояние ВУ");
		drawMode(rs, flag_factory.GetInterruptEnable(), 7, "Разрешение прерывания");
		drawMode(rs, flag_factory.GetProgram(), 8, "Программа");
		
		rs.setPaint(Color.BLACK);
	}
	
	private void drawMode(Graphics g, EFlag flag, int line_number, String mode_name) 	//Определяет состояние режима и выводит его названия 
	{
		Graphics2D rs = (Graphics2D) g;
		int shift = line_number*20;			//Расстояние от линии разделителя
		
		if(flag.SendData() == 1)
			rs.setPaint(Color.RED);
		else
			rs.setPaint(Color.BLACK);
		
		rs.drawString(mode_name, messX, (int)leftY+banner_height+shift);
	}
	
	private EFlagFactory	flag_factory;
	private double 			leftX;
	private double			leftY;
	private int				width;
	private int				height;
	private int				banner_height;		
	private int				messX;
	private int				messY;
}
