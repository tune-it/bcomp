import java.security.AccessControlContext;
import java.util.Arrays;

/*-----------------------------------------------------------------------------
  АЛУ (Арифметическо-логическое устройство)
-----------------------------------------------------------------------------*/
public class EALU
{
	EALU(ERegister left_input, ERegister right_input, ERegister buffer_register, ERegister accumulator, EFlag c, EFlag n, EFlag z)
	{
		this.left_input = left_input;
		this.right_input = right_input;                 // Exception?
		this.buffer_register = buffer_register;
		this.accumulator = accumulator;
		left_reverse = false;                           // 
		right_reverse = false;
		this.c=c;
		this.n=n;
		this.z=z;
		// Обработка исключений - разрядность входов ~ разрядности БР
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
	
	public void AND() // Логическое умножение
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
	
	public void ADD() // Логическое сложение
	{
		boolean[] buf  = Addition(Reverse(left_input, left_reverse), Reverse(right_input, right_reverse));
		buf = Incremention(buf);
		buffer_register.GetData(buf);
	}
	
	private boolean[] Addition(boolean[] left, boolean[] right)
	{
		boolean[] up    = new boolean[left.length+1]; // - remake
		boolean[] buf   = new boolean[left.length+1];
		
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
		if (up[up.length-1]) buf[buf.length-1]=true;
		return buf;
	}
	
	private boolean[] Incremention(boolean[] bits)
	{
		if (incrementor)
		{
			boolean[] one = new boolean[bits.length];
			one[0]=true;
			return Addition(bits, one);
		}
		else
		{
			return bits;
		}
	}
	
	public void ROL()
	{
		boolean[] bits = Arrays.copyOf(accumulator.SendData(), accumulator.Width()+1);
		bits[bits.length-1] = c.SendData()[0];
		boolean p1 = bits[0];
		boolean p2;
		
		for (int i = 1; i < bits.length; i++)
		{
			p2 = bits[i];
			bits[i] = p1;
			p1 = p2;
		}
		bits[0] = p1;
		
		buffer_register.GetData(bits);
	}
	
	public void ROR()
	{
		boolean[] bits = Arrays.copyOf(accumulator.SendData(), accumulator.Width()+1);
		bits[bits.length-1] = c.SendData()[0];
		boolean p1 = bits[bits.length-1];
		boolean p2;
		
		for (int i = bits.length - 2; i >= 0; i--)
		{
			p2 = bits[i];
			bits[i] = p1;
			p1 = p2;
		}
		bits[bits.length-1] = p1;
		
		buffer_register.GetData(bits);
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
	
	private ERegister left_input;      // Левый вход АЛУ (А, РС, КР)
	private ERegister right_input;     // Правый вход АЛУ (РД, РК, СК)
	private ERegister buffer_register; // Буферный регистр
	private ERegister accumulator;     // Аккумулятор
	private boolean   left_reverse;    // Вкл/Откл левый инвертор
	private boolean   right_reverse;   // Вкл/Откл правый инвертор
	private boolean   incrementor;     // Вкл/Откл инкрементор
	private EFlag     c;               // Флаг C
	private EFlag     n;               // Флаг N 
	private EFlag     z;               // Флаг Z
}