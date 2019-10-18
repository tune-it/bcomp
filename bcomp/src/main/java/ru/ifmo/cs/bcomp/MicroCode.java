/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

import static ru.ifmo.cs.bcomp.CS.*;
import static ru.ifmo.cs.bcomp.State.*;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class MicroCode {
	private class omc {
		public final String label;
		private final long microcmd;
		private final CS[] signals;

		public omc(String label, CS[] signals) {
			long microcmd = 0L;

			this.label = label;

			for (CS cs : (this.signals = signals)) {
				microcmd |= 1L << cs.ordinal();
			}

			this.microcmd = microcmd;
		}

		public omc(CS[] signals) {
			this(null, signals);
		}

		public long getMicroCommand() throws Exception {
			return microcmd;
		}
	}

	private class CMC extends omc {
		private final String labelto;
		private final long microcmd;

		public CMC(String label, CS[] signals, long startbit, long expected, String labelto) {
			super(label, signals);

			this.labelto = labelto;
			microcmd = (1L << TYPE.ordinal()) + (1L << (startbit + 16)) + (expected << 32);
		}

		public CMC(CS[] signals, long startbit, long expected, String labelto) {
			this(null, signals, startbit, expected, labelto);
		}

		@Override
		public long getMicroCommand() throws Exception {
			int addrto;

			for (addrto = 0; addrto < MP.length; addrto++)
				if (labelto.equals(MP[addrto].label))
					break;

			if (addrto == MP.length)
				throw new Exception("Label '" + labelto + "' not found");

			return microcmd | super.getMicroCommand() | (((long)addrto) << 24);
		}
	}

	private final omc[] MP = {
		new omc(			new CS[] {}),
		new omc("BEGIN",	new CS[] {RDIP, HTOH, LTOL, WRAR, WRBR}),						// IP -> AR, BR
		new omc(			new CS[] {RDBR, PLS1, HTOH, LTOL, WRIP, LOAD}),					// BR + 1 -> IP, MEM(AR) -> DR
		new omc(			new CS[] {RDDR, HTOH, LTOL, WRCR}),								// DR -> CR
		new CMC(			new CS[] {RDCR, HTOL}, 7, 1,						"CHKBR"),		// if CR(15) = 1 then GOTO CHKBR
		new CMC(			new CS[] {RDCR, HTOL}, 6, 1,						"ADDRTYPE"),	// if CR(14) = 1 then GOTO ADDRTYPE
		new CMC(			new CS[] {RDCR, HTOL}, 5, 1,						"ADDRTYPE"),	// if CR(13) = 1 then GOTO ADDRTYPE
		new CMC(			new CS[] {RDCR, HTOL}, 4, 0,						"ADDRLESS"),	// if CR(12) = 0 then GOTO ADDRLESS
		new CMC(			new CS[] {RDPS, LTOL}, PS0.ordinal(), 0,			"IO"),			// GOTO IO
		new CMC("CHKBR",	new CS[] {RDCR, HTOL}, 6, 0,						"ADDRTYPE"),	// if CR(14) = 0 then GOTO ADDRTYPE
		new CMC(			new CS[] {RDCR, HTOL}, 5, 0,						"ADDRTYPE"),	// if CR(13) = 0 then GOTO ADDRTYPE
		new CMC(			new CS[] {RDCR, HTOL}, 4, 1,						"BRANCHES"),	// if CR(12) = 1 then GOTO BRANCHES
		// закончили выборку и частичное декодирование
		new CMC("ADDRTYPE",	new CS[] {RDCR, HTOL}, 3, 0,						"LOADOPER"),	// if CR(11) = 0 then GOTO LOADOPER
		new CMC("T0XXX",	new CS[] {RDCR, HTOL}, 2, 1,						"T01XX"),		// if CR(10) = 1 then GOTO T01XX
		new omc("T00XX",	new CS[] {RDCR, HTOH, LTOL, WRAR}),								// CR -> AR
		new omc(			new CS[] {LOAD}),												// MEM(AR) -> DR 
		new CMC(			new CS[] {RDCR, HTOL}, 1, 1,						"T001X"),		// if CR(9) = 1 then GOTO T001X
		new	CMC("T000X",	new CS[] {RDCR, HTOL}, 0, 1,						"RESERVED"),	// if CR(8) = 1 then GOTO RESERVED
		new CMC("T1000",	new CS[] {RDPS, LTOL}, PS0.ordinal(), 0,			"LOADOPER"),	// GOTO LOADOPER
		new CMC("T101X",	new CS[] {RDCR, HTOL}, 0, 1,						"T1011"),		// if CR(8) = 1 then GOTO T1011
		new omc("T1010",	new CS[] {RDDR, PLS1, HTOH, LTOL, WRDR}),						// DR + 1 -> DR
		new omc(			new CS[] {STOR, RDDR, COML, HTOH, LTOL, WRBR}),					// DR -> MEM(AR); DR - 1 -> BR
		new omc(			new CS[] {RDBR, HTOH, LTOL, WRDR}),								// BR -> DR
		new CMC(			new CS[] {RDPS, LTOL}, PS0.ordinal(), 0,			"LOADOPER"),	// GOTO LOADOPER
		new omc("T1011",	new CS[] {RDDR, COML, HTOH, LTOL, WRDR}),						// DR - 1 -> DR
		new omc(			new CS[] {STOR}),												// DR -> MEM(AR)
		new CMC(			new CS[] {RDPS, LTOL}, PS0.ordinal(), 0,			"LOADOPER"),	// GOTO LOADOPER
		new omc("T11XX",	new CS[] {RDCR, LTOL, WRBR}),									// LTOL(CR) -> BR
		new CMC(			new CS[] {RDCR, HTOL}, 1, 1,						"T111X"),		// if CR(9) = 1 then GOTO T111X
		new CMC("T110X",	new CS[] {RDCR, HTOL}, 0, 1,						"T1101"),		// if CR(8) = 1 then GOTO T1101
		new omc("T1110",	new	CS[] {RDBR, RDIP, HTOH, LTOL, WRDR}),						// BR + IP -> DR !!! RECHECK TARGET
		new CMC(			new CS[] {RDPS, LTOL}, PS0.ordinal(), 0,			"LOADOPER"),	// GOTO LOADOPER
		new CMC("T111X",	new CS[] {RDCR, HTOL}, 0, 0,						"RESERVED"),	// if CR(8) = 0 then GOTO RESERVED
		new omc("T1111",	new CS[] {RDBR, HTOH, LTOL, WRDR}),								// BR -> DR
		new CMC(			new CS[] {RDPS, LTOL}, PS0.ordinal(), 0,			"EXECUTE"),		// GOTO EXECUTE
		new omc("T1101",	new CS[] {RDBR, RDSP, HTOH, LTOL, WRDR}),						// BR + SP -> DR !!! RECHECK TARGET
		// Выборка операнда
		new CMC("LOADOPER",	new CS[] {RDCR, HTOL}, 7, 0,						"RDVALUE"),		// if CR(15) = 0 then GOTO RDVALUE
		new CMC(			new CS[] {RDCR, HTOL}, 6, 1,						"CMD11XX"),		// if CR(14) = 1 then GOTO CMD11XX
		new omc("RDVALUE",	new CS[] {RDDR, HTOH, LTOL, WRAR}),								// DR -> AR
		new omc(			new CS[] {LOAD}),												// MEM(AR) -> DR
		// Декодирование и цикл исполнения адресных команд кроме JUMP/CALL/ST/FXXX
		new CMC("EXECUTE",	new CS[] {RDCR, HTOL}, 7, 1,						"CMD1XXX"),		// if CR(15) = 1 then GOTO CMD1XXX
		new CMC("CMD0XXX",	new CS[] {RDCR, HTOL}, 6, 1,						"CMD01XX"),		// if CR(14) = 1 then GOTO CMD01XX
		// 13th bit already checked !!! CHECK LABEL NAME !!!
		new CMC("CMD000X",	new CS[] {RDCR, HTOL}, 4, 1,						"OR"),			// if CR(12) = 1 then GOTO OR
		new omc("AND",		new CS[] {RDAC, RDDR, SORA, HTOH, LTOL, STNZ, WRAC}),			// AND: AC & DR -> AC, N, Z
		new CMC(			new CS[] {RDPS, LTOL}, PS0.ordinal(), 0,			"INT"),			// GOTO INT
		new omc("OR",		new CS[] {RDAC, RDDR, COML, COMR, SORA, HTOH, LTOL, WRBR}),		// OR: ~AC & ~DR & -> BR
		new omc(			new CS[] {RDBR, COML, HTOH, LTOL, STNZ, WRAC}),					//	~BR -> AC, N, Z
		new CMC(			new CS[] {RDPS, LTOL}, PS0.ordinal(), 0,			"INT"),			// GOTO INT
		new CMC("CMD01XX",	new CS[] {RDCR, HTOL}, 5, 1,						"CMD011X"),		// if CR(13) = 1 then GOTO CMD011X
		new CMC("CMD010X",	new CS[] {RDCR, HTOL}, 4, 1,						"ADC"),			// if CR(12) = 1 then GOTO ADC
		new omc("ADD",		new CS[] {RDAC, RDDR, HTOH, LTOL, STNZ, SETV, SETC, WRAC}),		// ADD: AC + DR -> AC, C, N, Z, V
		new CMC(			new CS[] {RDPS, LTOL}, PS0.ordinal(), 0,			"INT"),			// GOTO INT
		new CMC("ADC",		new CS[] {RDPS, LTOL}, C.ordinal(), 0,			"ADD"),			// if C = 0 then GOTO ADD
		new omc(			new CS[] {RDAC, RDDR, PLS1, HTOH, LTOL, STNZ, SETV, SETC, WRAC}),// DR + AC + 1 -> BR, C, N, Z, V
		new CMC(			new CS[] {RDPS, LTOL}, PS0.ordinal(), 0,			"INT"),			// GOTO INT
		// GOTO MOVTOAC
		// CMD011X: if CR(12) = 1 then GOTO CMP
		// SUB: ~DR + AC + 1 -> BR, C, N, Z, V !!! ACHTUNG !!! ACHTUNG !!! ACHTUNG !!!
		// MOVTOAC: BR -> AC
		// GOTO INT
		// 11XX already checked!!!
		// CMD1XXX: if CR(13) = 1 then GOTO CMD101X
		//	if CR(12) = 1 then GOTO космос CMD1001
		// LOOP: DR + ~0 -> BR
		//	BR -> DR
		//	DR -> MEM(AR)
		//	if DR(15) = 1 then GOTO INT
		// SKIPCMD: IP + 1 -> BR
		//	BR -> IP
		//	GOTO INT
		// CMD101X: if CR(12) = 1 then GOTO SWAM
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
		// PUSHVALUE: SP - 1 -> BR
		//	BR -> SP, AR
		//	DR -> MEM(AR)
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
		// BNE: if Z = 0 then GOTO INT
		//	GOTO BR
		// BR001X: if CR(8) then GOTO BPL
		// BMI: if N = 0 then GOTO INT
		//	GOTO BR
		// BPL: if N = 1 then GOTO INT
		//	GOTO BR
		// BR01XX: if CR(9) = 1 then GOTO BR011X
		//	if CR(8) = 1 then GOTO BCC
		// BCS: if C = 0 then GOTO INT
		//	GOTO BR
		// BCC: if ЧТОТОТАММ = ЧЕМУТОТАМ then GOTO INT
		//	GOTO BR
		// BR011X: if CR(8) = 1 then GOTO BVC
		// BVS: if ЧТОТОТАММ = ЧЕМУТОТАМ then GOTO INT
		//	GOTO BR
		// BVC: if ЧТОТОТАММ = ЧЕМУТОТАМ then GOTO INT
		//	GOTO BR
		// BR1XXX: if CR(10) = 1 then GOTO космос
		// BR10XX: if CR(9) = 1 then GOTO космос
		// BR100X: if CR(8) = 1 then GOTO BFC
		// BFS: if ЧТОТОТАММ = ЧЕМУТОТАМ then GOTO INT
		//	GOTO BR
		// BFC: if ЧТОТОТАММ = ЧЕМУТОТАМ then GOTO INT
		//	GOTO BR
		// ----
		// ADDRLESS: -- безадресные команды
		//	if CR(11) = 1 then GOTO AL1XXX
		// AL0XXX: if CR(10) = 1 then GOTO AL01XX
		// AL00XX: if CR(9) = 1 then GOTO AL001X
		// NOP: if CR(8) = 0 then GOTO INT // 
		// HLT: HLT
		// GOTO BEGIN
		// AL001X: if CR(8) = 1 then GOTO AL0011
		//	if CR(7) = 1 then GOTO CMA
		// CLA: 0 + 0 -> AC, N, V, Z
		//	GOTO INT
		// CMA: ~AC + 0 -> BR, N, Z, V, C
		//	GOTO MOVTOAC
		// AL0011: if (CR7) = 1 then GOTO CMC
		// CLC: 0 + 0 -> C
		//	GOTO INT
		// CMC: if C = 1 then GOTO CLC
		//	~0 + ~0 -> C
		//	GOTO INT
		// AL01XX: if CR(9) = 1 then GOTO AL011X
		// AL010X: if CR(8) = 1 then GOTO AL0101
		// AL0100: if CR(7) = 1 then GOTO ROR
		// ROL: ROL(AC) -> BR, C, N, Z
		//	GOTO MOVTOAC
		// ROR: ROR(AC) -> BR, C, N, Z
		//	GOTO MOVTOAC
		// AL0101: if CR(7) = 1 then GOTO ASR
		// ASL: ASL(AC) -> BR, C, N, Z
		//	GOTO MOVTOAC
		// ASR: ASR(AC) -> BR, C, N, Z
		//	GOTO MOVTOAC
		// AL011X: if CR(8) = 1 then AL0111
		// AL0110: if CR(7) = 1 then SWAB
		// AL01100: if CR(6) = 1 then TST
		// SXTB: SEXT(AC) -> BR, N, Z, V
		//	GOTO MOVTOAC
		// TST: AC -> N, Z, V, C???
		//	GOTO INT
		// SWAB: HTOL/LTOH (AC) -> BR, N, Z, V
		//	GOTO MOVTOAC
		// AL0111: if CR(7) = 1 then DEC
		// AL01110: if CR(6) = 1 then NEG
		// INC: AC + 1 -> BR, N, Z, V, C
		//	GOTO MOVTOAC
		// NEG: ~AC + 1 -> BR, N, Z, V, C
		//	GOTO MOVTOAC
		// DEC: AC + ~0 -> BR, N, Z, V, C
		//	GOTO MOVTOAC
		// AL1XXX: if CR(10) = 1 then AL11XX
		// AL10XX: SP -> AR
		//	MEM(AR) -> DR
		//	if CR(9) = 1 then AL101X
		// AL100X: if CR(8) = 1 then POPF
		// POP: DR -> AC
		// INCSP: SP + 1 -> BR
		//	BR -> SP
		//	GOTO INT
		// POPF: DR -> PS
		//	GOTO INCSP
		// AL101X: if CR(8) = 1 then IRET
		// RET: DR -> IP
		//	GOTO INCSP
		// IRET: DR -> PS
		//	SP + 1 -> BR, AR
		//	MEM(AR) -> DR; BR -> SP
		//	GOTO RET
		// AL11XX: if CR(9) = 1 then AL111X
		// AL110X: if CR(8) = 1 then PUSHF
		// PUSH: AC -> DR
		//	GOTO PUSHVALUE
		// PUSHF: PS -> DR
		//	GOTO PUSHVALUE
		// AL111X: if CR(8) = 1 then космос
		// SWAP: SP -> AR
		//	MEM(AR) -> DR
		//	DR -> BR
		//	AC -> DR
		//	BR -> AC; DR -> MEM(AR)
		//	GOTO INT
		// IO: IO ---------------------
		// INT: if RUN = 0 then HLT
		//	if INT = 0 then BEGIN
		//	SP + ~0 -> BR
		//	BR -> AR, SP
		//	IP -> DR
		//	DR -> MEM(AR)
		//	SP + ~0 -> BR
		//	BR -> AR, SP
		//	IP -> DR
		//	DR -> MEM(AR)
		//	0 -> AR
		//	MEM(AR) -> DR
		//	DR -> IP, DI
		//	GOTO BEGIN
		// SETIP:
		//	IR -> IP
		//	GOTO HLT
		// WRITE:
		//	IP -> AR
		//	IR -> DR
		//	DR -> MEM(AR); IP + 1 -> BR
		//	BR -> IP
		//	GOTO HLT
		// READ:
		//	IP -> AR
		//	IR -> DR
		//	MEM(AR) -> DR; IP + 1 -> BR
		//	BR -> IP
		//	GOTO HLT
		// START:
		//	0 -> AC, C, N, Z, V; DI; CLRF
		//	~0 -> SP
		//	GOTO INT
		new omc("RESERVED",	new  CS[] {})
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

	public int getMicroCodeLength() {
		return MP.length;
	}

	public long getMicroCommand(int addr) throws Exception {
		return MP[addr].getMicroCommand();
	}
}
