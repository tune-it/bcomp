import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JApplet;

/*-----------------------------------------------------------------------------
					Отрисовка памяти
-----------------------------------------------------------------------------*/

public class EUIMemory implements IUIBaseObject
{
	public EUIMemory (EMemory mem, double x, double y, int width, int height, String text)
	{
		memory = mem.GetMemory();
		mem_length = memory.length;
		mem_width = mem.Width();
		leftX = x;
		leftY = y;
		frame_width = width;
		frame_height = height; 
		this.text = text;
		page = 0;
	}
	
	public void Draw(Graphics g)			//Отрисовка названия и рамки
	{
		Graphics2D rs = (Graphics2D) g;
		
		Rectangle2D rect = new Rectangle2D.Double(leftX, leftY, frame_width, frame_height);
		rs.setPaint(Color.GRAY);
		rs.fill(rect);
		rs.setStroke(new BasicStroke(3.0f));
		rs.setPaint(Color.BLACK);
		rs.draw(rect);
		
		rs.drawLine((int)leftX+(frame_width/5*2), (int)leftY+30, (int)leftX+(frame_width/5*2), (int)leftY+frame_height);
		rs.drawLine((int)leftX, (int)leftY+30, (int)leftX+frame_width, (int)leftY+30);
		
		Font f = new Font("Courier New", Font.BOLD, 24);
		rs.setFont(f);
		rs.drawString(text, (int)leftX+(frame_width/4), (int)leftY+20);
	}
	
	public void LoadMem(Graphics g)			//Отрисовка адресов и содержимого

	{
		Graphics2D rs = (Graphics2D) g;
		
		Font f = new Font("Courier New", Font.BOLD, 20);
		rs.setFont(f);
		
		int a = 10;
		String adress, data;
		for (int i=0; i<16; i++)
		{
			adress = "" + page*16+i;
			data = "" + memory[page*16+i];
			rs.drawString(adress, (int)leftX+5, (int)leftY+40+a);
			rs.drawString(data, (int)leftX+(frame_width/5*2)+10, (int)leftY+40+a);
			a+=25;
		}
	}
	
	public void SetPage(int x) 				//Выбор страницы для отрисовки 
	{
		page = x;
	}
	
	public int GetPage()					//Получение номера текущей страницы
	{
		return page;
	}
	
	private int 		mem_length;		//Количество ячеек памяти
	private int 		mem_width;		//Разрядность адреса
	private int			frame_width;	//Ширина рамки
	private int			frame_height;	//Высота рамки
	private int[]		memory;			//Содержимое ячеек
	private double		leftX;			//Координата X левого верхнего угла рамки
	private double		leftY;			//Координата Y левого верхнего угла рамки
	private int			page;			//Номер страницы памяти (по 16 ячеек)
	private String		text;			//Названия памяти
}
