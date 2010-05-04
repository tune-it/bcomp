
public class EUIBasePCFabric 
{
	public EUIBasePCFabric()
	{
	}
	
	public EUIRegister[] CreateRegisters()
	{
		ERegister	RD = new ERegister();
		EUIRegister IRD = new EUIRegister(RD, 180, 85, 50, "Регистр Данных");
		
		ERegister RK = new ERegister();
		EUIRegister IRK = new EUIRegister(RK, 600, 130, 50, "Регистр Команд");
		
		ERegister RA = new ERegister(11);
		EUIRegister IRA = new EUIRegister(RA, 340, 20, 50, "Регистр Адреса");
		IRA.SetWidth(187);
		
		
		ERegister SK = new ERegister(11);
		EUIRegister ISK = new EUIRegister(SK, 340, 150, 50, "Счетчик Команд");
		ISK.SetWidth(187);
		
		ERegister Acc = new ERegister();
		EUIRegister IAcc = new EUIRegister(Acc, 350, 400, 50, "Аккумулятор");
		
		EFlag C = new EFlag();
		EUIRegister IC = new EUIRegister(C, 320, 400, 50, "C");
		IC.SetWidth(30);
		
		EUIRegister Registers[] = {IRD, IRK, IRA, ISK, IAcc, IC};
		
		return Registers;
	}
	
	public EUIAlu CreateAlu()
	{
		EUIAlu Alu = new EUIAlu(180,245,"АЛУ");
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
	   					{290, 220},
	   					{290, 220}, 
	   		   			{400, 220},
	   		   			{400, 220},
	   		   			{400, 236}};
		EUIChannel IRDtoALU = new EUIChannel(RDtoALU, mass5);
		
		//Канал из АЛУ в Акк
		EChannel ALUtoAcc = new EChannel(BR, Acc);
		int[][] mass6 = {{312, 350},
	   					{312, 373},
	   					{312, 373}, 
	   		   			{565, 373},
	   		   			{565, 373},
	   		   			{565, 391}};
		EUIChannel IALUtoAcc = new EUIChannel(ALUtoAcc, mass6);
		
		//Канал из АЛУ в Cчетчик Команд
		EChannel ALUtoSK = new EChannel(BR, SK);
		int[][] mass7 = {{312, 350},
	   					{312, 373},
	   					{312, 373}, 
	   		   			{565, 373},
	   		   			{565, 373},
	   		   			{565, 177},
	   		   			{565, 177},
	   		   			{536, 177}};
		EUIChannel IALUtoSK = new EUIChannel(ALUtoSK, mass7);
		
		//Канал из АЛУ в Регистр Данных
		EChannel ALUtoRD = new EChannel(BR, RD);
		int[][] mass8 = {{312, 350},
	   					{312, 373},
	   					{312, 373}, 
	   		   			{565, 373},
	   		   			{565, 373},
	   		   			{565, 110},
	   		   			{565, 110},
	   		   			{440, 110}};
		EUIChannel IALUtoRD = new EUIChannel(ALUtoRD, mass8);
		
		//Канал из АЛУ в Регистр Адреса
		EChannel ALUtoRA= new EChannel(BR, RA);
		int[][] mass9 = {{312, 350},
	   					{312, 373},
	   					{312, 373}, 
	   		   			{565, 373},
	   		   			{565, 373},
	   		   			{565, 45},
	   		   			{565, 45},
	   		   			{536, 45}};
		EUIChannel IALUtoRA = new EUIChannel(ALUtoRA, mass9);
		
		//Канал из Акк в АЛУ
		EChannel AcctoALU = new EChannel(Acc, BR);
		int[][] mass10 = {{316, 425},
	   					{165, 425},
	   					{165, 425}, 
	   		   			{165, 220},
	   		   			{165, 220},
	   		   			{220, 220},
	   		   			{220, 220},
	   		   			{220, 236}};
		EUIChannel IAcctoALU = new EUIChannel(AcctoALU, mass10);
		
		//Канал из АЛУ в Регистр Команд
		EChannel ALUtoRK= new EChannel(BR, RK);
		int[][] mass11 = {{312, 350},
	   					{312, 373},
	   					{312, 373}, 
	   		   			{565, 373},
	   		   			{565, 373},
	   		   			{565, 78},
	   		   			{565, 78},
	   		   			{725, 78},
	   		   			{725, 78},
	   		   			{725, 121}};
		EUIChannel IALUtoRK = new EUIChannel(ALUtoRK, mass11);
		
		
		EUIChannel Channels[] = {IRAtoMEM, IRDtoMEM, IMEMtoRD, IRDtoSK, IRDtoALU, IALUtoAcc, IALUtoSK, IALUtoRD, IALUtoRA, IAcctoALU, IALUtoRK};
		return Channels;
	}
	
}
