/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public enum ControlSignal {
	/**
	 * Control signal 0:
	 * Read Data Register
	 */
	RDDR,
	/**
	 * Control signal 1:
	 * Read Command Register
	 */
	RDCR,
	/**
	 * Control signal 2:
	 * Read Instruction Pointer
	 */
	RDIP,
	/**
	 * Control signal 3:
	 * Read Stack Pointer
	 */
	RDSP,
	/**
	 * Control signal 4:
	 * Read Accumulator
	 */
	RDAC,
	/**
	 * Control signal 5:
	 * Read Buffer Register
	 */
	RDBR,
	/**
	 * Control signal 6:
	 * Read Program State register
	 */
	RDPS,
	/**
	 * Control signal 7:
	 * Read Input Register
	 */
	RDIR,
	/**
	 * Control signal 8:
	 * Complement Right input
	 */
	COMR,
	/**
	 * Control signal 9:
	 * Complement Left input
	 */
	COML,
	/**
	 * Control signal 10:
	 * Plus one
	 */
	PLS1,
	/**
	 * Control signal 11:
	 * Summary OR And
	 */
	SORA,
	/**
	 * Control signal 12:
	 * Lower byte to lower
	 */
	LTOL,
	/**
	 * Control signal 13:
	 * Lower byte to high
	 */
	LTOH,
	/**
	 * Control signal 14:
	 * High byte to lower
	 */
	HTOL,
	/**
	 * Control signal 15:
	 * High byte to high
	 */
	HTOH,
	/**
	 * Control signal 16:
	 * Sign Extend from lower byte to high
	 */
	SEXT,
	/**
	 * Control signal 17:
	 * SHift Left
	 */
	SHLT,
	/**
	 * Control signal 18:
	 * Use old C as value for 0th bit (SH_L + SHL0 == ROL)
	 */
	SHL0,
	/**
	 * Control signal 19:
	 * SHift RighT
	 */
	SHRT,
	/**
	 * Control signal 20:
	 * ???
	 */
	SHRF,
	/**
	 * Control signal 21:
	 * Set flag C
	 */
	SETC,
	/**
	 * Control signal 22:
	 * Set flag oVerflow
	 */
	SETV,
	/**
	 * Control signal 23:
	 * Set flags N and Z
	 */
	STNZ,
	/**
	 * Control signal 24:
	 * Write to Data Register
	 */
	WRDR,
	/**
	 * Control signal 25:
	 * Write to Command Register
	 */
	WRCR,
	/**
	 * Control signal 26:
	 * Write to Instruction Pointer
	 */
	WRIP,
	/**
	 * Control signal 27:
	 * Write to Stack Pointer
	 */
	WRSP,
	/**
	 * Control signal 28:
	 * Write to Accumulator
	 */
	WRAC,
	/**
	 * Control signal 29:
	 * Write to Buffer Register
	 */
	WRBR,
	/**
	 * Control signal 30:
	 * Write to Program State register
	 */
	WRPS,
	/**
	 * Control signal 31:
	 * Write to Address Register
	 */
	WRAR,
	/**
	 * Control signal 32:
	 * Load value from Memory to Data Register
	 */
	LOAD,
	/**
	 * Control signal 33:
	 * Store value from Data Register to Memory
	 */
	STOR,
	/**
	 * Control signal 34:
	 * Input output
	 */
	IO,
	/**
	 * Control signal 35:
	 * 
	 */
	INTS,
	/**
	 * Control signal 36:
	 * Reserved
	 */
	RESERVED36,
	/**
	 * Control signal 37:
	 * Reserved
	 */
	RESERVED37,
	/**
	 * Control signal 38:
	 * HALT
	 */
	HALT,
	/**
	 * Control signal 39:
	 * Micro command type
	 */
	TYPE,
	/**
	 * Псевдосигнал: Установка программы
	 */
	SET_PROGRAM,
	/**
	 * Псевдосигнал: Тактовый генератор
	 */
	CLOCK0,
	/**
	 * Псевдосигнал: Тактовый генератор
	 */
	CLOCK1,
	/**
	 * Псевдосигнал: Disable interrupts
	 */
	SET_EI,
	/**
	 * Псевдосигнал: Запрос прерывания
	 */
	SET_REQUEST_INTERRUPT,
	/**
	 * Псевдосигнал: TSF 1
	 */
	IO0_TSF,
	/**
	 * Псевдосигнал: Установка флага ВУ1
	 */
	IO1_SETFLAG,
	/**
	 * Псевдосигнал: TSF 1
	 */
	IO1_TSF,
	/**
	 * Псевдосигнал: OUT 1
	 */
	IO1_OUT,
	/**
	 * Псевдосигнал: Установка флага ВУ2
	 */
	IO2_SETFLAG,
	/**
	 * Псевдосигнал: TSF 2
	 */
	IO2_TSF,
	/**
	 * Псевдосигнал: IN 2
	 */
	IO2_IN,
	/**
	 * Псевдосигнал: Установка флага ВУ3
	 */
	IO3_SETFLAG,
	/**
	 * Псевдосигнал: TSF 3
	 */
	IO3_TSF,
	/**
	 * Псевдосигнал: IN 3
	 */
	IO3_IN,
	/**
	 * Псевдосигнал: OUT 3
	 */
	IO3_OUT,
	/**
	 * Псевдосигнал: TSF 4
	 */
	IO4_TSF,
	/**
	 * Псевдосигнал: TSF 5
	 */
	IO5_TSF,
	/**
	 * Псевдосигнал: TSF 6
	 */
	IO6_TSF,
	/**
	 * Псевдосигнал: TSF 7
	 */
	IO7_TSF,
	/**
	 * Псевдосигнал: IN 7
	 */
	IO7_IN,
	/**
	 * Псевдосигнал: TSF 8
	 */
	IO8_TSF,
	/**
	 * Псевдосигнал: IN 8
	 */
	IO8_IN,
	/**
	 * Псевдосигнал: TSF 9
	 */
	IO9_TSF,
	/**
	 * Псевдосигнал: IN 9
	 */
	IO9_IN,
}
