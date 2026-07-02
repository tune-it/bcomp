package ru.ifmo.cs.bcomp.axl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AxlLongTest {

    @Test
    public void longAddWithCarry() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "long a; long b; long c;\n"
                + "void main() { a = 100000; b = 50000; c = a + b; halt(); }");
        assertEquals(0x49F0, r.mem("g_c"));
        assertEquals(0x0002, r.mem("g_c__hi"));
    }

    @Test
    public void longSubWithBorrow() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "long a; long b; long c;\n"
                + "void main() { a = 65536; b = 1; c = a - b; halt(); }");
        assertEquals(0xFFFF, r.mem("g_c"));
        assertEquals(0x0000, r.mem("g_c__hi"));
    }

    @Test
    public void longWidenNegative() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "long c; int x;\n"
                + "void main() { x = -5; c = x; halt(); }");
        assertEquals(0xFFFB, r.mem("g_c"));
        assertEquals(0xFFFF, r.mem("g_c__hi"));
    }

    @Test
    public void longCompareLess() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int r; long a; long b;\n"
                + "void main() { a = 100000; b = 200000; if (a < b) r = 1; else r = 2; halt(); }");
        assertEquals(1, r.mem("g_r"));
    }

    @Test
    public void longCompareHiEqualLoGreater() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int r; long a; long b;\n"
                + "void main() { a = 0x10005; b = 0x10003; if (a > b) r = 1; else r = 2; halt(); }");
        assertEquals(1, r.mem("g_r"));
    }

    @Test
    public void longCompareEqual() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int r; long a; long b;\n"
                + "void main() { a = 300000; b = 300000; if (a == b) r = 7; else r = 9; halt(); }");
        assertEquals(7, r.mem("g_r"));
    }

    @Test
    public void longLocalAddAndCompare() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int r;\n"
                + "void main() { long a; a = 100000; a = a + a; if (a < 200001) r = 1; else r = 2; halt(); }");
        assertEquals(1, r.mem("g_r"));
    }

    @Test
    public void longCompareWithIntLiteral() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int r; long a;\n"
                + "void main() { a = 70000; if (a > 5) r = 1; else r = 2; halt(); }");
        assertEquals(1, r.mem("g_r"));
    }
}
