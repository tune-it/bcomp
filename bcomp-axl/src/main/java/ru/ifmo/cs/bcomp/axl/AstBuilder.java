package ru.ifmo.cs.bcomp.axl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

public final class AstBuilder extends AxlBaseVisitor<Object> {

    public Ast.Program build(AxlParser.ProgramContext ctx) {
        Ast.Program prog = new Ast.Program();
        for (AxlParser.TopDeclContext td : ctx.topDecl()) {
            Object r = visit(td.getChild(0));
            if (r instanceof Ast.Program) {
                prog.decls.addAll(((Ast.Program) r).decls);
            } else {
                prog.decls.add((Ast.Node) r);
            }
        }
        return prog;
    }

    @Override
    public Object visitOrgDecl(AxlParser.OrgDeclContext ctx) {
        Ast.OrgDirective o = new Ast.OrgDirective();
        o.line = ctx.getStart().getLine();
        o.address = parseInt(ctx.INT().getText());
        return o;
    }

    @Override
    public Object visitWordDecl(AxlParser.WordDeclContext ctx) {
        Ast.WordData w = new Ast.WordData();
        w.line = ctx.getStart().getLine();
        w.address = parseInt(ctx.addr.getText());
        for (AxlParser.WordArgContext a : ctx.wordArg()) {
            if (a.ID() != null) {
                Ast.Ident id = new Ast.Ident();
                id.line = w.line;
                id.name = a.ID().getText();
                Ast.Unary u = new Ast.Unary();
                u.line = w.line;
                u.op = "&";
                u.operand = id;
                u.prefix = true;
                w.items.add(u);
            } else {
                Ast.IntLit lit = new Ast.IntLit();
                lit.line = w.line;
                int v = parseInt(a.INT().getText());
                lit.value = hasToken(a, "-") ? -v : v;
                w.items.add(lit);
            }
        }
        return w;
    }

    @Override
    public Object visitFuncDecl(AxlParser.FuncDeclContext ctx) {
        Ast.FuncDecl f = new Ast.FuncDecl();
        f.line = ctx.getStart().getLine();
        f.retType = buildType(ctx.type());
        f.name = ctx.ID().getText();
        if (ctx.params() != null) {
            for (AxlParser.ParamContext p : ctx.params().param()) {
                Ast.Param par = new Ast.Param();
                Ast.Type t = buildType(p.type());
                if (hasToken(p, "[")) {
                    t = t.addrOf();
                }
                par.type = t;
                par.name = p.ID().getText();
                f.params.add(par);
            }
        }
        f.body = (Ast.Block) visit(ctx.block());
        return f;
    }

    @Override
    public Object visitGlobalVar(AxlParser.GlobalVarContext ctx) {
        Ast.Type base = buildType(ctx.type());
        Ast.GlobalVar first = null;
        Ast.Program holder = new Ast.Program();
        for (AxlParser.InitDeclaratorContext d : ctx.initDeclarator()) {
            Ast.GlobalVar g = new Ast.GlobalVar();
            g.line = ctx.getStart().getLine();
            g.type = base;
            g.name = d.ID().getText();
            fillDeclarator(d, g);
            if (first == null) {
                first = g;
            } else {
                holder.decls.add(g);
            }
        }
        if (holder.decls.isEmpty()) {
            return first;
        }
        Ast.Program multi = new Ast.Program();
        multi.decls.add(first);
        multi.decls.addAll(holder.decls);
        return multi;
    }

    private void fillDeclarator(AxlParser.InitDeclaratorContext d, Ast.GlobalVar g) {
        if (hasToken(d, "[")) {
            g.arraySize = d.size != null ? parseInt(d.size.getText()) : 0;
        }
        if (d.initializer() != null) {
            g.init = new java.util.ArrayList<Ast.Expr>();
            for (AxlParser.ExprContext e : d.initializer().expr()) {
                g.init.add((Ast.Expr) visit(e));
            }
            if (g.arraySize == 0) {
                g.arraySize = g.init.size();
            }
        }
    }

    @Override
    public Object visitBlock(AxlParser.BlockContext ctx) {
        Ast.Block b = new Ast.Block();
        b.line = ctx.getStart().getLine();
        for (AxlParser.StatementContext s : ctx.statement()) {
            Ast.Stmt st = buildStatement(s);
            if (st != null) {
                b.stmts.add(st);
            }
        }
        return b;
    }

