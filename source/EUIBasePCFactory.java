import java.awt.Color;
import java.awt.Font;

import javax.swing.JCheckBox;

import Machine.*;

/*-----------------------------------------------------------------------------
				Фабрика объектов соединяет объекты пользовательского 
				интерфейса и объекты машинной части. 
-----------------------------------------------------------------------------*/

public class EUIBasePCFactory 
{
	public EUIBasePCFactory(EMachine machine)
	{
		regfact = machine.GetRegFac();
		flagfact = machine.GetFlagFac();
		channfact = machine.GetChannelFac();
		memory = machine.GetMemory();
		micro_memory = machine.GetMicroMem();
	}
	
	public EUIRegister[] CreateClassicRegisters() 					//Создание регистров для режима "Базовая ЭВМ"
	{
		EUIRegister RD = new EUIRegister(regfact.DataRegister(), 180, 85, 228, 103, "Регистр Данных");
		
		EUIRegister RK = new EUIRegister(regfact.CommandRegister(), 580, 110, 628, 128, "Регистр Команд");
		
		EUIRegister RA = new EUIRegister(regfact.AdressRegister(), 340, 20, "Регистр Адреса");
		RA.SetWidth(187);
		
		EUIRegister SK = new EUIRegister(regfact.InstructionPointer(), 340, 150, "Счетчик Команд");
		SK.SetWidth(187);
		
		EUIRegister Acc = new EUIRegister(regfact.Accumulator(), 335, 378, 400, 396, "Аккумулятор");
		
		EUIRegister C = new EUIRegister(flagfact.GetC(), 305, 378, "C");
		C.SetWidth(30);
		
		EUIRegister Registers[] = {RD, RK, RA, SK, Acc, C};
		
		return Registers;
	}
	
	public EUIRegister[] CreateOutputRegisters()					//Создание регистров для режима "Работа с ВУ"
	{
		EUIRegister RD = new EUIRegister(regfact.DataRegister(), 205, 70, 243, 88, "РД");
		RD.SetDataPosition((int)(RD.GetX()+24), (int)(RD.GetY()+42));
		RD.SetStyle(true);
		
		EUIRegister RA = new EUIRegister(regfact.AdressRegister(), 205, 140, 243, 158, "РА");
		RA.SetDataPosition((int)(RA.GetX()+31), (int)(RA.GetY()+42));
		RA.SetStyle(true);
		
		EUIRegister SK = new EUIRegister(regfact.InstructionPointer(), 205, 210, 243, 228, "СК");
		SK.SetDataPosition((int)(SK.GetX()+31), (int)(SK.GetY()+42));
		SK.SetStyle(true);
		
		EUIRegister RK = new EUIRegister(regfact.CommandRegister(), 205, 280, 243, 298, "РК");
		RK.SetDataPosition((int)(RK.GetX()+24), (int)(RK.GetY()+42));
		RK.SetStyle(true);
		
		EUIRegister Acc = new EUIRegister(regfact.Accumulator(), 205, 350, 236, 368, "Акк");
		Acc.SetDataPosition((int)(Acc.GetX()+24), (int)(Acc.GetY()+42));
		Acc.SetStyle(true);
		
		EUIRegister C = new EUIRegister(flagfact.GetC(), 175, 350, "C");
		C.SetWidth(30);
		
		EUIRegister Registers[] = {RD, RA, SK, RK, Acc, C};
		
		return Registers;
	}
	
	public EUIRegister[] CreateMicroRegisters()						//Создание регистров для режима "Работа с МПУ"
	{
		EUIRegister RD = new EUIRegister(regfact.DataRegister(), 165, 85, 203, 103, "РД");
		RD.SetDataPosition((int)(RD.GetX()+24), (int)(RD.GetY()+42));
		RD.SetStyle(true);
		
		EUIRegister RK = new EUIRegister(regfact.CommandRegister(), 315, 20, 353, 38, "РК");
		RK.SetDataPosition((int)(RK.GetX()+24), (int)(RK.GetY()+42));
		RK.SetStyle(true);
		
		EUIRegister RA = new EUIRegister(regfact.AdressRegister(), 165, 20, 203, 38, "РА");
		RA.SetDataPosition((int)(RA.GetX()+31), (int)(RA.GetY()+42));
		RA.SetStyle(true);
		
		EUIRegister SK = new EUIRegister(regfact.InstructionPointer(), 315, 85, 353, 103, "СК");
		SK.SetDataPosition((int)(SK.GetX()+31), (int)(SK.GetY()+42));
		SK.SetStyle(true);
		
		EUIRegister Acc = new EUIRegister(regfact.Accumulator(), 240, 255, 271, 273, "Акк");
		Acc.SetDataPosition((int)(Acc.GetX()+24), (int)(Acc.GetY()+42));
		Acc.SetStyle(true);
		
		EUIRegister BR = new EUIRegister(regfact.BufferRegister(), 240, 185, 278, 206, "БР");
		BR.SetDataPosition((int)(BR.GetX()+18), (int)(BR.GetY()+42));
		BR.SetStyle(true);
		
		EUIRegister SC = new EUIRegister(regfact.StateCounter(), 184, 338, 70, 190, 356, "Регистр Состояния");
		SC.SetDataPosition((int)(SC.GetX()+24), (int)(SC.GetY()+42));
		
		EUIRegister MCR = new EUIRegister(regfact.MicroCommandRegister(), 430, 85, 449, 103, "Регистр Микрокоманд");
		
		EUIRegister MIP = new EUIRegister(regfact.MicroInstructionPointer(), 528, 358, 535, 376, "Счетчик МК");
		MIP.SetWidth(135);
		
		EUIRegister Registers[] = {RD, RK, RA, SK, Acc, BR, SC, MCR, MIP};
		
		return Registers;		
	}
	
