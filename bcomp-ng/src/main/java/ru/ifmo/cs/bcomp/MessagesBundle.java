package ru.ifmo.cs.bcomp;

import java.util.ListResourceBundle;

// Key naming: engine.* (simulator core), cli.* (CLI output),
// binary.* (program loader), asm.* (assembler).
public class MessagesBundle extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private static final Object[][] contents = {
            {"engine.busy", "Operation impossible: a program is running"},
    };
}
