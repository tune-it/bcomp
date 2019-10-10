/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import static ru.ifmo.cs.bcomp.ControlSignal.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
class BaseMicroProgram /* extends MicroProgram */ {
	private static final MicroCommand[] MP = {
		new MicroCommand(),
		new MicroCommand("BEGIN", RDIP, LTOL, HTOH, WRAR, WRBR), // IP -> AR, BR
		new MicroCommand(RDBR, PLS1, LTOL, HTOH, WRIP, LOAD), // BR + 1 -> IP, MEM(AR) -> DR
		new MicroCommand(RDDR, LTOL, HTOH, WRCR), // DR -> CR
		new ControlMicroCommand("ADDRTYPE", 7, 1, RDCR, HTOL), // if CR(15) == 1 then GOTO CHKBR
		new ControlMicroCommand("ADDRTYPE", 6, 1, RDCR, HTOL), // if CR(14) == 1 then GOTO ADDRTYPE
		new ControlMicroCommand("ADDRTYPE", 5, 1, RDCR, HTOL), // if CR(13) == 1 then GOTO ADDRTYPE
		// if CR(12) == 0 then GOTO ADDRLESS
		// GOTO IO
		// CHKBR: if CR(14) == 0 then GOTO ADDRTYPE
		// if CR(13) == 0 then GOTO ADDRTYPE
		// if CR(12) == 1 then GOTO BRANCHES -- закончили выборку и частичное декодирование
		// ADDRTYPE: if CR(11) == 0 then GOTO LOADOPER -- адрес то уже в DR, муахахаха Type=0XXX
		// if CR(10) == 1 then GOTO OFFSET
		// CR -> AR
		// MEM(AR) -> DR 
		// if CR(9) == 1 then GOTO INCDEC
		// if CR(8) == 1 then GOTO космос -- зарезервированный тип адресации? -> Type=1001
		// GOTO LOADOPER -- Type= 1000
		// INCDEC: if CR(8) == 1 then GOTO PREDEC -- Type=101X
		// DR + 1 -> BR -- Type=1010
		// BR -> DR
		// DR - 1 -> BR, STOR
		// BR -> DR
		// GOTO LOADOPER
		// PREDEC: DR - 1 -> BR -- Type=1011
		// BR -> DR
		// STOR
		// GOTO LOADOPER
		// OFFSET: LTOL(CR) -> BR -- Type=11XX
		// if CR(9) == 1 then GOTO CHECKDIRECT -- Type=111X
		// if CR(8) == 1 then GOTO SPOFFSET -- Type=110X
		// BR + IP -> CR -- Type=1110
		// GOTO LOADOPER
		// CHECKDIRECT: if CR(8) == 0 then GOTO космос -- Type=1110
		// BR -> DR -- Type=1111
		// GOTO EXECUTE
		// SPOFFSET: BR + SP -> CR -- Type 1101 !!! Закончили выборку адреса
		// LOADOPER: if CR(15) == 0 then GOTO LOADWANTED !!! Выборка операнда?
		// if CR(14) == 1 then GOTO CMD11XX -- команды jump/call/st
		// LOADWANTED: DR -> AR
		// MEM(AR) -> DR
		// EXECUTE: -- Декодирование и цикл исполнения адресных команд кроме jump/call/st/FXXX
		// if CR(15) = 1 then GOTO CMD1XXX
		// if CR(14) = 1 then GOTO CMD01XX
		// 13th bit already checked
		// if CR(12) = 1 then GOTO OR
		// AND: DR & AC -> BR, N, Z
		//		GOTO MOVTOAC
		// OR: ~DR & ~AC -> BR
		//	~BR -> AC, N, Z
		//	GOTO INT
		// CMD01XX: if CR(13) = 1 then GOTO CMD011X
		// if CR(12) = 1 then GOTO ADC
		// ADD: DR + AC -> BR, C, N, Z, V
		// GOTO MOVTOAC
		// ADC: if C = 0 then GOTO ADD
		// DR + AC + 1 -> BR, C, N, Z, V
		// GOTO MOVTOAC
		// CMD011X: if CR(12) = 1 then GOTO CMP
		// SUB: ~DR + AC + 1 -> BR, C, N, Z, V !!! ACHTUNG !!! ACHTUNG !!! ACHTUNG !!!
		// MOVTOAC: BR -> AC
		// GOTO INT
		// 11XX already checked!!!
		// CMD1XXX: if CR(13) == 1 then GOTO CMD101X
		//	if CR(12) == 1 then GOTO космос CMD1001
		// LOOP: DR + ~0 -> BR
		//	BR -> DR
		//	DR -> MEM(AR)
		//	if DR(15) = 0 then GOTO INT
		// SKIPCMD: IP + 1 -> BR
		//	BR -> IP
		//	GOTO INT
		// CMD101X: if CR(12) == 1 then GOTO SWAM
		// LD: DR -> AC
		//	GOTO INT
		// SWAM: DR -> BR
		//	AC -> DR
		//	DR -> MEM(AR), BR -> AC
		//	GOTO INT
		// CMD11XX: -- Цикл исполнения jump/call/st
		//	if CR(13) = 1 then GOTO ST
		//	if CR(12) = 1 then GOTO CALL
		// JUMP: DR -> IP
		//	GOTO INT
		// CALL: DR -> BR
		//	IP -> DR
		//  BR -> IP
		// PUSHVALUE: SP -> AR
		//	DR -> MEM(AR), SP - 1 -> BR
		//	BR -> SP
		//	GOTO INT
		// ST: AC -> DR
		//	DR -> MEM(AR)
		//	GOTO INT
		// BRANCHES: -- Команды с "коротким" переходом
		//	if CR(11) = 1 then GOTO BR1XXX
		//	if CR(10) = 1 then GOTO BR01XX
		//	if CR(9) = 1 then GOTO BR001X
		//	if CR(8) = 1 then GOTO BNE
		// BEQ: if Z = 1 then GOTO INT
		// BR: SEXT(CR) -> BR
		//	BR + IP -> DR
		//	DR -> IP
		//	GOTO INT
		// BNE: 
		// ...
		// ADDRLESS: -- безадресные команды
		// ...
		// IO: IO
		// ...
		// INT:
		// ...
		// SETIP:
		// WRITE:
		// READ:
		// START:
	};

/*
	static final String NAME = "base";

	private static final String[][] mp = {
		{null, "0000", null},
		// Цикл выборки команды
		{"BEGIN", "0300", null},
		{null, "4001", null},
		{null, "0311", null},
		{null, "4004", null},
		{null, "0100", null},
		{null, "4003", null},
		// Определение типа команды
		{null, "AF00","ADDRCHK"},
		{null, "AE00", "ADDRCHK"},
		{null, "AD00", "ADDRCHK"},
		{null, "EC00", "BAD"},
		{null, "8300", "IO"},
		// Определение вида адресации
		{"ADDRCHK", "AB00", "EXEC"},
		// Цикл выборки адреса операнда
		{"ADDRGET", "0100", null},
		{null, "4001", null},
		{null, "0001", null},
		{null, "A300", "EXEC"},
		{null, "E400", "EXEC"},
		{null, "E500", "EXEC"},
		{null, "E600", "EXEC"},
		{null, "E700", "EXEC"},
		{null, "E800", "EXEC"},
		{null, "E900", "EXEC"},
		{null, "EA00", "EXEC"},
		{null, "0110", null},
		{null, "4002", null},
		{null, "0002", null},
		{null, "0140", null},
		{null, "4002", null},
		// Цикл исполнения адресных команд
		// Декодирование адресных команд
		{"EXEC", "EF00", "PRX"},
		{null, "0100", null},
		{null, "4001", null},
		{null, "EE00", "ARF"},
		{null, "AD00", "A1"},
		{null, "AC00", "JSR"},
		{null, "8300", "MOV"},
		{"A1", "0001", null},
		{null, "AC00", "ISZ"},
		{null, "8300", "AND"},
		{"ARF", "0001", null},
		{null, "AD00", "SUM"},
		{null, "AC00", "SUB"},
		{null, "83B0", null},
		{"SUM", "AC00", "ADD"},
		{null, "8300", "ADC"},
		{"PRX", "AE00", "UPX"},
		{null, "AC00", "BR"},
		{null, "83D0", null},
		{"UPX", "AD00", "P1"},
		{null, "AC00", "BMI"},
		{null, "8300", "BEQ"},
		{"P1", "AC00", "BCS"},
		{null, "8300", "BPL"},
		// Исполнение адресных команд
		{"AND", "1120", null},
		{null, "4035", null},
		{null, "8300", "INTR"},
		{"MOV", "1000", null},
		{null, "4002", null},
		{null, "0002", null},
		{null, "8300", "INTR"},
		{"ADD", "1100", null},
		{null, "4075", null},
		{null, "8300", "INTR"},
		{"ADC", "8000", "ADD"},
		{null, "1110", null},
		{null, "4075", null},
		{null, "8300", "INTR"},
		{"SUB", "1190", null},
		{null, "4075", null},
		{null, "8300", "INTR"},
		{"BCS", "8000", "INTR"},
		{"BR", "0100", null},
		{null, "4004", null},
		{null, "8300", "INTR"},
		{"BPL", "C200", "INTR"},
		{null, "8300", "BR"},
		{"BMI", "8200", "INTR"},
		{null, "8300", "BR"},
		{"BEQ", "8100", "INTR"},
		{null, "8300", "BR"},
		{"ISZ", "0110", null},
		{null, "4002", null},
		{null, "0002", null},
		{null, "DF00", "INTR"},
		{"SKPCMD", "0310", null},
		{null, "4004", null},
		{null, "8300", "INTR"},
		{"JSR", "0110", null},
		{null, "4003", null},
		{null, "0300", null},
		{null, "4002", null},
		{null, "0202", null},
		{null, "4004", null},
		{null, "8300", "INTR"},
		// Декодирование и исполнение безадресных команд
		{"BAD","AB00", "B0"},
		{null, "AA00", "B1"},
		{null, "83E0", null},
		{"B0", "AA00", "B2"},
		{null, "A900", "B3"},
		{null, "A800", "ROL"},
		{null, "8300", "ROR"},
		{"B3", "A800", "CMA"},
		{null, "8300", "CMC"},
		{"B2", "A900", "B4"},
		{null, "A800", "CLA"},
		{null, "8300", "CLC"},
		{"B4", "A800", "HLT"},
		{null, "8300", "NOP"},
		{"B1", "A900", "B5"},
		{null, "A800", "EI"},
		{null, "8300", "DI"},
		{"B5", "A800", "INC"},
		{"DEC", "1080", null},
		{null, "4075", null},
		{null, "8300", "INTR"},
		{"INC", "1010", null},
		{null, "4075", null},
		{null, "8300", "INTR"},
		{"CLA", "0020", null},
		{null, "4035", null},
		{null, "8300", "INTR"},
		{"CLC", "4080", null},
		{null, "8300", "INTR"},
		{"CMA", "1040", null},
		{null, "4035", null},
		{null, "8300", "INTR"},
		{"CMC", "8000", "B6"},
		{null, "8300", "CLC"},
		{"B6", "40C0", null},
		{null, "8300", "INTR"},
		{"ROL", "0008", null},
		{null, "4075", null},
		{null, "8300", "INTR"},
		{"ROR", "0004", null},
		{null, "4075", null},
		{"NOP", "8300", "INTR"},
		{"HLT", "4008", null},
		{"STP", "8300", "BEGIN"},
		{"EI", "4800", null},
		{null, "8300", "BEGIN"},
		{"DI", "4400", null},
		{null, "8300", "BEGIN"},
		// Исполнение команд ввода-вывода
		{"IO", "4100", null},
		{"TSF", "C600", "SKPCMD"},
		// Цикл прерывания
		{"INTR", "8700", "HLT"},
		{null, "8500", "BEGIN"},
		{null, "0020", null},
		{null, "4001", null},
		{null, "0300", null},
		{null, "4002", null},
		{null, "0012", null},
		{null, "4004", null},
		{null, "4400", null},
		{null, "8300", "BEGIN"},
		// Пультовые операции
		// Ввод адреса
		{"ADDR", "3000", null},
		{null, "4004", null},
		{null, "8300", "HLT"},
		// Чтение
		{"READ", "0300", null},
		{null, "4001", null},
		{null, "0311", null},
		{null, "4004", null},
		{null, "8300", "HLT"},
		// Запись
		{"WRITE", "0300", null},
		{null, "4001", null},
		{null, "3000", null},
		{null, "4002", null},
		{null, "0312", null},
		{null, "4004", null},
		{null, "8300", "HLT"},
		// Пуск
		{"START", "0020", null},
		{null, "4075", null},
		{null, "4200", null},
		{null, "4400", null},
		{null, "8300", "INTR"},
		// Продолжение выполнения нереализованных команд
		{"EXECCNT", "0000", null}
	};
*/
	BaseMicroProgram() {
		//super("исходная", BaseInstrSet.instructions, mp);
	}
}