	public EUIInputRegister CreateInputRegister()
	{
		return new EUIInputRegister(regfact.InputRegister(), 1, 460, 60, 32, 478, "Клавишный Регистр");
	}
	
	public EUIManagerDevice CreateManagerDevice()					//Создание устройства управления
	{
		return new EUIManagerDevice(flagfact, 620, 210);
	}
	
	public EUIMemory CreateСlassicMemory()							//Создание памяти
	{
		return new EUIMemory (memory, regfact.InstructionPointer(), 12, 1, 1, 150, 431, 33, 23, "Память");
	}
	
	public EUIMemory CreateMicroMemory()							//Создание памяти МК
	{
		return new EUIMemory (micro_memory, regfact.MicroInstructionPointer(), 8, 711, 1, 135, 431, 715, 23, "Память МК");
	}
	
	public EUIAlu CreateClassicAlu()								//Создание АЛУ для режима "Базовая ЭВМ"
	{		
		return new EUIAlu(180, 235, "АЛУ");
	}
	
	public EUIAlu CreateMicroAlu()									//Создание АЛУ для режима "Работа с МПУ"
	{		
		return new EUIAlu(175, 150, 263, 173, "АЛУ");
	}
	
	public EUIChannel[] CreateClassicChannels()						//Создание каналов для режима "Базовая ЭВМ"
	{	
		//Канал от Рестира Адреса к Памяти
		int[][] mass1 = {{336, 45}, 
			   			{160, 45}};	
		EUIChannel RAtoMEM = new EUIChannel(channfact.AdressRegToMem(), mass1);
		
		//Канал от Регистра Данных к Памяти
		int[][] mass2 = {{220, 140}, 
			   			{220, 150},
			   			{220, 150},
			   			{160, 150}};	
		EUIChannel RDtoMEM = new EUIChannel(channfact.WriteToMem(), mass2);
		
		//Канал от Памяти в Регистр Данных
		int[][] mass3 = {{156, 71},
	   					{220, 71},
	   					{220, 71}, 
	   		   			{220, 81}};
		EUIChannel MEMtoRD = new EUIChannel(channfact.ReadFromMem(), mass3);
		MEMtoRD.DisableArrow();
		
		//Канал из Счетчика Команд в АЛУ
		int[][] mass4 = {{336, 177},
	   					{290, 177},
	   					{290, 177}, 
	   		   			{290, 210},
	   		   			{290, 210}, 
	   		   			{380, 210},
	   		   			{380, 210},
	   		   			{380, 226}};
		EUIChannel RDtoSK = new EUIChannel(channfact.FromIP(), mass4);
		
		//Канал из Регистра Данных в АЛУ
		int[][] mass5 = {{290, 140},
	   					{290, 210},
	   					{290, 210}, 
	   		   			{380, 210},
	   		   			{380, 210},
	   		   			{380, 226}};
		EUIChannel RDtoALU = new EUIChannel(channfact.FromDR(), mass5);
		
		//Канал из АЛУ в Акк
		int[][] mass6 = {{295, 330},
	   					{295, 353},
	   					{295, 353}, 
	   		   			{565, 353},
	   		   			{565, 353},
	   		   			{565, 369}};
		EUIChannel ALUtoAcc = new EUIChannel(channfact.ToAcc(), mass6);
		
		//Канал из АЛУ в Cчетчик Команд
		int[][] mass7 = {{295, 330},
						{295, 353},
						{295, 353}, 
   		   				{565, 353},
	   		   			{565, 353},
	   		   			{565, 177},
	   		   			{565, 177},
	   		   			{536, 177}};
		EUIChannel ALUtoSK = new EUIChannel(channfact.ToIP(), mass7);
	
		//Канал из АЛУ в Регистр Данных
		int[][] mass8 = {{295, 330},
						{295, 353},
						{295, 353}, 
   		   				{565, 353},
	   		   			{565, 353},
	   		   			{565, 110},
	   		   			{565, 110},
	   		   			{455, 110}};
		EUIChannel ALUtoRD = new EUIChannel(channfact.ToDR(), mass8);
		
		//Канал из АЛУ в Регистр Адреса
		int[][] mass9 = {{295, 330},
						{295, 353},
						{295, 353}, 
   		   				{565, 353},
	   		   			{565, 353},
	   		   			{565, 45},
	   		   			{565, 45},
	   		   			{536, 45}};
		EUIChannel ALUtoRA = new EUIChannel(channfact.ToAR(), mass9);
		
		//Канал из Акк в АЛУ
		int[][] mass10 = {{301, 403},
	   					{165, 403},
	   					{165, 403}, 
	   		   			{165, 210},
	   		   			{165, 210},
	   		   			{210, 210},
	   		   			{210, 210},
	   		   			{210, 226}};
		EUIChannel AcctoALU = new EUIChannel(channfact.FromAcc(), mass10);
		
		//Канал из АЛУ в Регистр Команд
		int[][] mass11 = {{295, 330},
						{295, 353},
						{295, 353}, 
   		   				{565, 353},
	   		   			{565, 353},
	   		   			{565, 78},
	   		   			{565, 78},
	   		   			{714, 78},
	   		   			{714, 78},
	   		   			{714, 101}};
		EUIChannel ALUtoRK = new EUIChannel(channfact.ToCR(), mass11);
		
		EUIChannel Channels[] = {RAtoMEM, RDtoMEM, MEMtoRD, RDtoSK, RDtoALU, ALUtoAcc, ALUtoSK, ALUtoRD, ALUtoRA, AcctoALU, ALUtoRK};
		
		return Channels;
	}
	
