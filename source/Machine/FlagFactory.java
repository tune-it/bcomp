/**
 * Фабрика флагов
 * @author Ponomarev
 * @version $Id$
 */

package Machine;

public class FlagFactory
{
	public FlagFactory(RegisterFactory factory)
	{
		this.state_counter = factory.getStateCounter();
		
		this.flags = new Flag[13];
		
		for (int i = 0; i < 13; i++)
		{
			flags[i] = new Flag();
		}
		
		for (int i = 0; i < 13; i++)
		{
			flags[i].clearFlag();
		}
		refreshStateCounter();
	}
	
	public Flag getC()
	{
		return flags[0];
	}
	
	public Flag getZ()
	{
		return flags[1];
	}
	
	public Flag getN()
	{
		return flags[2];
	}
	
	public Flag getNull()
	{
		return flags[3];
	}
	
	public Flag getInterruptEnable()
	{
		return flags[4];
	}
	
	public Flag getInterruption()
	{
		return flags[5];
	}
	
	public Flag getStateOfExternalDevice()
	{
		return flags[6];
	}
	
	public Flag getStateOfTumbler()
	{
		return flags[7];
	}
	
	public Flag getProgram()
	{
		return flags[8];
	}
	
	public Flag getInstructionFetch()
	{
		return flags[9];
	}
	
	public Flag getAddressSelection()
	{
		return flags[10];
	}
	
	public Flag getExecution()
	{
		return flags[11];
	}
	
	public Flag getInputOutput()
	{
		return flags[12];
	}
	
	public void refreshStateCounter()
	{
		int sc = 0;
		for (int i = 0; i < 13; i++)
		{
			sc+=flags[i].getValue()<<i;
		}
		state_counter.setValue(sc);
	}
	
	private Register state_counter;
	private Flag[] flags;
	
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
