package ru.ifmo.cs.bcomp.axl;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class AxlCoverageTest {

    private static final String PROGRAM =
            "int g; int h; int p;\n"
            + "int f(int a, int b) { return a + b; }\n"
            + "void main() {\n"
            + "  int x; int y; x = 3; y = 5;\n"
            + "  g = x + y; g = x - y; g = x & y; g = x | y; g = x ^ y;\n"
            + "  g = x << 1; g = x >> 1; g = ~x; g = -x;\n"
            + "  g = f(x, y);\n"
            + "  if (x < y) g = 1;\n"
            + "  if (x >= y) g = 2;\n"
            + "  g = inc(x); g = dec(x); g = neg(x); g = not_(x);\n"
            + "  g = sxtb(x); g = swab(x);\n"
            + "  g = rol(x); g = ror(x); g = asl(x); g = asr(x);\n"
            + "  cla(); clc(); cmc(); nop();\n"
            + "  adc(g); swam(g); loop_dec(g);\n"
            + "  push(x); pop(); pushf(); popf(); swap_top();\n"
            + "  out(3, x); g = in(2); softint(1); ei(); di(); iret();\n"
            + "  g = zero(); g = notzero(); g = negative(); g = positive();\n"
            + "  g = carry(); g = nocarry(); g = overflow(); g = nooverflow();\n"
            + "  g = ld_imm(7); g = ld_abs(h);\n"
            + "  g = ld_rel(h); g = ld_ind(p); g = ld_postinc(p); g = ld_predec(p);\n"
            + "  g = ld_sp(0);\n"
            + "  halt();\n"
            + "}\n";

    private static final String[] OPCODES = {
        "AND", "OR", "ADD", "ADC", "SUB", "CMP", "LOOP", "LD", "SWAM", "JUMP", "CALL", "ST",
        "NOP", "HLT", "CLA", "NOT", "CLC", "CMC", "ROL", "ROR", "ASL", "ASR", "SXTB", "SWAB",
        "INC", "DEC", "NEG", "POP", "POPF", "RET", "IRET", "PUSH", "PUSHF", "SWAP", "DI", "EI",
        "BEQ", "BNE", "BMI", "BPL", "BCS", "BCC", "BVS", "BVC", "BLT", "BGE", "BR",
        "IN", "OUT", "INT"
    };

    @Test
    public void everyOpcodeIsReachable() {
        String asm = AxlTestSupport.compileToAsm(PROGRAM);
        Set<String> mnemonics = new HashSet<String>();
        for (String raw : asm.split("\n")) {
            String s = raw.trim();
            if (s.isEmpty() || s.endsWith(":")) {
                continue;
            }
            int sp = s.indexOf(' ');
            String head = sp < 0 ? s : s.substring(0, sp);
            mnemonics.add(head);
        }
        StringBuilder missing = new StringBuilder();
        for (String op : OPCODES) {
            if (!mnemonics.contains(op)) {
                missing.append(op).append(' ');
            }
        }
        assertTrue("missing opcodes: " + missing + "\nemitted: " + mnemonics, missing.length() == 0);
    }

    @Test
    public void everyAddressingModeIsReachable() {
        String asm = AxlTestSupport.compileToAsm(PROGRAM);
        assertTrue("DIRECT_LOAD (#)", asm.contains(" #"));
        assertTrue("DIRECT_ABSOLUTE ($)", asm.contains(" $"));
        assertTrue("DISPLACEMENT_SP (&)", asm.contains(" &"));
        assertTrue("DIRECT_RELATIVE", asm.contains("LD g_h"));
        assertTrue("INDIRECT", asm.contains("LD (g_p)"));
        assertTrue("POST_INCREMENT", asm.contains(")+"));
        assertTrue("PRE_DECREMENT", asm.contains("-("));
    }

    @Test
    public void coverageProgramAssemblesCleanly() {
        String asm = AxlTestSupport.compileToAsm(PROGRAM);
        AxlTestSupport.assemble(asm);
    }
}
