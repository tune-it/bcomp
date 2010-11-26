package Machine;

/*-----------------------------------------------------------------------------
  Фабрика регистров
-----------------------------------------------------------------------------*/
public class RegisterFactory
{
	public RegisterFactory()
	{
		this.registers = new Register[12];
		
		this.registers[0]  = new Register(11);
		this.registers[1]  = new Register();
		this.registers[2]  = new Register(11);
		this.registers[3]  = new Register();
		
		this.registers[4]  = new Register();
		this.registers[5]  = new Register();
		this.registers[6]  = new Register(17);
		this.registers[7]  = new Register();
		this.registers[8]  = new Register(13);
		
		this.registers[9]  = new Register(8);
		this.registers[10] = new Register();
		
		this.registers[11] = new Register();
	}
	
//	public ERegisterFactory(int address_register, int data_register, int instruction_pointer, int command_register, int left_alu_in, int right_alu_in, int buffer_register, int acc, int state_counter)
//	{
//		this.registers = new ERegister[9];
//		
//		this.registers[0] = new ERegister(address_register);
//		this.registers[1] = new ERegister(data_register);
//		this.registers[2] = new ERegister(instruction_pointer);
//		this.registers[3] = new ERegister(command_register);
//		this.registers[4] = new ERegister(left_alu_in);
//		this.registers[5] = new ERegister(right_alu_in);
//		this.registers[6] = new ERegister(buffer_register);
//		this.registers[7] = new ERegister(acc);
//		this.registers[8] = new ERegister(state_counter);
//	}
	
	/**
	 * Получить регистр адреса РА
	 */
	public Register getAddressRegister()
	{
		return registers[0];
	}
	
	public Register getDataRegister()
	{
		return registers[1];
	}

	public Register getInstructionPointer()
	{
		return registers[2];
	}
	
	public Register getCommandRegister()
	{
		return registers[3];
	}
	
	public Register getLeftALUInput()
	{
		return registers[4];
	}
	
	public Register getRightALUInput()
	{
		return registers[5];
	}
	
	public Register getBufferRegister()
	{
		return registers[6];
	}
	
	public Register getAccumulator()
	{
		return registers[7];
	}
	
	public Register getStateCounter()
	{
		return registers[8];
	}
	
	public Register getMicroInstructionPointer()
	{
		return registers[9];
	}
	
	public Register getMicroCommandRegister()
	{
		return registers[10];
	}
	
	public Register getInputRegister()
	{
		return registers[11];
	}
	
	private Register[] registers;
	
	//	registers[] - банк регистров
	
	//	0  - Регистр адресса
	//	1  - Регистр данных
	//	2  - Счетчик команд
	//	3  - Регистр команд
	
	//	4  - Левый вход АЛУ
	//	5  - Правый вход АЛУ
	//	6  - Буферный регистр
	//	7  - Аккумулятор
	//	8  - Регистр состояния
	 
	//	9  - Счетчик микро-команд
	//	10 - Регистр микро-команд
	
	// 	11 - Клавишный регистр
}
