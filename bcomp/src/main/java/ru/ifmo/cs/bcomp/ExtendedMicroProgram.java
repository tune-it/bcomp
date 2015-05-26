/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
class ExtendedMicroProgram extends MicroProgram {
	static final String NAME = "extended";

	private static final String[][] mp = {
		{null,		"0000",	null},		// Skip
		// Цикл выборки команды
		{"BEGIN",	"0300",	null},		// СК ==> БР
		{null,		"4001",	null},		// БР ==> РА
		{null,		"0311",	null},		// ОП(РА) ==> РД, СК + 1 ==> БР
		{null,		"4004",	null},		// БР ==> СК
		{null,		"0100",	null},		// РД ==> БР
		{null,		"4003",	null},		// БР ==> РК
		// Определение типа команды		//Дальше/Переход
		{null,		"EF00",	"ADDRCHK"},	// 0*** / 1***
		{null,		"EE00",	"ADDRCHK"},	// 00** / 01**
		{null,		"AD00",	"NONADDR"},	// 001* / 000*
		// Определение вида адресации
		{"ADDRCHK",	"AB00",	"EXEC"},

		// Цикл выборки адреса операнда
		{"ADDRGET",	"0100",	null},		// РД ==> БР
		{null,		"4001",	null},		// БР ==> РА
		{null,		"0001",	null},		// ОП(РА) ==> РД
		{null,		"9F00",	"EXEC"},
		{null,		"0110",	null},		// РД + 1 ==> БР
		{null,		"4002",	null},		// БР ==> РД
		{null,		"0142",	null},		// COM(0) + РД ==> БР, РД ==> ОП(РА)
		{null,		"4002",	null},		// БР ==> РД

		// Цикл исполнения адресных команд
		{"EXEC",	"EF00",	"8-F"},		// 0*** / 1***
		{null,		"EE00",	"4-7"},		// 001* / 01**
		{null,		"EC00",	"JMP"},		// 0010 / 0011

		// CALL 2
		{"CALL",	"0100",	null},		// РД ==> БР
		{null,		"4003",	null},		// БР ==> РК
		{"CALLSTR",	"0040",	null},		// COM(0) ==> БР
		{null,		"4001",	null},		// БР ==> РА
		{null,		"0001",	null},		// ОП(РА) ==> РД
		{null,		"0140",	null},		// COM(0) + РД ==> БР
		{null,		"4002",	null},		// БР ==> РД
		{null,		"0102",	null},		// РД ==> БР, РД ==> ОП(РА)
		{null,		"4001",	null},		// БР ==> РА
		{null,		"0300",	null},		// СК ==> БР
		{null,		"4002",	null},		// БР ==> РД
		{null,		"0202",	null},		// РК ==> БР, РД ==> ОП(РА)
		{null,		"4004",	null},		// БР ==> СК
		{null,		"8300",	"INTR"},

		// Команды перехода 01**
		{"4-7",		"ED00",	"6-7"},		// 010* / 011*
		{null,		"EC00",	"BPL"},		// 0100 / 0101
		// BCS 0100
		{"BCS",		"8000",	"INTR"},	// IF РС(0) = 0 THEN INTR
		// JMP 0011
		{"JMP",		"0100",	null},		// РД ==> БР
		{null,		"4004",	null},		// БР ==> СК
		{null,		"8300",	"INTR"},
		// BPL 0101
		{"BPL",		"C200",	"INTR"},	// IF РС(2) = 1 THEN INTR
		{null,		"8300",	"JMP"},		// GOTO JMP
		// 011*
		{"6-7",		"EC00",	"BEQ"},		// 0110 / 0111
		// BMI 0110
		{"BMI",		"8200",	"INTR"},	// IF РС(2) = 0 THEN INTR
		{null,		"8300",	"JMP"},		// GOTO JMP
		// BEQ 0111
		{"BEQ",		"8100",	"INTR"},	// IF РС(1) = 0 THEN INTR
		{null,		"8300",	"JMP"},		// GOTO JMP

		// Команды, работающие с данными 1***
		{"8-F",		"0100",	null},		// РД ==> БР
		{null,		"4001",	null},		// БР ==> РА
		{null,		"EE00",	"C-F"},		// 10** / 11**
		{null,		"ED00",	"A-B"},		// 100* / 101*
		{null,		"EC00",	"CMP"},		// 1000 / 1001
		// MOV 1000
		{"MOV",		"1000",	null},		// А ==> БР
		{null,		"4002",	null},		// БР ==> РД
		{"STORE",	"0002",	null},		// РД ==> ОП(РА)
		{null,		"8300",	"INTR"},
		// CMP 1001
		{"CMP",		"0001",	null},		// ОП(РА) ==> РД
		{null,		"1190",	null},		// А + COM(РД) + 1 ==> БР
		{null,		"4070",	null},		// БР ==> С, N, Z
		{null,		"8300",	"INTR"},
		// 101*
		{"A-B",		"0001",	null},		// ОП(РА) ==> РД
		{null,		"EC00",	"ISZ"},		// 1010 / 1011
		// LOOP 1010
		{"LOOP",	"0140",	null},		// COM(0) + РД ==> БР
		{null,		"4002",	null},		// БР ==> РД
		{"SAVE",	"0002",	null},		// РД ==> ОП(РА)
		{null,		"DF00",	"INTR"},	// IF РД(15) = 1 THEN INTR
		{"SKPCMD",	"0310",	null},		// СК + 1 ==> БР
		{null,		"4004",	null},		// БР ==> СК
		{null,		"8300",	"INTR"},
		// ISZ 1011
		{"ISZ",		"0110",	null},		// РД + 1 ==> БР
		{null,		"4002",	null},		// БР ==> РД
		{null,		"8300",	"SAVE"},	// GOTO SAVE
		// Арифметические команды 11**
		{"C-F",		"0001",	null},		// ОП(РА) ==> РД
		{null,		"ED00",	"E-F"},		// 110* / 111*
		{null,		"EC00",	"ADC"},		// 1100 / 1101
		// SUB 1100
		{"SUB",		"1190",	null},		// А + COM(РД) + 1 ==> БР
		{null,		"4075",	null},		// БР ==> А, С, N, Z
		{null,		"8300",	"INTR"},
		// ADC 1101
		{"ADC",		"8000",	"ADD"},		// IF РС(0) = 0 THEN ADD
		{null,		"1110",	null},		// А + РД + 1 ==> БР
		{null,		"4075",	null},		// БР ==> А, С, N, Z
		{null,		"8300",	"INTR"},
		// 111*
		{"E-F",		"EC00",	"AND"},		// 1110 / 1111
		// ADD 1110
		{"ADD",		"1100",	null},		// А + РД ==> БР
		{null,		"4075",	null},		// БР ==> А, С, N, Z
		{null,		"8300",	"INTR"},
		// AND 1111
		{"AND",		"1120",	null},		// А & РД ==> БР
		{null,		"4035",	null},		// БР ==> А, N, Z
		{null,		"8300",	"INTR"},

		// Безадресные и ввода/вывода 000*
		{"NONADDR",	"EC00",	"IO"},		// 0000 / 0001
		// Безадресные команды 0000
		{null,		"EB00",	"08-F"},	// 0*** / 1***
		{null,		"EA00",	"04-7"},	// 00** / 01**
		{null,		"E900",	"02-3"},	// 000* / 001*
		{null,		"A800",	"INTR"},	// 0001 / 0000 - NOP 0000
		// HLT 0001
		{"HLT",		"4008",	null},		// HLT
		{"STP",		"8300",	"BEGIN"},
		// 001*
		{"02-3",	"E800",	"DI"},		// 0010 / 0011
		// EI 0010
		{"EI",		"4800",	null},		// EI
		{null,		"8300",	"INTR"},
		// DI 0011
		{"DI",		"4400",	null},		// DI
		{null,		"8300",	"INTR"},
		// 01**
		{"04-7",	"E900",	"06-7"},	// 010* / 011*
		{"02-3",	"E800",	"CMC"},		// 0100 / 0101
		// CLC 0100
		{"CLC",		"4080",	null},		// 0 ==> C
		{null,		"8300",	"INTR"},
		// CMC 0101
		{"CMC",		"C000",	"CLC"},		// IF РС(0) = 1 THEN CLC
		{null,		"40C0",	null},		// 1 ==> C
		{null,		"8300",	"INTR"},
		// 011*
		{"06-7",	"E800",	"RIGHT"},		// 0110 / 0111
		// ROL/SHL 0110
		{"LEFT",	"A700",	"ROL"},
		{"SHL",		"4080",	null},		// 0 ==> C
		{"ROL",		"0008",	null},		// RAL(А) ==> БР
		{null,		"4075",	null},		// БР ==> А, С, N, Z
		{null,		"8300",	"INTR"},
		// ROR/SHR 0111
		{"RIGHT",	"A700",	"ROR"},
		{"SHR",		"4080",	null},		// 0 ==> C
		{"ROR",		"0004",	null},		// RAR(А) ==> БР
		{null,		"4075",	null},		// БР ==> А, С, N, Z
		{null,		"8300",	"INTR"},
		// 1***
		{"08-F",	"EA00",	"0C-F"},	// 10** / 11**
		{null,		"E900",	"0A-B"},	// 100* / 101*
		{null,		"E800",	"CMA"},		// 1000 / 1001
		// CLA 1000
		{"CLA",		"0020",	null},		// 0 & 0 ==> БР
		{null,		"4035",	null},		// БР ==> А, N, Z
		{null,		"8300",	"INTR"},
		// CMA 1001
		{"CMA",		"1040",	null},		// COM(А) + 0 ==> БР
		{null,		"4035",	null},		// БР ==> А, N, Z
		{null,		"8300",	"INTR"},
		// 101*
		{"0A-B",	"E800",	"DEC"},		// 1010 / 1011
		// INC 1010
		{"INC",		"1010",	null},		// А + 1 ==> БР
		{null,		"4075",	null},		// БР ==> А, С, N, Z
		{null,		"8300",	"INTR"},
		// DEC 1011
		{"DEC",		"1080",	null},		// А + COM(0) ==> БР
		{null,		"4075",	null},		// БР ==> А, С, N, Z
		{null,		"8300",	"INTR"},
		// Команды работы со стэком 11**
		{"0C-F",	"0040",	null},		// COM(0) ==> БР
		{null,		"4001",	null},		// БР ==> РА
		{null,		"0001",	null},		// ОП(РА) ==> РД
		{null,		"E900",	"0E-F"},	// 110* / 111*
		{null,		"E800",	"PUSH"},	// 0111/ 0110
		// SWAP 1100
		{"SWAP",	"0100",	null},		// РД ==> БР
		{null,		"4001",	null},		// БР ==> РА
		{null,		"0001",	null},		// ОП(РА) ==> РД
		{null,		"0100",	null},		// РД ==> БР
		{null,		"4003",	null},		// БР ==> РК
		{null,		"1000",	null},		// А ==> БР
		{null,		"4002",	null},		// БР ==> РД
		{null,		"0202",	null},		// РК ==> БР, РД ==> ОП(РА)
		{null,		"4035",	null},		// БР ==> А, N, Z
		{null,		"8300",	"INTR"},
		// PUSH/PUSHF 1101
		{"PUSH",	"0140",	null},		// COM(0) + РД ==> БР
		{null,		"4002",	null},		// БР ==> РД
		{null,		"0102",	null},		// РД ==> БР, РД ==> ОП(РА)
		{null,		"4001",	null},		// БР ==> РА
		{null,		"E700",	"PUSHF"},
		{null,		"1000",	null},		// А ==> БР
		{null,		"4002",	null},		// БР ==> РД
		{null,		"8300",	"STORE"},
		// PUSHF
		{"PUSHF",	"2000",	null},		// РС ==> БР
		{null,		"4002",	null},		// БР ==> РД
		{null,		"8300",	"STORE"},
		// 111*
		{"0E-F",	"0110",	null},		// РД + 1 ==> БР
		{null,		"4002",	null},		// БР ==> РД
		{null,		"0142",	null},		// COM(0) + РД ==> БР, РД ==> ОП(РА)
		{null,		"4001",	null},		// БР ==> РА
		{null,		"0001",	null},		// ОП(РА) ==> РД
		{null,		"E800",	"RET"},		// 1110 / 1111
		// POP/POPF 1110
		{null,		"E700",	"POPF"},	// 01110 / 11110
		// POP
		{"POP",		"0100",	null},		// РД ==> БР
		{null,		"4035",	null},		// БР ==> А, N, Z
		{null,		"8300",	"INTR"},
		// RET/IRET 1111
		{"RET",		"0100",	null},		// РД ==> БР
		{null,		"4004",	null},		// БР ==> СК
		{null,		"A700",	"INTR"},
		// IRET
		{"IRET",	"0040",	null},		// COM(0) ==> БР
		{null,		"4001",	null},		// БР ==> РА
		{null,		"0001",	null},		// ОП(РА) ==> РД
		{null,		"0110",	null},		// РД + 1 ==> БР
		{null,		"4002",	null},		// БР ==> РД
		{null,		"0142",	null},		// COM(0) + РД ==> БР, РД ==> ОП(РА)
		{null,		"4001",	null},		// БР ==> РА
		{null,		"0001",	null},		// ОП(РА) ==> РД
		// POPF
		{"POPF",	"D000",	"SETC"},	// IF РД(0) = 1 THEN SETC
		{null,		"4080",	null},		// 0 ==> C
		{null,		"8300",	"CHKZ"},
		{"SETC",	"40C0",	null},		// 1 ==> C
		{"CHKZ",	"D100",	"SETZ"},	// IF РД(1) = 1 THEN SETZ
		{null,		"0040",	null},		// COM(0) ==> БР
		{null,		"4010",	null},		// БР ==> Z
		{null,		"8300",	"CHKN"},
		{"SETZ",	"0020",	null},		// 0 & 0 ==> БР
		{null,		"4010",	null},		// БР ==> Z
		{"CHKN",	"D200",	"SETN"},	// IF РД(2) = 1 THEN SETN
		{null,		"0020",	null},		// 0 & 0 ==> БР
		{null,		"4020",	null},		// БР ==> N
		{null,		"8300",	"CHKINTR"},
		{"SETN",	"0040",	null},		// COM(0) ==> БР
		{null,		"4020",	null},		// БР ==> N
		{"CHKINTR",	"D400",	"EI"},		// IF РД(4) = 1 THEN EI
		{null,		"8300",	"DI"},		// GOTO DI

		// Исполнение команд ввода-вывода
		{"IO",		"4100",	null},		// Ввод/вывод
		{"TSF",		"C600",	"SKPCMD"},	// IF РС(6) = 1 THEN SKPCMD - TSF

		// Цикл прерывания
		{"INTR",	"8700",	"HLT"},		// IF РС(7) = 0 THEN HLT - Останов
		{null,		"8500",	"BEGIN"},	// IF РС(5) = 0 THEN BEGIN
		{null,		"0040",	null},		// COM(0) ==> БР
		{null,		"4001",	null},		// БР ==> РА
		{null,		"0001",	null},		// ОП(РА) ==> РД
		{null,		"0140",	null},		// COM(0) + РД ==> БР
		{null,		"4002",	null},		// БР ==> РД
		{null,		"0102",	null},		// РД ==> БР, РД ==> ОП(РА)
		{null,		"4001",	null},		// БР ==> РА
		{null,		"2000",	null},		// РС ==> БР
		{null,		"4002",	null},		// БР ==> РД
		{null,		"0022",	null},		// 0 & 0 ==> БР, РД ==> ОП(РА)
		{null,		"4001",	null},		// БР ==> РА
		{null,		"0001",	null},		// ОП(РА) ==> РД
		{null,		"0100",	null},		// РД ==> БР
		{null,		"4403",	null},		// БР ==> РК, DI
		{null,		"8300",	"CALLSTR"},	// GOTO STORE

		// Пультовые операции
		// Ввод адреса
		{"ADDR",	"3000",	null},		// КлР + 0 ==> БР
		{null,		"4004",	null},		// БР ==> СК
		{null,		"8300",	"HLT"},
		// Чтение
		{"READ",	"0300",	null},		// СК ==> БР
		{null,		"4001",	null},		// БР ==> РА
		{null,		"0311",	null},		// СК + 1 ==> БР, ОП(РА) ==> РД
		{null,		"4004",	null},		// БР ==> СК
		{null,		"8300",	"HLT"},
		// Запись
		{"WRITE",	"0300",	null},		// СК ==> БР
		{null,		"4001",	null},		// БР ==> РА
		{null,		"3000",	null},		// КлР + 0 ==> БР
		{null,		"4002",	null},		// БР ==> РД
		{null,		"0312",	null},		// СК + 1 ==> БР, РД ==> ОП(РА)
		{null,		"4004",	null},		// БР ==> СК
		{null,		"8300",	"HLT"},
		// Пуск
		{"START",	"0020",	null},		// 0 & 0 ==> БР
		{null,		"4675",	null},		// БР ==> А, С, N, Z; DI; Сброс всех флагов 
		{null,		"0040",	null},		// COM(0) ==> БР
		{null,		"4001",	null},		// БР ==> РА
		{null,		"4002",	null},		// БР ==> РД
		{null,		"8300",	"STORE"},
		// Продолжение выполнения нереализованных команд
		{"EXECCNT",	"0000",	null}
	};

	ExtendedMicroProgram() {
		super("расширенная", ExtendedInstrSet.instructions, mp);
	}
}
