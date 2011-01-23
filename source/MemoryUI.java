/**
 * Отрисовка памяти. Соединяется с машинным регистром
 * для динамического пролистывания страниц
 * @version $Id$
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import Machine.*;

public class MemoryUI
{	
	public MemoryUI (IMemory mem, IRegister reg, IChannel[] channels, int mem_width, double x, double y, int width, int height, int messX, int messY, String text)
	{
		memory = mem;
		this.mem_width = mem_width;
		
		register = reg;
		this.channels = channels;

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
	
	public void draw(Graphics g)						//Отрисовка
	{
		Graphics2D rs = (Graphics2D) g;
		
		Rectangle2D rect = new Rectangle2D.Double(leftX, leftY, frame_width, frame_height);
		
		//Отрисовка фона памяти
		rs.setPaint(new Color(157, 189, 165));
		rs.fill(rect);
		
		//Отрисовка фона колонки содержимого ячеек
		rs.setPaint(new Color(219, 249, 235));
		rs.fillRect(separatorX, (int)leftY + banner_height, (int)leftX + frame_width - separatorX, frame_height - banner_height);
		
		//Отрисовка рамки
		rs.setStroke(new BasicStroke(2.0f));
		rs.setPaint(Color.BLACK);
		rs.draw(rect);
		
		rs.drawLine(separatorX, (int)leftY + banner_height, separatorX, (int)leftY + frame_height);						//Разделитель
		rs.drawLine((int)leftX, (int)leftY + banner_height, (int)leftX + frame_width, (int)leftY + banner_height);		//Шапка
		
		//Заголовок
		rs.setFont(new Font("Courier New", Font.BOLD, 23));
		rs.drawString(text, messX, messY);

		rs.setFont(new Font("Courier New", Font.BOLD, 25));     //Шрифт содержимого
		
		String address, data;
		int a = 21;         //Расстояние между строкой и рамкой заголовка
		int shift = 25;     //Расстояние между строками

                //Определение номера текущей страницы
                for (int i = 0; i < channels.length; i++)
                    if (channels[i].getConnect())
                        setPage(register.getValue() / page_size);
		
                int mem[] = memory.getMemory();                          //Содержимое ячеек
		//Форматирование и вывод адресов и содержимого
		for (int i = 0; i < page_size; i++)
		{	
			address = "" + convertToString(page * 16 + i, mem_width);
			data = "" + convertToString(mem[page * 16 + i], page_size);
			rs.drawString(address, (int)leftX + 9, (int)leftY+ banner_height + a);
			rs.drawString(data, separatorX + 12, (int)leftY+ banner_height + a);
			a += shift;
		}
	}
	
	public void setPage(int page) 				//Выбор страницы для отрисовки 
	{
		this.page = page;
	}
	
	public int getPage()					//Получение номера текущей страницы
	{
		return page;
	}
	
	public void setBannerHeight(int banner_height)		//Установка высоты заголовка
	{
		this.banner_height = banner_height;
	}

	public void setSeparator(int x)				//Установка координаты Х линии-разделителя
	{
		separatorX =(int)leftX + x;
	}
	
	public void setAddressWidth(int x)			//Установка разрядности адреса
	{
		mem_width = x;
	}
	
	private String convertToString(int address, int width)	//Преобразование адреса/cодержимого в строку
	{
		Formatter fmt = new Formatter();
		fmt.format("%x", address);
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
        private IChannel[]              channels;               //Канал
	private IMemory			memory;			//Память ячеек
	private int 			mem_width;		//Разрядность адреса
	private int			frame_width;            //Ширина рамки
	private int			frame_height;           //Высота рамки
	private double			leftX;			//Координата X левого верхнего угла рамки
	private double			leftY;			//Координата Y левого верхнего угла рамки
	private int			messX;			//Координата X заголовка
	private int			messY;			//Координата Y заголовка
	private int			page;			//Номер страницы памяти (по 16 ячеек)
	private final int		page_size;		//Количество ячеек на странице
	private String			text;			//Название памяти
	private int			separatorX;		//Координата X разделяющей линии
	private int 			banner_height;          //Высота заголовка

}
