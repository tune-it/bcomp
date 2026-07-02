package ru.ifmo.cs.bcomp.axl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AxlRelaxTest {

    @Test
    public void farConditionalBranchRelaxes() throws Exception {
        StringBuilder body = new StringBuilder();
        for (int i = 0; i < 150; i++) {
            body.append("s = s + 1; ");
        }
        String src = "int r;\n"
                + "void main() {\n"
                + "  int i; int s; s = 0; i = 0;\n"
                + "  while (i < 1) { " + body + " i = 1; }\n"
                + "  r = s; halt();\n"
                + "}\n";

        String asm = AxlTestSupport.compileToAsm(src);
        assertTrue("relaxation should introduce a trampoline JUMP", asm.contains("JUMP $L"));

        AxlTestSupport.Result res = AxlTestSupport.run(src);
        assertEquals(150, res.mem("g_r"));
    }

    @Test
    public void farForwardIfRelaxes() throws Exception {
        StringBuilder body = new StringBuilder();
        for (int i = 0; i < 150; i++) {
            body.append("s = s + 2; ");
        }
        String src = "int r;\n"
                + "void main() {\n"
                + "  int s; s = 0;\n"
                + "  if (s == 0) { " + body + " }\n"
                + "  r = s; halt();\n"
                + "}\n";
        AxlTestSupport.Result res = AxlTestSupport.run(src);
        assertEquals(300, res.mem("g_r"));
    }
}
