package Machine;

/*-----------------------------------------------------------------------------
  Устройство управления
-----------------------------------------------------------------------------*/
public class EManagerDevice
{
	public EManagerDevice(ERegisterFactory reg_factory, EChannelFactory channels, EALU alu, EFlagFactory flag_factory, DeviceFactory dev)
	{
		this.instr_pointer = reg_factory.MicroInstructionPointer();
		this.reg_factory = reg_factory;
		this.flag_factory = flag_factory;
		this.channels = channels;
		this.alu = alu;
		this.dev = dev;
	}
	
	private boolean CheckBit(int bits, int number)
	{
		if ((bits & (int) StrictMath.pow(2, number)) != 0)
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
		alu.ResetALU(); // Отключение инверторов и инкрементора
		
		channels.CloseAllChannels(); // Закрыть все каналы
		dev.closeAllChannels(); // Закрыть все каналы ВУ
		
		channels.MicroCommandToRMC().Open(); // Пересылаем микрокоманду в регистр микрокоманд
		
		int n = reg_factory.MicroInstructionPointer().SendData();
		
		// Установка/сброс битов РС
		if (n <= 0xC)
		{
			flag_factory.GetAdressSelection().ClearFlag();
			flag_factory.GetInstructionFetch().SetFlag();
			//flag_factory.GetExecution().ClearFlag();
			flag_factory.GetInputOutput().ClearFlag();
			flag_factory.GetProgram().SetFlag();
			flag_factory.GetInterruption().ClearFlag();
		}
		else
		{
			if (n <= 0x1C)
			{
				flag_factory.GetAdressSelection().SetFlag();
				flag_factory.GetInstructionFetch().ClearFlag();
				//flag_factory.GetExecution().ClearFlag();
				flag_factory.GetInputOutput().ClearFlag();
				flag_factory.GetProgram().SetFlag();
				flag_factory.GetInterruption().ClearFlag();
			}
			else
			{
				if (n <= 0x8D)
				{
					flag_factory.GetAdressSelection().ClearFlag();
					flag_factory.GetInstructionFetch().ClearFlag();
					//flag_factory.GetExecution().SetFlag();
					flag_factory.GetInputOutput().ClearFlag();
					flag_factory.GetProgram().SetFlag();
					flag_factory.GetInterruption().ClearFlag();
				}
				else
				{
					if (n == 0x8E)
					{
						flag_factory.GetAdressSelection().ClearFlag();
						flag_factory.GetInstructionFetch().ClearFlag();
						//flag_factory.GetExecution().ClearFlag();
						flag_factory.GetInputOutput().SetFlag();
						flag_factory.GetProgram().ClearFlag();
						flag_factory.GetInterruption().ClearFlag();
					}
					else
					{
						if (n <= 0x98)
						{
							flag_factory.GetAdressSelection().ClearFlag();
							flag_factory.GetInstructionFetch().ClearFlag();
							//flag_factory.GetExecution().ClearFlag();
							flag_factory.GetInputOutput().ClearFlag();
							flag_factory.GetProgram().ClearFlag();
							flag_factory.GetInterruption().SetFlag();
						}
						else
						{
							if (n <= 0xff)
							{
								flag_factory.GetAdressSelection().ClearFlag();
								flag_factory.GetInstructionFetch().ClearFlag();
								//flag_factory.GetExecution().ClearFlag();
								flag_factory.GetInputOutput().ClearFlag();
								flag_factory.GetProgram().ClearFlag();
								flag_factory.GetInterruption().ClearFlag();
							}
						
						}
					}
				}
			}
		}
		if (n == 0x88)
		{
			flag_factory.GetAdressSelection().ClearFlag();
			flag_factory.GetInstructionFetch().ClearFlag();
			//flag_factory.GetExecution().ClearFlag();
			flag_factory.GetInputOutput().ClearFlag();
			flag_factory.GetProgram().ClearFlag();
			flag_factory.GetInterruption().ClearFlag();
		}
		
		flag_factory.RefreshStateCounter();
		
		reg_factory.MicroInstructionPointer().GetData(reg_factory.MicroInstructionPointer().SendData()+1);
		int command = reg_factory.MicroCommandRegister().SendData();
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
			int choose_bit =(command & 0xf00)>>8;
			
			// Сравнение
			if ( CheckBit(compare_reg.SendData(), choose_bit) == compare_bit )
			{
				instr_pointer.GetData(command & 0xff);
			}
			
		}
		else
		{
			if (!CheckBit(command, 14))
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
				if (CheckBit(command, 9) && !CheckBit(command, 8)) channels.FromCR().Open();

				// На правый вход счетчик команд
				if (CheckBit(command, 9) && CheckBit(command, 8)) channels.FromIP().Open();
				
			// Обратный код
				
				// Правый вход
				if (CheckBit(command, 7)) alu.SetRightReverse();

				// Левый вход
				if (CheckBit(command, 6)) alu.SetLeftReverse();
				
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
				if (CheckBit(command, 5) && !CheckBit(command, 4)) alu.AND();
				
			//Сдвиги
				
				// Сдвиг вправо
				if (!CheckBit(command, 3) && CheckBit(command, 2)) alu.ROR();
				
				// Сдвиг влево
				if (CheckBit(command, 3) && !CheckBit(command, 2)) alu.ROL();

			// Работа с памятью
				// Чтение
				if (!CheckBit(command, 1) && CheckBit(command, 0)) channels.ReadFromMem().Open();
				
				// Запись
				if (CheckBit(command, 1) && !CheckBit(command, 0)) channels.WriteToMem().Open();				
			}
			else
			{
			/////////////////////////////////////
			// Операционая микрокоманда (ОМК1) //
			/////////////////////////////////////
				
			//Управление обменом с ВУ
				
				// Разрешение прерывания
				if (CheckBit(command, 11))
				{
					flag_factory.GetInterruptEnable().SetFlag();
					flag_factory.RefreshStateCounter();
				}
				
				// Запрещение прерывания
				if (CheckBit(command, 10))
				{
					flag_factory.GetInterruptEnable().ClearFlag();
					flag_factory.RefreshStateCounter();
				}
				
				// Сброс флагов ВУ
				if (CheckBit(command, 9)) dev.clearAllFlags();
				
				// Организация связей с ВУ
				if (CheckBit(command, 8))
				{
					int cmd = reg_factory.CommandRegister().SendData()&0x0f00;
					int dev_adr = reg_factory.CommandRegister().SendData()&0x00ff;
					
					switch(cmd)
					{
						case 0:
							// clf B
							if (dev.getDeviceByAdress(dev_adr) != null) dev.getDeviceByAdress(dev_adr).getStateFlag().ClearFlag();
							break;
						case 1:
							// tsf B
							if (dev.getDeviceByAdress(dev_adr) != null)
							{
								dev.getDeviceByAdress(dev_adr).getStateFlagChannel().Open();
								if ( dev.getDeviceByAdress(dev_adr).getStateFlag().SendData() == 1)
								{
									reg_factory.MicroInstructionPointer().GetData(reg_factory.MicroInstructionPointer().SendData()+1);
								}
							}
							break;
						case 2:
							// in B
							if ( dev_adr == 1)
							{
								dev.getDeviceByAdress(1).getAdressChannel().Open();
								dev.getDeviceByAdress(1).getIORequestChannel().Open();
								dev.getDeviceByAdress(2).getAdressChannel().Open();
								dev.getDeviceByAdress(2).getIORequestChannel().Open();
								dev.getDeviceByAdress(3).getAdressChannel().Open();
								dev.getDeviceByAdress(3).getIORequestChannel().Open();
								
								
								dev.getDeviceByAdress(dev_adr).getDataChannel().Open();
							}
							break;
						case 3:
							// out B
							if ( (dev_adr == 2) || (dev_adr == 3))
							{
								dev.getDeviceByAdress(1).getAdressChannel().Open();
								dev.getDeviceByAdress(1).getIORequestChannel().Open();
								dev.getDeviceByAdress(2).getAdressChannel().Open();
								dev.getDeviceByAdress(2).getIORequestChannel().Open();
								dev.getDeviceByAdress(3).getAdressChannel().Open();
								dev.getDeviceByAdress(3).getIORequestChannel().Open();
								
								dev.getDeviceByAdress(dev_adr).getDataChannel().Open();
							}
							break;
						default: break;
					}	
				}
				
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
				if (CheckBit(command, 3))
				{
					flag_factory.GetStateOfTumbler().ClearFlag();
					flag_factory.RefreshStateCounter();
				}
				
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
	private EFlagFactory		flag_factory;
	private DeviceFactory		dev;
}
