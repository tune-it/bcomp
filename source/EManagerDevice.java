import java.util.Arrays;


public class EManagerDevice
{
	public EManagerDevice(EMemory memory, ERegister instr_counter, ERegister command_register, EChanell[] chanells)
	{
		this.memory = memory;
		this.instr_counter = instr_counter;
		this.command_register = command_register;
		this.chanells = chanells;
	}
	
	
	
	
	public void TimeStep()
	{
		boolean[] command = Arrays.copyOf(command_register.SendData(), command_register.Width());
		if (command[15])
		{
			// Управляющая микрокоманда (УМК)
			boolean compare_bit = command[14];
			ERegister compare_reg;
			
			// РС - проверяемый регистр
			if (!command[13] && !command[12])
			{
				
			}
			
			// РД - проверяемый регистр
			if (!command[13] && command[12])
			{
				
			}
			
			// РК - проверяемый регистр
			if (command[13] && !command[12])
			{
				
			}
			
			// А - проверяемый регистр
			if (command[13] && command[12])
			{
				
			}
			
		}
		else
		{
			if (command[14])
			{
				// Операционная микрокоманда 0 (ОМК0)
				
			}
			else
			{
				// Операционная микрокоманда 1 (ОМК1)
				
			}
		}
	}
	
	public int BinToInt(boolean[] bits)
	{
		int number = 0;
		
		for (int i=0; i < bits.length; i++)
		{
			if( bits[i] )
			{
				number+=Math.pow(2, i);
			}
		}
		
		return number;
	}
	
	private EMemory    memory;
	private ERegister  instr_counter;
	private ERegister  command_register;
	private EChanell[] chanells;
}
