package ru.ifmo.cs.bcomp;

import java.util.ListResourceBundle;

public class MessagesBundle_ru extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private static final Object[][] contents = {
            {"engine.busy", "Операция невозможна: выполняется программа"},
    };
}
