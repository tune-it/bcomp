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
	 * Выборка команды
	 */
	INFETCH,
	/**
	 * Выборка адреса
	 */
	ADFETCH,
	/**
	 * Выборка операнда
	 */
	OPFETCH,
	/**
	 * Исполнение
	 */
	EXEC,
	/**
	 * Прерывание
	 */
	INT,
	/**
	 * Пультовая операция
	 */
	PANEL,
	/**
	 * HALT
	 */
	NONE
}
