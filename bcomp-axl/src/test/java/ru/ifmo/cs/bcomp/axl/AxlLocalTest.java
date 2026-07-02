package ru.ifmo.cs.bcomp.axl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AxlLocalTest {

    @Test
    public void localArraySum() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int r;\n"
                + "void main() {\n"
                + "  int a[5]; int i; int s;\n"
                + "  for (i = 0; i < 5; i = i + 1) a[i] = i;\n"
                + "  s = 0;\n"
                + "  for (i = 0; i < 5; i = i + 1) s = s + a[i];\n"
                + "  r = s; halt();\n"
                + "}");
        assertEquals(10, r.mem("g_r"));
    }

    @Test
    public void addressOfLocal() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int r;\n"
                + "void set5(int *p) { *p = 5; }\n"
                + "void main() { int x; x = 0; set5(&x); r = x; halt(); }");
        assertEquals(5, r.mem("g_r"));
    }

    @Test
    public void localArrayReverse() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int r;\n"
                + "void main() {\n"
                + "  int a[4]; int i;\n"
                + "  for (i = 0; i < 4; i = i + 1) a[i] = i * 10;\n"
                + "  r = a[0] + a[3];\n"
                + "  halt();\n"
                + "}");
        assertEquals(30, r.mem("g_r"));
    }
}
