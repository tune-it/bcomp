package ru.ifmo.cs.bcomp.ui.components;

import java.util.ListResourceBundle;

public class loc_en extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return contents;
    }
   private static final Object[][] contents = {

            {"read"," F6 Read"},
            {"write","F5 Write"},
            {"setip","F4 Enter Address"},
            {"start","F7 Start"},
            {"continue","F8 Continue"},
            {"tick","Shift+F9 Tick"},
            {"run","F9  Run"},
            {"stop","F9 Stop  "},
            {"basename","Basic computer"}
    };
}
