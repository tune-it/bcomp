package ru.ifmo.cs.bcomp.ui.components;

import java.util.ListResourceBundle;

public class loc_ru extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private static final Object[][] contents = {
            {"read", "F6 Чтение"},
            {"write", "F5 Запись"},
            {"setip", "F4 Ввод адреса"},
            {"start", "F7 Пуск"},
            {"continue", "F8 Продолжение"},
            {"tick", "Shift+F9 Такт"},
            {"run", "F9   Работа"},
            {"stop", "F9 Останов"},
            {"basename", "Базовая ЭВМ"},
            {"assembler", "Ассемблер"},
            {"output", "КВУ-1 (0x2-0x3) Контроллер уст-ва вывода"},
            {"input", "КВУ-2 (0x4-0x5) Контроллер уст-ва ввода"},
            {"IO", "КВУ-3 (0x6-0x7) Контроллер уст-ва ввода-вывода"},
            {"ready", "Готов"},
            {"DEV-1", "КВУ 1"},
            {"DEV-2", "КВУ 2"},
            {"DEV-3", "КВУ 3"},
            {"DEV-4", "КВУ 4"},
            {"DEV-5", "ВУ 5"},
            {"DEV-6", "ВУ 6"},
            {"DEV-7", "ВУ 7"},
            {"DEV-8", "ВУ 8"},
            {"DEV-9", "ВУ 9"},
            {"printer", "Текстовый принтер (0xC-0xF)"},
            {"ticker", "Бегущая строка (0x10-0x13)"},
            {"ssd", "Семисегментный индикатор (0x14-0x17)"},
            {"kbd", "Клавиатура (0x18-0x1B)"},
            {"numpad", "Цифровая клавиатура (0x1C-0x1F)"},
            {"error", "Ошибка"},
            {"stopRunning", "Для компиляции остановите выполняющуюся программу"},
            {"compile", "Компилировать"},
            {"cdev", "Контроллер ВУ"},
            {"runstop", "F9 Работа/Останов"}
    };
}
