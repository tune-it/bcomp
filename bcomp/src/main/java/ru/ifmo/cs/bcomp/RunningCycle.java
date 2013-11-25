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
	INSTR_FETCH,
	/**
	 * Выборка адреса
	 */
	ADDR_FETCH,
	/**
	 * Исполнение
	 */
	EXECUTION,
	/**
	 * Прерывание
	 */
	INTERRUPT,
	/**
	 * Пультовая операция
	 */
	PANEL,
	/**
	 * HALT
	 */
	NONE
}
