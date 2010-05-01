/*-----------------------------------------------------------------------------
  Устройство управления
-----------------------------------------------------------------------------*/
public class EManagerDevice
{
	public EManagerDevice(EMemory memory, ERegister instr_pointer, ERegister command_register, EChannel[] chanells)
	{
		this.memory = memory;
		this.instr_pointer = instr_pointer;
		this.command_register = command_register;
		this.chanells = chanells;
	}
	
	private boolean CheckBit(int bits, int number)
	{
		if ((bits & (int) Math.pow(2, number)) != 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void TimeStep()
	{
		int command = command_register.SendData();
		if (CheckBit(command, 15))
		{
			////////////////////////////////////
			// Управляющая микрокоманда (УМК) //
			////////////////////////////////////
			
			
			boolean compare_bit = CheckBit(command, 14);
			
			ERegister compare_reg;
			
			// РС - проверяемый регистр
			if (!CheckBit(command, 13) && !CheckBit(command, 12));
			
			// РД - проверяемый регистр
			if (!CheckBit(command, 13) && CheckBit(command, 12));
			
			// РК - проверяемый регистр
			if (CheckBit(command, 13) && !CheckBit(command, 12));
			
			// А - проверяемый регистр
			if (CheckBit(command, 13) && CheckBit(command, 12));
			
			int choose_bit = (int) Math.pow(2, ((command & 0xf00)>>8));
			
			if ( CheckBit(compare_reg.SendData(), choose_bit) == compare_bit )
			{
				instr_pointer.GetData(command & 0xff);
			}
			
		}
		else
		{
			if (CheckBit(command, 14))
			{
				/////////////////////////////////////
				// Операционая микрокоманда (ОМК0) //
				/////////////////////////////////////
				
				// Выбираем левый вход
				
				// Выбираем правый вход
				
				// Обработка исключения - операция || сдвиги || память
				
				// Обратный код
				
				// Операция/Сдвиги
				
				// Работа с памятью
			}
			else
			{
				/////////////////////////////////////
				// Операционая микрокоманда (ОМК1) //
				/////////////////////////////////////
				
			}
		}
	}
	
	private EMemory    memory;
	private ERegister  instr_pointer;
	private ERegister  command_register;
	private EChannel[] chanells;
}
