/**
 * Фабрика объектов соединяет объекты пользовательского 
 * интерфейса и объекты машинной части. 
 * @version $Id$
 */

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import Machine.*;

public class ObjectFactoryUI 
{
	public ObjectFactoryUI(Machine machine)
	{
		regfact = machine.getRegFactory();
		flagfact = machine.getFlagFactory();
		channfact = machine.getChannelFactory();
		devfact = machine.getDeviceFactory();
		memory = machine.getMemory();
		micro_memory = machine.getMicroMem();

	}
	
	public RegisterUI[] createClassicRegisters() 					//Создание регистров для режима "Базовая ЭВМ"
	{
		RegisterUI RD = new RegisterUI(regfact.getDataRegister(), 180, 85, 228, 103, "Регистр Данных");
		
		RegisterUI RK = new RegisterUI(regfact.getCommandRegister(), 580, 110, 628, 128, "Регистр Команд");
		
		RegisterUI RA = new RegisterUI(regfact.getAddressRegister(), 340, 20, "Регистр Адреса");
		RA.setWidth(187);
		
		RegisterUI SK = new RegisterUI(regfact.getInstructionPointer(), 340, 150, "Счетчик Команд");
		SK.setWidth(187);
		
		RegisterUI Acc = new RegisterUI(regfact.getAccumulator(), 335, 378, 400, 396, "Аккумулятор");
		
		RegisterUI C = new RegisterUI(flagfact.getC(), 305, 378, "C");
		C.setWidth(30);
		
		RegisterUI Registers[] = {RD, RK, RA, SK, Acc, C};
		
		return Registers;
	}
	
	public RegisterUI[] createIORegisters()					//Создание регистров для режима "Работа с ВУ"
	{
		RegisterUI RD = new RegisterUI(regfact.getDataRegister(), 205, 70, 243, 88, "РД");
		RD.setContentPosition((int)(RD.getX()+24), (int)(RD.getY()+42));
		RD.setStyle(true);
		
		RegisterUI RA = new RegisterUI(regfact.getAddressRegister(), 205, 140, 243, 158, "РА");
		RA.setContentPosition((int)(RA.getX()+31), (int)(RA.getY()+42));
		RA.setStyle(true);
		
		RegisterUI SK = new RegisterUI(regfact.getInstructionPointer(), 205, 210, 243, 228, "СК");
		SK.setContentPosition((int)(SK.getX()+31), (int)(SK.getY()+42));
		SK.setStyle(true);
		
		RegisterUI RK = new RegisterUI(regfact.getCommandRegister(), 205, 280, 243, 298, "РК");
		RK.setContentPosition((int)(RK.getX()+24), (int)(RK.getY()+42));
		RK.setStyle(true);
		
		RegisterUI Acc = new RegisterUI(regfact.getAccumulator(), 205, 350, 236, 368, "Акк");
		Acc.setContentPosition((int)(Acc.getX()+24), (int)(Acc.getY()+42));
		Acc.setStyle(true);
		
		RegisterUI C = new RegisterUI(flagfact.getC(), 175, 350, "C");
		C.setWidth(30);
		
		RegisterUI OutDev = new RegisterUI(devfact.getInternalDevice(1).getDataRegister(), 400, 310, 445, 328, "ВУ 1");
		OutDev.setWidth(135);
		
		RegisterUI registers[] = {RD, RA, SK, RK, Acc, C, OutDev};
		
		return registers;
	}
	
