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
	 * Set address
	 * Ввод адреса
	 */
	SETIP,
	/**
	 * Write
	 * Запись
	 */
	WRITE,
	/**
	 * Read
	 * Чтение
	 */
	READ,
	/**
	 * Start
	 * Пуск
	 */
	START,
	/**
	 * Reserved for future expantion
	 */
	RESERVED,
}
