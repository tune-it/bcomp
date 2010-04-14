import java.util.Arrays;

/*-----------------------------------------------------------------------------
  АЛУ (Арифметическо-логическое устройство)
-----------------------------------------------------------------------------*/
public class EALU
{
	EALU(IRegister left_input, IRegister right_input, IRegister buffer_register, EFlag c, EFlag n, EFlag z)
	{
		this.left_input = left_input;
		this.right_input = right_input;                 // Exception?
		this.buffer_register = buffer_register;
		left_reverse = false;                            // 
		right_reverse = false;
		this.c=c;
		this.n=n;
		this.z=z;
		// Обработка исключений - разрядность входов ~ разрядности БР, разрядность С, N и Z = 1
	}
	
	public void SetRightReverse() // Включить правый инвертор
	{
		right_reverse = true;
	}
	
	public void SetLeftReverse() // Включить левый инвертор
	{
		left_reverse = true;
	}
	
	public void SetIncrement() // Включить инкрементор
	{
		incrementor = true;
	}
	
	private boolean[] Reverse(IRegister reg, boolean flag)  // Инвертор
	{
		boolean[] data = Arrays.copyOf(reg.SendData(), reg.Width());
		
		if (flag)
		{
			for (int i=0; i < data.length; i++)
			{
				if (data[i])
				{
					data[i]=false;
				}
				else
				{
					data[i]=true;
				}
			}
		}
		return data;
	}
	
	public void And() // Логическое умножение
	{
		boolean[] left  = Reverse(left_input,  left_reverse);
		boolean[] right = Reverse(right_input, right_reverse);
		boolean[] buf   = new boolean[buffer_register.Width()];
		
		for (int i=0; i < left.length; i++)
		{
			buf[i] = left[i] && right[i];
		}
		buffer_register.GetData(buf);
	}
	
	public void Addition() // Логическое сложение
	{
		boolean[] left  = Reverse(left_input,  left_reverse);
		boolean[] right = Reverse(right_input, right_reverse);
		boolean[] up    = new boolean[buffer_register.Width()]; // - remake
		boolean[] buf   = new boolean[buffer_register.Width()];
		
		for (int i=0; i < left.length; i++)
		{
			if (left[i] && right[i]) 
			{
				if (up[i])
				{
					buf[i]=true;
				}
				else
				{
					buf[i]=false;
				}
				up[i+1]=true;
			}
			else
			{
				if (left[i] || right[i])
				{
					if (up[i])
					{
						buf[i]=false;
						up[i+1]=true;
					}
					else
					{
						buf[i]=true;
					}
				}
				else
				{
					if (up[i])
					{
						buf[i]=true;
					}
					else
					{
						buf[i]=false;
					}
				}
			}
		}
		if (up[buffer_register.Width()-1]) buf[buffer_register.Width()-1]=true;
		buffer_register.GetData(buf);
	}
	
	public void SetCIfExist() // Перенос 17-го бита БР в С (установка С, если присутствует перенос) 
	{
		if(buffer_register.SendData()[16])
		{
			c.SetFlag();
		}
	}
	
	public void ClearC() // Очистить С 
	{
		c.ClearFlag();
	}
	
	public void SetC() // Установить С
	{
		c.SetFlag();
	}
	
	public void SetZ() // Установить Z, если присутствует
	{
		boolean sum = false;
		
		for (int i=0; i < buffer_register.Width(); i++)
		{
			sum = sum || buffer_register.SendData()[i];
		}
		
		if(!sum)
		{
			z.SetFlag();
		}
	}
	
	public void SetN() // Установить N, если присутствует
	{
		if(buffer_register.SendData()[15])
		{
			n.SetFlag();
		}
	}
	
//	private int MakeAdress(boolean[] bits)
//	{
//		boolean sum = false;
//		
//		for (int i=0; i < bits.length; i++)
//		{
//			sum = sum || bits[i];
//		}
//		return adress;
//	}
	
	private IRegister left_input;      // Левый вход АЛУ (А, РС, КР)
	private IRegister right_input;     // Правый вход АЛУ (РД, РК, СК)
	private IRegister buffer_register; // Буферный регистр
	private boolean   left_reverse;    // Вкл/Откл левый инвертор
	private boolean   right_reverse;   // Вкл/Откл правый инвертор
	private boolean   incrementor;     // Вкл/Откл инкрементор
	private EFlag c;
	private EFlag n;
	private EFlag z;
}
