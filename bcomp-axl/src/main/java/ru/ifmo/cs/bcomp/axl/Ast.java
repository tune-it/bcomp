package ru.ifmo.cs.bcomp.axl;

import java.util.ArrayList;
import java.util.List;

public final class Ast {

    public enum Base { INT, UINT, CHAR, LONG, VOID }

    public static final class Type {
        public Base base;
        public int ptr;

        public Type(Base base, int ptr) {
            this.base = base;
            this.ptr = ptr;
        }

        public boolean isPointer() {
            return ptr > 0;
        }

        public boolean isVoid() {
            return base == Base.VOID && ptr == 0;
        }

        public boolean isUnsigned() {
            return ptr == 0 && base == Base.UINT;
        }

        public Type deref() {
            return new Type(base, ptr - 1);
        }

        public Type addrOf() {
            return new Type(base, ptr + 1);
        }

        public String show() {
            StringBuilder sb = new StringBuilder(base.name().toLowerCase());
            for (int i = 0; i < ptr; i++) {
                sb.append('*');
            }
            return sb.toString();
        }
    }

    public static abstract class Node {
        public int line;
    }

    public static final class Program extends Node {
        public final List<Node> decls = new ArrayList<Node>();
    }

    public static final class GlobalVar extends Node {
        public Type type;
        public String name;
        public int arraySize = -1;
        public List<Expr> init;
    }

    public static final class OrgDirective extends Node {
        public int address;
    }

    public static final class WordData extends Node {
        public int address;
        public final List<Expr> items = new ArrayList<Expr>();
    }

    public static final class Param {
        public Type type;
        public String name;
    }

    public static final class FuncDecl extends Node {
        public Type retType;
        public String name;
        public final List<Param> params = new ArrayList<Param>();
        public Block body;
    }

    public static abstract class Stmt extends Node {
    }

    public static final class Block extends Stmt {
        public final List<Stmt> stmts = new ArrayList<Stmt>();
    }

    public static final class LocalVar extends Stmt {
        public Type type;
        public String name;
        public int arraySize = -1;
        public Expr init;
    }

    public static final class If extends Stmt {
        public Expr cond;
        public Stmt then;
        public Stmt els;
    }

    public static final class While extends Stmt {
        public Expr cond;
        public Stmt body;
    }

    public static final class DoWhile extends Stmt {
        public Stmt body;
        public Expr cond;
    }

    public static final class For extends Stmt {
        public List<Node> init = new ArrayList<Node>();
        public Expr cond;
        public List<Expr> update = new ArrayList<Expr>();
        public Stmt body;
    }

    public static final class Return extends Stmt {
        public Expr value;
    }

    public static final class Break extends Stmt {
    }

    public static final class Continue extends Stmt {
    }

    public static final class Goto extends Stmt {
        public String label;
    }

    public static final class LabelStmt extends Stmt {
        public String label;
    }

    public static final class ExprStmt extends Stmt {
        public Expr expr;
    }

    public static final class Empty extends Stmt {
    }

    public static final class Group extends Stmt {
        public final List<Stmt> stmts = new ArrayList<Stmt>();
    }

    public static abstract class Expr extends Node {
        public Type type;
    }

    public static final class IntLit extends Expr {
        public int value;
    }

    public static final class Ident extends Expr {
        public String name;
    }

    public static final class Assign extends Expr {
        public String op;
        public Expr target;
        public Expr value;
    }

    public static final class Binary extends Expr {
        public String op;
        public Expr left;
        public Expr right;
    }

    public static final class Unary extends Expr {
        public String op;
        public Expr operand;
        public boolean prefix;
    }

    public static final class Index extends Expr {
        public Expr base;
        public Expr idx;
    }

    public static final class Call extends Expr {
        public String callee;
        public final List<Expr> args = new ArrayList<Expr>();
    }

    private Ast() {
    }
}