	public EUIChannel[] CreateMicroChannels()						//Создание каналов для режима "Работа с МПУ"
	{
		EChannel empty_channel = new EChannel(null, null);			
		
		//Канал из Памяти МК в РМК
		int[][] mass1 = {{711, 43},
						{563, 43},
						{563, 43}, 
   		   				{563, 76}};
		EUIChannel MEMtoMCR = new EUIChannel(empty_channel, mass1);
		
		//Канал из РМК в Дешифратор МК
		int[][] mass2 =	{{563, 116}, 
						{563, 176}};
		EUIChannel MCRtoDEC = new EUIChannel(empty_channel, mass2);
		
		//Канал из Дешифратора МК в СМК
		int[][] mass3 =	{{628, 216}, 
						{628, 349}};
		EUIChannel DECtoMIP = new EUIChannel(empty_channel, mass3);
		
		//Первый управляющий
		int[][] mass4 =	{{502, 216}, 
						{502, 280},
						{502, 280},
						{443, 280}};
		EUIChannel fake_channel1 = new EUIChannel(empty_channel, mass4);
		
		//Последний управляющий
		int[][] mass5 =	{{543, 216}, 
						{543, 320},
						{543, 320},
						{443, 320}};
		EUIChannel fake_channel2 = new EUIChannel(empty_channel, mass5);
		
		EUIChannel[] channels = {MEMtoMCR, MCRtoDEC, DECtoMIP, fake_channel1, fake_channel2};
		
		return channels;	
	}
	
	public JCheckBox CreateMovementCheckBox()
	{
		JCheckBox movement_check = new JCheckBox("Сдвиг указателя при установке бита");
		
		movement_check.setBackground(new Color(231,236,119));
		movement_check.setBounds(2, 437, 300, 19);
		movement_check.setFocusable(false);
		movement_check.setForeground(Color.BLACK);
		movement_check.setFont(new Font("Courier New", Font.PLAIN, 13));
		
		return movement_check;
	}
	
	public JCheckBox CreateTactCheckBox()
	{
		JCheckBox tact = new JCheckBox("Такт");
		
		tact.setBackground(new Color(231,236,119));
		tact.setBounds(330, 466, 100, 49);
		tact.setFocusable(false);
		tact.setForeground(Color.BLACK);
		tact.setFont(new Font("Courier New", Font.PLAIN, 30));
		
		return tact;
	}
	
	public JCheckBox CreateMemoryCheckBox()
	{
		JCheckBox memory_check = new JCheckBox("Работа с памятью МК");
		
		memory_check.setBackground(new Color(231,236,119));
		memory_check.setBounds(437, 466, 295, 49);
		memory_check.setFocusable(false);
		memory_check.setForeground(Color.BLACK);
		memory_check.setFont(new Font("Courier New", Font.PLAIN, 24));
		
		return memory_check;
	}
	private ERegisterFactory 	regfact;					//Регистры
	private EFlagFactory 		flagfact;					//Флаги
	private EChannelFactory		channfact;					//Каналы
	private EMemory				memory;						//Память
	private EMemory				micro_memory;				//Память микрокомманд
}
