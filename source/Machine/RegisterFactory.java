package Machine;

/*-----------------------------------------------------------------------------
  Фабрика регистров
-----------------------------------------------------------------------------*/
public class RegisterFactory
{
	public RegisterFactory()
	{
		adressRegister		= new Register(11);
		dataRegister		= new Register();
		instructionPointer	= new Register(11);
		commandRegister  	= new Register();
		
		leftALUInput	= new Register();
		rightALUInput	= new Register();
		bufferRegister  = new Register(17);
		accumulator  	= new Register();
		stateCounter  	= new Register(13);
		
		microInstructionPointer	= new Register(8);
		microCommandRegister 	= new Register();
		
		inputRegister = new Register();
	}
	
	/**
	 * Получить регистр адреса РА
	 */
	public Register getAddressRegister()
	{
		return adressRegister;
	}
	
	public Register getDataRegister()
	{
		return dataRegister;
	}

	public Register getInstructionPointer()
	{
		return instructionPointer;
	}
	
	public Register getCommandRegister()
	{
		return commandRegister;
	}
	
	public Register getLeftALUInput()
	{
		return leftALUInput;
	}
	
	public Register getRightALUInput()
	{
		return rightALUInput;
	}
	
	public Register getBufferRegister()
	{
		return bufferRegister;
	}
	
	public Register getAccumulator()
	{
		return accumulator;
	}
	
	public Register getStateCounter()
	{
		return stateCounter;
	}
	
	public Register getMicroInstructionPointer()
	{
		return microInstructionPointer;
	}
	
	public Register getMicroCommandRegister()
	{
		return microCommandRegister;
	}
	
	public Register getInputRegister()
	{
		return inputRegister;
	}
	
	private Register adressRegister;
	private Register dataRegister;
	private Register instructionPointer;
	private Register commandRegister;
	private Register leftALUInput;
	private Register rightALUInput;
	private Register bufferRegister;
	private Register accumulator;
	private Register stateCounter;
	private Register microInstructionPointer;
	private Register microCommandRegister;
	private Register inputRegister;
	
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