    @Override
    public Object visitStatement(AxlParser.StatementContext ctx) {
        return buildStatement(ctx);
    }

    private Ast.Stmt buildStatement(AxlParser.StatementContext s) {
        if (s.getChildCount() == 1 && !(s.getChild(0) instanceof ParserRuleContext)) {
            return new Ast.Empty();
        }
        if (s.localVar() != null) {
            return buildLocalVar(s.localVar());
        }
        return (Ast.Stmt) visit(s.getChild(0));
    }

    private Ast.Stmt buildLocalVar(AxlParser.LocalVarContext ctx) {
        Ast.Type base = buildType(ctx.type());
        List<AxlParser.InitDeclaratorContext> ds = ctx.initDeclarator();
        if (ds.size() == 1) {
            return makeLocal(base, ds.get(0), ctx.getStart().getLine());
        }
        Ast.Group group = new Ast.Group();
        group.line = ctx.getStart().getLine();
        for (AxlParser.InitDeclaratorContext d : ds) {
            group.stmts.add(makeLocal(base, d, ctx.getStart().getLine()));
        }
        return group;
    }

    private Ast.LocalVar makeLocal(Ast.Type base, AxlParser.InitDeclaratorContext d, int line) {
        Ast.LocalVar lv = new Ast.LocalVar();
        lv.line = line;
        lv.type = base;
        lv.name = d.ID().getText();
        if (hasToken(d, "[")) {
            lv.arraySize = d.size != null ? parseInt(d.size.getText()) : 0;
        }
        if (d.initializer() != null && !d.initializer().expr().isEmpty()) {
            lv.init = (Ast.Expr) visit(d.initializer().expr(0));
        }
        return lv;
    }

    @Override
    public Object visitIfStmt(AxlParser.IfStmtContext ctx) {
        Ast.If n = new Ast.If();
        n.line = ctx.getStart().getLine();
        n.cond = (Ast.Expr) visit(ctx.expr());
        n.then = (Ast.Stmt) visit(ctx.statement(0));
        if (ctx.statement().size() > 1) {
            n.els = (Ast.Stmt) visit(ctx.statement(1));
        }
        return n;
    }

    @Override
    public Object visitWhileStmt(AxlParser.WhileStmtContext ctx) {
        Ast.While n = new Ast.While();
        n.line = ctx.getStart().getLine();
        n.cond = (Ast.Expr) visit(ctx.expr());
        n.body = (Ast.Stmt) visit(ctx.statement());
        return n;
    }

    @Override
    public Object visitDoWhileStmt(AxlParser.DoWhileStmtContext ctx) {
        Ast.DoWhile n = new Ast.DoWhile();
        n.line = ctx.getStart().getLine();
        n.body = (Ast.Stmt) visit(ctx.statement());
        n.cond = (Ast.Expr) visit(ctx.expr());
        return n;
    }

    @Override
    public Object visitForStmt(AxlParser.ForStmtContext ctx) {
        Ast.For n = new Ast.For();
        n.line = ctx.getStart().getLine();
        if (ctx.forInit() != null) {
            AxlParser.ForInitContext fi = ctx.forInit();
            if (fi.type() != null) {
                Ast.Type base = buildType(fi.type());
                for (AxlParser.InitDeclaratorContext d : fi.initDeclarator()) {
                    n.init.add(makeLocal(base, d, ctx.getStart().getLine()));
                }
            } else {
                for (AxlParser.ExprContext e : fi.expr()) {
                    n.init.add((Ast.Expr) visit(e));
                }
            }
        }
        if (ctx.expr() != null) {
            n.cond = (Ast.Expr) visit(ctx.expr());
        }
        if (ctx.forUpdate() != null) {
            for (AxlParser.ExprContext e : ctx.forUpdate().expr()) {
                n.update.add((Ast.Expr) visit(e));
            }
        }
        n.body = (Ast.Stmt) visit(ctx.statement());
        return n;
    }

