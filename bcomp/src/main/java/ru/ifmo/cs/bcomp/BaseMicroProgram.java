/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class BaseMicroProgram implements MicroProgram {
	private static final String mpname = "исходная";

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
		{"ISZ+1", "0310", null},
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
		// Декодирование и исполнение команд ввода-вывода
		{"IO", "4100", null},
		{null, "E900", "INTR"},
		{null, "A800", "INTR"},
		{"TSF", "C600", "ISZ+1"},
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
		{null, "4400", null},
		{null, "8300", "INTR"},
		// Продолжение выполнения нереализованных команд
		{"EXECCNT", "0000", null}
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
