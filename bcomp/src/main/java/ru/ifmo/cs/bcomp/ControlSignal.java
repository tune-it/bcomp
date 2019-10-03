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
	 * HALT
	 */
	HALT,
	/**
	 * Control signal 1:
	 * Read Data Register
	 */
	R_DR,
	/**
	 * Control signal 2:
	 * Read Instruction Pointer
	 */
	R_IP,
	/**
	 * Control signal 3:
	 * Read Command Register
	 */
	R_CR,
	/**
	 * Control signal 4:
	 * Read Stack Pointer
	 */
	R_SP,
	/**
	 * Control signal 5:
	 * Read Accumulator
	 */
	R_AC,
	/**
	 * Control signal 6:
	 * Read Program State register
	 */
	R_PS,
	/**
	 * Control signal 7:
	 * Read Buffer Register
	 */
	R_BR,
	/**
	 * Control signal 8:
	 * Read Input Register
	 */
	R_IR,
	/**
	 * Control signal 9:
	 * Write to Data Register
	 */
	W_DR,
	/**
	 * Control signal 10:
	 * Write to Instruction Pointer
	 */
	W_IP,
	/**
	 * Control signal 11:
	 * Write to Command Register
	 */
	W_CR,
	/**
	 * Control signal 12:
	 * Write to Stack Pointer
	 */
	W_SP,
	/**
	 * Control signal 13:
	 * Write to Accumulator
	 */
	W_AC,
	/**
	 * Control signal 14:
	 * Write to Program State register
	 */
	W_PS,
	/**
	 * Control signal 15:
	 * Write to Buffer Register
	 */
	W_BR,
	/**
	 * Control signal 16:
	 * Write to Address Register
	 */
	W_AR,
	/**
	 * Control signal 17:
	 * Complement Left input
	 */
	COML,
	/**
	 * Control signal 18:
	 * Complement Right input
	 */
	COMR,
	/**
	 * Control signal 19:
	 * Summary OR And
	 */
	SORA,
	/**
	 * Control signal 20:
	 * Plus one
	 */
	PLS1,
	/**
	 * Control signal 21:
	 * Lower byte to lower
	 */
	LTOL,
	/**
	 * Control signal 22:
	 * High byte to high
	 */
	HTOH,
	/**
	 * Control signal 23:
	 * Lower byte to high
	 */
	LTOH,
	/**
	 * Control signal 24:
	 * High byte to lower
	 */
	HTOL,
	/**
	 * Control signal 25:
	 * Sign Extend from lower byte to high
	 */
	SEXT,
	/**
	 * Control signal 26:
	 * SHift Left
	 */
	SH_L,
	/**
	 * Control signal 27:
	 * Use old C as value for 0th bit (SH_L + SHL0 == ROL)
	 */
	SHL0,
	/**
	 * Control signal 28:
	 * SHift Right
	 */
	SH_R,
	/**
	 * Control signal 29:
	 * ???
	 */
	SH15,
	/**
	 * Control signal 30:
	 * Set flag C
	 */
	SETC,
	/**
	 * Control signal 31:
	 * Set flags N and Z
	 */
	STNZ,
	/**
	 * Control signal 32:
	 * Set flag oVerflow
	 */
	SETV,
	/**
	 * Control signal 33:
	 * Read MEmory
	 */
	R_ME,
	/**
	 * Control signal 34:
	 *
	 */
	W_ME,
	/**
	 * Control signal 35:
	 * Input output
	 */
	IO,
	/**
	 * Control signal 36:
	 * Clear flags for all IO Controllers
	 */
	CLRF,
	/**
	 * Control signal 37:
	 * Disable interrupts
	 */
	CLRI,
	/**
	 * Control signal 38:
	 * Enable interrupts
	 */
	SETI,
	/**
	 * Control signal 39:
	 * Micro command type
	 */
	TYPE,
	/**
	 * Псевдосигнал: Переключение работа/останов
	 */
	SET_RUN_STATE,
	/**
	 * Псевдосигнал: Установка программы
	 */
	SET_PROGRAM,
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
