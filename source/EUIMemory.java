import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

/*-----------------------------------------------------------------------------
					Отрисовка памяти
-----------------------------------------------------------------------------*/

public class EUIMemory implements IUIBaseObject
{
	public EUIMemory (double x, double y, int width, int height, int messX, int messY, String text)
	{
		memory = null;
		mem_width = 12;
		
		leftX = x;
		leftY = y;
		
		frame_width = width;
		frame_height = height; 
		
		separatorX = (int)leftX+65;
		banner_height = 30;
		
		this.messX = messX;
		this.messY = messY;  
		
		page = 0;
		page_size = 16;
		
		this.text = text;
	}
	
	public EUIMemory (EMemory mem, double x, double y, int width, int height, int messX, int messY, String text)
	{
		memory = mem.GetMemory();
		mem_width = mem.Width();
		
		leftX = x;
		leftY = y;
		
		frame_width = width;
		frame_height = height;
		
		separatorX = (int)leftX+65;
		banner_height = 30;
		
		this.messX = messX;
		this.messY = messY; 
		
		page = 0;
		page_size = 16;
		
		this.text = text;
	}
	
	public void Draw(Graphics g)						//Отрисовка названия и рамки
	{
		Graphics2D rs = (Graphics2D) g;
		
		Rectangle2D rect = new Rectangle2D.Double(leftX, leftY, frame_width, frame_height);
		
		rs.setPaint(new Color(157, 189, 185));
		rs.fill(rect);
		
		rs.setPaint(new Color(219, 249, 235));
		rs.fillRect(separatorX, (int)leftY+banner_height, (int)leftX+frame_width-separatorX, frame_height-banner_height);
		
		rs.setStroke(new BasicStroke(2.0f));
		rs.setPaint(Color.BLACK);
		rs.draw(rect);
		
		rs.drawLine(separatorX, (int)leftY+banner_height, separatorX, (int)leftY+frame_height);
		rs.drawLine((int)leftX, (int)leftY+banner_height, (int)leftX+frame_width, (int)leftY+30);
		
		rs.setFont(new Font("Courier New", Font.BOLD, 23));
		rs.drawString(text, messX, messY);
	}
	
	public void LoadMem(Graphics g)						//Отрисовка адресов и содержимого

	{
		Graphics2D rs = (Graphics2D) g;

		rs.setFont(new Font("Courier New", Font.BOLD, 24));
		
		int a = 20; 		//Расстояние между строкой и рамкой заголовка 
		int shift = 25;		//Расстояние между строками 
		
		String adress, data;
		for (int i=0; i<page_size; i++)
		{
			adress = "" + ConvertAdress(page*16+i);
			data = "" + ConvertMemory(memory[page*16+i]);
			rs.drawString(adress, (int)leftX+12, (int)leftY+banner_height+a);
			rs.drawString(data, separatorX+12, (int)leftY+banner_height+a);
			a+=shift;
		}
	}
	
	public void SetPage(int page) 						//Выбор страницы для отрисовки 
	{
		this.page = page;
	}
	
	public int GetPage()								//Получение номера текущей страницы
	{
		return page;
	}
	
	public void SetPageSize(int page_size)				//Установка размера страницы
	{
		this.page_size = page_size;
	}
	
	public void SetBannerHeight(int banner_height)		//Установка высоты заголовка
	{
		this.banner_height = banner_height;
	}

	public void SetSeparator(int x)
	{
		separatorX =(int)leftX + x;
	}
	
	public void SetMemory(int[] x)
	{
		memory = x;
	}
	
	public void SetAdressWidth(int x)
	{
		mem_width = x;
	}
	private String ConvertAdress(int adress)			//Преобразование адреса в строку
	{
		Formatter fmt = new Formatter();
		fmt.format("%x", adress);
		String str = fmt.toString();
		
		int y = str.length();
		if (y < mem_width/4)
		{
			for (int i = 0; i < mem_width/4-y; i++) 
				str = "0" + str;
		}
		
		return str.toUpperCase();
	}
	
	private String ConvertMemory(int content)			//Преобразование памяти в строку
	{
		int width = page_size;	
		Formatter fmt = new Formatter();
		fmt.format("%x", content);
		String str = fmt.toString();
		
		int y = str.length();
		if (y < width/4)
		{
			for (int i = 0; i < width/4-y; i++) 
				str = "0" + str;
		}
		
		return str.toUpperCase();
	}

	
	private int 		mem_width;		//Разрядность адреса
	private int			frame_width;	//Ширина рамки
	private int			frame_height;	//Высота рамки
	private int[]		memory;			//Содержимое ячеек
	private double		leftX;			//Координата X левого верхнего угла рамки
	private double		leftY;			//Координата Y левого верхнего угла рамки
	private int			messX;			//Координата X заголовка
	private int			messY;			//Координата Y заголовка
	private int			page;			//Номер страницы памяти (по 16 ячеек)
	private int			page_size;		//Количество ячеек на странице
	private String		text;			//Названия памяти
	private int			separatorX;		//Координата X разделяющей линии
	private int 		banner_height;	//Высота заголовка		
}
