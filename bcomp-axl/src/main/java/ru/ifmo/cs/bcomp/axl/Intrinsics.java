package ru.ifmo.cs.bcomp.axl;

import java.util.HashSet;
import java.util.Set;

public final class Intrinsics {

    private static final Set<String> NAMES = new HashSet<String>();

    static {
        String[] all = {
            "inc", "dec", "neg", "not_", "sxtb", "swab", "rol", "ror", "asl", "asr",
            "cla", "clc", "cmc", "nop", "halt", "ei", "di", "iret", "ret",
            "push", "pop", "pushf", "popf", "swap_top",
            "in", "out", "softint", "adc", "swam", "loop_dec",
            "ld_abs", "ld_rel", "ld_ind", "ld_postinc", "ld_predec", "ld_sp", "ld_imm",
            "zero", "notzero", "negative", "positive", "carry", "nocarry", "overflow", "nooverflow"
        };
        for (String s : all) {
            NAMES.add(s);
        }
    }

    public static boolean isIntrinsic(String name) {
        return NAMES.contains(name);
    }

    private static final Ast.Type INT = new Ast.Type(Ast.Base.INT, 0);

    public static Ast.Type gen(Gen g, Ast.Call c) {
        String n = c.callee;
        int argc = c.args.size();

        if (acTransform(n)) {
            need(c, 1);
            g.genArg(c.args.get(0));
            g.ins(acOpcode(n));
            return INT;
        }

        if (n.equals("cla")) { need(c, 0); g.ins("CLA"); return INT; }
        if (n.equals("clc")) { need(c, 0); g.ins("CLC"); return INT; }
        if (n.equals("cmc")) { need(c, 0); g.ins("CMC"); return INT; }
        if (n.equals("nop")) { need(c, 0); g.ins("NOP"); return INT; }
        if (n.equals("halt")) { need(c, 0); g.ins("HLT"); return INT; }
        if (n.equals("ei")) { need(c, 0); g.ins("EI"); return INT; }
        if (n.equals("di")) { need(c, 0); g.ins("DI"); return INT; }
        if (n.equals("iret")) { need(c, 0); g.ins("IRET"); return INT; }
        if (n.equals("ret")) { need(c, 0); g.ins("RET"); return INT; }
        if (n.equals("swap_top")) { need(c, 0); g.ins("SWAP"); return INT; }

        if (n.equals("pop")) { need(c, 0); g.ins("POP"); g.noteStackPop(); return INT; }
        if (n.equals("popf")) { need(c, 0); g.ins("POPF"); g.noteStackPop(); return INT; }
        if (n.equals("pushf")) { need(c, 0); g.ins("PUSHF"); g.noteStackPush(); return INT; }

        if (n.equals("push")) {
            need(c, 1);
            g.genArg(c.args.get(0));
            g.ins("PUSH");
            g.noteStackPush();
            return INT;
        }

        if (n.equals("in")) {
            need(c, 1);
            g.ins("IN " + g.intConst(c.args.get(0)));
            return INT;
        }
        if (n.equals("out")) {
            need(c, 2);
            int dev = g.intConst(c.args.get(0));
            g.genArg(c.args.get(1));
            g.ins("OUT " + dev);
            return INT;
        }
        if (n.equals("softint")) {
            need(c, 1);
            g.ins("INT " + g.intConst(c.args.get(0)));
            return INT;
        }

        if (n.equals("adc")) {
            need(c, 1);
            g.ins("ADC $" + g.globalLabelOf(c.args.get(0)));
            return INT;
        }
        if (n.equals("swam")) {
            need(c, 1);
            g.ins("SWAM $" + g.globalLabelOf(c.args.get(0)));
            return INT;
        }
        if (n.equals("loop_dec")) {
            need(c, 1);
            g.ins("LOOP $" + g.globalLabelOf(c.args.get(0)));
            g.ins("NOP");
            return INT;
        }

        if (n.equals("ld_abs")) { need(c, 1); g.ins("LD $" + g.globalLabelOf(c.args.get(0))); return INT; }
        if (n.equals("ld_rel")) { need(c, 1); g.ins("LD " + g.globalLabelOf(c.args.get(0))); return INT; }
        if (n.equals("ld_ind")) { need(c, 1); g.ins("LD (" + g.globalLabelOf(c.args.get(0)) + ")"); return INT; }
        if (n.equals("ld_postinc")) { need(c, 1); g.ins("LD (" + g.globalLabelOf(c.args.get(0)) + ")+"); return INT; }
        if (n.equals("ld_predec")) { need(c, 1); g.ins("LD -(" + g.globalLabelOf(c.args.get(0)) + ")"); return INT; }
        if (n.equals("ld_sp")) { need(c, 1); g.ins("LD &" + g.intConst(c.args.get(0))); return INT; }
        if (n.equals("ld_imm")) { need(c, 1); g.ins("LD #" + g.intConst(c.args.get(0))); return INT; }

        if (n.equals("zero")) { need(c, 0); return flag(g, "BEQ"); }
        if (n.equals("notzero")) { need(c, 0); return flag(g, "BNE"); }
        if (n.equals("negative")) { need(c, 0); return flag(g, "BMI"); }
        if (n.equals("positive")) { need(c, 0); return flag(g, "BPL"); }
        if (n.equals("carry")) { need(c, 0); return flag(g, "BCS"); }
        if (n.equals("nocarry")) { need(c, 0); return flag(g, "BCC"); }
        if (n.equals("overflow")) { need(c, 0); return flag(g, "BVS"); }
        if (n.equals("nooverflow")) { need(c, 0); return flag(g, "BVC"); }

        throw new CompileException(c.line, "unknown intrinsic " + n);
    }

    private static Ast.Type flag(Gen g, String branch) {
        String lt = g.newLabel();
        String le = g.newLabel();
        g.ins(branch + " " + lt);
        g.ins("LD #0");
        g.ins("BR " + le);
        g.lbl(lt);
        g.ins("LD #1");
        g.lbl(le);
        return INT;
    }

    private static boolean acTransform(String n) {
        return n.equals("inc") || n.equals("dec") || n.equals("neg") || n.equals("not_")
                || n.equals("sxtb") || n.equals("swab") || n.equals("rol") || n.equals("ror")
                || n.equals("asl") || n.equals("asr");
    }

    private static String acOpcode(String n) {
        if (n.equals("inc")) return "INC";
        if (n.equals("dec")) return "DEC";
        if (n.equals("neg")) return "NEG";
        if (n.equals("not_")) return "NOT";
        if (n.equals("sxtb")) return "SXTB";
        if (n.equals("swab")) return "SWAB";
        if (n.equals("rol")) return "ROL";
        if (n.equals("ror")) return "ROR";
        if (n.equals("asl")) return "ASL";
        return "ASR";
    }

    private static void need(Ast.Call c, int count) {
        if (c.args.size() != count) {
            throw new CompileException(c.line, c.callee + " expects " + count + " argument(s)");
        }
    }

    private Intrinsics() {
    }
}
