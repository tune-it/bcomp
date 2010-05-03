/*-----------------------------------------------------------------------------
  Устройство управления
-----------------------------------------------------------------------------*/
public class EManagerDevice
{
	public EManagerDevice(ERegisterFactory reg_factory, EChannel[] chanells)
	{
		this.instr_pointer = reg_factory.GetMicroInstructionPointer();
		this.reg_factory = reg_factory;
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
			if (!CheckBit(command, 13) && !CheckBit(command, 12));
			
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
	
	private ERegister			instr_pointer;
	private ERegisterFactory	reg_factory;
	private EChannel[]			chanells;
}
