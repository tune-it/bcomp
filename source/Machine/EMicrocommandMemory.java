package Machine;

/*-----------------------------------------------------------------------------
	Память микропрограмм.
-----------------------------------------------------------------------------*/
public class EMicrocommandMemory extends EMemory {

	public EMicrocommandMemory(ERegisterFactory factory)
	{
		super(factory.MicroInstructionPointer(), 256, 16);
		
		int[] programm = new int[256];
		int i = 1;
		
		
		// Цикл выборки команды
		programm[i++]=0x0300;
		programm[i++]=0x4001;
		programm[i++]=0x0311;
		programm[i++]=0x4004;
		programm[i++]=0x0100;
		programm[i++]=0x4003;
			// Определение типа команды
		programm[i++]=0xAF0C;
		programm[i++]=0xAE0C;
		programm[i++]=0xAD0C;
		programm[i++]=0xEC5E;
		programm[i++]=0x838E;
			// Определение вида адресации
		programm[i++]=0xAB1D;
		
		// Цикл выборки операнда
		programm[i++]=0x0100;
		programm[i++]=0x4001;
		programm[i++]=0x0001;
		programm[i++]=0xA31D;
		programm[i++]=0xE41D;
		programm[i++]=0xE51D;
		programm[i++]=0xE61D;
		programm[i++]=0xE71D;
		programm[i++]=0xE81D;
		programm[i++]=0xE91D;
		programm[i++]=0xEA1D;
		programm[i++]=0x0110;
		programm[i++]=0x4002;
		programm[i++]=0x0002;
		programm[i++]=0x0140;
		programm[i++]=0x4002;
		
		// Цикл исполнения адресных команд
			// Декодирование адресных команд
		programm[i++]=0xEF2D;
		programm[i++]=0x0100;
		programm[i++]=0x4001;
		programm[i++]=0xEE27;
		programm[i++]=0xAD24;
		programm[i++]=0xAC57;
		programm[i++]=0x8338;
		programm[i++]=0x0001;
		programm[i++]=0xAC50;
		programm[i++]=0x8335;
		programm[i++]=0x0001;
		programm[i++]=0xAD2B;
		programm[i++]=0xAC43;
		programm[i++]=0x83B0;
		programm[i++]=0xAC3C;
		programm[i++]=0x833F;
		programm[i++]=0xAE30;
		programm[i++]=0xAC47;
		programm[i++]=0x83D0;
		programm[i++]=0xAD33;
		programm[i++]=0xAC4C;
		programm[i++]=0x834E;
		programm[i++]=0xAC46;
		programm[i++]=0x834A;
			// Исполнение адресных команд
		programm[i++]=0x1120;
		programm[i++]=0x4035;
		programm[i++]=0x838F;
		programm[i++]=0x1000;
		programm[i++]=0x4002;
		programm[i++]=0x0002;
		programm[i++]=0x838F;
		programm[i++]=0x1100;
		programm[i++]=0x4075;
		programm[i++]=0x838F;
		programm[i++]=0x803C;
		programm[i++]=0x1110;
		programm[i++]=0x4075;
		programm[i++]=0x838F;
		programm[i++]=0x1190;
		programm[i++]=0x4075;
		programm[i++]=0x838F;
		programm[i++]=0x808F;
		programm[i++]=0x0100;
		programm[i++]=0x4004;
		programm[i++]=0x838F;
		programm[i++]=0xC28F;
		programm[i++]=0x8347;
		programm[i++]=0x828F;
		programm[i++]=0x8347;
		programm[i++]=0x818F;
		programm[i++]=0x8347;
		programm[i++]=0x0110;
		programm[i++]=0x4002;
		programm[i++]=0x0002;
		programm[i++]=0xDF8F;
		programm[i++]=0x0310;
		programm[i++]=0x4004;
		programm[i++]=0x838F;
		programm[i++]=0x0110;
		programm[i++]=0x4003;
		programm[i++]=0x0300;
		programm[i++]=0x4002;
		programm[i++]=0x0202;
		programm[i++]=0x4004;
		programm[i++]=0x838F;
		
		// Продолжение цикла выборки команды
		// Декодирование и исполнение безадресных команд
		programm[i++]=0xAB61;
		programm[i++]=0xAA6C;
		programm[i++]=0x83E0;
		programm[i++]=0xAA67;
		programm[i++]=0xA965;
		programm[i++]=0xA882;
		programm[i++]=0x8385;
		programm[i++]=0xA87B;
		programm[i++]=0x837E;
		programm[i++]=0xA96A;
		programm[i++]=0xA876;
		programm[i++]=0x8379;
		programm[i++]=0xA888;
		programm[i++]=0x8387;
		programm[i++]=0xA96F;
		programm[i++]=0xA88A;
		programm[i++]=0x838C;
		programm[i++]=0xA873;
		programm[i++]=0x1080;
		programm[i++]=0x4075;
		programm[i++]=0x838F;
		programm[i++]=0x1010;
		programm[i++]=0x4075;
		programm[i++]=0x838F;
		programm[i++]=0x0020;
		programm[i++]=0x4035;
		programm[i++]=0x838F;
		programm[i++]=0x4080;
		programm[i++]=0x838F;
		programm[i++]=0x1040;
		programm[i++]=0x4035;
		programm[i++]=0x838F;
		programm[i++]=0x8080;
		programm[i++]=0x8379;
		programm[i++]=0x40C0;
		programm[i++]=0x838F;
		programm[i++]=0x0008;
		programm[i++]=0x4075;
		programm[i++]=0x838F;
		programm[i++]=0x0004;
		programm[i++]=0x4075;
		programm[i++]=0x838F;
		programm[i++]=0x4008;
		programm[i++]=0x8301;
		programm[i++]=0x4800;
		programm[i++]=0x8301;
		programm[i++]=0x4400;
		programm[i++]=0x8301;
		
		// Продолжение выборки команды
		// Декодирование и исполнение команд ввода-вывода
		programm[i++]=0x4100;
			// Цикл прерывания
		programm[i++]=0x8788;
		programm[i++]=0x8501;
		programm[i++]=0x0020;
		programm[i++]=0x4001;
		programm[i++]=0x0300;
		programm[i++]=0x4002;
		programm[i++]=0x0012;
		programm[i++]=0x4004;
		programm[i++]=0x4400;
		programm[i++]=0x8301;
		
		// Пультовые операции
			// Ввод адреса
		programm[i++]=0x3000;
		programm[i++]=0x4004;
		programm[i++]=0x838F;
			// Чтение
		programm[i++]=0x0300;
		programm[i++]=0x4001;
		programm[i++]=0x0311;
		programm[i++]=0x4004;
		programm[i++]=0x838F;
			// Запись
		programm[i++]=0x0300;
		programm[i++]=0x4001;
		programm[i++]=0x3000;
		programm[i++]=0x4002;
		programm[i++]=0x0312;
		programm[i++]=0x4004;
		programm[i++]=0x838F;
			// Пуск
		programm[i++]=0x0020;
		programm[i++]=0x4077;
		programm[i++]=0x4200;
		programm[i++]=0x4400;
		programm[i++]=0x838F;
		
		for (int k = 0; k < programm.length; k++)
		{
			super.SetValue(programm[k], k);
		}
	}
}
