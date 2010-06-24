import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import Machine.*;

/*-----------------------------------------------------------------------------
				Фабрика объектов соединяет объекты пользовательского 
				интерфейса и объекты машинной части. 
-----------------------------------------------------------------------------*/

public class ObjectFactoryUI 
{
	public ObjectFactoryUI(EMachine machine)
	{
		regfact = machine.GetRegFac();
		flagfact = machine.GetFlagFac();
		channfact = machine.GetChannelFac();
		devfact = machine.GetDeviceFactory();
		memory = machine.GetMemory();
		micro_memory = machine.GetMicroMem();

	}
	
	public RegisterUI[] createClassicRegisters() 					//Создание регистров для режима "Базовая ЭВМ"
	{
		RegisterUI RD = new RegisterUI(regfact.DataRegister(), 180, 85, 228, 103, "Регистр Данных");
		
		RegisterUI RK = new RegisterUI(regfact.CommandRegister(), 580, 110, 628, 128, "Регистр Команд");
		
		RegisterUI RA = new RegisterUI(regfact.AdressRegister(), 340, 20, "Регистр Адреса");
		RA.setWidth(187);
		
		RegisterUI SK = new RegisterUI(regfact.InstructionPointer(), 340, 150, "Счетчик Команд");
		SK.setWidth(187);
		
		RegisterUI Acc = new RegisterUI(regfact.Accumulator(), 335, 378, 400, 396, "Аккумулятор");
		
		RegisterUI C = new RegisterUI(flagfact.GetC(), 305, 378, "C");
		C.setWidth(30);
		
		RegisterUI Registers[] = {RD, RK, RA, SK, Acc, C};
		
		return Registers;
	}
	
	public RegisterUI[] createIORegisters()					//Создание регистров для режима "Работа с ВУ"
	{
		RegisterUI RD = new RegisterUI(regfact.DataRegister(), 205, 70, 243, 88, "РД");
		RD.setContentPosition((int)(RD.getX()+24), (int)(RD.getY()+42));
		RD.setStyle(true);
		
		RegisterUI RA = new RegisterUI(regfact.AdressRegister(), 205, 140, 243, 158, "РА");
		RA.setContentPosition((int)(RA.getX()+31), (int)(RA.getY()+42));
		RA.setStyle(true);
		
		RegisterUI SK = new RegisterUI(regfact.InstructionPointer(), 205, 210, 243, 228, "СК");
		SK.setContentPosition((int)(SK.getX()+31), (int)(SK.getY()+42));
		SK.setStyle(true);
		
		RegisterUI RK = new RegisterUI(regfact.CommandRegister(), 205, 280, 243, 298, "РК");
		RK.setContentPosition((int)(RK.getX()+24), (int)(RK.getY()+42));
		RK.setStyle(true);
		
		RegisterUI Acc = new RegisterUI(regfact.Accumulator(), 205, 350, 236, 368, "Акк");
		Acc.setContentPosition((int)(Acc.getX()+24), (int)(Acc.getY()+42));
		Acc.setStyle(true);
		
		RegisterUI C = new RegisterUI(flagfact.GetC(), 175, 350, "C");
		C.setWidth(30);
		
		RegisterUI OutDev = new RegisterUI(devfact.getOutputDevice().getDataRegister(), 400, 310, 445, 328, "ВУ 1");
		OutDev.setWidth(135);
		
		RegisterUI registers[] = {RD, RA, SK, RK, Acc, C, OutDev};
		
		return registers;
	}
	
