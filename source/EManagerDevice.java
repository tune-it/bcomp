/*-----------------------------------------------------------------------------
  Устройство управления
-----------------------------------------------------------------------------*/
public class EManagerDevice
{
	public EManagerDevice(ERegisterFactory reg_factory, EChannel[] chanells, EALU alu)
	{
		this.instr_pointer = reg_factory.GetMicroInstructionPointer();
		this.reg_factory = reg_factory;
		this.chanells = chanells;
		this.alu = alu;
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
		int command = reg_factory.GetCommandRegister().SendData();
		if (CheckBit(command, 15))
		{
			////////////////////////////////////
			// Управляющая микрокоманда (УМК) //
			////////////////////////////////////
			
			// Поле (бит сравнения)
			boolean compare_bit = CheckBit(command, 14);
			
			ERegister compare_reg = null;
			
			// РС - проверяемый регистр
			if (!CheckBit(command, 13) && !CheckBit(command, 12)) compare_reg = reg_factory.GetStateCounter();
			
			// РД - проверяемый регистр
			if (!CheckBit(command, 13) && CheckBit(command, 12)) compare_reg = reg_factory.GetDataRegister();
			
			// РК - проверяемый регистр
			if (CheckBit(command, 13) && !CheckBit(command, 12)) compare_reg = reg_factory.GetCommandRegister();
			
			// А - проверяемый регистр
			if (CheckBit(command, 13) && CheckBit(command, 12)) compare_reg = reg_factory.GetAccumulator();
			
			// Проверяемый бит
			int choose_bit = (int) Math.pow(2, ((command & 0xf00)>>8));
			
			// Сравнение
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
				if (!CheckBit(command, 13) && !CheckBit(command, 12)); // На левый вход ноль
				{
					reg_factory.GetLeftALUInput().GetData(0);
				}
				
				if (!CheckBit(command, 13) && CheckBit(command, 12)); // На левый вход аккумулятор
				{
					
				}
				
				if (CheckBit(command, 13) && !CheckBit(command, 12)); // На левый вход регистр состояния
				{
					
				}
				
				if (CheckBit(command, 13) && CheckBit(command, 12)); // На левый вход клавишный регистр
				{
					
				}
				
				// Выбираем правый вход
				if (!CheckBit(command, 9) && !CheckBit(command, 8)); // На правый вход ноль
				{
					reg_factory.GetRightALUInput().GetData(0);
				}
				
				if (!CheckBit(command, 9) && CheckBit(command, 8)); // На правый вход регистр данных
				{
					
				}
				
				if (!CheckBit(command, 9) && !CheckBit(command, 8)); // На правый вход регистр команд
				{
					
				}
				
				if (!CheckBit(command, 9) && !CheckBit(command, 8)); // На правый вход счетчик команд
				{
					
				}
				
				// Обратный код
				if (!CheckBit(command, 7)) // Правый вход
				{
					alu.SetRightReverse();
				}
				
				if (!CheckBit(command, 6)) // Левый вход
				{
					alu.SetLeftReverse();
				}
				
				// Операция
				if (!CheckBit(command, 5) && !CheckBit(command, 4)) // Лев.вх + Прав.вх
				{
					alu.ADD();
				}
				
				if (!CheckBit(command, 5) && CheckBit(command, 4)) // Лев.вх + Прав.вх + 1
				{
					alu.SetIncrement();
					alu.ADD();
				}
				
				if (CheckBit(command, 5) && CheckBit(command, 4)) // Лев.вх & Прав.вх
				{
					alu.AND();
				}
				
				//Сдвиги
				if (!CheckBit(command, 3) && CheckBit(command, 2)) // Сдвиг вправо
				{
					alu.ROR();
				}
				
				if (CheckBit(command, 3) && !CheckBit(command, 2)) // Сдвиг влево
				{
					alu.ROL();
				}
				
				// Работа с памятью
				if (!CheckBit(command, 3) && CheckBit(command, 2)) // Чтение
				{
					
				}
				
				if (CheckBit(command, 3) && !CheckBit(command, 2)) // Запись
				{
					
				}
			}
			else
			{
				/////////////////////////////////////
				// Операционая микрокоманда (ОМК1) //
				/////////////////////////////////////
				
			}
		}
	}
	
	private ERegister			instr_pointer;
	private ERegisterFactory	reg_factory;
	private EChannel[]			chanells;
	private EALU				alu;
}
