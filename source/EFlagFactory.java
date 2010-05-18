/*-----------------------------------------------------------------------------
  Фабрика флагов
-----------------------------------------------------------------------------*/
public class EFlagFactory
{
	public EFlagFactory(ERegisterFactory factory)
	{
		this.state_counter = factory.StateCounter();
		
		this.flags = new EFlag[13];
		
		for (int i = 0; i < 13; i++)
		{
			flags[i] = new EFlag();
		}
		
		for (int i = 0; i < 13; i++)
		{
			flags[i].ClearFlag();
		}
		RefreshStateCounter();
	}
	
	public EFlag GetC()
	{
		return flags[0];
	}
	
	public EFlag GetZ()
	{
		return flags[1];
	}
	
	public EFlag GetN()
	{
		return flags[2];
	}
	
	public EFlag GetNull()
	{
		return flags[3];
	}
	
	public EFlag GetInterruptEnable()
	{
		return flags[4];
	}
	
	public EFlag GetInterruption()
	{
		return flags[5];
	}
	
	public EFlag GetStateOfExternalDevice()
	{
		return flags[6];
	}
	
	public EFlag GetStateOfTumbler()
	{
		return flags[7];
	}
	
	public EFlag GetProgram()
	{
		return flags[8];
	}
	
	public EFlag GetInstructionFetch()
	{
		return flags[9];
	}
	
	public EFlag GetAdressSelection()
	{
		return flags[10];
	}
	
	public EFlag GetExecution()
	{
		return flags[11];
	}
	
	public EFlag GetInputOutput()
	{
		return flags[12];
	}
	
	public void RefreshStateCounter()
	{
		int sc = 0;
		for (int i = 0; i < 13; i++)
		{
			sc+=flags[i].SendData()<<i;
		}
		state_counter.GetData(sc);
	}
	
	private ERegister state_counter;
	private EFlag[] flags;
	
	//	0  - Перенос
	//	1  - Нуль
	//	2  - Знак
	
	//	3  - 0 (для организации безусловных переходов в МПУ)
	
	//	4  - Разрешение прерывания
	//	5  - Прерывание
	//	6  - Состояние ВУ
	
	//	7  - Состояние тумблеров РАБОТА/ОСТАНОВ (1 - РАБОТА)
	
	//	8  - Программа
	//	9  - Выборка команды
	//	10 - Выборка адреса
	//	11 - Исполнение
	//	12 - Ввод-вывод
}