	public RegisterUI[] createMPURegisters()						//Создание регистров для режима "Работа с МПУ"
	{
		RegisterUI RD = new RegisterUI(regfact.DataRegister(), 165, 85, 203, 103, "РД");
		RD.setContentPosition((int)(RD.getX()+24), (int)(RD.getY()+42));
		RD.setStyle(true);
		
		RegisterUI RK = new RegisterUI(regfact.CommandRegister(), 315, 20, 353, 38, "РК");
		RK.setContentPosition((int)(RK.getX()+24), (int)(RK.getY()+42));
		RK.setStyle(true);
		
		RegisterUI RA = new RegisterUI(regfact.AdressRegister(), 165, 20, 203, 38, "РА");
		RA.setContentPosition((int)(RA.getX()+31), (int)(RA.getY()+42));
		RA.setStyle(true);
		
		RegisterUI SK = new RegisterUI(regfact.InstructionPointer(), 315, 85, 353, 103, "СК");
		SK.setContentPosition((int)(SK.getX()+31), (int)(SK.getY()+42));
		SK.setStyle(true);
		
		RegisterUI Acc = new RegisterUI(regfact.Accumulator(), 240, 255, 271, 273, "Акк");
		Acc.setContentPosition((int)(Acc.getX()+24), (int)(Acc.getY()+42));
		Acc.setStyle(true);
		
		RegisterUI BR = new RegisterUI(regfact.BufferRegister(), 240, 185, 278, 206, "БР");
		BR.setContentPosition((int)(BR.getX()+18), (int)(BR.getY()+42));
		BR.setStyle(true);
		
		RegisterUI SC = new RegisterUI(regfact.StateCounter(), 184, 338, 70, 190, 356, "Регистр Состояния");
		SC.setContentPosition((int)(SC.getX()+24), (int)(SC.getY()+42));
		
		RegisterUI MCR = new RegisterUI(regfact.MicroCommandRegister(), 430, 85, 449, 103, "Регистр Микрокоманд");
		
		RegisterUI MIP = new RegisterUI(regfact.MicroInstructionPointer(), 528, 358, 535, 376, "Счетчик МК");
		MIP.setWidth(135);
		
		RegisterUI registers[] = {RD, RK, RA, SK, Acc, BR, SC, MCR, MIP};
		
		return registers;		
	}
	
	public InputRegisterUI createKeyRegister()
	{
		InputRegisterUI key_register = new InputRegisterUI(regfact.InputRegister(), 1, 460, 60, 32, 478, "Клавишный Регистр");
		key_register.setActive(true);
		
		return key_register;
	}
	
	public InputRegisterUI createInputRegister1()
	{
		InputRegisterUI InpDev1 = new InputRegisterUI(devfact.getInputDevice1().getDataRegister(), 550, 310, 60, 595, 328, "ВУ 2");
		InpDev1.setWidth(135);
		
		return InpDev1;
	}
	
	public InputRegisterUI createInputRegister2()
	{
		InputRegisterUI InpDev2 = new InputRegisterUI(devfact.getInputDevice2().getDataRegister(), 700, 310, 60, 745, 328, "ВУ 3");
		InpDev2.setWidth(135);
				
		return InpDev2;
	}
	
	public ManagerDeviceUI createManagerDevice()					//Создание устройства управления
	{
		return new ManagerDeviceUI(flagfact, 620, 210);
	}
	
	public MemoryUI createСlassicMemory()							//Создание памяти
	{
		return new MemoryUI (memory, regfact.InstructionPointer(), 12, 1, 1, 150, 431, 33, 23, "Память");
	}
	
	public MemoryUI createMCMemory()							//Создание памяти МК
	{
		return new MemoryUI (micro_memory, regfact.MicroInstructionPointer(), 8, 711, 1, 135, 431, 715, 23, "Память МК");
	}
	
	public AluUI createClassicAlu()								//Создание АЛУ для режима "Базовая ЭВМ"
	{		
		return new AluUI(180, 235, "АЛУ");
	}
	
	public AluUI createMPUAlu()									//Создание АЛУ для режима "Работа с МПУ"
	{		
		return new AluUI(175, 150, 263, 173, "АЛУ");
	}
	
