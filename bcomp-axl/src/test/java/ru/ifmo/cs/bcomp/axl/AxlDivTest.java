package ru.ifmo.cs.bcomp.axl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AxlDivTest {

    private int run(String body) throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int q;\nvoid main() { " + body + " halt(); }");
        return r.mem("g_q");
    }

    @Test
    public void divPositive() throws Exception {
        assertEquals(14, run("q = 100 / 7;"));
    }

    @Test
    public void modPositive() throws Exception {
        assertEquals(2, run("q = 100 % 7;"));
    }

    @Test
    public void divByLarger() throws Exception {
        assertEquals(0, run("q = 3 / 10;"));
    }

    @Test
    public void modByLarger() throws Exception {
        assertEquals(3, run("q = 3 % 10;"));
    }

    @Test
    public void divNegDividend() throws Exception {
        assertEquals((-4) & 0xFFFF, run("q = -13 / 3;"));
    }

    @Test
    public void modNegDividend() throws Exception {
        assertEquals((-1) & 0xFFFF, run("q = -13 % 3;"));
    }

    @Test
    public void divNegDivisor() throws Exception {
        assertEquals((-4) & 0xFFFF, run("q = 13 / -3;"));
    }

    @Test
    public void modSignFollowsDividend() throws Exception {
        assertEquals(1, run("q = 13 % -3;"));
    }

    @Test
    public void divBothNegative() throws Exception {
        assertEquals(4, run("q = -13 / -3;"));
    }

    @Test
    public void divModInExpression() throws Exception {
        assertEquals(16, run("q = (100 / 7) + (100 % 7);"));
    }

    @Test
    public void divLargeValues() throws Exception {
        assertEquals(1000, run("q = 30000 / 30;"));
    }
}
