
public class EUIBasePCFabric 
{
	public EUIBasePCFabric(ERegisterFactory register_factory)
	{
		regfact = register_factory;
	}
	
	public EUIRegister[] CreateBinRegisters()
	{
		EUIRegister RD = new EUIRegister(regfact.GetDataRegister(), 180, 85, "Регистр Данных");
		
		EUIRegister RK = new EUIRegister(regfact.GetCommandRegister(), 580, 110, "Регистр Команд");
		
		EUIRegister RA = new EUIRegister(regfact.GetAdressRegister(), 340, 20, "Регистр Адреса");
		RA.SetWidth(187);
		
		EUIRegister SK = new EUIRegister(regfact.GetInstructionPointer(), 340, 150, "Счетчик Команд");
		SK.SetWidth(187);
		
		EUIRegister Acc = new EUIRegister(regfact.GetAccumulator(), 335, 378, "Аккумулятор");
		
		EFlag C = new EFlag();
		EUIRegister IC = new EUIRegister(C, 305, 378, 50, "C");
		IC.SetWidth(30);
		
		EUIRegister Registers[] = {RD, RK, RA, SK, Acc, IC};
		
		return Registers;
	}
	
	public EUIMemory CreateMemory()
	{
		EUIMemory Imem = new EUIMemory (1, 1, 150, 431, "Память");
		return Imem;
	}
	
	public EUIAlu CreateAlu()
	{
		EUIAlu Alu = new EUIAlu(180,235,"АЛУ");
		return Alu;
	}
	
	public EUIChannel[] CreateChannels()							
	{
		ERegister MEM = new ERegister();
		ERegister RA = new ERegister(12);
		ERegister RD = new ERegister();
		ERegister SK = new ERegister(12);
		ERegister BR = new ERegister();
		ERegister Acc = new ERegister();
		ERegister RK = new ERegister();
		
		//Канал от Регистра Адреса к Памяти
		EChannel RAtoMEM = new EChannel(RA, MEM);		
		int[][] mass1 = {{336, 45}, 
			   			{160, 45}};	
		EUIChannel IRAtoMEM = new EUIChannel(RAtoMEM, mass1);
		
		//Канал от Регистра Данных к Памяти
		EChannel RDtoMEM = new EChannel(RD, MEM);	
		int[][] mass2 = {{220, 140}, 
			   			{220, 150},
			   			{220, 150},
			   			{160, 150}};	
		EUIChannel IRDtoMEM = new EUIChannel(RDtoMEM, mass2);
		
		//Канал от Памяти в Регистр Данных
		EChannel MEMtoRD = new EChannel(MEM, RD);
		int[][] mass3 = {{156, 71},
	   					{220, 71},
	   					{220, 71}, 
	   		   			{220, 81}};
		EUIChannel IMEMtoRD = new EUIChannel(MEMtoRD, mass3);
		IMEMtoRD.DisableArrow();
		
		//Канал из Регистра Данных в Счетчик Команд
		EChannel RDtoSK = new EChannel(RD, SK);
		int[][] mass4 = {{290, 140},
	   					{290, 177},
	   					{290, 177}, 
	   		   			{336, 177}};
		EUIChannel IRDtoSK = new EUIChannel(RDtoSK, mass4);
		IRDtoSK.DisableArrow();
		
		//Канал из Регистра Данных в АЛУ
		EChannel RDtoALU = new EChannel(RD, BR);
		int[][] mass5 = {{290, 140},
	   					{290, 210},
	   					{290, 210}, 
	   		   			{380, 210},
	   		   			{380, 210},
	   		   			{380, 226}};
		EUIChannel IRDtoALU = new EUIChannel(RDtoALU, mass5);
		
		//Канал из АЛУ в Акк
		EChannel ALUtoAcc = new EChannel(BR, Acc);
		int[][] mass6 = {{295, 330},
	   					{295, 353},
	   					{295, 353}, 
	   		   			{565, 353},
	   		   			{565, 353},
	   		   			{565, 369}};
		EUIChannel IALUtoAcc = new EUIChannel(ALUtoAcc, mass6);
		
		//Канал из АЛУ в Cчетчик Команд
		EChannel ALUtoSK = new EChannel(BR, SK);
		int[][] mass7 = {{295, 330},
						{295, 353},
						{295, 353}, 
   		   				{565, 353},
	   		   			{565, 353},
	   		   			{565, 177},
	   		   			{565, 177},
	   		   			{536, 177}};
		EUIChannel IALUtoSK = new EUIChannel(ALUtoSK, mass7);
		
		//Канал из АЛУ в Регистр Данных
		EChannel ALUtoRD = new EChannel(BR, RD);
		int[][] mass8 = {{295, 330},
						{295, 353},
						{295, 353}, 
   		   				{565, 353},
	   		   			{565, 353},
	   		   			{565, 110},
	   		   			{565, 110},
	   		   			{455, 110}};
		EUIChannel IALUtoRD = new EUIChannel(ALUtoRD, mass8);
		
		//Канал из АЛУ в Регистр Адреса
		EChannel ALUtoRA= new EChannel(BR, RA);
		int[][] mass9 = {{295, 330},
						{295, 353},
						{295, 353}, 
   		   				{565, 353},
	   		   			{565, 353},
	   		   			{565, 45},
	   		   			{565, 45},
	   		   			{536, 45}};
		EUIChannel IALUtoRA = new EUIChannel(ALUtoRA, mass9);
		
		//Канал из Акк в АЛУ
		EChannel AcctoALU = new EChannel(Acc, BR);
		int[][] mass10 = {{301, 403},
	   					{165, 403},
	   					{165, 403}, 
	   		   			{165, 210},
	   		   			{165, 210},
	   		   			{210, 210},
	   		   			{210, 210},
	   		   			{210, 226}};
		EUIChannel IAcctoALU = new EUIChannel(AcctoALU, mass10);
		
		//Канал из АЛУ в Регистр Команд
		EChannel ALUtoRK= new EChannel(BR, RK);
		int[][] mass11 = {{295, 330},
						{295, 353},
						{295, 353}, 
   		   				{565, 353},
	   		   			{565, 353},
	   		   			{565, 78},
	   		   			{565, 78},
	   		   			{725, 78},
	   		   			{725, 78},
	   		   			{725, 101}};
		EUIChannel IALUtoRK = new EUIChannel(ALUtoRK, mass11);
		
		
		EUIChannel Channels[] = {IRAtoMEM, IRDtoMEM, IMEMtoRD, IRDtoSK, IRDtoALU, IALUtoAcc, IALUtoSK, IALUtoRD, IALUtoRA, IAcctoALU, IALUtoRK};
		return Channels;
	}
	
	private ERegisterFactory regfact;
}
