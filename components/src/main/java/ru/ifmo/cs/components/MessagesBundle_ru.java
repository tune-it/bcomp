package ru.ifmo.cs.components;

import java.util.ListResourceBundle;

public class MessagesBundle_ru extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private static final Object[][] contents = {
            // Engine
            {"engine.busy",    "Операция невозможна: выполняется программа"},
            {"engine.aborted", "Операция прервана: выполняется программа"},

            // Program binary loader
            {"binary.corrupted",  "BcompNG: Program data is corrupted"},
            {"binary.empty.load", "BcompNG: Программа пуста: load_address"},
            {"binary.empty.start","BcompNG: Программа пуста: start_address"},
            {"binary.empty.body", "BcompNG: Программа пуста: binary body"},

            // Assembler Program container
            {"asm.program.corrupted",      "AsmNG Program.getBinaryFormat: Program data is corrupted"},
            {"asm.program.empty.load",     "AsmNG Program.loadBinaryFormat: Программа пуста: load_address"},
            {"asm.program.empty.start",    "AsmNG Program.loadBinaryFormat: Программа пуста: start_address"},
            {"asm.program.empty.body",     "AsmNG Program.loadBinaryFormat: Программа пуста: binary body"},
            {"asm.program.labels.unset",   "AsmNG Program.getLabelAddr: Labels are not set up"},
            {"asm.program.label.not_found","AsmNG Program.getLabelAddr: Label {0} not found"},

            // CLI banner / general
            {"cli.banner.title",     "Эмулятор Базовой ЭВМ. Версия {0}"},
            {"cli.banner.ready",     "БЭВМ готова к работе."},
            {"cli.banner.help_hint", "Используйте ? или help для получения справки"},
            {"cli.error",            "Ошибка: {0}"},
            {"cli.unknown_cmd",      "Неизвестная команда {0}"},
            {"cli.op_failed",        "операция не выполнена: выполняется программа"},

            // CLI register / micro tables
            {"cli.regs.header.addr",        "Адр "},
            {"cli.regs.header.value",       "Знчн"},
            {"cli.regs.header.mc",          "   МК    "},
            {"cli.regs.header.nzvc",        " NZVC "},
            {"cli.regs.header.addr_value",  "Адр Знчн"},
            {"cli.regs.header.mp_counter",  "СчМК"},
            {"cli.micro.header",            "Адр    МК       Метка           Расшифровка"},

            // CLI clock / run mode
            {"cli.clock.label",      "Такт: {0}"},
            {"cli.run_mode.label",   "Режим работы: {0}"},
            {"cli.yes",              "Да"},
            {"cli.no",               "Нет"},
            {"cli.run_mode.running", "Работа"},
            {"cli.run_mode.stopped", "Останов"},

            // CLI IO
            {"cli.io.entry",         "ВУ{0} {1}"},

            // CLI arg errors
            {"cli.cmd.mwrite_needs_arg", "команда mwrite требует аргумент"},
            {"cli.cmd.flag_needs_arg",   "команда flag требует аргумент"},
            {"cli.cmd.sleep_needs_arg",  "команда sleep требует аргумент"},

            // CLI assembler
            {"cli.asm.prompt",        "Введите текст программы. Для окончания введите END"},
            {"cli.asm.start_address", "Программа начинается с адреса {0}"},
            {"cli.asm.has_errors",    "Программа содержит ошибки"},

            // CLI help
            {"cli.help",
                "Доступные команды:\n"
                + "a[ddress]\t- Пультовая операция \"Ввод адреса\"\n"
                + "w[rite]\t\t- Пультовая операция \"Запись\"\n"
                + "r[ead]\t\t- Пультовая операция \"Чтение\"\n"
                + "s[tart]\t\t- Пультовая операция \"Пуск\"\n"
                + "c[continue]\t- Пультовая операция \"Продолжить\"\n"
                + "ru[n]\t\t- Переключение режима Работа/Останов\n"
                + "cl[ock]\t\t- Переключение режима потактового выполнения\n"
                + "ma[ddress]\t- Переход на микрокоманду\n"
                + "mw[rite] value\t- Запись микрокоманды\n"
                + "mr[ead]\t\t- Чтение микрокоманды\n"
                + "md[ecode]\t- Декодировать текущую микрокоманду\n"
                + "mdecodea[ll]\t- Декодировать всю микропрограмму\n"
                + "stat[e]\t\t- Вывести регистр состояния БЭВМ\n"
                + "io\t\t- Вывод состояния всех ВУ\n"
                + "io addr\t\t- Вывод состояния указанного ВУ\n"
                + "io addr value\t- Запись value в указанное ВУ\n"
                + "flag addr\t- Установка флага готовности указанного ВУ\n"
                + "asm\t\t- Ввод программы на ассемблере\n"
                + "sleep value\t- Задержка между тактами при фоновом выполнении\n"
                + "{exit|quit}\t- Выход из эмулятора\n"
                + "(0000-FFFF)\t- Ввод шестнадцатеричного значения в клавишный регистр\n"
                + "labelname\t- Ввод адреса метки в клавишный регистр"
            },
    };
}
