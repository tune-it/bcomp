package ru.ifmo.cs.bcomp.axl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AxlOrgPlacementTest {

    private int memAt(AxlTestSupport.Result r, int addr) {
        return (int) (r.cpu.getMemory().getValue(addr) & 0xFFFF);
    }

    @Test
    public void defaultOriginIs0x10() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int r;\nvoid main() { r = 1; halt(); }");
        assertEquals(0x10, r.program.start_address);
    }

    @Test
    public void chosenOrigin() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "org 0x100;\nint r;\nvoid main() { r = 42; halt(); }");
        assertEquals(0x100, r.program.start_address);
        assertEquals(42, r.mem("g_r"));
    }

    @Test
    public void wordConstants() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "word 0x00 : 0x10, 0x20, 0x30;\nvoid main() { halt(); }");
        assertEquals(0x10, memAt(r, 0x00));
        assertEquals(0x20, memAt(r, 0x01));
        assertEquals(0x30, memAt(r, 0x02));
    }

    @Test
    public void wordNegativeConstant() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "word 0x04 : -1, 5;\nvoid main() { halt(); }");
        assertEquals(0xFFFF, memAt(r, 0x04));
        assertEquals(5, memAt(r, 0x05));
    }

    @Test
    public void wordAddressOfGlobal() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "int data = 0x1234;\nword 0x06 : &data;\nvoid main() { halt(); }");
        assertEquals(r.program.getLabelAddr("g_data"), memAt(r, 0x06));
    }

    @Test
    public void wordVectorToHandler() throws Exception {
        AxlTestSupport.Result r = AxlTestSupport.run(
                "org 0x100;\n"
                + "word 0x00 : &handler, 0x00;\n"
                + "void handler() { iret(); }\n"
                + "void main() { halt(); }");
        assertEquals(r.program.getLabelAddr("f_handler"), memAt(r, 0x00));
        assertEquals(0x00, memAt(r, 0x01));
    }
}
