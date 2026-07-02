package ru.ifmo.cs.bcomp.axl;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class AxlEditorPanelTest {

    @Test
    public void constructs() {
        AxlEditorPanel panel = new AxlEditorPanel(new AxlLoadHandler() {
            @Override
            public void loadBinary(List<Integer> binaryFormat) {
            }
        });
        assertNotNull(panel);
    }
}
