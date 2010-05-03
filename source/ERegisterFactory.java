/*-----------------------------------------------------------------------------
  Фабрика регистров
-----------------------------------------------------------------------------*/
public class ERegisterFactory
{
	public ERegisterFactory()
	{
		this.registers = new ERegister[9];
		
		this.registers[0]  = new ERegister(11);
		this.registers[1]  = new ERegister();
		this.registers[2]  = new ERegister(11);
		this.registers[3]  = new ERegister();
		
		this.registers[4]  = new ERegister();
		this.registers[5]  = new ERegister();
		this.registers[6]  = new ERegister(17);
		this.registers[7]  = new ERegister();
		this.registers[8]  = new ERegister(13);
		
		this.registers[9]  = new ERegister();
		this.registers[10] = new ERegister(8);
	}
	
	public ERegisterFactory(int adress_register, int data_register, int instruction_pointer, int command_register, int left_alu_in, int right_alu_in, int buffer_register, int acc, int state_counter)
	{
		this.registers = new ERegister[9];
		
		this.registers[0] = new ERegister(adress_register);
		this.registers[1] = new ERegister(data_register);
		this.registers[2] = new ERegister(instruction_pointer);
		this.registers[3] = new ERegister(command_register);
		this.registers[4] = new ERegister(left_alu_in);
		this.registers[5] = new ERegister(right_alu_in);
		this.registers[6] = new ERegister(buffer_register);
		this.registers[7] = new ERegister(acc);
		this.registers[8] = new ERegister(state_counter);
	}
	
	public ERegister GetAdressRegister()
	{
		return registers[0];
	}
	
	public ERegister GetDataRegister()
	{
		return registers[1];
	}
	
	public ERegister GetInstructionPointer()
	{
		return registers[2];
	}
	
	public ERegister GetCommandRegister()
	{
		return registers[3];
	}
	
	public ERegister GetLeftALUInput()
	{
		return registers[4];
	}
	
	public ERegister GetRightALUInput()
	{
		return registers[5];
	}
	
	public ERegister GetBufferRegister()
	{
		return registers[6];
	}
	
	public ERegister GetAccumulator()
	{
		return registers[7];
	}
	
	public ERegister GetStateCounter()
	{
		return registers[8];
	}
	
	public ERegister GetMicroInstructionPointer()
	{
		return registers[9];
	}
	
	public ERegister GetMicroCommandRegister()
	{
		return registers[10];
	}
	
	private ERegister[] registers;
	
	//	registers[] - банк регистров
	
	//	registers[0]  - Регистр адресса
	//	registers[1]  - Регистр данных
	//	registers[2]  - Счетчик команд
	//	registers[3]  - Регистр команд
	
	//	registers[4]  - Левый вход АЛУ
	//	registers[5]  - Правый вход АЛУ
	//	registers[6]  - Буферный регистр
	//	registers[7]  - Аккумулятор
	//	registers[8]  - Регистр состояния
	 
	//	registers[9]  - Счетчик микро-команд
	//	registers[10] - Регистр микро-команд
}
