
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
		
		ERegister RA = new ERegister(12);
		EUIRegister IRA = new EUIRegister(RA, 340, 20, 50, "Регистр Адреса");
		
		ERegister SK = new ERegister(12);
		EUIRegister ISK = new EUIRegister(SK, 340, 190, 50, "Счетчик Команд");

		ERegister Acc = new ERegister();
		EUIRegister IAcc = new EUIRegister(Acc, 350, 400, 50, "Аккумулятор");
		
		EFlag C = new EFlag();
		EUIRegister IC = new EUIRegister(C, 320, 400, 50, "C");
		IC.SetWidth(30);
		
		EUIRegister Registers[] = {IRD, IRK, IRA, ISK, IAcc, IC};
		
		return Registers;
	}
	
	public EUIChannel[] CreateChannels()
	{
		ERegister MEM = new ERegister();
		ERegister RA = new ERegister(12);
		ERegister RD = new ERegister();
		
		EChannel RAtoMEM = new EChannel(RA, MEM);		
		int[][] mass1 = {{340, 45}, 
			   			{160, 45}};	
		EUIChannel IRAtoMEM = new EUIChannel(RAtoMEM, mass1);
		
		EChannel RDtoMEM = new EChannel(RD, MEM);	
		int[][] mass2 = {{220, 120}, 
			   			{220, 130},
			   			{220, 130},
			   			{160, 130}};	
		EUIChannel IRDtoMEM = new EUIChannel(RDtoMEM, mass2);
		
		EUIChannel Channels[] = {IRAtoMEM, IRDtoMEM};
		
		
		
		return Channels;
	}
}
