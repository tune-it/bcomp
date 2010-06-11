import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import Machine.*;

/*-----------------------------------------------------------------------------
					Отрисовка памяти
-----------------------------------------------------------------------------*/

public class EUIMemory
{	
	public EUIMemory (IMemory mem, IRegister reg, int mem_width, double x, double y, int width, int height, int messX, int messY, String text)
	{
		memory = mem.GetMemory();
		this.mem_width = mem_width;
		
		register = reg;
		
		leftX = x;
		leftY = y;
		
		frame_width = width;
		frame_height = height;
		
		separatorX = (int)leftX + 65;
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
		
		//Отрисовка фона памяти
		rs.setPaint(new Color(157, 189, 185));
		rs.fill(rect);
		
		//Отрисовка колонки адресов
		rs.setPaint(new Color(219, 249, 235));
		rs.fillRect(separatorX, (int)leftY+banner_height, (int)leftX+frame_width-separatorX, frame_height-banner_height);
		
		//Отрисовка рамки
		rs.setStroke(new BasicStroke(2.0f));
		rs.setPaint(Color.BLACK);
		rs.draw(rect);
		
		rs.drawLine(separatorX, (int)leftY + banner_height, separatorX, (int)leftY + frame_height);						//Разделитель
		rs.drawLine((int)leftX, (int)leftY + banner_height, (int)leftX + frame_width, (int)leftY + banner_height);		//Шапка
		
		//Заголовок
		rs.setFont(new Font("Courier New", Font.BOLD, 23));
		rs.drawString(text, messX, messY);

		rs.setFont(new Font("Courier New", Font.BOLD, 25));		//Шрифт содержимого
		
		int a = 20; 								//Расстояние между строкой и рамкой заголовка 
		int shift = 25;								//Расстояние между строками 
		page = register.SendData() / page_size;		//Текущая страница
		String adress, data;			
		
		//Форматирование и вывод адресов и содержимого
		for (int i = 0; i < page_size; i++)
		{
			adress = "" + ConvertAdress(page * 16 + i);
			data = "" + ConvertMemory(memory[page * 16 + i]);
			rs.drawString(adress, (int)leftX + 9, (int)leftY+ banner_height + a);
			rs.drawString(data, separatorX + 12, (int)leftY+ banner_height + a);
			a += shift;
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
	
	public void SetBannerHeight(int banner_height)		//Установка высоты заголовка
	{
		this.banner_height = banner_height;
	}

	public void SetSeparator(int x)						//Установка координаты Х линии-разделителя
	{
		separatorX =(int)leftX + x;
	}
	
	public void SetMemory(int[] x)						//Загрузка содержимого
	{
		memory = x;
	}
	
	public void SetAdressWidth(int x)					//Установка разрядности адреса
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
			for (int i = 0; i <= mem_width/4-y-1; i++) 
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
			for (int i = 0; i <= width/4-y-1; i++) 
				str = "0" + str;
		}
		
		return str.toUpperCase();
	}
	
	private IRegister		register;		//Регистр		
	private int 			mem_width;		//Разрядность адреса
	private int				frame_width;	//Ширина рамки
	private int				frame_height;	//Высота рамки
	private int[]			memory;			//Содержимое ячеек
	private double			leftX;			//Координата X левого верхнего угла рамки
	private double			leftY;			//Координата Y левого верхнего угла рамки
	private int				messX;			//Координата X заголовка
	private int				messY;			//Координата Y заголовка
	private int				page;			//Номер страницы памяти (по 16 ячеек)
	private final int		page_size;		//Количество ячеек на странице
	private String			text;			//Названия памяти
	private int				separatorX;		//Координата X разделяющей линии
	private int 			banner_height;	//Высота заголовка		

}
