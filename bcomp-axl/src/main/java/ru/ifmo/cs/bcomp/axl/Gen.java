package ru.ifmo.cs.bcomp.axl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Gen {

    static final class GlobalSym {
        String label;
        String hiLabel;
        Ast.Type type;
        int arraySize;
    }

    static final class FuncSym {
        String label;
        Ast.Type ret;
        List<Ast.Type> params = new ArrayList<Ast.Type>();
        int paramCount;
    }

    static final class LocalSym {
        int baseOffset;
        Ast.Type type;
        String staticLabel;
        String hiLabel;
        int arraySize = -1;
    }

    static final class StaticDef {
        String label;
        int arraySize;
        boolean isLong;
    }

    private final StringBuilder asm = new StringBuilder();
    private final Map<String, GlobalSym> globals = new LinkedHashMap<String, GlobalSym>();
    private final Map<String, FuncSym> funcs = new HashMap<String, FuncSym>();
    private final LinkedList<Map<String, LocalSym>> scopes = new LinkedList<Map<String, LocalSym>>();
    private final Map<String, String> pool = new LinkedHashMap<String, String>();
    private final List<Ast.GlobalVar> globalData = new ArrayList<Ast.GlobalVar>();
    private final List<Ast.WordData> wordBlocks = new ArrayList<Ast.WordData>();

    private int labelCounter = 0;
    private int poolCounter = 0;
    private int tempDepth = 0;
    private int localCount = 0;
    private final Map<Ast.LocalVar, Integer> localSlot = new HashMap<Ast.LocalVar, Integer>();
    private final Map<Ast.LocalVar, String> staticLabelOf = new HashMap<Ast.LocalVar, String>();
    private final List<StaticDef> staticDefs = new ArrayList<StaticDef>();

    private String curFunc;
    private boolean usesPointer;
    private boolean usesMul;
    private boolean usesDiv;
    private boolean usesShl;
    private boolean usesSar;
    private boolean usesShr;
    private boolean usesLong;
    private final LinkedList<String> breakLabels = new LinkedList<String>();
    private final LinkedList<String> continueLabels = new LinkedList<String>();

    private static final Ast.Type INT = new Ast.Type(Ast.Base.INT, 0);

    public String generate(Ast.Program prog) {
        collectTop(prog);
        line("        ORG 0x" + Integer.toHexString(codeOrigin));
        lbl("START");
        ins("CALL $f_main");
        ins("HLT");
        for (Ast.Node n : prog.decls) {
            if (n instanceof Ast.FuncDecl) {
                genFunc((Ast.FuncDecl) n);
            }
        }
        if (usesMul) {
            emitMulRuntime();
        }
        if (usesDiv) {
            emitDivRuntime();
        }
        if (usesShl || usesSar || usesShr) {
            emitShiftRuntime();
        }
        emitData();
        return relax(asm.toString());
    }

    private int codeOrigin = 0x10;
    private int relaxCounter = 0;

    private String relax(String text) {
        List<String> lines = new ArrayList<String>();
        for (String s : text.split("\n", -1)) {
            lines.add(s);
        }
        int guard = 0;
        while (guard++ < 100000) {
            Map<String, Integer> labelAddr = new HashMap<String, Integer>();
            int[] lineAddr = new int[lines.size()];
            int addr = 0x10;
            for (int i = 0; i < lines.size(); i++) {
                String raw = lines.get(i);
                String t = raw.trim();
                lineAddr[i] = addr;
                if (t.isEmpty()) {
                    continue;
                }
                if (t.startsWith("ORG ")) {
                    addr = parseNum(t.substring(4).trim());
                    lineAddr[i] = addr;
                    continue;
                }
                boolean indented = raw.charAt(0) == ' ' || raw.charAt(0) == '\t';
                if (!indented) {
                    int c = t.indexOf(':');
                    if (c >= 0) {
                        labelAddr.put(t.substring(0, c), addr);
                        String rest = t.substring(c + 1).trim();
                        if (!rest.isEmpty()) {
                            addr += wordCount(rest);
                        }
                    }
                } else {
                    addr += 1;
                }
            }
            int idx = -1;
            String[] repl = null;
            for (int i = 0; i < lines.size(); i++) {
                String raw = lines.get(i);
                if (raw.isEmpty() || !(raw.charAt(0) == ' ' || raw.charAt(0) == '\t')) {
                    continue;
                }
                String t = raw.trim();
                String[] parts = t.split("\\s+");
                if (parts.length != 2 || !isRelBranch(parts[0]) || !isBareLabel(parts[1])) {
                    continue;
                }
                Integer ta = labelAddr.get(parts[1]);
                if (ta == null) {
                    continue;
                }
                int disp = ta.intValue() - lineAddr[i] - 1;
                if (disp >= -128 && disp <= 127) {
                    continue;
                }
                if (parts[0].equals("BR")) {
                    repl = new String[]{"        JUMP $" + parts[1]};
                } else {
                    String over = "R" + (relaxCounter++);
                    repl = new String[]{
                        "        " + invBranch(parts[0]) + " " + over,
                        "        JUMP $" + parts[1],
                        over + ":"
                    };
                }
                idx = i;
                break;
            }
            if (idx < 0) {
                break;
            }
            lines.remove(idx);
            for (int k = repl.length - 1; k >= 0; k--) {
                lines.add(idx, repl[k]);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String s : lines) {
            sb.append(s).append('\n');
        }
        return sb.toString();
    }

    private int wordCount(String rest) {
        int sp = rest.indexOf(' ');
        String args = sp < 0 ? "" : rest.substring(sp + 1).trim();
        if (args.isEmpty()) {
            return 1;
        }
        int total = 0;
        for (String part : args.split(",")) {
            String p = part.trim();
            int dup = p.toUpperCase().indexOf("DUP");
            if (dup >= 0) {
                total += parseNum(p.substring(0, dup).trim());
            } else {
                total += 1;
            }
        }
        return total;
    }

    private int parseNum(String s) {
        s = s.trim();
        if (s.length() > 2 && s.charAt(0) == '0' && (s.charAt(1) == 'x' || s.charAt(1) == 'X')) {
            return (int) Long.parseLong(s.substring(2), 16);
        }
        return (int) Long.parseLong(s, 10);
    }

    private static boolean isRelBranch(String m) {
        return m.equals("BEQ") || m.equals("BNE") || m.equals("BMI") || m.equals("BPL")
                || m.equals("BCS") || m.equals("BCC") || m.equals("BVS") || m.equals("BVC")
                || m.equals("BLT") || m.equals("BGE") || m.equals("BR");
    }

    private static boolean isBareLabel(String s) {
        char c = s.charAt(0);
        return Character.isLetter(c) || c == '_';
    }

    private static String invBranch(String m) {
        if (m.equals("BEQ")) return "BNE";
        if (m.equals("BNE")) return "BEQ";
        if (m.equals("BMI")) return "BPL";
        if (m.equals("BPL")) return "BMI";
        if (m.equals("BCS")) return "BCC";
        if (m.equals("BCC")) return "BCS";
        if (m.equals("BVS")) return "BVC";
        if (m.equals("BVC")) return "BVS";
        if (m.equals("BLT")) return "BGE";
        return "BLT";
    }

    private void collectTop(Ast.Program prog) {
        for (Ast.Node n : prog.decls) {
            if (n instanceof Ast.GlobalVar) {
                Ast.GlobalVar g = (Ast.GlobalVar) n;
                if (globals.containsKey(g.name)) {
                    throw new CompileException(g.line, "duplicate global " + g.name);
                }
                GlobalSym s = new GlobalSym();
                s.label = "g_" + g.name;
                s.type = g.type;
                s.arraySize = g.arraySize;
                if (isLong(g.type) && g.arraySize < 0) {
                    s.hiLabel = s.label + "__hi";
                    usesLong = true;
                }
                globals.put(g.name, s);
                globalData.add(g);
            } else if (n instanceof Ast.OrgDirective) {
                codeOrigin = ((Ast.OrgDirective) n).address & 0x7FF;
            } else if (n instanceof Ast.WordData) {
                wordBlocks.add((Ast.WordData) n);
            } else if (n instanceof Ast.FuncDecl) {
                Ast.FuncDecl f = (Ast.FuncDecl) n;
                if (funcs.containsKey(f.name)) {
                    throw new CompileException(f.line, "duplicate function " + f.name);
                }
                if (Intrinsics.isIntrinsic(f.name)) {
                    throw new CompileException(f.line, "function name " + f.name + " is reserved (intrinsic)");
                }
                if (f.name.equals("main") && !f.params.isEmpty()) {
                    throw new CompileException(f.line, "main must take no arguments");
                }
                if (isLong(f.retType)) {
                    throw new CompileException(f.line, "long return values are not supported");
                }
                FuncSym s = new FuncSym();
                s.label = "f_" + f.name;
                s.ret = f.retType;
                for (Ast.Param p : f.params) {
                    if (isLong(p.type)) {
                        throw new CompileException(f.line, "long parameters are not supported");
                    }
                    s.params.add(p.type);
                }
                s.paramCount = f.params.size();
                funcs.put(f.name, s);
            }
        }
        if (!funcs.containsKey("main")) {
            throw new CompileException(0, "function main is required");
        }
    }

    private void genFunc(Ast.FuncDecl f) {
        curFunc = f.name;
        usesPointer = false;
        tempDepth = 0;
        localCount = 0;
        localSlot.clear();
        staticLabelOf.clear();
        Set<String> addrTaken = new HashSet<String>();
        scanAddrTaken(f.body, addrTaken);
        planLocals(f.body, addrTaken);
        int n = f.params.size();
        scopes.clear();
        scopes.push(new HashMap<String, LocalSym>());

        for (int j = 0; j < n; j++) {
            Ast.Param p = f.params.get(j);
            LocalSym ls = new LocalSym();
            ls.baseOffset = localCount + (n - j);
            ls.type = p.type;
            scopes.peek().put(p.name, ls);
        }

        String flabel = "f_" + f.name;
        lbl(flabel);
        if (localCount > 0) {
            ins("CLA");
            for (int i = 0; i < localCount; i++) {
                ins("PUSH");
            }
        }
        genStmt(f.body);
        lbl(flabel + "_ret");
        if (localCount > 0) {
            ins("ST $g__t");
            for (int i = 0; i < localCount; i++) {
                ins("POP");
            }
            ins("LD $g__t");
        }
        ins("RET");
        if (usesPointer) {
            line("p_" + f.name + ":  WORD ?");
        }
        scopes.pop();
    }

    private void planLocals(Ast.Stmt s, Set<String> addrTaken) {
        if (s == null) {
            return;
        }
        if (s instanceof Ast.LocalVar) {
            Ast.LocalVar lv = (Ast.LocalVar) s;
            if (lv.arraySize >= 0 || addrTaken.contains(lv.name) || isLong(lv.type)) {
                String label = "gl_" + curFunc + "_" + lv.name;
                staticLabelOf.put(lv, label);
                StaticDef d = new StaticDef();
                d.label = label;
                d.arraySize = lv.arraySize;
                d.isLong = isLong(lv.type);
                if (d.isLong) {
                    usesLong = true;
                }
                staticDefs.add(d);
            } else {
                localSlot.put(lv, localCount);
                localCount++;
            }
        } else if (s instanceof Ast.Block) {
            for (Ast.Stmt c : ((Ast.Block) s).stmts) {
                planLocals(c, addrTaken);
            }
        } else if (s instanceof Ast.Group) {
            for (Ast.Stmt c : ((Ast.Group) s).stmts) {
                planLocals(c, addrTaken);
            }
        } else if (s instanceof Ast.If) {
            planLocals(((Ast.If) s).then, addrTaken);
            planLocals(((Ast.If) s).els, addrTaken);
        } else if (s instanceof Ast.While) {
            planLocals(((Ast.While) s).body, addrTaken);
        } else if (s instanceof Ast.DoWhile) {
            planLocals(((Ast.DoWhile) s).body, addrTaken);
        } else if (s instanceof Ast.For) {
            Ast.For f = (Ast.For) s;
            for (Ast.Node ini : f.init) {
                if (ini instanceof Ast.LocalVar) {
                    planLocals((Ast.LocalVar) ini, addrTaken);
                }
            }
            planLocals(f.body, addrTaken);
        }
    }

    private void scanAddrTaken(Ast.Stmt s, Set<String> out) {
        if (s == null) {
            return;
        }
        if (s instanceof Ast.Block) {
            for (Ast.Stmt c : ((Ast.Block) s).stmts) {
                scanAddrTaken(c, out);
            }
        } else if (s instanceof Ast.Group) {
            for (Ast.Stmt c : ((Ast.Group) s).stmts) {
                scanAddrTaken(c, out);
            }
        } else if (s instanceof Ast.LocalVar) {
            scanAddrExpr(((Ast.LocalVar) s).init, out);
        } else if (s instanceof Ast.ExprStmt) {
            scanAddrExpr(((Ast.ExprStmt) s).expr, out);
        } else if (s instanceof Ast.If) {
            scanAddrExpr(((Ast.If) s).cond, out);
            scanAddrTaken(((Ast.If) s).then, out);
            scanAddrTaken(((Ast.If) s).els, out);
        } else if (s instanceof Ast.While) {
            scanAddrExpr(((Ast.While) s).cond, out);
            scanAddrTaken(((Ast.While) s).body, out);
        } else if (s instanceof Ast.DoWhile) {
            scanAddrExpr(((Ast.DoWhile) s).cond, out);
            scanAddrTaken(((Ast.DoWhile) s).body, out);
        } else if (s instanceof Ast.For) {
            Ast.For f = (Ast.For) s;
            for (Ast.Node ini : f.init) {
                if (ini instanceof Ast.LocalVar) {
                    scanAddrExpr(((Ast.LocalVar) ini).init, out);
                } else if (ini instanceof Ast.Expr) {
                    scanAddrExpr((Ast.Expr) ini, out);
                }
            }
            scanAddrExpr(f.cond, out);
            for (Ast.Expr u : f.update) {
                scanAddrExpr(u, out);
            }
            scanAddrTaken(f.body, out);
        } else if (s instanceof Ast.Return) {
            scanAddrExpr(((Ast.Return) s).value, out);
        }
    }

    private void scanAddrExpr(Ast.Expr e, Set<String> out) {
        if (e == null) {
            return;
        }
        if (e instanceof Ast.Unary) {
            Ast.Unary u = (Ast.Unary) e;
            if (u.op.equals("&") && u.operand instanceof Ast.Ident) {
                out.add(((Ast.Ident) u.operand).name);
            }
            scanAddrExpr(u.operand, out);
        } else if (e instanceof Ast.Binary) {
            scanAddrExpr(((Ast.Binary) e).left, out);
            scanAddrExpr(((Ast.Binary) e).right, out);
        } else if (e instanceof Ast.Assign) {
            scanAddrExpr(((Ast.Assign) e).target, out);
            scanAddrExpr(((Ast.Assign) e).value, out);
        } else if (e instanceof Ast.Index) {
            scanAddrExpr(((Ast.Index) e).base, out);
            scanAddrExpr(((Ast.Index) e).idx, out);
        } else if (e instanceof Ast.Call) {
            for (Ast.Expr a : ((Ast.Call) e).args) {
                scanAddrExpr(a, out);
            }
        }
    }

    private void genStmt(Ast.Stmt s) {
        if (s instanceof Ast.Block) {
            scopes.push(new HashMap<String, LocalSym>());
            for (Ast.Stmt c : ((Ast.Block) s).stmts) {
                genStmt(c);
            }
            scopes.pop();
        } else if (s instanceof Ast.Group) {
            for (Ast.Stmt c : ((Ast.Group) s).stmts) {
                genStmt(c);
            }
        } else if (s instanceof Ast.LocalVar) {
            genLocalVar((Ast.LocalVar) s);
        } else if (s instanceof Ast.ExprStmt) {
            genExpr(((Ast.ExprStmt) s).expr);
        } else if (s instanceof Ast.If) {
            genIf((Ast.If) s);
        } else if (s instanceof Ast.While) {
            genWhile((Ast.While) s);
        } else if (s instanceof Ast.DoWhile) {
            genDoWhile((Ast.DoWhile) s);
        } else if (s instanceof Ast.For) {
            genFor((Ast.For) s);
        } else if (s instanceof Ast.Return) {
            genReturn((Ast.Return) s);
        } else if (s instanceof Ast.Break) {
            if (breakLabels.isEmpty()) {
                throw new CompileException(s.line, "break outside loop");
            }
            ins("JUMP $" + breakLabels.peek());
        } else if (s instanceof Ast.Continue) {
            if (continueLabels.isEmpty()) {
                throw new CompileException(s.line, "continue outside loop");
            }
            ins("JUMP $" + continueLabels.peek());
        } else if (s instanceof Ast.Goto) {
            ins("JUMP $u_" + curFunc + "_" + ((Ast.Goto) s).label);
        } else if (s instanceof Ast.LabelStmt) {
            lbl("u_" + curFunc + "_" + ((Ast.LabelStmt) s).label);
        } else if (s instanceof Ast.Empty) {
            return;
        } else {
            throw new CompileException(s.line, "unsupported statement");
        }
    }

    private void genLocalVar(Ast.LocalVar lv) {
        LocalSym ls = new LocalSym();
        ls.type = lv.type;
        ls.arraySize = lv.arraySize;
        String stat = staticLabelOf.get(lv);
        if (stat != null) {
            ls.staticLabel = stat;
        } else {
            ls.baseOffset = localCount - 1 - localSlot.get(lv).intValue();
        }
        if (isLong(lv.type)) {
            ls.hiLabel = ls.staticLabel + "__hi";
        }
        scopes.peek().put(lv.name, ls);
        if (lv.init != null) {
            if (isLong(lv.type)) {
                genLongStore(new String[]{ls.staticLabel, ls.hiLabel}, lv.init);
                return;
            }
            if (lv.arraySize >= 0) {
                throw new CompileException(lv.line, "local array initializers are not supported");
            }
            genExpr(lv.init);
            if (ls.staticLabel != null) {
                ins("ST $" + ls.staticLabel);
            } else {
                ins("ST &" + (ls.baseOffset + tempDepth));
            }
        }
    }

    private void genIf(Ast.If n) {
        String els = newLabel();
        genJumpIfFalse(n.cond, els);
        genStmt(n.then);
        if (n.els != null) {
            String end = newLabel();
            ins("JUMP $" + end);
            lbl(els);
            genStmt(n.els);
            lbl(end);
        } else {
            lbl(els);
        }
    }

    private void genWhile(Ast.While n) {
        String top = newLabel();
        String end = newLabel();
        lbl(top);
        genJumpIfFalse(n.cond, end);
        breakLabels.push(end);
        continueLabels.push(top);
        genStmt(n.body);
        breakLabels.pop();
        continueLabels.pop();
        ins("JUMP $" + top);
        lbl(end);
    }

    private void genDoWhile(Ast.DoWhile n) {
        String top = newLabel();
        String end = newLabel();
        String cont = newLabel();
        lbl(top);
        breakLabels.push(end);
        continueLabels.push(cont);
        genStmt(n.body);
        breakLabels.pop();
        continueLabels.pop();
        lbl(cont);
        genJumpIfTrue(n.cond, top);
        lbl(end);
    }

    private void genFor(Ast.For n) {
        scopes.push(new HashMap<String, LocalSym>());
        for (Ast.Node ini : n.init) {
            if (ini instanceof Ast.LocalVar) {
                genLocalVar((Ast.LocalVar) ini);
            } else {
                genExpr((Ast.Expr) ini);
            }
        }
        String top = newLabel();
        String end = newLabel();
        String cont = newLabel();
        lbl(top);
        if (n.cond != null) {
            genJumpIfFalse(n.cond, end);
        }
        breakLabels.push(end);
        continueLabels.push(cont);
        genStmt(n.body);
        breakLabels.pop();
        continueLabels.pop();
        lbl(cont);
        for (Ast.Expr u : n.update) {
            genExpr(u);
        }
        ins("JUMP $" + top);
        lbl(end);
        scopes.pop();
    }

    private void genReturn(Ast.Return n) {
        if (n.value != null) {
            genExpr(n.value);
        }
        ins("JUMP $f_" + curFunc + "_ret");
    }

    private Ast.Type genExpr(Ast.Expr e) {
        if (e instanceof Ast.IntLit) {
            loadConst(((Ast.IntLit) e).value);
            return INT;
        }
        if (e instanceof Ast.Ident) {
            return genLoadLvalue(e);
        }
        if (e instanceof Ast.Index) {
            return genLoadLvalue(e);
        }
        if (e instanceof Ast.Unary) {
            return genUnary((Ast.Unary) e);
        }
        if (e instanceof Ast.Binary) {
            return genBinary((Ast.Binary) e);
        }
        if (e instanceof Ast.Assign) {
            return genAssign((Ast.Assign) e);
        }
        if (e instanceof Ast.Call) {
            return genCall((Ast.Call) e);
        }
        throw new CompileException(e.line, "unsupported expression");
    }

    private Ast.Type genUnary(Ast.Unary u) {
        if (u.op.equals("*")) {
            return genLoadLvalue(u);
        }
        if (u.op.equals("&")) {
            return genAddr(u.operand);
        }
        if (u.op.equals("-")) {
            Ast.Type t = genExpr(u.operand);
            ins("NEG");
            return t;
        }
        if (u.op.equals("+")) {
            return genExpr(u.operand);
        }
        if (u.op.equals("~")) {
            Ast.Type t = genExpr(u.operand);
            ins("NOT");
            return t;
        }
        if (u.op.equals("!")) {
            genBoolNot(u.operand);
            return INT;
        }
        if (u.op.equals("++") || u.op.equals("--")) {
            return genIncDec(u);
        }
        throw new CompileException(u.line, "unsupported unary " + u.op);
    }

    private Ast.Type genIncDec(Ast.Unary u) {
        String delta = u.op.equals("++") ? "INC" : "DEC";
        String undo = u.op.equals("++") ? "DEC" : "INC";
        genLoadLvalue(u.operand);
        ins(delta);
        genStoreToLvalue(u.operand);
        if (!u.prefix) {
            ins(undo);
        }
        return INT;
    }

    private Ast.Type genBinary(Ast.Binary b) {
        String op = b.op;
        if (op.equals("&&") || op.equals("||") || op.equals("==") || op.equals("!=")
                || op.equals("<") || op.equals("<=") || op.equals(">") || op.equals(">=")) {
            genBool(b);
            return INT;
        }
        if (op.equals("*") || op.equals("/") || op.equals("%")) {
            return genMulDiv(b);
        }
        if (op.equals("<<") || op.equals(">>")) {
            return genShift(b);
        }
        if (op.equals("+") && isPointer(b.left)) {
            genExpr(b.left);
            pushAc();
            genExpr(b.right);
            ins("ST $g__t");
            popAc();
            ins("ADD $g__t");
            return typeOf(b.left);
        }
        Ast.Type lt = genExpr(b.left);
        pushAc();
        genExpr(b.right);
        ins("ST $g__t");
        popAc();
        if (op.equals("+")) {
            ins("ADD $g__t");
        } else if (op.equals("-")) {
            ins("SUB $g__t");
        } else if (op.equals("&")) {
            ins("AND $g__t");
        } else if (op.equals("|")) {
            ins("OR $g__t");
        } else if (op.equals("^")) {
            ins("ST $g__t2");
            ins("AND $g__t");
            ins("NOT");
            ins("ST $g__t3");
            ins("LD $g__t2");
            ins("OR $g__t");
            ins("AND $g__t3");
        } else {
            throw new CompileException(b.line, "unsupported operator " + op);
        }
        return lt;
    }

    private Ast.Type callHelper2(String label, Ast.Expr left, Ast.Expr right) {
        genExpr(left);
        pushAc();
        genExpr(right);
        pushAc();
        ins("CALL $" + label);
        ins("ST $g__t");
        ins("POP");
        tempDepth--;
        ins("POP");
        tempDepth--;
        ins("LD $g__t");
        return INT;
    }

    private Ast.Type genMulDiv(Ast.Binary b) {
        if (b.op.equals("*")) {
            usesMul = true;
            return callHelper2("f___mul", b.left, b.right);
        }
        if (b.op.equals("/")) {
            usesDiv = true;
            return callHelper2("f___div", b.left, b.right);
        }
        usesDiv = true;
        return callHelper2("f___mod", b.left, b.right);
    }

    private Ast.Type genShift(Ast.Binary b) {
        Ast.Type lt = typeOf(b.left);
        boolean unsigned = isUnsignedType(lt);
        if (b.right instanceof Ast.IntLit) {
            int k = ((Ast.IntLit) b.right).value;
            genExpr(b.left);
            for (int i = 0; i < k; i++) {
                if (b.op.equals("<<")) {
                    ins("ASL");
                } else if (unsigned) {
                    ins("CLC");
                    ins("ROR");
                } else {
                    ins("ASR");
                }
            }
            return lt;
        }
        String label;
        if (b.op.equals("<<")) {
            usesShl = true;
            label = "f___shl";
        } else if (unsigned) {
            usesShr = true;
            label = "f___shr";
        } else {
            usesSar = true;
            label = "f___sar";
        }
        callHelper2(label, b.left, b.right);
        return lt;
    }

    private Ast.Type genAssign(Ast.Assign a) {
        if (!a.op.equals("=")) {
            String bop = a.op.substring(0, a.op.length() - 1);
            if (isIndirectLvalue(a.target)) {
                return genCompoundIndirect(a, bop);
            }
            Ast.Binary syn = new Ast.Binary();
            syn.line = a.line;
            syn.op = bop;
            syn.left = a.target;
            syn.right = a.value;
            Ast.Assign simple = new Ast.Assign();
            simple.line = a.line;
            simple.op = "=";
            simple.target = a.target;
            simple.value = syn;
            return genAssign(simple);
        }
        if (isLong(typeOf(a.target))) {
            String[] t = longVarLabels(a.target);
            genLongStore(t, a.value);
            return typeOf(a.target);
        }
        genExpr(a.value);
        genStoreToLvalue(a.target);
        return typeOf(a.target);
    }

    private boolean isIndirectLvalue(Ast.Expr e) {
        return e instanceof Ast.Index
                || (e instanceof Ast.Unary && ((Ast.Unary) e).op.equals("*"));
    }

    private void genLvalueAddr(Ast.Expr target) {
        if (target instanceof Ast.Index) {
            genElemAddr((Ast.Index) target);
        } else {
            genExpr(((Ast.Unary) target).operand);
        }
    }

    // address computed once, reused for read and write
    private Ast.Type genCompoundIndirect(Ast.Assign a, String bop) {
        usesPointer = true;
        boolean unsigned = isUnsignedType(typeOf(a.target));
        genLvalueAddr(a.target);
        pushAc();
        ins("ST p_" + curFunc);
        ins("LD (p_" + curFunc + ")");
        applyBinOp(bop, a.value, unsigned);
        ins("ST $g__t");
        popAc();
        ins("ST p_" + curFunc);
        ins("LD $g__t");
        ins("ST (p_" + curFunc + ")");
        return typeOf(a.target);
    }

    private void applyBinOp(String op, Ast.Expr right, boolean unsigned) {
        if (op.equals("*") || op.equals("/") || op.equals("%")) {
            pushAc();
            genExpr(right);
            pushAc();
            String lbl;
            if (op.equals("*")) { usesMul = true; lbl = "f___mul"; }
            else if (op.equals("/")) { usesDiv = true; lbl = "f___div"; }
            else { usesDiv = true; lbl = "f___mod"; }
            ins("CALL $" + lbl);
            ins("ST $g__t");
            ins("POP"); tempDepth--;
            ins("POP"); tempDepth--;
            ins("LD $g__t");
            return;
        }
        if (op.equals("<<") || op.equals(">>")) {
            pushAc();
            genExpr(right);
            pushAc();
            String lbl;
            if (op.equals("<<")) { usesShl = true; lbl = "f___shl"; }
            else if (unsigned) { usesShr = true; lbl = "f___shr"; }
            else { usesSar = true; lbl = "f___sar"; }
            ins("CALL $" + lbl);
            ins("ST $g__t");
            ins("POP"); tempDepth--;
            ins("POP"); tempDepth--;
            ins("LD $g__t");
            return;
        }
        pushAc();
        genExpr(right);
        ins("ST $g__t");
        popAc();
        if (op.equals("+")) {
            ins("ADD $g__t");
        } else if (op.equals("-")) {
            ins("SUB $g__t");
        } else if (op.equals("&")) {
            ins("AND $g__t");
        } else if (op.equals("|")) {
            ins("OR $g__t");
        } else if (op.equals("^")) {
            ins("ST $g__t2");
            ins("AND $g__t");
            ins("NOT");
            ins("ST $g__t3");
            ins("LD $g__t2");
            ins("OR $g__t");
            ins("AND $g__t3");
        } else {
            throw new CompileException(right.line, "unsupported compound operator " + op + "=");
        }
    }

    private void genLongStore(String[] t, Ast.Expr v) {
        usesLong = true;
        if (v instanceof Ast.IntLit) {
            int val = ((Ast.IntLit) v).value;
            loadConst((short) (val & 0xFFFF));
            ins("ST $" + t[0]);
            loadConst((short) ((val >> 16) & 0xFFFF));
            ins("ST $" + t[1]);
            return;
        }
        if (v instanceof Ast.Binary && (((Ast.Binary) v).op.equals("+") || ((Ast.Binary) v).op.equals("-"))) {
            Ast.Binary b = (Ast.Binary) v;
            String[] la = longVarLabels(b.left);
            String[] lb = longVarLabels(b.right);
            if (b.op.equals("+")) {
                ins("LD $" + la[0]);
                ins("ADD $" + lb[0]);
                ins("ST $" + t[0]);
                ins("LD $" + la[1]);
                ins("ADC $" + lb[1]);
                ins("ST $" + t[1]);
            } else {
                ins("LD $" + la[0]);
                ins("SUB $" + lb[0]);
                ins("ST $" + t[0]);
                String nb = newLabel();
                ins("CLA");
                ins("BCS " + nb);
                ins("INC");
                lbl(nb);
                ins("ST $g__lborrow");
                ins("LD $" + la[1]);
                ins("SUB $" + lb[1]);
                ins("ST $g__t");
                ins("LD $g__t");
                ins("SUB $g__lborrow");
                ins("ST $" + t[1]);
            }
            return;
        }
        if (isLong(typeOf(v))) {
            String[] vl = longVarLabels(v);
            ins("LD $" + vl[0]);
            ins("ST $" + t[0]);
            ins("LD $" + vl[1]);
            ins("ST $" + t[1]);
            return;
        }
        widenIntToLong(v, t[0], t[1]);
    }

    private void widenIntToLong(Ast.Expr v, String lo, String hi) {
        usesLong = true;
        boolean uns = isUnsignedType(typeOf(v));
        genExpr(v);
        ins("ST $" + lo);
        ins("CLA");
        ins("ST $" + hi);
        if (!uns) {
            ins("LD $" + lo);
            String done = newLabel();
            ins("BPL " + done);
            ins("LD #-1");
            ins("ST $" + hi);
            lbl(done);
        }
    }

    private String[] longVarLabels(Ast.Expr e) {
        if (e instanceof Ast.Ident) {
            String name = ((Ast.Ident) e).name;
            LocalSym ls = findLocal(name);
            if (ls != null && ls.hiLabel != null) {
                return new String[]{ls.staticLabel, ls.hiLabel};
            }
            GlobalSym gs = globals.get(name);
            if (gs != null && gs.hiLabel != null) {
                return new String[]{gs.label, gs.hiLabel};
            }
        }
        throw new CompileException(e.line, "expected a long variable");
    }

    private String[] longOperand(Ast.Expr e, String sLo, String sHi) {
        if (e instanceof Ast.Ident) {
            String name = ((Ast.Ident) e).name;
            LocalSym ls = findLocal(name);
            if (ls != null && ls.hiLabel != null) {
                return new String[]{ls.staticLabel, ls.hiLabel};
            }
            GlobalSym gs = globals.get(name);
            if (gs != null && gs.hiLabel != null) {
                return new String[]{gs.label, gs.hiLabel};
            }
        }
        if (e instanceof Ast.IntLit) {
            int val = ((Ast.IntLit) e).value;
            loadConst((short) (val & 0xFFFF));
            ins("ST $" + sLo);
            loadConst((short) ((val >> 16) & 0xFFFF));
            ins("ST $" + sHi);
            return new String[]{sLo, sHi};
        }
        widenIntToLong(e, sLo, sHi);
        return new String[]{sLo, sHi};
    }

    private void genLongCmp3(Ast.Expr a, Ast.Expr b) {
        usesLong = true;
        String[] la = longOperand(a, "g__lA_lo", "g__lA_hi");
        String[] lb = longOperand(b, "g__lB_lo", "g__lB_hi");
        String neg = newLabel();
        String pos = newLabel();
        String zero = newLabel();
        String end = newLabel();
        ins("LD $" + la[1]);
        ins("CMP $" + lb[1]);
        ins("BLT " + neg);
        ins("BNE " + pos);
        ins("LD $" + la[0]);
        ins("CMP $" + lb[0]);
        ins("BEQ " + zero);
        ins("BCC " + neg);
        lbl(pos);
        ins("LD #1");
        ins("BR " + end);
        lbl(neg);
        ins("LD #-1");
        ins("BR " + end);
        lbl(zero);
        ins("LD #0");
        lbl(end);
    }

    private boolean isLong(Ast.Type t) {
        return t != null && t.base == Ast.Base.LONG && t.ptr == 0;
    }

    private Ast.Type genCall(Ast.Call c) {
        if (Intrinsics.isIntrinsic(c.callee)) {
            return Intrinsics.gen(this, c);
        }
        FuncSym fs = funcs.get(c.callee);
        if (fs == null) {
            throw new CompileException(c.line, "unknown function " + c.callee);
        }
        int n = c.args.size();
        for (int i = 0; i < n; i++) {
            genExpr(c.args.get(i));
            pushAc();
        }
        ins("CALL $" + fs.label);
        if (n > 0) {
            ins("ST $g__t");
            for (int i = 0; i < n; i++) {
                ins("POP");
                tempDepth--;
            }
            ins("LD $g__t");
        }
        return fs.ret;
    }

    private Ast.Type genLoadLvalue(Ast.Expr e) {
        if (e instanceof Ast.Ident) {
            String name = ((Ast.Ident) e).name;
            LocalSym ls = findLocal(name);
            if (ls != null) {
                if (ls.staticLabel != null) {
                    if (ls.arraySize >= 0) {
                        ins("LD $" + kAddr(ls.staticLabel));
                        return ls.type.addrOf();
                    }
                    ins("LD $" + ls.staticLabel);
                    return ls.type;
                }
                ins("LD &" + (ls.baseOffset + tempDepth));
                return ls.type;
            }
            GlobalSym gs = globals.get(name);
            if (gs != null) {
                if (gs.arraySize >= 0) {
                    ins("LD $" + kAddr(gs.label));
                    return gs.type.addrOf();
                }
                ins("LD $" + gs.label);
                return gs.type;
            }
            throw new CompileException(e.line, "unknown identifier " + name);
        }
        if (e instanceof Ast.Unary && ((Ast.Unary) e).op.equals("*")) {
            Ast.Type pt = genExpr(((Ast.Unary) e).operand);
            usesPointer = true;
            ins("ST p_" + curFunc);
            ins("LD (p_" + curFunc + ")");
            return pt.isPointer() ? pt.deref() : INT;
        }
        if (e instanceof Ast.Index) {
            Ast.Index ix = (Ast.Index) e;
            Ast.Type pt = genElemAddr(ix);
            usesPointer = true;
            ins("ST p_" + curFunc);
            ins("LD (p_" + curFunc + ")");
            return pt;
        }
        throw new CompileException(e.line, "expression is not an lvalue");
    }

    private void genStoreToLvalue(Ast.Expr e) {
        if (e instanceof Ast.Ident) {
            String name = ((Ast.Ident) e).name;
            LocalSym ls = findLocal(name);
            if (ls != null) {
                if (ls.arraySize >= 0) {
                    throw new CompileException(e.line, "cannot assign to array " + name);
                }
                if (ls.staticLabel != null) {
                    ins("ST $" + ls.staticLabel);
                } else {
                    ins("ST &" + (ls.baseOffset + tempDepth));
                }
                return;
            }
            GlobalSym gs = globals.get(name);
            if (gs != null) {
                if (gs.arraySize >= 0) {
                    throw new CompileException(e.line, "cannot assign to array " + name);
                }
                ins("ST $" + gs.label);
                return;
            }
            throw new CompileException(e.line, "unknown identifier " + name);
        }
        if (e instanceof Ast.Unary && ((Ast.Unary) e).op.equals("*")) {
            usesPointer = true;
            pushAc();
            genExpr(((Ast.Unary) e).operand);
            ins("ST p_" + curFunc);
            popAc();
            ins("ST (p_" + curFunc + ")");
            return;
        }
        if (e instanceof Ast.Index) {
            usesPointer = true;
            pushAc();
            genElemAddr((Ast.Index) e);
            ins("ST p_" + curFunc);
            popAc();
            ins("ST (p_" + curFunc + ")");
            return;
        }
        throw new CompileException(e.line, "expression is not an lvalue");
    }

    private Ast.Type genElemAddr(Ast.Index ix) {
        Ast.Type bt = genExpr(ix.base);
        pushAc();
        genExpr(ix.idx);
        ins("ST $g__t");
        popAc();
        ins("ADD $g__t");
        return bt.isPointer() ? bt.deref() : INT;
    }

    private Ast.Type genAddr(Ast.Expr e) {
        if (e instanceof Ast.Ident) {
            String name = ((Ast.Ident) e).name;
            LocalSym ls = findLocal(name);
            if (ls != null) {
                if (ls.staticLabel != null) {
                    ins("LD $" + kAddr(ls.staticLabel));
                    return ls.type.addrOf();
                }
                throw new CompileException(e.line, "cannot take address of local " + name);
            }
            GlobalSym gs = globals.get(name);
            if (gs != null) {
                ins("LD $" + kAddr(gs.label));
                return gs.type.addrOf();
            }
            throw new CompileException(e.line, "unknown identifier " + name);
        }
        if (e instanceof Ast.Unary && ((Ast.Unary) e).op.equals("*")) {
            return genExpr(((Ast.Unary) e).operand);
        }
        if (e instanceof Ast.Index) {
            return genElemAddr((Ast.Index) e).addrOf();
        }
        throw new CompileException(e.line, "cannot take address of expression");
    }

    private void genBool(Ast.Expr cond) {
        String lf = newLabel();
        String le = newLabel();
        genJumpIfFalse(cond, lf);
        ins("LD #1");
        ins("BR " + le);
        lbl(lf);
        ins("LD #0");
        lbl(le);
    }

    private void genBoolNot(Ast.Expr operand) {
        String lt = newLabel();
        String le = newLabel();
        genJumpIfFalse(operand, lt);
        ins("LD #0");
        ins("BR " + le);
        lbl(lt);
        ins("LD #1");
        lbl(le);
    }

    private void genJumpIfFalse(Ast.Expr cond, String label) {
        if (cond instanceof Ast.Unary && ((Ast.Unary) cond).op.equals("!")) {
            genJumpIfTrue(((Ast.Unary) cond).operand, label);
            return;
        }
        if (cond instanceof Ast.Binary) {
            Ast.Binary b = (Ast.Binary) cond;
            if (b.op.equals("&&")) {
                genJumpIfFalse(b.left, label);
                genJumpIfFalse(b.right, label);
                return;
            }
            if (b.op.equals("||")) {
                String keep = newLabel();
                genJumpIfTrue(b.left, keep);
                genJumpIfFalse(b.right, label);
                lbl(keep);
                return;
            }
            if (isRelational(b.op)) {
                if (isLong(typeOf(b.left)) || isLong(typeOf(b.right))) {
                    genLongCmp3(b.left, b.right);
                    ins("CMP #0");
                    transferWhen(negate(b.op), false, label);
                    return;
                }
                genCompare(b);
                transferWhen(negate(b.op), isUnsignedCmp(b), label);
                return;
            }
        }
        genExpr(cond);
        ins("CMP #0");
        transferWhen("==", false, label);
    }

    private void genJumpIfTrue(Ast.Expr cond, String label) {
        if (cond instanceof Ast.Unary && ((Ast.Unary) cond).op.equals("!")) {
            genJumpIfFalse(((Ast.Unary) cond).operand, label);
            return;
        }
        if (cond instanceof Ast.Binary) {
            Ast.Binary b = (Ast.Binary) cond;
            if (b.op.equals("&&")) {
                String keep = newLabel();
                genJumpIfFalse(b.left, keep);
                genJumpIfTrue(b.right, label);
                lbl(keep);
                return;
            }
            if (b.op.equals("||")) {
                genJumpIfTrue(b.left, label);
                genJumpIfTrue(b.right, label);
                return;
            }
            if (isRelational(b.op)) {
                if (isLong(typeOf(b.left)) || isLong(typeOf(b.right))) {
                    genLongCmp3(b.left, b.right);
                    ins("CMP #0");
                    transferWhen(b.op, false, label);
                    return;
                }
                genCompare(b);
                transferWhen(b.op, isUnsignedCmp(b), label);
                return;
            }
        }
        genExpr(cond);
        ins("CMP #0");
        transferWhen("!=", false, label);
    }

    private void genCompare(Ast.Binary b) {
        genExpr(b.left);
        pushAc();
        genExpr(b.right);
        ins("ST $g__t");
        popAc();
        ins("CMP $g__t");
    }

    private void transferWhen(String cond, boolean unsigned, String target) {
        branchWhen(cond, unsigned, target);
    }

    private void branchWhen(String cond, boolean unsigned, String near) {
        if (cond.equals("==")) {
            ins("BEQ " + near);
        } else if (cond.equals("!=")) {
            ins("BNE " + near);
        } else if (cond.equals("<")) {
            ins(unsigned ? "BCC " + near : "BLT " + near);
        } else if (cond.equals(">=")) {
            ins(unsigned ? "BCS " + near : "BGE " + near);
        } else if (cond.equals("<=")) {
            if (unsigned) {
                ins("BCC " + near);
            } else {
                ins("BLT " + near);
            }
            ins("BEQ " + near);
        } else if (cond.equals(">")) {
            String over = newLabel();
            ins("BEQ " + over);
            ins(unsigned ? "BCS " + near : "BGE " + near);
            lbl(over);
        } else {
            throw new CompileException(0, "bad relation " + cond);
        }
    }

    private static boolean isRelational(String op) {
        return op.equals("==") || op.equals("!=") || op.equals("<") || op.equals("<=")
                || op.equals(">") || op.equals(">=");
    }

    private static String negate(String op) {
        if (op.equals("==")) return "!=";
        if (op.equals("!=")) return "==";
        if (op.equals("<")) return ">=";
        if (op.equals(">=")) return "<";
        if (op.equals(">")) return "<=";
        if (op.equals("<=")) return ">";
        throw new CompileException(0, "cannot negate " + op);
    }

    private boolean isUnsignedCmp(Ast.Binary b) {
        return isUnsignedType(typeOf(b.left)) || isUnsignedType(typeOf(b.right));
    }

    private static boolean isUnsignedType(Ast.Type t) {
        return t != null && (t.isUnsigned() || t.isPointer());
    }

    private boolean isPointer(Ast.Expr e) {
        Ast.Type t = typeOf(e);
        return t != null && t.isPointer();
    }

    private Ast.Type typeOf(Ast.Expr e) {
        if (e instanceof Ast.IntLit) {
            return INT;
        }
        if (e instanceof Ast.Ident) {
            String name = ((Ast.Ident) e).name;
            LocalSym ls = findLocal(name);
            if (ls != null) {
                return ls.arraySize >= 0 ? ls.type.addrOf() : ls.type;
            }
            GlobalSym gs = globals.get(name);
            if (gs != null) {
                return gs.arraySize >= 0 ? gs.type.addrOf() : gs.type;
            }
            return INT;
        }
        if (e instanceof Ast.Unary) {
            Ast.Unary u = (Ast.Unary) e;
            if (u.op.equals("&")) {
                Ast.Type t = typeOf(u.operand);
                return t == null ? INT : t.addrOf();
            }
            if (u.op.equals("*")) {
                Ast.Type t = typeOf(u.operand);
                return t != null && t.isPointer() ? t.deref() : INT;
            }
            return typeOf(u.operand);
        }
        if (e instanceof Ast.Index) {
            Ast.Type t = typeOf(((Ast.Index) e).base);
            return t != null && t.isPointer() ? t.deref() : INT;
        }
        if (e instanceof Ast.Binary) {
            Ast.Binary b = (Ast.Binary) e;
            if (b.op.equals("+") || b.op.equals("-")) {
                Ast.Type lt = typeOf(b.left);
                if (lt != null && lt.isPointer()) {
                    return lt;
                }
            }
            return INT;
        }
        if (e instanceof Ast.Assign) {
            return typeOf(((Ast.Assign) e).target);
        }
        if (e instanceof Ast.Call) {
            FuncSym fs = funcs.get(((Ast.Call) e).callee);
            return fs != null ? fs.ret : INT;
        }
        return INT;
    }

    private LocalSym findLocal(String name) {
        for (Map<String, LocalSym> sc : scopes) {
            LocalSym ls = sc.get(name);
            if (ls != null) {
                return ls;
            }
        }
        return null;
    }

    void pushAc() {
        ins("PUSH");
        tempDepth++;
    }

    void popAc() {
        ins("POP");
        tempDepth--;
    }

    void loadConst(int v) {
        short s = (short) v;
        if (s >= -128 && s <= 127) {
            ins("LD #" + (int) s);
        } else {
            ins("LD $" + kConst(v));
        }
    }

    private String kConst(int v) {
        String key = String.format("0x%04X", v & 0xFFFF);
        return poolEntry(key);
    }

    String kAddr(String label) {
        return poolEntry("$" + label);
    }

    private String poolEntry(String arg) {
        String existing = pool.get(arg);
        if (existing != null) {
            return existing;
        }
        String label = "K" + (poolCounter++);
        pool.put(arg, label);
        return label;
    }

    private void emitData() {
        for (Ast.GlobalVar g : globalData) {
            emitGlobalWord(g, globals.get(g.name));
        }
        for (StaticDef d : staticDefs) {
            if (d.isLong) {
                line(d.label + ":  WORD ?");
                line(d.label + "__hi:  WORD ?");
            } else if (d.arraySize >= 0) {
                int sz = d.arraySize == 0 ? 1 : d.arraySize;
                line(d.label + ":  WORD " + sz + " DUP (?)");
            } else {
                line(d.label + ":  WORD ?");
            }
        }
        line("g__t:  WORD ?");
        line("g__t2:  WORD ?");
        line("g__t3:  WORD ?");
        if (usesLong) {
            line("g__lborrow:  WORD ?");
            line("g__lA_lo:  WORD ?");
            line("g__lA_hi:  WORD ?");
            line("g__lB_lo:  WORD ?");
            line("g__lB_hi:  WORD ?");
        }
        for (Map.Entry<String, String> e : pool.entrySet()) {
            line(e.getValue() + ":  WORD " + e.getKey());
        }
        for (Ast.WordData w : wordBlocks) {
            StringBuilder vals = new StringBuilder();
            for (int i = 0; i < w.items.size(); i++) {
                if (i > 0) {
                    vals.append(",");
                }
                vals.append(wordArgText(w.items.get(i)));
            }
            line("        ORG 0x" + Integer.toHexString(w.address & 0x7FF));
            line("        WORD " + vals);
        }
    }

    private String wordArgText(Ast.Expr e) {
        if (e instanceof Ast.IntLit) {
            return String.format("0x%04X", ((Ast.IntLit) e).value & 0xFFFF);
        }
        if (e instanceof Ast.Unary && ((Ast.Unary) e).op.equals("&")
                && ((Ast.Unary) e).operand instanceof Ast.Ident) {
            String name = ((Ast.Ident) ((Ast.Unary) e).operand).name;
            GlobalSym gs = globals.get(name);
            if (gs != null) {
                return "$" + gs.label;
            }
            FuncSym fs = funcs.get(name);
            if (fs != null) {
                return "$" + fs.label;
            }
            throw new CompileException(e.line, "unknown name in word directive: " + name);
        }
        throw new CompileException(e.line, "word items must be constants or &name");
    }

    private void emitGlobalWord(Ast.GlobalVar g, GlobalSym s) {
        if (s.hiLabel != null) {
            int val = 0;
            if (g.init != null && !g.init.isEmpty() && g.init.get(0) instanceof Ast.IntLit) {
                val = ((Ast.IntLit) g.init.get(0)).value;
            }
            line(s.label + ":  WORD " + String.format("0x%04X", val & 0xFFFF));
            line(s.hiLabel + ":  WORD " + String.format("0x%04X", (val >> 16) & 0xFFFF));
            return;
        }
        if (g.arraySize >= 0) {
            if (g.init != null && !g.init.isEmpty()) {
                StringBuilder vals = new StringBuilder();
                for (int i = 0; i < g.init.size(); i++) {
                    if (i > 0) {
                        vals.append(",");
                    }
                    vals.append(constText(g.init.get(i)));
                }
                int rest = g.arraySize - g.init.size();
                if (rest > 0) {
                    vals.append(",").append(rest).append(" DUP (?)");
                }
                line(s.label + ":  WORD " + vals);
            } else {
                int sz = g.arraySize == 0 ? 1 : g.arraySize;
                line(s.label + ":  WORD " + sz + " DUP (?)");
            }
        } else if (g.init != null && !g.init.isEmpty()) {
            line(s.label + ":  WORD " + constText(g.init.get(0)));
        } else {
            line(s.label + ":  WORD ?");
        }
    }

    private String constText(Ast.Expr e) {
        if (e instanceof Ast.IntLit) {
            return String.format("0x%04X", ((Ast.IntLit) e).value & 0xFFFF);
        }
        if (e instanceof Ast.Unary && ((Ast.Unary) e).op.equals("&")
                && ((Ast.Unary) e).operand instanceof Ast.Ident) {
            String name = ((Ast.Ident) ((Ast.Unary) e).operand).name;
            GlobalSym gs = globals.get(name);
            if (gs != null) {
                return "$" + gs.label;
            }
        }
        throw new CompileException(e.line, "global initializer must be a constant");
    }

    private void emitMulRuntime() {
        lbl("f___mul");
        ins("CLA");
        ins("ST $g__mul_r");
        ins("LD #1");
        ins("ST $g__mul_bit");
        ins("CLA");
        ins("ST $g__mul_i");
        lbl("f___mul_top");
        ins("LD $g__mul_i");
        ins("CMP #16");
        ins("BLT f___mul_cont");
        ins("JUMP $f___mul_end");
        lbl("f___mul_cont");
        ins("LD &1");
        ins("AND $g__mul_bit");
        ins("BEQ f___mul_noadd");
        ins("LD $g__mul_r");
        ins("ADD &2");
        ins("ST $g__mul_r");
        lbl("f___mul_noadd");
        ins("LD &2");
        ins("ADD &2");
        ins("ST &2");
        ins("LD $g__mul_bit");
        ins("ADD $g__mul_bit");
        ins("ST $g__mul_bit");
        ins("LD $g__mul_i");
        ins("INC");
        ins("ST $g__mul_i");
        ins("JUMP $f___mul_top");
        lbl("f___mul_end");
        ins("LD $g__mul_r");
        ins("RET");
        line("g__mul_r:  WORD ?");
        line("g__mul_bit:  WORD ?");
        line("g__mul_i:  WORD ?");
    }

    private void emitDivRuntime() {
        lbl("f___div");
        ins("CLA");
        ins("ST $g__nc");
        ins("LD &2");
        ins("BPL f___div_ap");
        ins("NEG");
        ins("ST $g__ua");
        ins("LD $g__nc");
        ins("INC");
        ins("ST $g__nc");
        ins("JUMP $f___div_a2");
        lbl("f___div_ap");
        ins("LD &2");
        ins("ST $g__ua");
        lbl("f___div_a2");
        ins("LD &1");
        ins("BPL f___div_bp");
        ins("NEG");
        ins("ST $g__ub");
        ins("LD $g__nc");
        ins("INC");
        ins("ST $g__nc");
        ins("JUMP $f___div_b2");
        lbl("f___div_bp");
        ins("LD &1");
        ins("ST $g__ub");
        lbl("f___div_b2");
        ins("LD $g__ua");
        ins("PUSH");
        ins("LD $g__ub");
        ins("PUSH");
        ins("CALL $f___udivmod");
        ins("POP");
        ins("POP");
        ins("LD $g__nc");
        ins("AND #1");
        ins("BEQ f___div_done");
        ins("LD $g__q");
        ins("NEG");
        ins("ST $g__q");
        lbl("f___div_done");
        ins("LD $g__q");
        ins("RET");

        lbl("f___mod");
        ins("CLA");
        ins("ST $g__nc");
        ins("LD &2");
        ins("BPL f___mod_ap");
        ins("NEG");
        ins("ST $g__ua");
        ins("LD #1");
        ins("ST $g__nc");
        ins("JUMP $f___mod_a2");
        lbl("f___mod_ap");
        ins("LD &2");
        ins("ST $g__ua");
        lbl("f___mod_a2");
        ins("LD &1");
        ins("BPL f___mod_bp");
        ins("NEG");
        ins("ST $g__ub");
        ins("JUMP $f___mod_b2");
        lbl("f___mod_bp");
        ins("LD &1");
        ins("ST $g__ub");
        lbl("f___mod_b2");
        ins("LD $g__ua");
        ins("PUSH");
        ins("LD $g__ub");
        ins("PUSH");
        ins("CALL $f___udivmod");
        ins("POP");
        ins("POP");
        ins("LD $g__nc");
        ins("BEQ f___mod_done");
        ins("LD $g__r");
        ins("NEG");
        ins("ST $g__r");
        lbl("f___mod_done");
        ins("LD $g__r");
        ins("RET");

        lbl("f___udivmod");
        ins("CLA");
        ins("ST $g__q");
        ins("CLA");
        ins("ST $g__r");
        ins("LD $g__c8000");
        ins("ST $g__mask");
        lbl("f___udivmod_top");
        ins("LD $g__mask");
        ins("BEQ f___udivmod_end");
        ins("LD $g__r");
        ins("ADD $g__r");
        ins("ST $g__r");
        ins("LD &2");
        ins("AND $g__mask");
        ins("BEQ f___udivmod_nobit");
        ins("LD $g__r");
        ins("INC");
        ins("ST $g__r");
        lbl("f___udivmod_nobit");
        ins("LD $g__r");
        ins("CMP &1");
        ins("BCC f___udivmod_nosub");
        ins("LD $g__r");
        ins("SUB &1");
        ins("ST $g__r");
        ins("LD $g__q");
        ins("ADD $g__mask");
        ins("ST $g__q");
        lbl("f___udivmod_nosub");
        ins("LD $g__mask");
        ins("CLC");
        ins("ROR");
        ins("ST $g__mask");
        ins("JUMP $f___udivmod_top");
        lbl("f___udivmod_end");
        ins("RET");

        line("g__q:  WORD ?");
        line("g__r:  WORD ?");
        line("g__mask:  WORD ?");
        line("g__ua:  WORD ?");
        line("g__ub:  WORD ?");
        line("g__nc:  WORD ?");
        line("g__c8000:  WORD 0x8000");
    }

    private void emitShiftRuntime() {
        if (usesShl) {
            emitShiftHelper("f___shl", "ASL", false);
        }
        if (usesSar) {
            emitShiftHelper("f___sar", "ASR", false);
        }
        if (usesShr) {
            emitShiftHelper("f___shr", null, true);
        }
        line("g__sh_v:  WORD ?");
        line("g__sh_c:  WORD ?");
    }

    private void emitShiftHelper(String label, String op, boolean logical) {
        lbl(label);
        ins("LD &2");
        ins("ST $g__sh_v");
        ins("LD &1");
        ins("ST $g__sh_c");
        lbl(label + "_top");
        ins("LD $g__sh_c");
        ins("CMP #1");
        ins("BLT " + label + "_done");
        ins("LD $g__sh_v");
        if (logical) {
            ins("CLC");
            ins("ROR");
        } else {
            ins(op);
        }
        ins("ST $g__sh_v");
        ins("LD $g__sh_c");
        ins("DEC");
        ins("ST $g__sh_c");
        ins("JUMP $" + label + "_top");
        lbl(label + "_done");
        ins("LD $g__sh_v");
        ins("RET");
    }

    private void line(String s) {
        asm.append(s).append('\n');
    }

    void ins(String s) {
        asm.append("        ").append(s).append('\n');
    }

    void lbl(String s) {
        asm.append(s).append(":").append('\n');
    }

    String newLabel() {
        return "L" + (labelCounter++);
    }

    void markPointerUse() {
        usesPointer = true;
    }

    String currentFunc() {
        return curFunc;
    }

    Ast.Type genArg(Ast.Expr e) {
        return genExpr(e);
    }

    void noteStackPush() {
        tempDepth++;
    }

    void noteStackPop() {
        tempDepth--;
    }

    String globalLabelOf(Ast.Expr e) {
        if (e instanceof Ast.Ident) {
            GlobalSym gs = globals.get(((Ast.Ident) e).name);
            if (gs != null) {
                return gs.label;
            }
        }
        throw new CompileException(e.line, "expected a global variable");
    }

    int intConst(Ast.Expr e) {
        if (e instanceof Ast.IntLit) {
            return ((Ast.IntLit) e).value;
        }
        if (e instanceof Ast.Unary && ((Ast.Unary) e).op.equals("-")
                && ((Ast.Unary) e).operand instanceof Ast.IntLit) {
            return -((Ast.IntLit) ((Ast.Unary) e).operand).value;
        }
        throw new CompileException(e.line, "expected a constant");
    }
}