	public ChannelUI[] createClassicChannels()						//Создание каналов для режима "Базовая ЭВМ"
	{	
		//Канал от Рестира Адреса к Памяти
		int[][] mass1 = {{336, 45}, 
			   			{160, 45}};	
		ChannelUI RAtoMEM = new ChannelUI(channfact.AdressRegToMem(), mass1);
		
		//Канал от Регистра Данных к Памяти
		int[][] mass2 = {{220, 140}, 
			   			{220, 150},
			   			{220, 150},
			   			{160, 150}};	
		ChannelUI RDtoMEM = new ChannelUI(channfact.WriteToMem(), mass2);
		
		//Канал от Памяти в Регистр Данных
		int[][] mass3 = {{156, 71},
	   					{220, 71},
	   					{220, 71}, 
	   		   			{220, 81}};
		ChannelUI MEMtoRD = new ChannelUI(channfact.ReadFromMem(), mass3);
		MEMtoRD.disableArrow();
		
		//Канал из Счетчика Команд в АЛУ
		int[][] mass4 = {{336, 177},
	   					{290, 177},
	   					{290, 177}, 
	   		   			{290, 210},
	   		   			{290, 210}, 
	   		   			{380, 210},
	   		   			{380, 210},
	   		   			{380, 226}};
		ChannelUI RDtoSK = new ChannelUI(channfact.FromIP(), mass4);
		
		//Канал из Регистра Данных в АЛУ
		int[][] mass5 = {{290, 140},
	   					{290, 210},
	   					{290, 210}, 
	   		   			{380, 210},
	   		   			{380, 210},
	   		   			{380, 226}};
		ChannelUI RDtoALU = new ChannelUI(channfact.FromDR(), mass5);
		
		//Канал из АЛУ в Акк
		int[][] mass6 = {{295, 330},
	   					{295, 353},
	   					{295, 353}, 
	   		   			{565, 353},
	   		   			{565, 353},
	   		   			{565, 369}};
		ChannelUI ALUtoAcc = new ChannelUI(channfact.ToAcc(), mass6);
		
		//Канал из АЛУ в Cчетчик Команд
		int[][] mass7 = {{295, 330},
						{295, 353},
						{295, 353}, 
   		   				{565, 353},
	   		   			{565, 353},
	   		   			{565, 177},
	   		   			{565, 177},
	   		   			{536, 177}};
		ChannelUI ALUtoSK = new ChannelUI(channfact.ToIP(), mass7);
	
		//Канал из АЛУ в Регистр Данных
		int[][] mass8 = {{295, 330},
						{295, 353},
						{295, 353}, 
   		   				{565, 353},
	   		   			{565, 353},
	   		   			{565, 110},
	   		   			{565, 110},
	   		   			{455, 110}};
		ChannelUI ALUtoRD = new ChannelUI(channfact.ToDR(), mass8);
		
		//Канал из АЛУ в Регистр Адреса
		int[][] mass9 = {{295, 330},
						{295, 353},
						{295, 353}, 
   		   				{565, 353},
	   		   			{565, 353},
	   		   			{565, 45},
	   		   			{565, 45},
	   		   			{536, 45}};
		ChannelUI ALUtoRA = new ChannelUI(channfact.ToAR(), mass9);
		
		//Канал из Акк в АЛУ
		int[][] mass10 = {{301, 403},
	   					{165, 403},
	   					{165, 403}, 
	   		   			{165, 210},
	   		   			{165, 210},
	   		   			{210, 210},
	   		   			{210, 210},
	   		   			{210, 226}};
		ChannelUI AcctoALU = new ChannelUI(channfact.FromAcc(), mass10);
		
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
		ChannelUI ALUtoRK = new ChannelUI(channfact.ToCR(), mass11);
		
		ChannelUI channels[] = {RAtoMEM, RDtoMEM, MEMtoRD, RDtoSK, RDtoALU, ALUtoAcc, ALUtoSK, ALUtoRD, ALUtoRA, AcctoALU, ALUtoRK};
		
		return channels;
	}
	
