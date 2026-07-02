package ru.ifmo.cs.bcomp.axl;

public final class CompileException extends RuntimeException {

    public final int line;

    public CompileException(int line, String message) {
        super(line > 0 ? ("line " + line + ": " + message) : message);
        this.line = line;
    }
}
