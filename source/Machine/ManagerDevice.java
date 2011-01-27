/**
 * Устройство управления
 * @author Ponomarev
 * @version $Id$
 */

package Machine;

enum Cycle {
	INSTRFETCH, ADDRFETCH, EXECUTION, INTERRUPTION, ANOTHER
}
 
public class ManagerDevice
{
	public ManagerDevice(RegisterFactory reg_factory, ChannelFactory channels, ALU alu, FlagFactory flag_factory, DeviceFactory dev)
	{
		this.instr_pointer = reg_factory.getMicroInstructionPointer();
		this.reg_factory = reg_factory;
		this.flag_factory = flag_factory;
		this.channels = channels;
		this.alu = alu;
		this.dev = dev;
	}

	public boolean timeStep()
	{
		boolean halt = false; // Не останавливать БЭВМ после выполнения такта

		alu.resetALU(); // Отключение инверторов и инкрементора
		
		channels.CloseAllChannels(); // Закрыть все каналы
		dev.closeAllChannels(); // Закрыть все каналы ВУ
		
		channels.MicroCommandToRMC().open(); // Пересылаем микрокоманду в регистр микрокоманд

		// Установка/сброс битов РС
		updateStateBits();
		
		flag_factory.refreshStateCounter();

		reg_factory.getMicroInstructionPointer().setValue(reg_factory.getMicroInstructionPointer().getValue()+1);
		Register command = reg_factory.getMicroCommandRegister();
		if (command.checkBit(15)) {
			////////////////////////////////////
			// Управляющая микрокоманда (УМК) //
			////////////////////////////////////
			
			// Поле (бит сравнения)
			int compare_bit = command.getBit(14);

			//Поле проверяемый регистр
			switch (command.getBits(12, 2)) {
			// РС - проверяемый регистр
			case 0:
				channels.FromSC().open();
				reg_factory.getRightALUInput().setValue(0);
				break;
			// РД - проверяемый регистр
			case 1:
				channels.FromDR().open();
				reg_factory.getLeftALUInput().setValue(0);
				break;
			// РК - проверяемый регистр
			case 2:
				channels.FromCR().open();
				reg_factory.getLeftALUInput().setValue(0);
				break;
			// А - проверяемый регистр
			case 3:
				channels.FromAcc().open();
				reg_factory.getRightALUInput().setValue(0);
				break;
			}

			alu.add();

			// Проверяемый бит
			int choose_bit = command.getBits(8, 4);

			// Сравнение
			if (reg_factory.getBufferRegister().getBit(choose_bit) == compare_bit)
				instr_pointer.setValue(command.getBits(0, 8));
		} else if (command.checkBit(14)) {
			/////////////////////////////////////
			// Операционая микрокоманда (ОМК1) //
			/////////////////////////////////////
				
			//Управление обменом с ВУ
				
			// Разрешение прерывания
			if (command.checkBit(11)) {
				flag_factory.getInterruptEnable().setFlag();
				flag_factory.refreshStateCounter();
			}

			// Запрещение прерывания
			if (command.checkBit(10)) {
				flag_factory.getInterruptEnable().clearFlag();
				flag_factory.refreshStateCounter();
			}
				
			// Сброс флагов ВУ
			if (command.checkBit(9))
				dev.clearAllFlags();
				
			// Организация связей с ВУ
			if (command.checkBit(8)) {
				int cmd = reg_factory.getCommandRegister().getBits(8, 4);
				int dev_adr = reg_factory.getCommandRegister().getBits(0, 8);

				flag_factory.getInputOutput().setFlag();

				switch(cmd) {
				// clf B
				case 0:
					if (dev.getInternalDevice(dev_adr) != null)
						dev.getInternalDevice(dev_adr).getStateFlag().clearFlag();
					break;
				// tsf B
				case 1:
					if (dev.getInternalDevice(dev_adr) != null) {
						dev.getInternalDevice(dev_adr).getStateFlagChannel().open();
						if ( dev.getInternalDevice(dev_adr).getStateFlag().getValue() == 1)
									reg_factory.getInstructionPointer().setValue(reg_factory.getInstructionPointer().getValue()+1);
					}
					break;
				// OUT B
				case 3:
					if ((dev_adr == 1) || (dev_adr == 3)) {
						dev.getInternalDevice(1).getAddressChannel().open();
						dev.getInternalDevice(1).getIORequestChannel().open();
						dev.getInternalDevice(2).getAddressChannel().open();
						dev.getInternalDevice(2).getIORequestChannel().open();
						dev.getInternalDevice(3).getAddressChannel().open();
						dev.getInternalDevice(3).getIORequestChannel().open();

						dev.getInternalDevice(dev_adr).getOutputChannel().open();
					}
					break;
				// IN B
				case 2:
					if ((dev_adr == 2) || (dev_adr == 3)) {
						dev.getInternalDevice(1).getAddressChannel().open();
						dev.getInternalDevice(1).getIORequestChannel().open();
						dev.getInternalDevice(2).getAddressChannel().open();
						dev.getInternalDevice(2).getIORequestChannel().open();
						dev.getInternalDevice(3).getAddressChannel().open();
						dev.getInternalDevice(3).getIORequestChannel().open();

						dev.getInternalDevice(dev_adr).getInputChannel().open();
					}
					break;
				}	
			}
				
			// Регистр C	
			switch(command.getBits(6, 2)) {
			// Перенос
			case 1:
				alu.setCIfExist();
				break;
			// Сброс
			case 2:
				alu.clearC();
				break;
			// Установка
			case 3:
				alu.setC();
				break;
			}

			// Установка регистра N
			if (command.checkBit(5))
				alu.setN();
				
			// Установка регистра Z
			if (command.checkBit(4))
				alu.setZ();
				
			// Остановка ЭВМ
			if (command.checkBit(3)) {
				flag_factory.getAddressSelection().clearFlag();
				flag_factory.getInstructionFetch().clearFlag();
				flag_factory.getExecution().clearFlag();
				flag_factory.getProgram().clearFlag();
				halt = true; // Остановить БЭВМ после выполнения такта
			}
				
			// Выход АЛУ (Содержимое БР)
			switch (command.getBits(0, 3)) {
			// в РА
			case 1:
				channels.ToAR().open();
				break;
			// в РД
			case 2:
				channels.ToDR().open();
				break;
			// в РК
			case 3:
				channels.ToCR().open();
				break;
			// в СК
			case 4:
				channels.ToIP().open();
				break;
			// в A
			case 5:
				channels.ToAcc().open();
				break;
			// в РА, РД, РК, А
			case 7:
				channels.ToAR(). open();
				channels.ToDR(). open();
				channels.ToCR(). open();
				channels.ToAcc().open();
				break;
			}
		} else {
			/////////////////////////////////////
			// Операционая микрокоманда (ОМК0) //
			/////////////////////////////////////
				
			// Выбираем левый вход
			switch (command.getBits(12, 2)) {
			// На левый вход ноль
			case 0:
				reg_factory.getLeftALUInput().setValue(0);
				break;
			// На левый вход аккумулятор
			case 1:
				channels.FromAcc().open();
				break;
			// На левый вход регистр состояния
			case 2:
				channels.FromSC().open();
				break;
			// На левый вход клавишный регистр
			case 3:
				channels.FromIR().open();
				break;
			}

			// Выбираем правый вход
			switch (command.getBits(8, 2)) {				
			// На правый вход ноль
			case 0:
				reg_factory.getRightALUInput().setValue(0);
				break;
			// На правый вход регистр данных
			case 1:
				channels.FromDR().open();
				break;
			// На правый вход регистр команд
			case 2:
				channels.FromCR().open();
				break;
			// На правый вход счетчик команд
			case 3:
				channels.FromIP().open();
				break;
			}

			// Обратный код
			switch (command.getBits(6, 2)) {
			// Левый вход
			case 1:
				alu.setLeftReverse();
				break;
			// Правый вход
			case 2:
				alu.setRightReverse();
				break;
			}

			// Операция
			switch (command.getBits(4, 2)) {
			// Лев.вх + Прав.вх
			case 0:
				alu.add();
				break;
			// Лев.вх + Прав.вх + 1
			case 1:
				alu.setIncrement();
				alu.add();
				break;
			// Лев.вх & Прав.вх
			case 2:
				alu.and();
				break;
			}

			//Сдвиги
			switch (command.getBits(2, 2)) {
			// Сдвиг вправо
			case 1:
				alu.ror();
				break;
			// Сдвиг влево
			case 2:
				alu.rol();
				break;
			}

			// Работа с памятью
			switch (command.getBits(0, 2)) {
			// Чтение
			case 1:
				channels.ReadFromMem().open();
				break;
			// Запись
			case 2:
				channels.WriteToMem().open();
				break;
			}
		}

		return halt;
	}

