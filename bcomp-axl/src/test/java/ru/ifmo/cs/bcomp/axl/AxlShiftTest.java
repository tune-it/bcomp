package ru.ifmo.cs.bcomp.axl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AxlShiftTest {

    @Test
    public void variableShiftLeft() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int v;\nvoid main() { int n; n = 3; v = 1 << n; halt(); }");
        assertEquals(8, r.mem("g_v"));
    }

    @Test
    public void variableShiftRightSigned() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int v;\nvoid main() { int n; n = 2; v = -32 >> n; halt(); }");
        assertEquals((-8) & 0xFFFF, r.mem("g_v"));
    }

    @Test
    public void variableShiftRightUnsigned() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "uint v;\nvoid main() { uint x; int n; x = 0x8000; n = 4; v = x >> n; halt(); }");
        assertEquals(0x0800, r.mem("g_v"));
    }

    @Test
    public void constantShiftStillWorks() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int v;\nvoid main() { v = 5 << 2; halt(); }");
        assertEquals(20, r.mem("g_v"));
    }
}
