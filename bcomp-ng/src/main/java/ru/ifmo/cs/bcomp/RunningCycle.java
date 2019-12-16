/*
 * $Id$
 */

package ru.ifmo.cs.bcomp;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public enum RunningCycle {
	/**
	 * Instruction fetch
	 * Выборка команды
	 */
	INFETCH,
	/**
	 * Address fetch
	 * Выборка адреса
	 */
	ADFETCH,
	/**
	 * Operator fetch
	 * Выборка операнда
	 */
	OPFETCH,
	/**
	 * Execution
	 * Исполнение
	 */
	EXEC,
	/**
	 * Interrupt
	 * Прерывание
	 */
	INT,
	/**
	 * Start
	 * Пуск
	 */
	START,
	/**
	 * Write
	 * Запись
	 */
	READ,
	/**
	 * Set address
	 * Ввод адреса
	 */
	WRITE,
	/**
	 * Read
	 * Чтение
	 */
	SETIP,
	/**
	 * STOP
	 */
	STOP,
	/**
	 * Reserved for future extension
	 */
	RESERVED,
}
