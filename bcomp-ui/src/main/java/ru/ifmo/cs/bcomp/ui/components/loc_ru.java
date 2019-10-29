package ru.ifmo.cs.bcomp.ui.components;

import java.util.ListResourceBundle;

public class loc_ru extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return  contents;
    }
   private static final Object[][] contents = {
            {"read"," F6 Чтение"},
            {"write","F5 Запись"},
            {"setip","F4 Ввод адреса"},
            {"start","F7 Пуск"},
            {"continue","F8 Продолжение"},
            {"tick","Shift+F9 Такт"},
            {"run","F9  Работа"},
            {"stop","F9 Останов"}

    };
}
