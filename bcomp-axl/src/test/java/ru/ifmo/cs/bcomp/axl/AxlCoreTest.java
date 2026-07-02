package ru.ifmo.cs.bcomp.axl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AxlCoreTest {

    @Test
    public void halt() throws Exception {
        AxlTestSupport.run("void main() { halt(); }");
    }

    @Test
    public void globalArithmetic() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int a; int b; int r;\n"
                + "void main() { a = 7; b = 5; r = a + b - 2; halt(); }");
        assertEquals(10, r.mem("g_r"));
    }

    @Test
    public void bitwise() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int r;\n"
                + "void main() { r = (0x0F0F & 0x00FF) | 0x1000; halt(); }");
        assertEquals(0x100F, r.mem("g_r"));
    }

    @Test
    public void ifElse() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int r;\n"
                + "void main() { int x; x = 8; if (x > 5) r = 1; else r = 2; halt(); }");
        assertEquals(1, r.mem("g_r"));
    }

    @Test
    public void whileSum() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int r;\n"
                + "void main() { int i; int s; i = 1; s = 0;\n"
                + "  while (i <= 10) { s = s + i; i = i + 1; }\n"
                + "  r = s; halt(); }");
        assertEquals(55, r.mem("g_r"));
    }

    @Test
    public void forLoop() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int r;\n"
                + "void main() { int i; int s; s = 0;\n"
                + "  for (i = 0; i < 5; i = i + 1) s = s + i;\n"
                + "  r = s; halt(); }");
        assertEquals(10, r.mem("g_r"));
    }

    @Test
    public void multiply() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int r;\n"
                + "void main() { r = 6 * 7; halt(); }");
        assertEquals(42, r.mem("g_r"));
    }

    @Test
    public void recursionFactorial() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int res;\n"
                + "int fact(int n) { if (n <= 1) return 1; return n * fact(n - 1); }\n"
                + "void main() { res = fact(5); halt(); }");
        assertEquals(120, r.mem("g_res"));
    }

    @Test
    public void arraysAndPointers() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int buf[4]; int s;\n"
                + "void main() { int i; s = 0;\n"
                + "  for (i = 0; i < 4; i = i + 1) buf[i] = i + 1;\n"
                + "  for (i = 0; i < 4; i = i + 1) s = s + buf[i];\n"
                + "  halt(); }");
        assertEquals(10, r.mem("g_s"));
    }

    @Test
    public void pointerDeref() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int v; int r;\n"
                + "void main() { int *p; p = &v; v = 99; r = *p; *p = 5; halt(); }");
        assertEquals(5, r.mem("g_v"));
        assertEquals(99, r.mem("g_r"));
    }

    @Test
    public void intrinsicShift() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int r;\n"
                + "void main() { r = asl(3); halt(); }");
        assertEquals(6, r.mem("g_r"));
    }

    @Test
    public void functionArgs() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int r;\n"
                + "int add3(int a, int b, int c) { return a + b + c; }\n"
                + "void main() { r = add3(10, 20, 12); halt(); }");
        assertEquals(42, r.mem("g_r"));
    }
}