    @Override
    public Object visitReturnStmt(AxlParser.ReturnStmtContext ctx) {
        Ast.Return n = new Ast.Return();
        n.line = ctx.getStart().getLine();
        if (ctx.expr() != null) {
            n.value = (Ast.Expr) visit(ctx.expr());
        }
        return n;
    }

    @Override
    public Object visitBreakStmt(AxlParser.BreakStmtContext ctx) {
        Ast.Break n = new Ast.Break();
        n.line = ctx.getStart().getLine();
        return n;
    }

    @Override
    public Object visitContinueStmt(AxlParser.ContinueStmtContext ctx) {
        Ast.Continue n = new Ast.Continue();
        n.line = ctx.getStart().getLine();
        return n;
    }

    @Override
    public Object visitGotoStmt(AxlParser.GotoStmtContext ctx) {
        Ast.Goto n = new Ast.Goto();
        n.line = ctx.getStart().getLine();
        n.label = ctx.ID().getText();
        return n;
    }

    @Override
    public Object visitLabelStmt(AxlParser.LabelStmtContext ctx) {
        Ast.LabelStmt n = new Ast.LabelStmt();
        n.line = ctx.getStart().getLine();
        n.label = ctx.ID().getText();
        return n;
    }

    @Override
    public Object visitExprStmt(AxlParser.ExprStmtContext ctx) {
        Ast.ExprStmt n = new Ast.ExprStmt();
        n.line = ctx.getStart().getLine();
        n.expr = (Ast.Expr) visit(ctx.expr());
        return n;
    }

    @Override
    public Object visitIntLitExpr(AxlParser.IntLitExprContext ctx) {
        Ast.IntLit n = new Ast.IntLit();
        n.line = ctx.getStart().getLine();
        n.value = parseInt(ctx.INT().getText());
        return n;
    }

    @Override
    public Object visitCharLitExpr(AxlParser.CharLitExprContext ctx) {
        Ast.IntLit n = new Ast.IntLit();
        n.line = ctx.getStart().getLine();
        n.value = parseChar(ctx.CHARLIT().getText());
        return n;
    }

    @Override
    public Object visitIdExpr(AxlParser.IdExprContext ctx) {
        Ast.Ident n = new Ast.Ident();
        n.line = ctx.getStart().getLine();
        n.name = ctx.ID().getText();
        return n;
    }

