package ru.ifmo.cs.bcomp.axl;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AxlDiagnosticsTest {

    private String firstError(String src) {
        AxlCompiler cc = new AxlCompiler();
        String asm = cc.compile(src);
        assertNull("expected a compile error for:\n" + src, asm);
        assertTrue(!cc.getErrors().isEmpty());
        return cc.getErrors().get(0);
    }

    @Test
    public void mainWithArgumentsRejected() {
        String err = firstError("void main(int x) { halt(); }");
        assertTrue(err, err.contains("main"));
    }

    @Test
    public void intrinsicNameAsFunctionRejected() {
        String err = firstError("int halt() { return 1; }\nvoid main() { }");
        assertTrue(err, err.contains("reserved"));
    }

    @Test
    public void normalProgramStillCompiles() {
        AxlCompiler cc = new AxlCompiler();
        String asm = cc.compile("void main() { halt(); }");
        assertNotNull(asm);
        assertTrue(cc.getErrors().isEmpty());
    }
}