	public RegisterUI[] createMPURegisters()						//Создание регистров для режима "Работа с МПУ"
	{
		RegisterUI RD = new RegisterUI(regfact.getDataRegister(), 165, 85, 203, 103, "РД");
		RD.setContentPosition((int)(RD.getX()+24), (int)(RD.getY()+42));
		RD.setStyle(true);
		
		RegisterUI RK = new RegisterUI(regfact.getCommandRegister(), 315, 20, 353, 38, "РК");
		RK.setContentPosition((int)(RK.getX()+24), (int)(RK.getY()+42));
		RK.setStyle(true);
		
		RegisterUI RA = new RegisterUI(regfact.getAddressRegister(), 165, 20, 203, 38, "РА");
		RA.setContentPosition((int)(RA.getX()+31), (int)(RA.getY()+42));
		RA.setStyle(true);
		
		RegisterUI SK = new RegisterUI(regfact.getInstructionPointer(), 315, 85, 353, 103, "СК");
		SK.setContentPosition((int)(SK.getX()+31), (int)(SK.getY()+42));
		SK.setStyle(true);
		
		RegisterUI Acc = new RegisterUI(regfact.getAccumulator(), 240, 255, 271, 273, "Акк");
		Acc.setContentPosition((int)(Acc.getX()+24), (int)(Acc.getY()+42));
		Acc.setStyle(true);
		
		RegisterUI BR = new RegisterUI(regfact.getBufferRegister(), 240, 185, 278, 206, "БР");
		BR.setContentPosition((int)(BR.getX()+18), (int)(BR.getY()+42));
		BR.setStyle(true);
		
		RegisterUI SC = new RegisterUI(regfact.getStateCounter(), 184, 338, 70, 190, 356, "Регистр Состояния");
		SC.setContentPosition((int)(SC.getX()+24), (int)(SC.getY()+42));
		
		RegisterUI MCR = new RegisterUI(regfact.getMicroCommandRegister(), 430, 85, 449, 103, "Регистр Микрокоманд");
		
		RegisterUI MIP = new RegisterUI(regfact.getMicroInstructionPointer(), 528, 358, 535, 376, "Счетчик МК");
		MIP.setWidth(135);
		
		RegisterUI registers[] = {RD, RK, RA, SK, Acc, BR, SC, MCR, MIP};
		
		return registers;		
	}
	
	public FlagUI[] createIOFlags()
	{
            FlagUI OutFlag = new FlagUI(devfact.getInternalDevice(1).getStateFlag(), 450, 180);
            FlagUI InpFlag1 = new FlagUI(devfact.getInternalDevice(2).getStateFlag(), 600, 180);
            FlagUI InpFlag2 = new FlagUI(devfact.getInternalDevice(3).getStateFlag(), 750, 180);

            FlagUI[] flags = {OutFlag, InpFlag1, InpFlag2};
		
            return flags;
	}
	
	public InputRegisterUI createKeyRegister()
	{
            InputRegisterUI key_register = new InputRegisterUI(regfact.getInputRegister(), 1, 460, 60, 32, 478, "Клавишный Регистр");
            key_register.setActive(true);
		
	return key_register;
	}
	
	public InputRegisterUI createInputRegister1()
	{
            InputRegisterUI InpDev1 = new InputRegisterUI(devfact.getInternalDevice(2).getDataRegister(), 550, 310, 60, 595, 328, "ВУ 2");
            InpDev1.setWidth(135);
		
	return InpDev1;
	}
	
	public InputRegisterUI createInputRegister2()
	{
            InputRegisterUI InpDev2 = new InputRegisterUI(devfact.getInternalDevice(3).getDataRegister(), 700, 310, 60, 745, 328, "ВУ 3");
            InpDev2.setWidth(135);

            return InpDev2;
	}
	
	public ManagerDeviceUI createManagerDevice()					//Создание устройства управления
	{
            return new ManagerDeviceUI(flagfact, 620, 210);
	}
	
	public MemoryUI createСlassicMemory()							//Создание памяти
	{
            IChannel[] channels = {channfact.ReadFromMem(), channfact.WriteToMem()};

            return new MemoryUI (memory, regfact.getInstructionPointer(), channels, 12, 1, 1, 150, 431, 33, 23, "Память");
	}
	
