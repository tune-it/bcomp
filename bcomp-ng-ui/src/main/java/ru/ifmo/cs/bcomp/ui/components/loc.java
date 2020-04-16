package ru.ifmo.cs.bcomp.ui.components;

import java.util.ListResourceBundle;

public class loc extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private static final Object[][] contents = {

            {"read", " F6 Read"},
            {"write", "F5 Write"},
            {"setip", "F4 Enter Address"},
            {"start", "F7 Start"},
            {"continue", "F8 Continue"},
            {"tick", "Shift+F9 Tick"},
            {"run", "F9  Run"},
            {"stop", "F9 Stop  "},
            {"basename", "Basic computer"},
            {"assembler", "Assembler"},
            {"output", "Dev-1 (0x2-0x3) Output device"},
            {"input", "Dev-2 (0x4-0x5) Input device"},
            {"IO", "Dev-3 (0x6-0x7) IO device"},
            {"ready", "Ready"},
            {"DEV-1", "Dev 1"},
            {"DEV-2", "Dev 2"},
            {"DEV-3", "Dev 3"},
            {"DEV-4", "Dev 4"},
            {"DEV-5", "Dev 5"},
            {"DEV-6", "Dev 6"},
            {"DEV-7", "Dev 7"},
            {"DEV-8", "Dev 8"},
            {"DEV-9", "Dev 9"},
            {"printer", "Text printer (0xC-0xF)"},
            {"ticker", "Ticker (0x10-0x13)"},
            {"ssd", "Seven segment display (0x14-0x17)"},
            {"kbd", "Keyboard (0x18-0x1B)"},
            {"numpad", "Numpad (0x1C-0x1F)"},
            {"error", "Error"},
            {"stopRunning", "To compile, stop the running program"},
            {"compile", "Compile"}
    };
}