	public ChannelUI[] createMPUChannels()						//Создание каналов для режима "Работа с МПУ"
	{
		EChannel empty_channel = new EChannel(null, null);			
		
		//Канал из Памяти МК в РМК
		int[][] mass1 = {{711, 43},
						{563, 43},
						{563, 43}, 
   		   				{563, 76}};
		ChannelUI MEMtoMCR = new ChannelUI(empty_channel, mass1);
		
		//Канал из РМК в Дешифратор МК
		int[][] mass2 =	{{563, 116}, 
						{563, 176}};
		ChannelUI MCRtoDEC = new ChannelUI(empty_channel, mass2);
		
		//Канал из Дешифратора МК в СМК
		int[][] mass3 =	{{628, 216}, 
						{628, 349}};
		ChannelUI DECtoMIP = new ChannelUI(empty_channel, mass3);
		
		//Первый управляющий
		int[][] mass4 =	{{502, 216}, 
						{502, 280},
						{502, 280},
						{443, 280}};
		ChannelUI fake_channel1 = new ChannelUI(empty_channel, mass4);
		
		//Последний управляющий
		int[][] mass5 =	{{543, 216}, 
						{543, 320},
						{543, 320},
						{443, 320}};
		ChannelUI fake_channel2 = new ChannelUI(empty_channel, mass5);
		
		ChannelUI[] channels = {MEMtoMCR, MCRtoDEC, DECtoMIP, fake_channel1, fake_channel2};
		
		return channels;	
	}
	
	public JCheckBox createMovementCheckBox()
	{
		JCheckBox movement_check = new JCheckBox("Сдвиг указателя при установке бита");
		
		movement_check.setBackground(new Color(231,236,119));
		movement_check.setBounds(2, 437, 300, 19);
		movement_check.setFocusable(false);
		movement_check.setForeground(Color.BLACK);
		movement_check.setFont(new Font("Courier New", Font.PLAIN, 13));
		
		return movement_check;
	}
	
	public JCheckBox createTactCheckBox()
	{
		JCheckBox tact = new JCheckBox("Такт");
		
		tact.setBackground(new Color(231,236,119));
		tact.setBounds(330, 466, 100, 49);
		tact.setFocusable(false);
		tact.setForeground(Color.BLACK);
		tact.setFont(new Font("Courier New", Font.PLAIN, 30));
		
		return tact;
	}
	
	public JCheckBox createMemoryCheckBox()
	{
		JCheckBox memory_check = new JCheckBox("Работа с памятью МК");
		
		memory_check.setBackground(new Color(231,236,119));
		memory_check.setBounds(438, 466, 275, 49);
		memory_check.setFocusable(false);
		memory_check.setForeground(Color.BLACK);
		memory_check.setFont(new Font("Courier New", Font.PLAIN, 22));
		
		return memory_check;
	}
	
	public JButton createWorkButton()
	{
		JButton work = new JButton("Останов.");
		work.setSize(127, 51);
		work.setHorizontalTextPosition(2);
		work.setFocusable(false);
		work.setForeground(Color.BLACK);
		work.setFont(new Font("Courier New", Font.PLAIN, 19));
		
		return work;
	}
	
	public JRadioButton[] createRegisterRadioButtons()
	{

		JRadioButton key_check = new JRadioButton("Ввод в КР", true);
		key_check.setBackground(new Color(231,236,119));
		key_check.setBounds(438, 440, 174, 25);
		key_check.setFocusable(false);
		key_check.setForeground(Color.BLACK);
		key_check.setFont(new Font("Courier New", Font.PLAIN, 22));
		
		JRadioButton inp1_check = new JRadioButton("Ввод в ВУ 2");
		inp1_check.setBackground(new Color(231,236,119));
		inp1_check.setBounds(438, 465, 174, 25);
		inp1_check.setFocusable(false);
		inp1_check.setForeground(Color.BLACK);
		inp1_check.setFont(new Font("Courier New", Font.PLAIN, 22));

		JRadioButton inp2_check = new JRadioButton("Ввод в ВУ 3");
		inp2_check.setBackground(new Color(231,236,119));
		inp2_check.setBounds(438, 490, 174, 25);
		inp2_check.setFocusable(false);
		inp2_check.setForeground(Color.BLACK);
		inp2_check.setFont(new Font("Courier New", Font.PLAIN, 22));
		
		JRadioButton[] buttons = {key_check, inp1_check, inp2_check};
		
		return buttons;
	}
	
	private ERegisterFactory 	regfact;					//Регистры
	private EFlagFactory 		flagfact;					//Флаги
	private EChannelFactory		channfact;					//Каналы
	private DeviceFactory		devfact;					//Устройства ввода-вывода
	private EMemory				memory;						//Память
	private EMemory				micro_memory;				//Память микрокомманд
}