	public MemoryUI createMCMemory()							//Создание памяти МК
	{
            IChannel[] channels = {channfact.MicroCommandToRMC()};

            return new MemoryUI (micro_memory, regfact.getMicroInstructionPointer(), channels, 8, 711, 1, 135, 431, 715, 23, "Память МК");
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
		ChannelUI RAtoMEM = new ChannelUI(channfact.AddressRegToMem(), mass1);
		
		//Канал от Регистра Данных к Памяти
		int[][] mass2 = {{220, 140}, 
			   			{220, 150},
			   			{220, 150},
			   			{160, 150}};	
		ChannelUI RDtoMEM = new ChannelUI(channfact.WriteToMem(), mass2);
		
		//Канал от Памяти в Регистр Данных
		int[][] mass3 = {{156, 68},
	   					{220, 68},
	   					{220, 68}, 
	   		   			{220, 76}};
		ChannelUI MEMtoRD = new ChannelUI(channfact.ReadFromMem(), mass3);
		
		//Канал из Счетчика Команд в АЛУ
		int[][] mass4 = {{336, 177},
	   					{290, 177},
	   					{290, 177}, 
	   		   			{290, 215},
	   		   			{290, 215}, 
	   		   			{380, 215},
	   		   			{380, 215},
	   		   			{380, 226}};
		ChannelUI RDtoSK = new ChannelUI(channfact.FromIP(), mass4);
		
		//Канал из Регистра Данных в АЛУ
		int[][] mass5 = {{290, 140},
	   					{290, 215},
	   					{290, 215}, 
	   		   			{380, 215},
	   		   			{380, 215},
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
	   		   			{165, 215},
	   		   			{165, 215},
	   		   			{210, 215},
	   		   			{210, 215},
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
		
		//Канал из Регистра Команд в АЛУ
		int[][] mass12 = {{603, 165},
	   					{603, 215},
						{603, 215},
						{380, 215},
	   		   			{380, 215},
	   		   			{380, 226}};
		ChannelUI RKtoALU = new ChannelUI(channfact.FromCR(), mass12);
		
		ChannelUI channels[] = {RAtoMEM, RDtoMEM, MEMtoRD, RDtoSK, RDtoALU, ALUtoAcc, ALUtoSK, ALUtoRD, ALUtoRA, AcctoALU, ALUtoRK, RKtoALU};
		
		return channels;
	}
	
	public ChannelUI[] createIOChannels()
	{
		Channel empty_channel = new Channel(null, null);			

		int [][] mass1 = {{369, 160},
						{468, 160},
						{468, 160},
						{468, 190}};
		ChannelUI request1 = new ChannelUI(devfact.getInternalDevice(1).getInterruptionRequestChannel(), mass1);
		
		int [][] mass2 = {{369, 160},
						{618, 160},
						{618, 160},
						{618, 190}};
		ChannelUI request2 = new ChannelUI(devfact.getInternalDevice(2).getInterruptionRequestChannel(), mass2);
		
		int [][] mass3 = {{369, 160},
						{768, 160},
						{768, 160},
						{768, 190}};
		ChannelUI request3 = new ChannelUI(devfact.getInternalDevice(3).getInterruptionRequestChannel(), mass3);
		
		int [][] mass4 = {{369, 235},
						{468, 235},
						{468, 235},
						{468, 190}};
		ChannelUI state1 = new ChannelUI(empty_channel, mass4);
		
		int [][] mass5 = {{369, 235},
						{618, 235},
						{618, 235},
						{618, 190}};
		ChannelUI state2 = new ChannelUI(empty_channel, mass5);
		
		int [][] mass6 = {{369, 235},
						{768, 235},
						{768, 235},
						{768, 190}};
		ChannelUI state3 = new ChannelUI(empty_channel, mass6);
		
		int [][] mass7 = {{369, 288},
						{618, 288},
						{618, 288},
						{618, 310}};
		ChannelUI input2 = new ChannelUI(empty_channel, mass7);
		
		int [][] mass8 = {{369, 288},
						{768, 288},
						{768, 288},
						{768, 310}};
		ChannelUI input3 = new ChannelUI(empty_channel, mass8);
		
		int [][] mass9 = {{369, 392},
						{468, 392},
						{468, 392},
						{468, 360}};
		ChannelUI output1 = new ChannelUI(empty_channel, mass9);
		
		int [][] mass10 = {{369, 392},
						{768, 392},
						{768, 392},
						{768, 360}};
		ChannelUI output3 = new ChannelUI(empty_channel, mass10);
		
		int [][] mass11 = {{369, 27},
						{468, 27},
						{468, 27},
						{468, 50}};
		ChannelUI todec1 = new ChannelUI(empty_channel, mass11);
		
		int [][] mass12 = {{369, 27},
						{618, 27},
						{618, 27},
						{618, 50}};
		ChannelUI todec2 = new ChannelUI(empty_channel, mass12);
		
		int [][] mass13 = {{369, 27},
						{768, 27},
						{768, 27},
						{768, 50}};
		ChannelUI todec3 = new ChannelUI(empty_channel, mass13);
		
		int [][] mass14 = {{369, 108},
						{468, 108},
						{468, 108},
						{468, 85}};
		ChannelUI fromdec1 = new ChannelUI(empty_channel, mass14);
		
		int [][] mass15 = {{369, 108},
						{618, 108},
						{618, 108},
						{618, 58}};
		ChannelUI fromdec2 = new ChannelUI(empty_channel, mass15);
		
		int [][] mass16 = {{369, 108},
						{768, 108},
						{768, 108},
						{768, 85}};
		ChannelUI fromdec3 = new ChannelUI(empty_channel, mass16);
		
		ChannelUI channels[] = {request1, request2, request3, state1, state2, state3, input2, input3, output1, output3, todec1, todec2, todec3, fromdec1, fromdec2, fromdec3};
		for (int i = 0; i < channels.length; i++)
			channels[i].disableArrow();
		
		return channels;
	}
	
	public ChannelUI[] createMPUChannels()						//Создание каналов для режима "Работа с МПУ"
	{
		Channel empty_channel = new Channel(null, null);			
		
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

        public JButton[] createDbuttons()
        {
            JButton adress = new JButton("Ввод адреса(F4/4)");
            adress.setBounds(1, 525, 153, 30);
            adress.setFocusable(false);
            adress.setForeground(Color.BLACK);
            adress.setFont(new Font("Courier New", Font.PLAIN, 12));

            JButton record = new JButton("Запись(F5/5)");
            record.setBounds(157, 525, 118, 30);
            record.setFocusable(false);
            record.setForeground(Color.BLACK);
            record.setFont(new Font("Courier New", Font.PLAIN, 12));

            JButton read = new JButton("Чтение(F6/6)");
            read.setBounds(278, 525, 118, 30);
            read.setFocusable(false);
            read.setForeground(Color.BLACK);
            read.setFont(new Font("Courier New", Font.PLAIN, 12));

            JButton start = new JButton("Пуск(F7/7)");
            start.setBounds(399, 525, 104, 30);
            start.setFocusable(false);
            start.setForeground(Color.BLACK);
            start.setFont(new Font("Courier New", Font.PLAIN, 12));

            JButton continuebasepc = new JButton("Продолжение(F8/8)");
            continuebasepc.setBounds(506, 525, 153, 30);
            continuebasepc.setFocusable(false);
            continuebasepc.setForeground(Color.BLACK);
            continuebasepc.setFont(new Font("Courier New", Font.PLAIN, 12));

            JButton[] buttons =  {adress, record, read, start, continuebasepc};

            return buttons;
        }

        public JButton createWorkButton()
	{
		JButton work = new JButton("Остановка(F9/9)");
		work.setBounds(662, 523, 185, 34);
		work.setHorizontalTextPosition(2);
		work.setFocusable(false);
		work.setForeground(Color.BLACK);
		work.setFont(new Font("Courier New", Font.PLAIN, 12));

		return work;
	}

        public JButton[] createFlagButtons()
        {
                JButton flag1 = new JButton("Готовность ВУ1(F1/1)");
                flag1.setBounds(619, 439, 175, 25);
		flag1.setHorizontalTextPosition(2);
		flag1.setFocusable(false);
		flag1.setForeground(Color.BLACK);
		flag1.setFont(new Font("Courier New", Font.PLAIN, 12));

                JButton flag2 = new JButton("Готовность ВУ2(F2/2)");
                flag2.setBounds(619, 465, 175, 25);
		flag2.setHorizontalTextPosition(2);
		flag2.setFocusable(false);
		flag2.setForeground(Color.BLACK);
		flag2.setFont(new Font("Courier New", Font.PLAIN, 12));

                JButton flag3 = new JButton("Готовность ВУ3(F3/3)");
                flag3.setBounds(619, 491, 175, 25);
		flag3.setHorizontalTextPosition(2);
		flag3.setFocusable(false);
		flag3.setForeground(Color.BLACK);
		flag3.setFont(new Font("Courier New", Font.PLAIN, 12));

                JButton[] flags = {flag1, flag2, flag3};

                return flags;
        }
        
	private RegisterFactory 	regfact;					//Регистры
	private FlagFactory 		flagfact;					//Флаги
	private ChannelFactory		channfact;					//Каналы
	private DeviceFactory		devfact;					//Устройства ввода-вывода
	private Memory			memory;						//Память
	private Memory			micro_memory;				//Память микрокомманд
}
