/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class OptimizedMicroProgram implements MicroProgram {
	private static final String mpname = "оптимизированная";

	private static final String[][] mp = {
		{null,		"0000",	null},
		// Цикл выборки команды
		{"BEGIN",	"0300",	null},
		{null,		"4001",	null},
		{null,		"0311",	null},
		{null,		"4004",	null},
		{null,		"0100",	null},
		{null,		"4003",	null},
		// Определение типа команды		//Дальше/Переход
		{null,		"AF00",	"ADDRCHK"},	// 1*** / 0***
		{null,		"AE00",	"ADDRCHK"},	// 11** / 10**
		{null,		"ED00",	"NONADDR"},	// 110* / 111*
		// Определение вида адресации
		{"ADDRCHK",	"AB00",	"EXEC"},
		// Цикл выборки адреса операнда
		{"ADDRGET",	"0100",	null},
		{null,		"4001",	null},
		{null,		"0001",	null},
		{null,		"A300",	"EXEC"},
		{null,		"E400",	"EXEC"},
		{null,		"E500",	"EXEC"},
		{null,		"E600",	"EXEC"},
		{null,		"E700",	"EXEC"},
		{null,		"E800",	"EXEC"},
		{null,		"E900",	"EXEC"},
		{null,		"EA00",	"EXEC"},
		{null,		"0110",	null},
		{null,		"4002",	null},
		{null,		"0142",	null},
		{null,		"4002",	null},
		// Цикл исполнения адресных команд
		{"EXEC",	"EF00",	"PRX"},		// 0*** / 1***
		// Арифметические команды
		{null,		"0100",	null},
		{null,		"4001",	null},
		{null,		"EE00",	"ARF"},		// 00** / 01**
		{null,		"AD00",	"A1"},		// 001* / 000*
		{null,		"AC00",	"JSR"},		// 0011 / 0010
		{"MOV",		"1000",	null},		// 0011
		{null,		"4002",	null},
		{null,		"0002",	null},
		{null,		"8300",	"INTR"},
		{"A1",		"0001",	null},		// 000*
		{null,		"AC00",	"ISZ"},		// 0001 / 0000
		{"AND",		"1120",	null},		// 0001
		{null,		"4035",	null},
		{null,		"8300",	"INTR"},
		{"ARF",		"0001",	null},		// 01**
		{null,		"AD00",	"SUM"},		// 011* / 010*
		{null,		"ECB0",	null},		// 0110 / 0111
		{"SUB",		"1190",	null},		// 0110
		{null,		"4075",	null},
		{null,		"8300",	"INTR"},
		{"SUM",		"EC00",	"ADC"},		// 0100 / 0101
		{"ADD",		"1100",	null},		// 0100
		{null,		"4075",	null},
		{null,		"8300",	"INTR"},
		{"ADC",		"8000",	"ADD"},
		{null,		"1110",	null},
		{null,		"4075",	null},
		{null,		"8300",	"INTR"},
		{"ISZ",		"0110",	null},		// 0000
		{null,		"4002",	null},
		{null,		"0002",	null},
		{null,		"DF00",	"INTR"},
		{"ISZ+1",	"0310",	null},
		{null,		"4004",	null},
		{null,		"8300",	"INTR"},
		{"JSR",		"0110",	null},		// 0010
		{null,		"4003",	null},
		{null,		"0300",	null},
		{null,		"4002",	null},
		{null,		"0202",	null},
		{null,		"4004",	null},
		{null,		"8300",	"INTR"},
		// Команды перехода
		{"PRX",		"EC00",	"PR1"},		// 1**0 / 1**1
		{null,		"ED00",	"BMI"},		// 1*00 / 1010
		{null,		"EE00",	"BR"},		// 1000 / 1100
		{"BCS",		"8000",	"INTR"},
		{"BR",		"0100",	null},
		{null,		"4004",	null},
		{null,		"8300",	"INTR"},
		{"PR1",		"ED00",	"BEQ"},		// 1*01 / 1011
		{null,		"EED0",	null},		// 1001 / 1101
		{"BPL",		"C200",	"INTR"},
		{null,		"8300",	"BR"},
		{"BMI",		"8200",	"INTR"},
		{null,		"8300",	"BR"},
		{"BEQ",		"8100",	"INTR"},
		{null,		"8300",	"BR"},
		// Команда безадресная или ввода/вывода?
		{"NONADDR",	"AC00",	"IO"},
		// Декодирование и исполнение безадресных команд
		{"BAD",		"AB00",	"B0"},		// 1*** / 0***
		{null,		"EAE0",	null},		// 10** / 11**
		{"B1",		"A900",	"B5"},		// 101* / 100*
		{null,		"A800",	"EI"},		// 1011 / 1010
		{"DI",		"4400",	null},		// 1011
		{null,		"8300",	"BEGIN"},
		{"B0",		"AA00",	"B2"},		// 01** / 00**
		{null,		"A900",	"B3"},		// 011* / 010*
		{null,		"A800",	"ROL"},		// 0111 / 0110
		{"ROR",		"0004",	null},		// 0111
		{null,		"4075",	null},
		{null,		"8300",	"INTR"},
		{"B3",		"A800",	"CMA"},		// 0101 / 0100
		{"CMC",		"C000",	"CLC"},		// 0101
		{null,		"40C0",	null},
		{null,		"8300",	"INTR"},
		{"B2",		"A900",	"B4"},		// 001* / 000*
		{null,		"A800",	"CLA"},		// 0011 / 0010
		{"CLC",		"4080",	null},		// 0011
		{null,		"8300",	"INTR"},
		{"B4",		"E800",	"INTR"},	// 0000 / 0001
		{"HLT",		"4008",	null},		// 0000
		{"STP",		"8300",	"BEGIN"},
		{"B5",		"A800",	"INC"},		// 1001 / 1000
		{"DEC",		"1080",	null},		// 1001
		{null,		"4075",	null},
		{null,		"8300",	"INTR"},
		{"INC",		"1010",	null},		// 1000
		{null,		"4075",	null},
		{null,		"8300",	"INTR"},
		{"CLA",		"0020",	null},		// 0010
		{null,		"4035",	null},
		{null,		"8300",	"INTR"},
		{"CMA",		"1040",	null},		// 0100
		{null,		"4035",	null},
		{null,		"8300",	"INTR"},
		{"ROL",		"0008",	null},		// 0110
		{null,		"4075",	null},
		{null,		"8300",	"INTR"},
		{"EI",		"4800",	null},		// 1010
		{null,		"8300",	"BEGIN"},
		// Декодирование и исполнение команд ввода-вывода
		{"IO",		"4100",	null},
		{null,		"E900",	"INTR"},
		{null,		"A800",	"INTR"},
		{"TSF",		"C600",	"ISZ+1"},
		// Цикл прерывания
		{"INTR",	"8700",	"HLT"},
		{null,		"8500",	"BEGIN"},
		{null,		"0020",	null},
		{null,		"4001",	null},
		{null,		"0300",	null},
		{null,		"4002",	null},
		{null,		"0012",	null},
		{null,		"4404",	null},
		{null,		"8300",	"BEGIN"},
		// Пультовые операции
		// Ввод адреса
		{"ADDR",	"3000",	null},
		{null,		"4004",	null},
		{null,		"8300",	"HLT"},
		// Чтение
		{"READ",	"0300",	null},
		{null,		"4001",	null},
		{null,		"0311",	null},
		{null,		"4004",	null},
		{null,		"8300",	"HLT"},
		// Запись
		{"WRITE",	"0300",	null},
		{null,		"4001",	null},
		{null,		"3000",	null},
		{null,		"4002",	null},
		{null,		"0312",	null},
		{null,		"4004",	null},
		{null,		"8300",	"HLT"},
		// Пуск
		{"START",	"0020",	null},
		{null,		"4475",	null},
		{null,		"8300",	"INTR"},
		// Продолжение выполнения нереализованных команд
		{"EXECCNT",	"0000",	null}
	};

	public String[][] getMicroProgram() {
		return mp;
	}

	public String getMicroProgramName() {
		return mpname;
	}

	public Instruction[] getInstructionSet() {
		return BaseInstrSet.getInstructionSet();
	}
}
