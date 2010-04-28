/*-----------------------------------------------------------------------------
  Устройство управления
-----------------------------------------------------------------------------*/
public class EManagerDevice
{
	public EManagerDevice(EMemory memory, ERegister instr_counter, ERegister command_register, EChanell[] chanells)
	{
		this.memory = memory;
		this.instr_counter = instr_counter;
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
			if (!CheckBit(command, 13) && !CheckBit(command, 12))
			{
				
			}
			
			// РД - проверяемый регистр
			if (!CheckBit(command, 13) && CheckBit(command, 12))
			{
				
			}
			
			// РК - проверяемый регистр
			if (CheckBit(command, 13) && !CheckBit(command, 12))
			{
				
			}
			
			// А - проверяемый регистр
			if (CheckBit(command, 13) && CheckBit(command, 12))
			{
				
			}
			
		}
		else
		{
			if (CheckBit(command, 14))
			{
				// Операционная микрокоманда 0 (ОМК0)
				
			}
			else
			{
				// Операционная микрокоманда 1 (ОМК1)
				
			}
		}
	}
	
	private EMemory    memory;
	private ERegister  instr_counter;
	private ERegister  command_register;
	private EChanell[] chanells;
}
