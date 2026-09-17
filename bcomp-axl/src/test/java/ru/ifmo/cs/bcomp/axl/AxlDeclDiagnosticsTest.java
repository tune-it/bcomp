package ru.ifmo.cs.bcomp.axl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AxlDeclDiagnosticsTest {

    private String firstError(String src) {
        AxlCompiler cc = new AxlCompiler();
        String asm = cc.compile(src);
        assertNull("expected a compile error for:\n" + src, asm);
        assertTrue(!cc.getErrors().isEmpty());
        return cc.getErrors().get(0);
    }

    @Test
    public void negativeGlobalInitializer() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int x = -5; int y;\n"
                + "void main() { y = x + 1; halt(); }");
        assertEquals(0xFFFB, r.mem("g_x"));
        assertEquals(0xFFFC, r.mem("g_y"));
    }

    @Test
    public void tooManyArrayInitializers() {
        String err = firstError("int a[2] = {1, 2, 3};\nvoid main() { halt(); }");
        assertTrue(err, err.contains("too many initializers"));
    }

    @Test
    public void orgOutOfRange() {
        String err = firstError("org 0x8000;\nvoid main() { halt(); }");
        assertTrue(err, err.contains("out of range"));
    }

    @Test
    public void wordAddressOutOfRange() {
        String err = firstError("void main() { halt(); }\nword 0x1000 : 1;");
        assertTrue(err, err.contains("out of range"));
    }

    @Test
    public void shiftCountOutOfRange() {
        String err = firstError("int x;\nvoid main() { x = 1 << 40; halt(); }");
        assertTrue(err, err.contains("shift count"));
    }
}