	private void updateStateBits()
	{
		// В/В осуществляется только в определённых микрокомандах
		flag_factory.getInputOutput().clearFlag();

		// Установка PC(5)
		if ((flag_factory.getInterruptEnable().getValue() == 1) && dev.deviceRequest())
			flag_factory.getInterruption().setFlag();
		else
			flag_factory.getInterruption().clearFlag();

		int n = reg_factory.getMicroInstructionPointer().getValue();
		Cycle cycle;

		if ((n <= 0x0c) || ((n >= 0x5e) && (n <= 0x8e)) || (n >= 0xe0))
			cycle = Cycle.INSTRFETCH;
		else if ((n >= 0x0d) && (n <= 0x1c))
			cycle = Cycle.ADDRFETCH;
		else if (((n >= 0x1d) && (n <= 0x5b)) || ((n >= 0xb0) && (n <= 0xdf)))
			cycle = Cycle.EXECUTION;
		else if ((n >= 0x8f) && (n <= 0x98))
			cycle = Cycle.INTERRUPTION;
		else
			cycle = Cycle.ANOTHER;

		flag_factory.getInstructionFetch().setValue(cycle == Cycle.INSTRFETCH);
		flag_factory.getAddressSelection().setValue(cycle == Cycle.ADDRFETCH);
		flag_factory.getExecution().setValue(cycle == Cycle.EXECUTION);
		flag_factory.getProgram().setValue(cycle != Cycle.ANOTHER);
	}
	
	private Register			instr_pointer;
	private RegisterFactory		reg_factory;
	private ChannelFactory		channels;
	private ALU					alu;
	private FlagFactory			flag_factory;
	private DeviceFactory		dev;
}