    @Override
    public Object visitParenExpr(AxlParser.ParenExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Object visitCallExpr(AxlParser.CallExprContext ctx) {
        Ast.Call n = new Ast.Call();
        n.line = ctx.getStart().getLine();
        ParseTree target = ctx.expr();
        if (!(target instanceof AxlParser.IdExprContext)) {
            throw new CompileException(n.line, "callee must be a name");
        }
        n.callee = ((AxlParser.IdExprContext) target).ID().getText();
        if (ctx.args() != null) {
            for (AxlParser.ExprContext e : ctx.args().expr()) {
                n.args.add((Ast.Expr) visit(e));
            }
        }
        return n;
    }

    @Override
    public Object visitIndexExpr(AxlParser.IndexExprContext ctx) {
        Ast.Index n = new Ast.Index();
        n.line = ctx.getStart().getLine();
        n.base = (Ast.Expr) visit(ctx.expr(0));
        n.idx = (Ast.Expr) visit(ctx.expr(1));
        return n;
    }

    @Override
    public Object visitPostIncDecExpr(AxlParser.PostIncDecExprContext ctx) {
        return unary(ctx.op.getText(), (Ast.Expr) visit(ctx.expr()), false, ctx.getStart().getLine());
    }

    @Override
    public Object visitPrefixExpr(AxlParser.PrefixExprContext ctx) {
        return unary(ctx.op.getText(), (Ast.Expr) visit(ctx.expr()), true, ctx.getStart().getLine());
    }

    @Override
    public Object visitMulExpr(AxlParser.MulExprContext ctx) {
        return binary(ctx.op.getText(), ctx.expr(0), ctx.expr(1), ctx.getStart().getLine());
    }

    @Override
    public Object visitAddExpr(AxlParser.AddExprContext ctx) {
        return binary(ctx.op.getText(), ctx.expr(0), ctx.expr(1), ctx.getStart().getLine());
    }

    @Override
    public Object visitShiftExpr(AxlParser.ShiftExprContext ctx) {
        return binary(ctx.op.getText(), ctx.expr(0), ctx.expr(1), ctx.getStart().getLine());
    }

    @Override
    public Object visitRelExpr(AxlParser.RelExprContext ctx) {
        return binary(ctx.op.getText(), ctx.expr(0), ctx.expr(1), ctx.getStart().getLine());
    }

    @Override
    public Object visitEqExpr(AxlParser.EqExprContext ctx) {
        return binary(ctx.op.getText(), ctx.expr(0), ctx.expr(1), ctx.getStart().getLine());
    }

    @Override
    public Object visitBitAndExpr(AxlParser.BitAndExprContext ctx) {
        return binary("&", ctx.expr(0), ctx.expr(1), ctx.getStart().getLine());
    }

    @Override
    public Object visitBitXorExpr(AxlParser.BitXorExprContext ctx) {
        return binary("^", ctx.expr(0), ctx.expr(1), ctx.getStart().getLine());
    }

    @Override
    public Object visitBitOrExpr(AxlParser.BitOrExprContext ctx) {
        return binary("|", ctx.expr(0), ctx.expr(1), ctx.getStart().getLine());
    }

    @Override
    public Object visitLogAndExpr(AxlParser.LogAndExprContext ctx) {
        return binary("&&", ctx.expr(0), ctx.expr(1), ctx.getStart().getLine());
    }

    @Override
    public Object visitLogOrExpr(AxlParser.LogOrExprContext ctx) {
        return binary("||", ctx.expr(0), ctx.expr(1), ctx.getStart().getLine());
    }

    @Override
    public Object visitAssignExpr(AxlParser.AssignExprContext ctx) {
        Ast.Assign n = new Ast.Assign();
        n.line = ctx.getStart().getLine();
        n.op = ctx.op.getText();
        n.target = (Ast.Expr) visit(ctx.expr(0));
        n.value = (Ast.Expr) visit(ctx.expr(1));
        return n;
    }

    private Ast.Expr binary(String op, AxlParser.ExprContext l, AxlParser.ExprContext r, int line) {
        Ast.Binary n = new Ast.Binary();
        n.line = line;
        n.op = op;
        n.left = (Ast.Expr) visit(l);
        n.right = (Ast.Expr) visit(r);
        return n;
    }

    private Ast.Expr unary(String op, Ast.Expr operand, boolean prefix, int line) {
        Ast.Unary n = new Ast.Unary();
        n.line = line;
        n.op = op;
        n.operand = operand;
        n.prefix = prefix;
        return n;
    }

    private Ast.Type buildType(AxlParser.TypeContext t) {
        Ast.Base b = parseBase(t.baseType().getText());
        int ptr = 0;
        for (int i = 0; i < t.getChildCount(); i++) {
            if (t.getChild(i).getText().equals("*")) {
                ptr++;
            }
        }
        return new Ast.Type(b, ptr);
    }

    private Ast.Base parseBase(String s) {
        if (s.equals("int")) return Ast.Base.INT;
        if (s.equals("uint")) return Ast.Base.UINT;
        if (s.equals("char")) return Ast.Base.CHAR;
        if (s.equals("long")) return Ast.Base.LONG;
        return Ast.Base.VOID;
    }

    private boolean hasToken(ParserRuleContext ctx, String text) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree c = ctx.getChild(i);
            if (c instanceof TerminalNode && c.getText().equals(text)) {
                return true;
            }
        }
        return false;
    }

    private int parseInt(String s) {
        if (s.length() > 2 && s.charAt(0) == '0' && (s.charAt(1) == 'x' || s.charAt(1) == 'X')) {
            return (int) Long.parseLong(s.substring(2), 16);
        }
        return (int) Long.parseLong(s, 10);
    }

    private int parseChar(String s) {
        String inner = s.substring(1, s.length() - 1);
        if (inner.length() >= 2 && inner.charAt(0) == '\\') {
            char e = inner.charAt(1);
            switch (e) {
                case 'n': return 10;
                case 't': return 9;
                case 'r': return 13;
                case '0': return 0;
                case '\\': return 92;
                case '\'': return 39;
                case '"': return 34;
                default: return e;
            }
        }
        return inner.charAt(0);
    }
}
