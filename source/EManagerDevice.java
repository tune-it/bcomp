/*-----------------------------------------------------------------------------
  Устройство управления
-----------------------------------------------------------------------------*/
public class EManagerDevice
{
<<<<<<< .mine
	public EManagerDevice(ERegisterFactory reg_factory, EChannelFactory channels, EALU alu)
=======
	public EManagerDevice(ERegisterFactory reg_factory, EChannel[] chanells, EALU alu)
>>>>>>> .r50
	{
		this.instr_pointer = reg_factory.MicroInstructionPointer();
		this.reg_factory = reg_factory;
		this.channels = channels;
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
		int command = reg_factory.CommandRegister().SendData();
		if (CheckBit(command, 15))
		{
			////////////////////////////////////
			// Управляющая микрокоманда (УМК) //
			////////////////////////////////////
			
			// Поле (бит сравнения)
			boolean compare_bit = CheckBit(command, 14);
			
			ERegister compare_reg = null;
			
			// РС - проверяемый регистр
			if (!CheckBit(command, 13) && !CheckBit(command, 12)) compare_reg = reg_factory.StateCounter();
			
			// РД - проверяемый регистр
			if (!CheckBit(command, 13) && CheckBit(command, 12)) compare_reg = reg_factory.DataRegister();
			
			// РК - проверяемый регистр
			if (CheckBit(command, 13) && !CheckBit(command, 12)) compare_reg = reg_factory.CommandRegister();
			
			// А - проверяемый регистр
			if (CheckBit(command, 13) && CheckBit(command, 12)) compare_reg = reg_factory.Accumulator();
			
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
				
				// На левый вход ноль
				if (!CheckBit(command, 13) && !CheckBit(command, 12)) reg_factory.LeftALUInput().GetData(0);
				
				// На левый вход аккумулятор
				if (!CheckBit(command, 13) && CheckBit(command, 12)) channels.FromAcc().Open();
				
				// На левый вход регистр состояния
				if (CheckBit(command, 13) && !CheckBit(command, 12)) channels.FromSC().Open();
				
				// На левый вход клавишный регистр
				if (CheckBit(command, 13) && CheckBit(command, 12)) channels.FromIR().Open();
				
			// Выбираем правый вход
				
				// На правый вход ноль
				if (!CheckBit(command, 9) && !CheckBit(command, 8)) reg_factory.RightALUInput().GetData(0);
				
				// На правый вход регистр данных
				if (!CheckBit(command, 9) && CheckBit(command, 8)) channels.FromDR().Open();

				// На правый вход регистр команд
				if (!CheckBit(command, 9) && !CheckBit(command, 8)) channels.FromCR().Open();

				// На правый вход счетчик команд
				if (!CheckBit(command, 9) && !CheckBit(command, 8)) channels.FromIP().Open();
				
			// Обратный код
				
				// Правый вход
				if (!CheckBit(command, 7)) alu.SetRightReverse();

				// Левый вход
				if (!CheckBit(command, 6)) alu.SetLeftReverse();
				
			// Операция
				
				// Лев.вх + Прав.вх
				if (!CheckBit(command, 5) && !CheckBit(command, 4)) alu.ADD(); 

				// Лев.вх + Прав.вх + 1
				if (!CheckBit(command, 5) && CheckBit(command, 4)) 
				{
					alu.SetIncrement();
					alu.ADD();
				}
				
				// Лев.вх & Прав.вх
				if (CheckBit(command, 5) && CheckBit(command, 4)) alu.AND();
				
			//Сдвиги
				
				// Сдвиг вправо
				if (!CheckBit(command, 3) && CheckBit(command, 2)) alu.ROR();
				
				// Сдвиг влево
				if (CheckBit(command, 3) && !CheckBit(command, 2)) alu.ROL();

			// Работа с памятью
				
				// Чтение
				if (!CheckBit(command, 3) && CheckBit(command, 2)) channels.ReadFromMem();
				
				// Запись
				if (CheckBit(command, 3) && !CheckBit(command, 2)) channels.WriteToMem();
			}
			else
			{
			/////////////////////////////////////
			// Операционая микрокоманда (ОМК1) //
			/////////////////////////////////////
				
			//Управление обмен в ВУ
				
			// Регистр C	
				
				// Перенос
				if (!CheckBit(command, 7) && CheckBit(command, 6)) alu.SetCIfExist();

				// Сброс
				if (CheckBit(command, 7) && !CheckBit(command, 6)) alu.ClearC();
				
				 // Установка
				if (CheckBit(command, 7) && CheckBit(command, 6)) alu.SetC();
				
			// Установка регистра N
				if (CheckBit(command, 5)) alu.SetN();
				
			// Установка регистра Z
				if (CheckBit(command, 4)) alu.SetZ();
				
			// Остановка ЭВМ
				if (CheckBit(command, 3));
				
			// Выход АЛУ (Содержимое БР)
				
				// в РА
				if (!CheckBit(command, 2) && !CheckBit(command, 1) && CheckBit(command, 0)) channels.ToAR().Open();

				// в РД
				if (!CheckBit(command, 2) && CheckBit(command, 1) && !CheckBit(command, 0)) channels.ToDR().Open();
				
				// в РК
				if (!CheckBit(command, 2) && CheckBit(command, 1) && CheckBit(command, 0)) channels.ToCR().Open();
				
				// в СК
				if (CheckBit(command, 2) && !CheckBit(command, 1) && !CheckBit(command, 0)) channels.ToIP().Open();

				// в A
				if (CheckBit(command, 2) && !CheckBit(command, 1) && CheckBit(command, 0)) channels.ToAcc().Open();
				
				// в РА, РД, РК, А
				if (CheckBit(command, 2) && CheckBit(command, 1) && CheckBit(command, 0)) 
				{
					channels.ToAR(). Open();
					channels.ToDR(). Open();
					channels.ToCR(). Open();
					channels.ToIP(). Open();
					channels.ToAcc().Open();
				}
			}
		}
	}
	
	private ERegister			instr_pointer;
	private ERegisterFactory	reg_factory;
	private EChannelFactory		channels;
	private EALU				alu;
}
